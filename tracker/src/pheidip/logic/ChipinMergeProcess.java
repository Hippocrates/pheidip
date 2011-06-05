package pheidip.logic;

import java.util.List;

import org.jsoup.nodes.Document;

import pheidip.objects.ChipinDonation;

public class ChipinMergeProcess implements Runnable
{
  public interface MergeStateCallback
  {
    void stateChanged(ChipinMergeState newState);
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
  
  private synchronized void setState(ChipinMergeState newState)
  {
    this.currentState = newState;
    
    if (this.listener != null)
    {
      this.listener.stateChanged(this.currentState);
    }
  }
  
  public void run()
  {
    try
    {
      Thread.sleep(0);
      this.setState(ChipinMergeState.RETRIEVING);
      
      Document html = this.documentSource.provideDocument();
      
      Thread.sleep(0);
      this.setState(ChipinMergeState.EXTRACTING);

      List<ChipinDonation> donations = ChipinDonations.extractDonations(html);
      
      Thread.sleep(0);
      this.setState(ChipinMergeState.MERGING);
      
      for (ChipinDonation d : donations)
      {
        Thread.sleep(0);
        ChipinDonations.mergeDonation(this.donationDatabase, d);
      }
      
      this.setState(ChipinMergeState.COMPLETED);
    }
    catch (InterruptedException e)
    {
      this.setState(ChipinMergeState.CANCELLED);
    }
    catch (Exception e)
    {
      this.setState(ChipinMergeState.FAILED);
    }
  }

}
