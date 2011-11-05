package pheidip.objects;

import java.math.BigDecimal;

import pheidip.util.IdUtils;

public class ChallengeBid implements DonationBid
{
  private int id = IdUtils.generateId();
  private BigDecimal amount;
  private Donation donation;
  private Challenge challenge;
  
  public ChallengeBid()
  {
  }
  
  public ChallengeBid(int id, BigDecimal amount, Challenge challenge, Donation donation)
  {
    this.setId(id);
    this.setAmount(amount);
    this.setChallenge(challenge);
    this.setDonation(donation);
  }
  
  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public void setDonation(Donation donation)
  {/*
    if (this.donation != null)
    {
      this.donation.getBids().remove(this);
    }
    
    if (donation != null)
    {
      donation.getBids().add(this);
    }
    */
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
    this.amount = amount;
  }

  @Override
  public BidType getType()
  {
    return BidType.CHALLENGE;
  }

  public void setChallenge(Challenge challenge)
  {
    this.challenge = challenge;
  }

  public Challenge getChallenge()
  {
    return challenge;
  }
  
  public int hashCode()
  {
    return this.getId();
  }
  
  public boolean equals(Object other)
  {
    if (other instanceof ChallengeBid)
    {
      return this.getId() == ((ChallengeBid)other).getId();
    }
    else
    {
      return false;
    }
  }
}
