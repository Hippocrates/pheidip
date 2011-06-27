package pheidip.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import pheidip.objects.ChipinDonation;
import pheidip.objects.Donation;
import pheidip.objects.DonationDomain;

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

      List<ChipinDonation> donations = ChipinDonations.extractDonations(html);
      
      Thread.sleep(0);
      this.setState(ChipinMergeState.COMPARING, 0.3);
      
      DonationSearch searcher = new DonationSearch(this.donationDatabase);
      
      List<Donation> all = searcher.searchDonations(new DonationSearchParams());
      
      Map<String,Donation> mappedDonations = new HashMap<String,Donation>();
      
      for (Donation d : all)
      {
        if (d.getDomain() == DonationDomain.CHIPIN)
        {
          mappedDonations.put(d.getDomainId(), d);
        }
      }
      
      int current = 0;
      
      this.setState(ChipinMergeState.MERGING, 0.4);
      
      for (ChipinDonation d : donations)
      {
        Thread.sleep(0);
        this.setState(ChipinMergeState.MERGING, 0.4 + (((double)current / (double)donations.size()) * 0.6));
        
        ChipinDonations.mergeDonation(this.donationDatabase, d, mappedDonations);
        ++current;
      }
      
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
