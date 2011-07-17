package pheidip.objects;

import pheidip.util.StringUtils;

public class Choice implements Bid
{
	private String name;
  private int id;
  private int speedRunId;
  private String description;
  private BidState bidState;

	public Choice(int id, String name, String description, BidState bidState, int speedRunId)
	{
	  this.id = id;
		this.name = name == null ? null : name.toLowerCase();
		this.speedRunId = speedRunId;
		this.description = description;
		this.bidState = bidState;
	}
	
	public String getName()
	{
		return this.name;
	}

  public int getSpeedRunId()
  {
    return speedRunId;
  }

  public int getId()
  {
    return id;
  }
  
  public String getDescription()
  {
    return this.description;
  }

  @Override
  public BidType getType()
  {
    return BidType.CHOICE;
  }
  
  public BidState getBidState()
  {
    return this.bidState;
  }
  
  public String toString()
  {
    return "Choice : " + (StringUtils.isEmptyOrNull(this.name) ? ("#" + this.id) : this.name);
  }
}
