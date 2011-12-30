package pheidip.objects;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Challenge extends Bid
{
  private BigDecimal goalAmount = new BigDecimal("0.00");
  private Set<ChallengeBid> bids = new HashSet<ChallengeBid>();
  
  public Challenge()
  {
  }

  public Challenge(int id, String name, BigDecimal goalAmount, String description, BidState bidState, SpeedRun speedRun)
  {
    this.setId(id);
    this.setName(name);
    this.setGoalAmount(goalAmount);
    this.setDescription(description);
    this.setBidState(bidState);
    this.setSpeedRun(speedRun);
  }

  public BigDecimal getGoalAmount()
  {
    return this.goalAmount;
  }

  public void setGoalAmount(BigDecimal goalAmount)
  {
    if (goalAmount == null || goalAmount.compareTo(BigDecimal.ZERO) < 0)
    {
      throw new RuntimeException("Goal amount must be greater than or equal to zero.");
    }
    
    BigDecimal oldGoalAmount = this.goalAmount;
    this.goalAmount = goalAmount.setScale(2);
    this.firePropertyChange("goalAmount", oldGoalAmount, this.goalAmount);
  }

  @Override
  public BidType getType()
  {
    return BidType.CHALLENGE;
  }
  
  public String toString()
  {
    return "Challenge: " + this.getName();
  }
  
  public boolean equals(Object other)
  {
    if (other instanceof Challenge)
    {
      return this.getId() == ((Challenge)other).getId();
    }
    else
    {
      return false;
    }
  }

  public void setBids(Set<ChallengeBid> bids)
  {
    this.bids = bids;
  }

  public Set<ChallengeBid> getBids()
  {
    return bids;
  }
}
