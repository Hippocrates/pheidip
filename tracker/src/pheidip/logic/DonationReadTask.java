package pheidip.logic;

import java.util.List;

import pheidip.db.DonationData;
import pheidip.objects.Donation;
import pheidip.objects.DonationReadState;
import pheidip.objects.DonationSearchParams;

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
  public DonationControl getControl(Donation d)
  {
    return new DonationControl(this.manager, d.getId());
  }

  @Override
  public void clearTask(Donation d)
  {
    DonationControl control = getControl(d);
    d.setReadState(DonationReadState.READ);
    control.updateData(d);
  }

  @Override
  public List<Donation> refreshTaskList()
  {
    DonationSearchParams params = new DonationSearchParams();
    params.targetReadState = DonationReadState.PENDING;
    
    return this.donations.searchDonations(params);
  }
  
  public boolean isTaskCleared(Donation d)
  {
    return d.getReadState() != DonationReadState.PENDING;
  }

  @Override
  public String taskName()
  {
    return TASK_NAME;
  }
}
