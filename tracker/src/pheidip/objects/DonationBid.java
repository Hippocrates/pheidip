package pheidip.objects;

import java.math.BigDecimal;

public abstract class DonationBid extends Entity
{
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

  public Donation getDonation()
  {
    return donation;
  }

  public void setDonation(Donation donation)
  {
    Donation oldDonation = this.donation;
    this.donation = donation;
    this.firePropertyChange("donation", oldDonation, this.donation);
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
    
    BigDecimal oldAmount = this.amount;
    this.amount = amount;
    this.firePropertyChange("amount", oldAmount, this.amount);
  }
}
