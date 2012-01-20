package pheidip.logic.chipin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import pheidip.db.DonationData;
import pheidip.db.DonorData;
import pheidip.logic.AbstractExternalProcess;
import pheidip.logic.DonationDatabaseManager;
import pheidip.logic.DonationSearch;
import pheidip.objects.Donation;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationSearchParams;
import pheidip.objects.Donor;

public class ChipinMergeProcess extends AbstractExternalProcess
{
  private DonationDatabaseManager donationDatabase;
  private ChipinDonationSource donationSource;

  public ChipinMergeProcess(DonationDatabaseManager donationDatabase, ChipinDonationSource documentSource)
  {
    this(donationDatabase, documentSource, null);
  }
  
  public ChipinMergeProcess(DonationDatabaseManager donationDatabase, ChipinDonationSource documentSource, ProcessStateCallback listener)
  {
    super(listener);
    
    this.donationDatabase = donationDatabase;
    this.donationSource = documentSource;
  }

  @Override
  public void run()
  {
    try
    {
      this.resetState(ExternalProcessState.RUNNING, 0.1, "Retreiving donations from chipin...");
      Thread.sleep(0);
      
      List<ChipinDonation> chipinDonations = this.donationSource.provideChipinDonations();
      
      this.resetState(ExternalProcessState.RUNNING, 0.3, "Reading current donation set...");
      Thread.sleep(0);
      
      DonationData donations = this.donationDatabase.getDataAccess().getDonationData();
      DonorData donors = this.donationDatabase.getDataAccess().getDonorData();
      DonationSearchParams params = new DonationSearchParams();
      params.setDomain(new HashSet<DonationDomain>(Arrays.asList(DonationDomain.CHIPIN)));

      List<Donor> allDonors = donors.getAllDonors();
      List<Donation> donationsToInsert = new ArrayList<Donation>();
      List<Donation> donationsToUpdate = new ArrayList<Donation>();
      List<Donor> donorsToInsert = new ArrayList<Donor>();
      
      Map<String, ChipinDonation> chipinDonationMap = ChipinDonations.mapDonations(chipinDonations);
      
      DonationSearch searcher = new DonationSearch(this.donationDatabase);
      
      List<Donation> databaseDonations = searcher.runSearch(params);
      
      this.resetState(ExternalProcessState.RUNNING, 0.4, "Merging donations into database...");
      Thread.sleep(0);
      
      double step = (1.0 - 0.4) / chipinDonationMap.size();

      while (databaseDonations.size() > 0)
      {
        donationsToUpdate.addAll(ChipinDonations.updateMergeTable(chipinDonationMap, databaseDonations));
        databaseDonations = searcher.moveNext();

        this.resetState(ExternalProcessState.RUNNING, 1.0 - step*chipinDonationMap.size(), "Merging donations into database...");
        Thread.sleep(0);
      }

      ChipinDonations.buildInsertTables(chipinDonationMap, allDonors, donorsToInsert, donationsToInsert);

      donations.updateMultipleDonations(donationsToUpdate);

      donors.insertMultipleDonors(donorsToInsert);
      donations.insertMultipleDonations(donationsToInsert);

      Thread.sleep(0);
      
      this.resetState(ExternalProcessState.COMPLETED, 1.0, "Merge operation complete.");
    }
    catch (InterruptedException e)
    {
      this.resetState(ExternalProcessState.CANCELLED, 0.0, "Merge operation cancelled.");
    }
    catch (Exception e)
    {
      this.resetState(ExternalProcessState.FAILED, 0.0, "The merge operation failed.");
    }
  }
  
  @Override
  public String getProcessName()
  {
    return "Chipin Merge";
  }
}
