package pheidip.objects;

import java.math.BigDecimal;

public class ChallengeBid extends DonationBid
{
  private Challenge challenge;
  
  public ChallengeBid()
  {
  }
  
  public ChallengeBid(int id, BigDecimal amount, Challenge challenge, Donation donation)
  {
    super(id, amount, donation);
    this.setChallenge(challenge);
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
