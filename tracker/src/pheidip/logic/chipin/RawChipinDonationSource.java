package pheidip.logic.chipin;

import java.util.Collections;
import java.util.List;

public class RawChipinDonationSource implements ChipinDonationSource
{
  private List<ChipinDonation> donations;

  public RawChipinDonationSource(List<ChipinDonation> chipinDonations)
  {
    this.donations = Collections.unmodifiableList(chipinDonations);
  }
  
  @Override
  public List<ChipinDonation> provideChipinDonations()
  {
    return this.donations;
  }
}
