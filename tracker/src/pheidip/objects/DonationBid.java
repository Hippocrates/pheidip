package pheidip.objects;

import java.math.BigDecimal;

public interface DonationBid
{
  int getId();
  BigDecimal getAmount();
  int getDonationId();
  BidType getType();
}
