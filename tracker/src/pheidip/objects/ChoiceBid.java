package pheidip.objects;

import javax.validation.constraints.NotNull;

import lombok.BoundPropertySupport;
import lombok.BoundSetter;
import lombok.Getter;

@BoundPropertySupport
public class ChoiceBid extends DonationBid
{
  @Getter @BoundSetter @NotNull
  private ChoiceOption choiceOption;
  
  @Override
  public BidType getBidType()
  {
    return BidType.CHOICE;
  }

}
