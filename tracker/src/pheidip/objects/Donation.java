package pheidip.objects;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

public class Donation extends Entity
{
  @Getter @Setter @NotNull
  private Date timeReceived = new Date();
  
  @Getter @Setter @NotNull @DecimalMin("0.00")
  private BigDecimal amount = new BigDecimal("0.00");
  
  @Getter @Setter
  private String comment;
  
  @Getter @Setter @NotNull 
  private DonationDomain domain;
  
  @Getter @Setter @NotNull @Size(min=1, max=64)
  private String domainId;
  
  @Getter @Setter @NotNull 
  private DonationBidState bidState = DonationBidState.PENDING;
  
  @Getter @Setter @NotNull 
  private DonationReadState readState = DonationReadState.PENDING;
  
  @Getter @Setter @NotNull 
  private DonationCommentState commentState = DonationCommentState.PENDING;
  
  @Getter @Setter @NotNull
  private Set<DonationBid> bids = new HashSet<DonationBid>();
  
  @Getter @Setter @NotNull
  private Donor donor;
  
  @Override
  public String toString()
  {
    return this.domain.toString() + ":" + this.donor.toString() + ":" + this.getAmount().toString();
  }
}
