package pheidip.objects;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

public class ChoiceOption extends Entity
{
  @Getter @Setter @NotNull @Size(min=1, max=255)
  private String name;
  
  @Getter @Setter @NotNull 
  private Choice choice;
  
  @Getter @Setter @DecimalMin("0.00")
  private BigDecimal totalCollected;
  
  @Getter @Setter @NotNull 
  private Set<ChoiceBid> bids = new HashSet<ChoiceBid>();
}
