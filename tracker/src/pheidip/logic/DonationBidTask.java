package pheidip.logic;

import java.util.List;

import pheidip.db.DonationData;
import pheidip.objects.Donation;
import pheidip.objects.DonationSearchParams;

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
  public DonationControl getControl(Donation d)
  {
    return new DonationControl(this.manager, d);
  }

  @Override
  public void clearTask(Donation d)
  {
    this.getControl(d).markAsBidsHandled();
  }
  
  public boolean isTaskCleared(Donation d)
  {
    return d.isBidStateHandled();
  }

  @Override
  public List<Donation> refreshTaskList()
  {
    DonationSearchParams params = new DonationSearchParams();
    params.onlyIfUnbid = true;
    
    return this.donations.searchDonations(params);
  }

  @Override
  public String taskName()
  {
    return TASK_NAME;
  }

}
