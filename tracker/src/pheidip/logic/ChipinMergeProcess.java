package pheidip.logic;

import java.util.List;

import org.jsoup.nodes.Document;

import pheidip.objects.ChipinDonation;

public class ChipinMergeProcess implements Runnable
{
  private DonationDatabaseManager donationDatabase;
  private ChipinDocumentSource documentSource;
  private ChipinMergeState currentState;

  public ChipinMergeProcess(DonationDatabaseManager donationDatabase, ChipinDocumentSource documentSource)
  {
    this.donationDatabase = donationDatabase;
    this.documentSource = documentSource;
    
    this.currentState = ChipinMergeState.IDLE;
  }
  
  public synchronized ChipinMergeState getState()
  {
    return this.currentState;
  }
  
  private synchronized void setState(ChipinMergeState newState)
  {
    this.currentState = newState;
  }
  
  public void run()
  {
    try
    {
      this.setState(ChipinMergeState.RETRIEVING);
      
      Thread.sleep(0);
      
      Document html = this.documentSource.provideDocument();
      
      this.setState(ChipinMergeState.EXTRACTING);
      
      Thread.sleep(0);
      
      List<ChipinDonation> donations = ChipinDonations.extractDonations(html);
      
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
