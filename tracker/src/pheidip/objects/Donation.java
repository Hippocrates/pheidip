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
  private DonationBidState bidState;

	public Donation(int id, DonationDomain domain, String domainId, DonationBidState bidState, BigDecimal amount, Date timeReceived, int donorId, String comment)
	{
	  this.id = id;
		this.donorId = donorId;
		this.amount = amount.setScale( 2, RoundingMode.FLOOR );
		this.timeReceived = timeReceived;
		this.comment = comment;
		this.domain = domain;
		this.domainId = domainId;
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

  public DonationBidState getBidState()
  {
    return bidState;
  }
  
  public String getDomainString()
  {
    return StringUtils.symbolToNatural(this.domain.toString()) + ":" + (this.domainId == null ? "" + this.id : this.domainId);
  }
}
