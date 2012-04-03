package pheidip.objects;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

public class Challenge extends Bid
{
  @Getter @Setter @NotNull @DecimalMin("0.00")
  private BigDecimal goalAmount;
  
  @Getter @Setter @DecimalMin("0.00")
  private BigDecimal totalCollected;
  
  @Getter @Setter @NotNull
  private Set<ChallengeBid> bids = new HashSet<ChallengeBid>();

  @Override
  public BidType bidType()
  {
    return BidType.CHALLENGE;
  }
}
