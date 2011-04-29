package pheidip.objects;

import java.math.BigDecimal;

public class ChallengeBid
{
  private int id;
  private BigDecimal amount;
  private int challengeId;
  private int donationId;
  
  public ChallengeBid(int id, BigDecimal amount, int challengeId, int donationId)
  {
    this.donationId = donationId;
    this.challengeId = challengeId;
    this.amount = amount;
    this.id = id;
  }

  public int getId()
  {
    return id;
  }

  public BigDecimal getAmount()
  {
    return amount;
  }

  public int getChallengeId()
  {
    return challengeId;
  }

  public int getDonationId()
  {
    return donationId;
  }
}
