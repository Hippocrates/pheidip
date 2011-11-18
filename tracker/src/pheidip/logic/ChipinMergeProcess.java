package pheidip.logic;

import java.util.ArrayList;
import java.util.List;

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
      Thread.sleep(0);
      this.setState(ChipinMergeState.RETRIEVING, 0.1);
      
      Document html = this.documentSource.provideDocument();
      
      Thread.sleep(0);
      this.setState(ChipinMergeState.EXTRACTING, 0.2);

      List<ChipinDonation> chipinDonations = ChipinDonations.extractDonations(html);
      
      Thread.sleep(0);
      this.setState(ChipinMergeState.COMPARING, 0.3);
      
      //int current = 0;
      
      DonationData donations = this.donationDatabase.getDataAccess().getDonationData();
      DonorData donors = this.donationDatabase.getDataAccess().getDonorData();
      DonationSearchParams params = new DonationSearchParams();
      params.domain = DonationDomain.CHIPIN;
      
      List<Donation> databaseDonations = donations.searchDonations(params);
      List<Donor> allDonors = donors.getAllDonors();
      List<Donation> donationsToInsert = new ArrayList<Donation>();
      List<Donation> donationsToUpdate = new ArrayList<Donation>();
      List<Donor> donorsToInsert = new ArrayList<Donor>();
      
      ChipinDonations.buildMergeTables(chipinDonations, databaseDonations, allDonors, donorsToInsert, donationsToInsert, donationsToUpdate);

      Thread.sleep(0);
      this.setState(ChipinMergeState.MERGING, 0.4);
      
      donors.insertMultipleDonors(donorsToInsert);
      donations.insertMultipleDonations(donationsToInsert);
      donations.updateMultiplDonations(donationsToUpdate);
      
      Thread.sleep(0);
      this.setState(ChipinMergeState.MERGING, 0.8);
      
      donations.updateMultiplDonations(donationsToUpdate);
      
      
      Thread.sleep(0);
      
      /*
      for (ChipinDonation d : chipinDonations)
      {
        Thread.sleep(0);
        this.setState(ChipinMergeState.MERGING, 0.4 + (((double)current / (double)chipinDonations.size()) * 0.6));
        
        ChipinDonations.mergeDonation(this.donationDatabase, d, donorTable, donationTable);
        ++current;
      }
      */
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
