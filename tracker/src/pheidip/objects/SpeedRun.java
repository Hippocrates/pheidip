package pheidip.objects;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

public class SpeedRun extends Entity
{
  @Getter @Setter @NotNull @Size(min=1,max=255)
  private String name = "" + this.getId();
  
  @Getter @Setter @NotNull @Size(min=0, max=1024)
  private String description = "";
  
  @Getter @Setter @NotNull @Size(min=0, max=255)
  private String runners = "";
  
  @Getter @Setter @NotNull
  private Date startTime = new Date();
  
  @Getter @Setter @NotNull
  private Date endTime = new Date();
  
  @Getter @Setter @NotNull
  private int sortKey = this.getId();
  
  @Getter @Setter @NotNull
  private Set<Bid> bids = new HashSet<Bid>();
  
  @Getter @Setter @NotNull
  private Set<Prize> prizeStartGame = new HashSet<Prize>();
  
  @Getter @Setter @NotNull
  private Set<Prize> prizeEndGame = new HashSet<Prize>();
  
  @Override
  public String toString()
  {
    return this.getName();
  }
}
