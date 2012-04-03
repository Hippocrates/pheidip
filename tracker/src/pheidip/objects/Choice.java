package pheidip.objects;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

public class Choice extends Bid
{
  @Getter @Setter @NotNull
  private Set<ChoiceOption> options = new HashSet<ChoiceOption>();
  
  @Override
  public BidType bidType()
  {
    return BidType.CHOICE;
  }
}
