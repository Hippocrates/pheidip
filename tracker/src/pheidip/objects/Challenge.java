package pheidip.objects;

import java.math.BigDecimal;

import pheidip.util.StringUtils;

public class Challenge implements Bid
{
  private int id;
  private int speedRunId;
  private String name;
  private BigDecimal goalAmount;
  
  public Challenge(int id, String name, BigDecimal goalAmount, int speedRunId)
  {
    this.id = id;
    this.name = name == null ? null : name.toLowerCase();
    this.speedRunId = speedRunId;
    this.goalAmount = goalAmount;
  }

  public String getName()
  {
    return name;
  }

  public int getSpeedRunId()
  {
    return speedRunId;
  }

  public int getId()
  {
    return id;
  }
  
  public BigDecimal getGoalAmount()
  {
    return this.goalAmount;
  }

  @Override
  public BidType getType()
  {
    return BidType.CHALLENGE;
  }
  
  public String toString()
  {
    return "Challenge: " + (StringUtils.isEmptyOrNull(this.name) ? ("#" + this.id) : this.name);
  }
}
