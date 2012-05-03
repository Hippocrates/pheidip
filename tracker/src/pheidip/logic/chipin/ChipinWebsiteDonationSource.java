package pheidip.logic.chipin;

import java.util.List;

public class ChipinWebsiteDonationSource implements ChipinDonationSource
{
  private ChipinLoginManager chipinLogin;

  public ChipinWebsiteDonationSource(ChipinLoginManager chipinLogin)
  {
    this.chipinLogin = chipinLogin;
  }

  @Override
  public List<ChipinDonation> provideChipinDonations()
  {
    return ChipinDonations.extractDonations(chipinLogin.getChipinPage());
  }
}
