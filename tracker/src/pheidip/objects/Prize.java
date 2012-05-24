package pheidip.objects;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

public class Prize extends Entity
{
  @Getter @Setter @NotNull @Size(min=1, max=255)
  private String name = "" + this.getId();
  
  @Getter @Setter @NotNull @Size(min=0, max=1024)
  private String description = "";
  
  @Getter @Setter @Size(min=0, max=255)
  private String imageURL;
  
  @Getter @Setter
  private SpeedRun startGame;
  
  @Getter @Setter
  private SpeedRun endGame;
  
  @Getter @Setter @NotNull
  private int sortKey = this.getId();
  
  @Getter @Setter
  private Donor winner;
  
  @Getter @Setter @NotNull
  private PrizeDrawMethod drawMethod = PrizeDrawMethod.RANDOM_UNIFORM_DRAW;
  
  @Getter @Setter @NotNull @DecimalMin("0.00")
  private BigDecimal minimumBid = new BigDecimal("5.00");
  
  @Override
  public String toString()
  {
    return "" + this.name;
  }
}
