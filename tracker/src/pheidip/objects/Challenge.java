package pheidip.objects;

import java.math.BigDecimal;

import pheidip.util.IdUtils;
import pheidip.util.StringUtils;

public class Challenge implements Bid
{
  private int id = IdUtils.generateId();
  private String name;
  private BigDecimal goalAmount;
  private String description;
  private BidState bidState;
  private SpeedRun speedRun;
  
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

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name == null ? null : name.toLowerCase();
  }

  public int getId()
  {
    return id;
  }
  
  public void setId(int id)
  {
    this.id = id;
  }

  public BigDecimal getGoalAmount()
  {
    return this.goalAmount;
  }

  public void setGoalAmount(BigDecimal goalAmount)
  {
    this.goalAmount = goalAmount;
  }

  public String getDescription()
  {
    return this.description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public BidState getBidState()
  {
    return this.bidState;
  }
  
  public void setBidState(BidState bidState)
  {
    this.bidState = bidState;
  }

  @Override
  public BidType getType()
  {
    return BidType.CHALLENGE;
  }
  
  public String toString()
  {
    return "Challenge: " + (StringUtils.isEmptyOrNull(this.getName()) ? ("#" + this.getId()) : this.getName());
  }
  
  public int hashCode()
  {
    return this.getId();
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

  public void setSpeedRun(SpeedRun speedRun)
  {
    this.speedRun = speedRun;
  }

  public SpeedRun getSpeedRun()
  {
    return speedRun;
  }
}
