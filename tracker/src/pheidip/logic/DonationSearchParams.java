package pheidip.logic;

import java.math.BigDecimal;
import java.util.Date;

import pheidip.objects.Donation;
import pheidip.util.FilterFunction;

public class DonationSearchParams implements FilterFunction<Donation>
{
  public Integer donorId;
  public Date loTime;
  public Date hiTime;
  public BigDecimal loAmount;
  public BigDecimal hiAmount;
  public boolean onlyIfUnread;
  public boolean onlyIfUnbid;
  
  @Override
  public boolean predicate(Donation x)
  {
    return 
      (donorId == null || x.getDonorId() == donorId) &&
      (loTime == null || x.getTimeReceived().compareTo(loTime) >= 0) &&
      (hiTime == null || x.getTimeReceived().compareTo(hiTime) <= 0) &&
      (loAmount == null || x.getAmount().compareTo(loAmount) >= 0) &&
      (hiAmount == null || x.getAmount().compareTo(hiAmount) <= 0) &&
      (onlyIfUnread ? !x.isMarkedAsRead() : true) &&
      (onlyIfUnbid ? !x.isBidStateHandled() : true);
  }
}
