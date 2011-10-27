package pheidip.objects;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;
import java.math.RoundingMode;

import pheidip.util.IdUtils;
import pheidip.util.StringUtils;

public class Donation 
{
	private Date timeReceived;
	private BigDecimal amount;
	private int donorId;
	private int id = IdUtils.generateId();
	private String comment;
	private DonationDomain domain;
  private String domainId;
  private DonationBidState bidState;
  private DonationReadState readState;
  private DonationCommentState commentState;
  private Set<DonationBid> bids = new HashSet<DonationBid>();
  private Donor donor;

  public Donation()
  {
  }
  
	public Donation(int id, DonationDomain domain, String domainId, DonationBidState bidState, DonationReadState readState, DonationCommentState commentState, BigDecimal amount, Date timeReceived, int donorId, String comment)
	{
	  this.setId(id);
		this.setDonorId(donorId);
		this.setAmount(amount.setScale( 2, RoundingMode.FLOOR ));
		this.setTimeReceived(timeReceived);
		this.setComment(comment);
		this.setDomain(domain);
		this.setDomainId(domainId);
		this.setBidState(bidState);
		this.setReadState(readState);
		this.setCommentState(commentState);
	}
	
	public Date getTimeReceived()
	{
		return this.timeReceived;
	}
	
	public void setTimeReceived(Date timeReceived)
  {
    this.timeReceived = timeReceived;
  }

  public BigDecimal getAmount()
	{
		return this.amount;
	}
	
  public void setAmount(BigDecimal amount)
  {
    this.amount = amount;
  }

  public int getDonorId()
	{
		return this.donorId;
	}
	
  public void setDonorId(int donorId)
  {
    this.donorId = donorId;
  }

  public int getId()
	{
	  return this.id;
	}
	
  public void setId(int id)
  {
    this.id = id;
  }

  public String getComment()
	{
	  return this.comment;
	}
	
  public void setComment(String comment)
  {
    this.comment = comment;
  }

  public DonationDomain getDomain()
	{
	  return this.domain;
	}

  public void setDomain(DonationDomain domain)
  {
    this.domain = domain;
  }

  public String getDomainId()
  {
    return this.domainId;
  }

  public void setDomainId(String domainId)
  {
    this.domainId = domainId;
  }

  public DonationBidState getBidState()
  {
    return bidState;
  }
  
  public void setBidState(DonationBidState bidState)
  {
    this.bidState = bidState;
  }

  public DonationReadState getReadState()
  {
    return this.readState;
  }
  
  public void setCommentState(DonationCommentState commentState)
  {
    this.commentState = commentState;
  }

  public void setReadState(DonationReadState readState)
  {
    this.readState = readState;
  }

  public DonationCommentState getCommentState()
  {
    return this.commentState;
  }
  
  public String getDomainString()
  {
    return StringUtils.symbolToNatural(this.getDomain().toString()) + ":" + (this.getDomainId() == null ? "" + this.getId() : this.getDomainId());
  }
  
  public boolean isMarkedAsRead()
  {
    return this.getReadState() == DonationReadState.COMMENT_READ ||
      (this.getComment() == null && this.getReadState() == DonationReadState.AMOUNT_READ);
  }
  
  public boolean isBidStateHandled()
  {
    return this.getBidState() == DonationBidState.PROCESSED || this.getComment() == null;
  }
  
  public String toString()
  {
    return this.getDomain().toString() + " : $" + this.getAmount().toString() + " : " + this.getTimeReceived().toString();
  }
  
  public int hashCode()
  {
    return this.getId();
  }
  
  public boolean equals(Object other)
  {
    if (other instanceof Donation)
    {
      return this.getId() == ((Donation)other).getId();
    }
    else
    {
      return false;
    }
  }

  public void setBids(Set<DonationBid> bids)
  {
    this.bids = bids;
  }

  public void setDonor(Donor donor)
  {
    this.donor = donor;
  }

  public Donor getDonor()
  {
    return donor;
  }

  public Set<DonationBid> getBids()
  {
    return bids;
  }
}
