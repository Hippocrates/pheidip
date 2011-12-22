package pheidip.objects;

import java.math.BigDecimal;

public class ChoiceBid extends DonationBid
{
  private ChoiceOption option;
  
  public ChoiceBid()
  {
  }

  public ChoiceBid(int id, BigDecimal amount, ChoiceOption option, Donation donation)
  {
    super(id, amount, donation);
    this.setOption(option);
  }

  public void setOption(ChoiceOption option)
  {
    this.option = option;
  }

  public ChoiceOption getOption()
  {
    return option;
  }

  @Override
  public BidType getType()
  {
    return BidType.CHOICE;
  }
  
  public int hashCode()
  {
    return this.getId();
  }
  
  public boolean equals(Object other)
  {
    if (other instanceof ChoiceBid)
    {
      return this.getId() == ((ChoiceBid)other).getId();
    }
    else
    {
      return false;
    }
  }
}
