package pheidip.objects;

import java.math.BigDecimal;

public class Challenge implements Bid
{
  private int id;
  private int speedRunId;
  private String name;
  private BigDecimal goalAmount;
  
  public Challenge(int id, String name, BigDecimal goalAmount, int speedRunId)
  {
    this.id = id;
    this.name = name;
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
}
