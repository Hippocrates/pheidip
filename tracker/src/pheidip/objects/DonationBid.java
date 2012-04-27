package pheidip.objects;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

public abstract class DonationBid extends Entity
{
  @Getter @Setter @NotNull @DecimalMin("0.00")
  private BigDecimal amount;
  
  @Getter @Setter @NotNull
  private Donation donation;
  
  public String getDonorName()
  {
    return this.donation.getDonor().toString();
  }

  public abstract BidType getBidType();
}
