package pheidip.logic;

import java.util.EnumSet;
import java.util.List;

import pheidip.model.PropertyReflectSupport;
import pheidip.objects.Donation;
import pheidip.objects.DonationBidState;

public class DonationBidTask implements DonationTask
{
  public static String TASK_NAME = "Bid Task";

  private EntityControl<Donation> control;
  private EntitySearch<Donation> search;
  private EntitySearchInstance<Donation> searchInstance;
  
  public DonationBidTask(EntityControl<Donation> control, EntitySearch<Donation> search)
  {
    this.control = control;
    this.search = search;
    this.searchInstance = this.search.createSearchInstance();
    this.searchInstance.setPageSize(Integer.MAX_VALUE);
    PropertyReflectSupport.setProperty(this.searchInstance.getSearchParams(), "bidState", EnumSet.of(DonationBidState.PENDING));
  }

  @Override
  public void clearTask(Donation d)
  {
    if (d.getBidState() == DonationBidState.PENDING)
    {
      d.setBidState(DonationBidState.PROCESSED);
    }
    this.control.save(d);
  }
  
  public boolean isTaskCleared(Donation d)
  {
    return d.getBidState() != DonationBidState.PENDING;
  }

  @Override
  public List<Donation> refreshTaskList()
  {
    this.searchInstance.runSearch();
    return this.searchInstance.getResults();
  }

  @Override
  public String taskName()
  {
    return TASK_NAME;
  }
}
