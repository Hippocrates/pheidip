package pheidip.objects;

import java.util.HashSet;
import java.util.Set;

public class Choice extends Bid
{
  private Set<ChoiceOption> options = new HashSet<ChoiceOption>();

  public Choice()
  {
  }
  
	public Choice(int id, String name, String description, BidState bidState, SpeedRun speedRun)
  {
    this.setId(id);
    this.setName(name);
    this.setDescription(description);
    this.setBidState(bidState);
    this.setSpeedRun(speedRun);
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
    return "Choice : " + this.getName();
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
}
