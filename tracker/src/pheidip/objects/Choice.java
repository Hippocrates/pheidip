package pheidip.objects;

import pheidip.util.StringUtils;

public class Choice implements Bid
{
	private String name;
  private int id;
  private int speedRunId;
  private String description;

	public Choice(int id, String name, String description, int speedRunId)
	{
	  this.id = id;
		this.name = name == null ? null : name.toLowerCase();
		this.speedRunId = speedRunId;
		this.description = description;
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
  
  public String toString()
  {
    return "Choice : " + (StringUtils.isEmptyOrNull(this.name) ? ("#" + this.id) : this.name);
  }
}
