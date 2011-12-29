package pheidip.logic;

import java.math.BigDecimal;
import java.util.List;

import pheidip.objects.Donation;
import pheidip.objects.Donor;

public class PrizeDrawCandidate
{
  private Donor donor;
  List<Donation> donationList;
  private BigDecimal donationSum;
  private BigDecimal maxDonation;
  private boolean alreadyWon;
  
  public PrizeDrawCandidate(Donor donor, List<Donation> donationList)
  {
    this.donor = donor;
    this.donationList = donationList;
    
    this.donationSum = BigDecimal.ZERO;
    this.maxDonation = BigDecimal.ZERO;
    
    for (Donation d : donationList)
    {
      if (d.getAmount().compareTo(this.maxDonation) > 0)
      {
        this.maxDonation = d.getAmount();
      }
      
      this.donationSum = this.donationSum.add(d.getAmount());
    }
    
    this.alreadyWon = this.donor.getPrizes().size() > 0;
  }
  
  public Donor getDonor()
  {
    return this.donor;
  }
  
  public BigDecimal getMaxDonation()
  {
    return this.maxDonation;
  }
  
  public BigDecimal getDonationSum()
  {
    return this.donationSum;
  }
  
  public boolean alreadyHasPrize()
  {
    return this.alreadyWon;
  }
}
