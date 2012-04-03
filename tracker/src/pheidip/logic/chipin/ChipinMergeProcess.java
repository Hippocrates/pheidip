package pheidip.logic.chipin;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import meta.MetaEntity;
import meta.reflect.MetaEntityReflector;

import pheidip.db.DataAccess;
import pheidip.logic.AbstractExternalProcess;
import pheidip.logic.EntitySearch;
import pheidip.logic.EntitySearchInstance;
import pheidip.logic.ProgramInstance;
import pheidip.objects.Donation;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationSearchParams;
import pheidip.objects.Donor;

public class ChipinMergeProcess extends AbstractExternalProcess
{
  private ProgramInstance instance;
  private ChipinDonationSource donationSource;
  private EntitySearch<Donation> donationSearch;
  private EntitySearch<Donor> donorSearch;
  private MetaEntity donationEntity;
  private MetaEntity donorEntity;

  public ChipinMergeProcess(ProgramInstance instance, ChipinDonationSource documentSource)
  {
    this(instance, documentSource, null);
  }
  
  public ChipinMergeProcess(ProgramInstance instance, ChipinDonationSource documentSource, ProcessStateCallback listener)
  {
    super(listener);
    
    this.instance = instance;
    this.donationSource = documentSource;
    
    this.donationSearch = this.instance.getEntitySearch(Donation.class);
    this.donorSearch = this.instance.getEntitySearch(Donor.class);
    this.donationEntity = MetaEntityReflector.getMetaEntity(Donation.class);
    this.donorEntity = MetaEntityReflector.getMetaEntity(Donor.class);
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
      
      DonationSearchParams params = new DonationSearchParams();
      params.setDomain(EnumSet.of(DonationDomain.CHIPIN));
      
      EntitySearchInstance<Donation> donationSearchInstance = this.donationSearch.createSearchInstance(params);
      EntitySearchInstance<Donor> donorSearchInstance = this.donorSearch.createSearchInstance();

      donorSearchInstance.setPageSize(Integer.MAX_VALUE);

      donorSearchInstance.runSearch();
      
      List<Donor> allDonors = donorSearchInstance.getResults();

      List<Donation> donationsToInsert = new ArrayList<Donation>();
      List<Donation> donationsToUpdate = new ArrayList<Donation>();
      List<Donor> donorsToInsert = new ArrayList<Donor>();
      
      Map<String, ChipinDonation> chipinDonationMap = ChipinDonations.mapDonations(chipinDonations);
      
      
      
      this.resetState(ExternalProcessState.RUNNING, 0.4, "Merging donations into database...");
      Thread.sleep(0);
      
      double step = (1.0 - 0.4) / chipinDonationMap.size();

      donationSearchInstance.runSearch();
      List<Donation> databaseDonations = donationSearchInstance.getResults();
      
      while(databaseDonations.size() > 0)
      {
        donationsToUpdate.addAll(ChipinDonations.updateMergeTable(chipinDonationMap, databaseDonations));

        if (donationSearchInstance.isNextPageAvailable())
        {
          donationSearchInstance.nextPage();
          databaseDonations = donationSearchInstance.getResults();
        }
        else
        {
          break;
        }
        
        this.resetState(ExternalProcessState.RUNNING, 1.0 - step*chipinDonationMap.size(), "Merging donations into database...");
        Thread.sleep(0);
      }

      ChipinDonations.buildInsertTables(chipinDonationMap, allDonors, donorsToInsert, donationsToInsert);

      DataAccess database = this.instance.getDataAccess();
      
      database.updateMultiple(this.donationEntity, donationsToUpdate);
      
      database.saveMultiple(this.donorEntity, donorsToInsert);
      database.saveMultiple(this.donationEntity, donationsToInsert);

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
