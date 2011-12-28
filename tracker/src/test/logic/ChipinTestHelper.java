package test.logic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import junit.framework.TestCase;

import pheidip.db.DonationData;
import pheidip.db.DonorData;
import pheidip.logic.DonationDatabaseManager;
import pheidip.logic.chipin.ChipinDonation;
import pheidip.logic.chipin.ChipinDonations;
import pheidip.objects.Donation;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationDomain;
import pheidip.objects.Donor;

public class ChipinTestHelper extends TestCase
{
  public void checkAllDonationsAreInDatabase(List<ChipinDonation> sourceDonations, DonationDatabaseManager manager)
  {
    DonationData donations = manager.getDataAccess().getDonationData();
    DonorData donors = manager.getDataAccess().getDonorData();

    List<Donation> allDonations = donations.getAllDonations();
    
    Map<String, Donor> mappedDonors = ChipinDonations.generateDonorSet(donors.getAllDonors());
    Map<String, Donation> mappedChipinDonations = new HashMap<String, Donation>();
    
    for (Donation d : allDonations)
    {
      if (d.getDomain() == DonationDomain.CHIPIN)
      {
        mappedChipinDonations.put(d.getDomainId(), d);
      }
    }

    for (ChipinDonation cDonation : sourceDonations)
    {
      Donation donation = mappedChipinDonations.get(cDonation.getChipinId());
      
      assertNotNull(donation);
      
      assertEquals(cDonation.getAmount().setScale(2), donation.getAmount().setScale(2));
      
      assertEquals(cDonation.getChipinId(), donation.getDomainId());
      
      if (cDonation.getComment() != null && cDonation.getAmount().compareTo(BigDecimal.ONE) >= 0)
      {
        assertEquals(DonationBidState.PENDING, donation.getBidState());
      }
      else
      {
        assertEquals(DonationBidState.IGNORED, donation.getBidState());
      }
      
      Donor mapped = mappedDonors.get(cDonation.getEmail());
      Donor donor = donation.getDonor();

      assertNotNull(mapped);
      assertEquals(mapped.getId(), donor.getId());

      String[] toks = cDonation.getName().trim().split("\\s+");
      
      if (toks.length == 2)
      {
        assertEquals(toks[0], mapped.getFirstName());
        assertEquals(toks[1], mapped.getLastName());
      }
    }
  }
}
