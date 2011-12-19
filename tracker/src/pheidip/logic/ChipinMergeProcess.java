package pheidip.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import pheidip.db.DonationData;
import pheidip.db.DonorData;
import pheidip.objects.ChipinDonation;
import pheidip.objects.Donation;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationSearchParams;
import pheidip.objects.Donor;

public class ChipinMergeProcess implements Runnable
{
  public interface MergeStateCallback
  {
    void stateChanged(ChipinMergeState newState, double percentage);
  }
  
  private DonationDatabaseManager donationDatabase;
  private ChipinDocumentSource documentSource;
  private ChipinMergeState currentState;
  private MergeStateCallback listener;

  public ChipinMergeProcess(DonationDatabaseManager donationDatabase, ChipinDocumentSource documentSource)
  {
    this.donationDatabase = donationDatabase;
    this.documentSource = documentSource;
    
    this.currentState = ChipinMergeState.IDLE;
    
    this.listener = null;
  }
  
  public synchronized void setListener(MergeStateCallback listener)
  {
    this.listener = listener;
  }
  
  public ChipinMergeState getState()
  {
    return this.currentState;
  }
  
  private synchronized void setState(ChipinMergeState newState, double percentage)
  {
    this.currentState = newState;
    
    if (this.listener != null)
    {
      this.listener.stateChanged(this.currentState, percentage);
    }
  }
  
  public void run()
  {
    try
    {
      this.setState(ChipinMergeState.RETRIEVING, 0.1);
      Thread.sleep(0);
      
      Document html = this.documentSource.provideDocument();
      
      this.setState(ChipinMergeState.EXTRACTING, 0.2);
      Thread.sleep(0);
      
      List<ChipinDonation> chipinDonations = ChipinDonations.extractDonations(html);
      
      this.setState(ChipinMergeState.COMPARING, 0.3);
      Thread.sleep(0);
      
      //int current = 0;
      
      DonationData donations = this.donationDatabase.getDataAccess().getDonationData();
      DonorData donors = this.donationDatabase.getDataAccess().getDonorData();
      DonationSearchParams params = new DonationSearchParams();
      params.domain = DonationDomain.CHIPIN;

      List<Donor> allDonors = donors.getAllDonors();
      List<Donation> donationsToInsert = new ArrayList<Donation>();
      List<Donation> donationsToUpdate = new ArrayList<Donation>();
      List<Donor> donorsToInsert = new ArrayList<Donor>();
      
      Map<String, ChipinDonation> chipinDonationMap = ChipinDonations.mapDonations(chipinDonations);
      
      DonationSearch searcher = new DonationSearch(this.donationDatabase);
      
      List<Donation> databaseDonations = searcher.runSearch(params);
      
      this.setState(ChipinMergeState.MERGING, 0.4);
      Thread.sleep(0);
      
      double step = (1.0 - 0.4) / chipinDonationMap.size();

      while (databaseDonations.size() > 0)
      {
        donationsToUpdate.addAll(ChipinDonations.updateMergeTable(chipinDonationMap, databaseDonations));
        databaseDonations = searcher.getNext();

        this.setState(ChipinMergeState.MERGING, 1.0 - step*chipinDonationMap.size());
        Thread.sleep(0);
      }

      ChipinDonations.buildInsertTables(chipinDonationMap, allDonors, donorsToInsert, donationsToInsert);


      donors.insertMultipleDonors(donorsToInsert);
      donations.insertMultipleDonations(donationsToInsert);

      Thread.sleep(0);
      
      this.setState(ChipinMergeState.COMPLETED, 1.0);
    }
    catch (InterruptedException e)
    {
      this.setState(ChipinMergeState.CANCELLED, 0.0);
    }
    catch (Exception e)
    {
      this.setState(ChipinMergeState.FAILED, 0.0);
    }
  }

}
