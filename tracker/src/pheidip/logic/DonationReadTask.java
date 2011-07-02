package pheidip.logic;

import java.util.List;

import pheidip.db.DonationData;
import pheidip.objects.Donation;

public class DonationReadTask implements DonationTask
{
  public static String TASK_NAME = "Read Task";
  private DonationDatabaseManager manager;
  private DonationData donations;
  
  public DonationReadTask(DonationDatabaseManager manager)
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
    this.getControl(donationId).markDonationAsRead();
  }

  @Override
  public List<Donation> refreshTaskList()
  {
    return this.donations.getDonationsToBeRead();
  }
  
  public boolean isTaskCleared(Donation d)
  {
    return d.isMarkedAsRead();
  }

  @Override
  public String taskName()
  {
    return TASK_NAME;
  }

}
