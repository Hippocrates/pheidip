package pheidip.logic;

import java.util.List;

import pheidip.objects.Donation;

public class DonationBidTask implements DonationTask
{
  public static String TASK_NAME = "Bid Task";
  private DonationDatabaseManager manager;
  
  public DonationBidTask(DonationDatabaseManager manager)
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
    this.getControl(donationId).markAsBidsHandled();
  }
  
  public boolean isTaskCleared(Donation d)
  {
    return d.isBidStateHandled();
  }

  @Override
  public List<Donation> refreshTaskList()
  {
    DonationSearch searcher = new DonationSearch(this.manager);
    DonationSearchParams params = new DonationSearchParams();
    params.onlyIfUnbid = true;
    return searcher.searchDonations(params);
  }

  @Override
  public String taskName()
  {
    return TASK_NAME;
  }

}
