package pheidip.logic;

import java.util.List;

import pheidip.objects.Donation;

public class DonationReadTask implements DonationTask
{
  public static String TASK_NAME = "Read Task";
  private DonationDatabaseManager manager;
  
  public DonationReadTask(DonationDatabaseManager manager)
  {
    this.manager = manager;
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
    DonationSearch searcher = new DonationSearch(this.manager);
    DonationSearchParams params = new DonationSearchParams();
    params.onlyIfUnread = true;
    return searcher.searchDonations(params);
  }

  @Override
  public String taskName()
  {
    return TASK_NAME;
  }

}
