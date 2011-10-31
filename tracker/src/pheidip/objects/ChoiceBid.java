package pheidip.objects;

import java.math.BigDecimal;

import pheidip.util.IdUtils;

public class ChoiceBid implements DonationBid
{
  private int id;
  private BigDecimal amount;
  private int optionId;
  private int donationId;
  private Donation donation;
  private ChoiceOption option;
  
  public ChoiceBid()
  {
    this.id = IdUtils.generateId();
  }
  
  public ChoiceBid(int id, BigDecimal amount, int optionId, int donationId)
  {
    this.setId(id);
    this.setDonationId(donationId);
    this.setOptionId(optionId);
    this.setAmount(amount);
  }
  
  public ChoiceBid(int id, BigDecimal amount, ChoiceOption option, Donation donation)
  {
    this.setId(id);
    this.setAmount(amount);
    this.setOption(option);
    this.setDonation(donation);
  }

  public BigDecimal getAmount()
  {
    return amount;
  }

  public void setOption(ChoiceOption option)
  {
    this.option = option;
  }

  public ChoiceOption getOption()
  {
    return option;
  }

  public void setAmount(BigDecimal amount)
  {
    this.amount = amount;
  }

  public int getOptionId()
  {
    return optionId;
  }

  public void setOptionId(int optionId)
  {
    this.optionId = optionId;
  }

  public int getDonationId()
  {
    return donationId;
  }

  public void setDonationId(int donationId)
  {
    this.donationId = donationId;
  }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public void setDonation(Donation donation)
  {/*
    if (this.donation != null)
    {
      this.donation.getBids().remove(this);
    }
    
    if (donation != null)
    {
      donation.getBids().add(this);
    }
    */
    this.donation = donation;
  }

  public Donation getDonation()
  {
    return donation;
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
