package pheidip.objects;

import java.math.BigDecimal;

import pheidip.util.IdUtils;

public class ChallengeBid implements DonationBid
{
  private int id = IdUtils.generateId();
  private BigDecimal amount;
  private int challengeId;
  private int donationId;
  private Donation donation;
  private Challenge challenge;
  
  public ChallengeBid()
  {
  }
  
  public ChallengeBid(int id, BigDecimal amount, int challengeId, int donationId)
  {
    this.setDonationId(donationId);
    this.setChallengeId(challengeId);
    this.setAmount(amount);
    this.setId(id);
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
    this.amount = amount;
  }

  public int getChallengeId()
  {
    return challengeId;
  }

  public void setChallengeId(int challengeId)
  {
    this.challengeId = challengeId;
  }

  public int getDonationId()
  {
    return donationId;
  }

  public void setDonationId(int donationId)
  {
    this.donationId = donationId;
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
