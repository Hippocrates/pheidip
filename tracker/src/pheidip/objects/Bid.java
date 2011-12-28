package pheidip.objects;

import pheidip.util.StringUtils;

public abstract class Bid extends Entity
{
  private String name;
  private String description;
  private BidState bidState;
  private SpeedRun speedRun;
  
  public abstract BidType getType();

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    String oldName = this.name;
    this.name = StringUtils.isEmptyOrNull(name) ? "#" + this.getId() : name.toLowerCase();
    this.firePropertyChange("name", oldName, this.name);
  }
  
  public String getDescription()
  {
    return this.description;
  }

  public void setDescription(String description)
  {
    String oldDescription = this.description;
    this.description = StringUtils.emptyIfNull(description);
    this.firePropertyChange("description", oldDescription, this.description);
  }

  public BidState getBidState()
  {
    return this.bidState;
  }
  
  public void setBidState(BidState bidState)
  {
    BidState oldBidState = this.bidState;
    this.bidState = bidState;
    this.firePropertyChange("bidState", oldBidState, this.bidState);
  }

  public SpeedRun getSpeedRun()
  {
    return speedRun;
  }
  
  public void setSpeedRun(SpeedRun speedRun)
  {
    SpeedRun oldSpeedRun = this.speedRun;
    this.speedRun = speedRun;
    this.firePropertyChange("speedRun", oldSpeedRun, this.speedRun);
  }
}
