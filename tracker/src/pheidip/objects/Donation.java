package pheidip.objects;

import java.util.Date;
import java.math.BigDecimal;
import java.math.RoundingMode;

import pheidip.util.StringUtils;

public class Donation 
{
	private Date timeReceived;
	private BigDecimal amount;
	private int donorId;
	private int id;
	private String comment;
	private DonationDomain domain;
  private String domainId;
  private DonationPaymentState paymentState;
  private DonationAnnounceState announceState;
  private DonationBidState bidState;

	public Donation(int id, DonationDomain domain, String domainId, DonationPaymentState paymentState, DonationAnnounceState announceState, DonationBidState bidState, BigDecimal amount, Date timeReceived, int donorId, String comment)
	{
	  this.id = id;
		this.donorId = donorId;
		this.amount = amount.setScale( 2, RoundingMode.FLOOR );
		this.timeReceived = timeReceived;
		this.comment = comment;
		this.domain = domain;
		this.domainId = domainId;
		this.paymentState = paymentState;
		this.announceState = announceState;
		this.bidState = bidState;
	}
	
	public Date getTimeReceived()
	{
		return this.timeReceived;
	}
	
	public BigDecimal getAmount()
	{
		return this.amount;
	}
	
	public int getDonorId()
	{
		return this.donorId;
	}
	
	public int getId()
	{
	  return this.id;
	}
	
	public String getComment()
	{
	  return this.comment;
	}
	
	public DonationDomain getDomain()
	{
	  return this.domain;
	}

  public String getDomainId()
  {
    return this.domainId;
  }

  public DonationPaymentState getPaymentState()
  {
    return paymentState;
  }

  public DonationAnnounceState getAnnounceState()
  {
    return announceState;
  }
  
  public DonationBidState getBidState()
  {
    return bidState;
  }
  
  public String getDomainString()
  {
    return StringUtils.symbolToNatural(this.domain.toString()) + ":" + (this.domainId == null ? "" + this.id : this.domainId);
  }
}
