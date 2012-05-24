package pheidip.logic.chipin;

import java.util.List;

import org.jsoup.Jsoup;

public class ChipinTextDonationSource implements ChipinDonationSource
{
  private String htmlText;
  
  public ChipinTextDonationSource(String htmlText)
  {
    this.htmlText = htmlText;
  }

  @Override
  public List<ChipinDonation> provideChipinDonations()
  {
    return ChipinDonations.extractDonations(Jsoup.parse(this.htmlText));
  }
}
