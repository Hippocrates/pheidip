package pheidip.objects;

public class Choice implements Bid
{
	private String name;
  private int id;
  private int speedRunId;

	public Choice(int id, String name, int speedRunId)
	{
	  this.id = id;
		this.name = name;
		this.speedRunId = speedRunId;
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

  @Override
  public BidType getType()
  {
    return BidType.CHOICE;
  }
}
