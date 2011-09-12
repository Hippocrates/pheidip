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
  private DonationReadState readState;
  private DonationCommentState commentState;

	public Donation(int id, DonationDomain domain, String domainId, DonationBidState bidState, DonationReadState readState, DonationCommentState commentState, BigDecimal amount, Date timeReceived, int donorId, String comment)
	{
	  this.id = id;
		this.donorId = donorId;
		this.amount = amount.setScale( 2, RoundingMode.FLOOR );
		this.timeReceived = timeReceived;
		this.comment = comment;
		this.domain = domain;
		this.domainId = domainId;
		this.bidState = bidState;
		this.readState = readState;
		this.commentState = commentState;
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
  
  public DonationReadState getReadState()
  {
    return this.readState;
  }
  
  public DonationCommentState getCommentState()
  {
    return this.commentState;
  }
  
  public String getDomainString()
  {
    return StringUtils.symbolToNatural(this.domain.toString()) + ":" + (this.domainId == null ? "" + this.id : this.domainId);
  }
  
  public boolean isMarkedAsRead()
  {
    return this.getReadState() == DonationReadState.COMMENT_READ ||
      (this.comment == null && this.getReadState() == DonationReadState.AMOUNT_READ);
  }
  
  public boolean isBidStateHandled()
  {
    return this.bidState == DonationBidState.PROCESSED || this.comment == null;
  }
  
  public String toString()
  {
    return this.domain.toString() + " : $" + this.amount.toString() + " : " + this.timeReceived.toString();
  }
}
