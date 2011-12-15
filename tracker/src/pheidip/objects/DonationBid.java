package pheidip.objects;

import java.math.BigDecimal;

import pheidip.util.IdUtils;

public abstract class DonationBid
{
  private int id = IdUtils.generateId();
  private BigDecimal amount;
  private Donation donation;
  
  public DonationBid()
  {
  }
  
  public DonationBid(int id, BigDecimal amount, Donation donation)
  {
    this.setId(id);
    this.setAmount(amount);
    this.setDonation(donation);
  }
  
  public abstract BidType getType();
  
  public int getId()
  {
    return this.id;
  }
  
  public void setId(int id)
  {
    this.id = id;
  }
  
  public void setDonation(Donation donation)
  {
    this.donation = donation;
  }

  public Donation getDonation()
  {
    return donation;
  }

  public BigDecimal getAmount()
  {
    return amount;
  }

  public void setAmount(BigDecimal amount)
  {
    if (amount.compareTo(BigDecimal.ZERO) <= 0)
    {
      throw new RuntimeException("Bid amount must be greater than zero.");
    }
    
    this.amount = amount;
  }
}
