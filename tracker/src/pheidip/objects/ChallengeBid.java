package pheidip.objects;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

public class ChallengeBid extends DonationBid
{
  @Getter @Setter @NotNull
  private Challenge challenge;

  @Override
  public BidType getBidType()
  {
    return BidType.CHALLENGE;
  }
}
