package pheidip.objects;

import java.math.BigDecimal;

public interface DonationBid
{
  BidType getType();
  
  int getId();
  void setId(int id);
  BigDecimal getAmount();
  void setAmount(BigDecimal amount);
  int getDonationId();
  void setDonationId(int donationId);
  Donation getDonation();
  void setDonation(Donation donation);
}
