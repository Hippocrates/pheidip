package pheidip.objects;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;
import java.math.RoundingMode;

import pheidip.util.StringUtils;

public class Donation extends Entity
{
	private Date timeReceived;
	private BigDecimal amount;
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
	
	public Donation(int id, DonationDomain domain, String domainId, DonationBidState bidState, DonationReadState readState, DonationCommentState commentState, BigDecimal amount, Date timeReceived, Donor donor, String comment)
  {
    this.setId(id);
    this.setAmount(amount.setScale( 2, RoundingMode.FLOOR ));
    this.setTimeReceived(timeReceived);
    this.setComment(comment);
    this.setDomain(domain);
    this.setDomainId(domainId);
    this.setBidState(bidState);
    this.setReadState(readState);
    this.setCommentState(commentState);
    this.setDonor(donor);
  }
	
	public Date getTimeReceived()
	{
		return this.timeReceived;
	}
	
	public void setTimeReceived(Date timeReceived)
  {
	  Date oldTimeReceived = this.timeReceived;
    this.timeReceived = timeReceived;
    this.firePropertyChange("timeReceived", oldTimeReceived, this.timeReceived);
  }

  public BigDecimal getAmount()
	{
		return this.amount;
	}
	
  public void setAmount(BigDecimal amount)
  {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0)
    {
      throw new RuntimeException("Donation amount invalid.");
    }

    BigDecimal oldAmount = amount;
    this.amount = amount;
    this.firePropertyChange("amount", oldAmount, this.amount);
  }

  public String getComment()
	{
	  return this.comment;
	}
	
  public void setComment(String comment)
  {
    String oldComment = this.comment;
    this.comment = StringUtils.nullIfEmpty(comment);
    this.firePropertyChange("comment", oldComment, this.comment);
  }

  public DonationDomain getDomain()
	{
	  return this.domain;
	}

  public void setDomain(DonationDomain domain)
  {
    DonationDomain oldDomain = this.domain;
    this.domain = domain;
    this.firePropertyChange("domain", oldDomain, this.domain);
  }

  public String getDomainId()
  {
    return this.domainId;
  }

  public void setDomainId(String domainId)
  {
    String oldDomainId = this.domainId;
    this.domainId = domainId;
    this.firePropertyChange("domainId", oldDomainId, this.domainId);
  }

  public DonationBidState getBidState()
  {
    return bidState;
  }
  
  public void setBidState(DonationBidState bidState)
  {
    DonationBidState oldBidState = this.bidState;
    this.bidState = bidState;
    this.firePropertyChange("bidState", oldBidState, this.bidState);
  }

  public DonationReadState getReadState()
  {
    return this.readState;
  }

  public void setReadState(DonationReadState readState)
  {
    DonationReadState oldReadState = this.readState;
    this.readState = readState;
    this.firePropertyChange("readState", oldReadState, this.readState);
  }

  public DonationCommentState getCommentState()
  {
    return this.commentState;
  }
  
  public void setCommentState(DonationCommentState commentState)
  {
    DonationCommentState oldCommentState = this.commentState;
    this.commentState = commentState;
    this.firePropertyChange("commentState", oldCommentState, this.commentState);
  }

  public Set<DonationBid> getBids()
  {
    return bids;
  }
  
  public void setBids(Set<DonationBid> bids)
  {
    this.bids = bids;
  }

  public Donor getDonor()
  {
    return donor;
  }
  
  public void setDonor(Donor donor)
  {
    Donor oldDonor = this.donor;
    this.donor = donor;
    this.firePropertyChange("donor", oldDonor, this.donor);
  }

  public String getDomainString()
  {
    return StringUtils.symbolToNatural(this.getDomain().toString()) + ":" + (this.getDomainId() == null ? "" + this.getId() : this.getDomainId());
  }

  public String toString()
  {
    return this.getDomain().toString() + " : $" + this.getAmount().toString() + " : " + this.getTimeReceived().toString();
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
}
