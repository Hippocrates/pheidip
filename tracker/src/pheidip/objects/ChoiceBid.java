package pheidip.objects;

import java.math.BigDecimal;

public class ChoiceBid implements DonationBid
{
  private int id;
  private BigDecimal amount;
  private int optionId;
  private int donationId;
  
  public ChoiceBid(int id, BigDecimal amount, int optionId, int donationId)
  {
    this.id = id;
    this.donationId = donationId;
    this.optionId = optionId;
    this.amount = amount;
  }

  public BigDecimal getAmount()
  {
    return amount;
  }

  public int getOptionId()
  {
    return optionId;
  }

  public int getDonationId()
  {
    return donationId;
  }

  public int getId()
  {
    return id;
  }

  @Override
  public BidType getType()
  {
    return BidType.CHOICE;
  }
}
