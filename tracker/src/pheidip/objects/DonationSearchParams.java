package pheidip.objects;

import java.math.BigDecimal;
import java.util.Date;

import pheidip.util.FilterFunction;

public class DonationSearchParams implements FilterFunction<Donation>
{
  public Donor donor;
  public DonationDomain domain;
  public String domainId;
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
      (donor == null || x.getDonor().equals(donor)) &&
      (domain == null || x.getDomain() == domain) &&
      (domainId == null || x.getDomainId().equals(domainId)) && 
      (loTime == null || x.getTimeReceived().compareTo(loTime) >= 0) &&
      (hiTime == null || x.getTimeReceived().compareTo(hiTime) <= 0) &&
      (loAmount == null || x.getAmount().compareTo(loAmount) >= 0) &&
      (hiAmount == null || x.getAmount().compareTo(hiAmount) <= 0) &&
      (onlyIfUnread ? !x.isMarkedAsRead() : true) &&
      (onlyIfUnbid ? !x.isBidStateHandled() : true);
  }
}
