package pheidip.objects;

import java.util.EnumSet;

import lombok.Getter;
import lombok.Setter;

public class DonationSearchParams
{
  @Getter @Setter
  private EnumSet<DonationDomain> domain;
  
  @Getter @Setter
  private EnumSet<DonationBidState> bidState;
}
