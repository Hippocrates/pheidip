package pheidip.logic;

import java.util.EnumSet;
import java.util.List;

import pheidip.model.PropertyReflectSupport;
import pheidip.objects.Donation;
import pheidip.objects.DonationCommentState;
import pheidip.objects.DonationReadState;
import pheidip.util.StringUtils;

public class DonationReadTask implements DonationTask
{
  public static String TASK_NAME = "Read Task";

  private EntityControl<Donation> control;
  private EntitySearch<Donation> search;
  private EntitySearchInstance<Donation> searchInstance;
  
  public DonationReadTask(EntityControl<Donation> control, EntitySearch<Donation> search)
  {
    this.control = control;
    this.search = search;
    this.searchInstance = this.search.createSearchInstance();
    this.searchInstance.setPageSize(Integer.MAX_VALUE);
    PropertyReflectSupport.setProperty(this.searchInstance.getSearchParams(), "bidState", EnumSet.of(DonationReadState.PENDING));
  }
  
  @Override
  public void clearTask(Donation d)
  {
    if (d.getReadState() == DonationReadState.PENDING)
    {
      d.setReadState(DonationReadState.READ);
    }
    
    if (d.getCommentState() == DonationCommentState.PENDING && !StringUtils.isEmptyOrNull(d.getComment()))
    {
      d.setCommentState(DonationCommentState.APPROVED);
    }

    this.control.save(d);
  }

  @Override
  public List<Donation> refreshTaskList()
  {
    this.searchInstance.runSearch();
    return this.searchInstance.getResults();
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
