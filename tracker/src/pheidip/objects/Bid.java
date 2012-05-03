package pheidip.objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

public abstract class Bid extends Entity
{
  @Getter @Setter @NotNull @Size(min=1, max=255)
  private String name = "" + this.getId();
  
  @Getter @Setter @NotNull @Size(min=0, max=1024)
  private String description = "";
  
  @Getter @Setter @NotNull 
  private BidState bidState = BidState.HIDDEN;
  
  @Getter @Setter
  private SpeedRun speedRun;
  
  public abstract BidType bidType();
  
  
  @Override
  public String toString()
  {
    return this.bidType().toString() + ":" + this.getSpeedRun().getName() + ":" + this.getName();
  }
}
