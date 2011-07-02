package pheidip.logic;

import java.util.List;

import pheidip.db.DonationData;
import pheidip.objects.Donation;

public class DonationBidTask implements DonationTask
{
  public static String TASK_NAME = "Bid Task";
  private DonationDatabaseManager manager;
  private DonationData donations;
  
  public DonationBidTask(DonationDatabaseManager manager)
  {
    this.manager = manager;
    this.donations = this.manager.getDataAccess().getDonationData();
  }
  
  @Override
  public DonationControl getControl(int donationId)
  {
    return new DonationControl(this.manager, donationId);
  }

  @Override
  public void clearTask(int donationId)
  {
    this.getControl(donationId).markAsBidsHandled();
  }
  
  public boolean isTaskCleared(Donation d)
  {
    return d.isBidStateHandled();
  }

  @Override
  public List<Donation> refreshTaskList()
  {
    return this.donations.getDonationsWithPendingBids();
  }

  @Override
  public String taskName()
  {
    return TASK_NAME;
  }

}
