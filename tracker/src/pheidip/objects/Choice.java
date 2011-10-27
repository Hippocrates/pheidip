package pheidip.objects;

import java.util.HashSet;
import java.util.Set;

import pheidip.util.IdUtils;
import pheidip.util.StringUtils;

public class Choice implements Bid
{
	private String name;
  private int id = IdUtils.generateId();
  private Integer speedRunId;
  private String description;
  private BidState bidState;
  private Set<ChoiceOption> options = new HashSet<ChoiceOption>();
  private SpeedRun speedRun;

  public Choice()
  {
  }
  
	public Choice(int id, String name, String description, BidState bidState, Integer speedRunId)
	{
	  this.setId(id);
	  this.setName(name);
		this.setSpeedRunId(speedRunId);
		this.setDescription(description);
		this.setBidState(bidState);
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String name)
	{
	  this.name = name == null ? null : name.toLowerCase();
	}

  public Integer getSpeedRunId()
  {
    return speedRunId;
  }
  
  public void setSpeedRunId(int speedRunId)
  {
    this.speedRunId = speedRunId;
  }

  public int getId()
  {
    return id;
  }
  
  public void setId(int id)
  {
    this.id = id;
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

  public void setOptions(Set<ChoiceOption> options)
  {
    this.options = options;
  }

  public Set<ChoiceOption> getOptions()
  {
    return options;
  }

  @Override
  public BidType getType()
  {
    return BidType.CHOICE;
  }

  public String toString()
  {
    return "Choice : " + (StringUtils.isEmptyOrNull(this.name) ? ("#" + this.getId()) : this.name);
  }
  
  public int hashCode()
  {
    return this.getId();
  }
  
  public boolean equals(Object other)
  {
    if (other instanceof Choice)
    {
      return this.getId() == ((Choice)other).getId();
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
