package pheidip.logic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import pheidip.db.DonationData;
import pheidip.objects.Donation;
import pheidip.objects.DonationCommentState;
import pheidip.objects.DonationReadState;
import pheidip.objects.DonationSearchParams;
import pheidip.util.StringUtils;

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
    if (control.getData().getReadState() == DonationReadState.PENDING)
    {
      control.getData().setReadState(DonationReadState.READ);
    }
    
    if (control.getData().getCommentState() == DonationCommentState.PENDING && !StringUtils.isEmptyOrNull(control.getData().getComment()))
    {
      control.getData().setCommentState(DonationCommentState.APPROVED);
    }
    control.updateData(control.getData());
  }

  @Override
  public List<Donation> refreshTaskList()
  {
    DonationSearchParams params = new DonationSearchParams();
    params.setTargetReadState(new HashSet<DonationReadState>(Arrays.asList(DonationReadState.PENDING)));
    
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
