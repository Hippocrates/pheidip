package test.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import pheidip.db.DonationData;
import pheidip.logic.ChipinDonations;
import pheidip.logic.DonationDatabaseManager;
import pheidip.objects.ChipinDonation;
import pheidip.objects.Donation;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationDomain;
import pheidip.objects.Donor;
import pheidip.util.StringUtils;

public class ChipinTestUtils
{
  public static List<ChipinDonation> generateRandomDonations(int numDonors, int numDonations, Random rand)
  {
    List<ChipinDonation> result = new ArrayList<ChipinDonation>();
    
    String[] possibleTopLevelDomains = new String[]
    {
      "com","ca","uk","jp","au","fr","de","edu","org","net"
    };

    String[] donorNames = new String[numDonors];
    String[] donorEmails = new String[numDonors];
    
    for (int i = 0; i < numDonors; ++i)
    {
      donorNames[i] = StringUtils.randomName(4, 25, rand) + " " + StringUtils.randomName(4, 25, rand);
      
      donorEmails[i] = StringUtils.randomEmail(4, 25, 4, 25, possibleTopLevelDomains[rand.nextInt(possibleTopLevelDomains.length)], rand);
    }
    
    Date currenttime = new Date();
    
    for (int i = 0; i < numDonations; ++i)
    {
      // I'm too lazy to implement a proper uniform usage system
      int donor = i % numDonors;
      
      String amount = StringUtils.randomDollarAmountString(1, 5, rand);
      
      String comment = StringUtils.randomStringOverAlphabet(0, 1000, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789()@:.,! ;'\"-?_", rand);
    
      result.add(new ChipinDonation(donorNames[donor], donorEmails[donor], comment, ""+(currenttime.getTime()+rand.nextInt(1000000000)), new BigDecimal(amount)));
    }
    
    return result;
  }
  
  public static String generateChipinHTMLTable(List<ChipinDonation> donations)
  {
    StringBuilder builder = new StringBuilder();
    
    builder.append("<table id=\"" + ChipinDonations.CONTRIBUTOR_TABLE_ID + "\" style=\"display:none\">\n\n");
    
    for (ChipinDonation donation : donations)
    {
      builder.append("\t<tr>\n");
      
      builder.append("\t\t<td>");
      builder.append(donation.getName());
      builder.append("\t\t</td>\n");
      
      builder.append("\t\t<td>");
      builder.append(donation.getEmail());
      builder.append("\t\t</td>\n");
      
      builder.append("\t\t<td></td>\n");
      
      builder.append("\t\t<td>");
      builder.append(StringUtils.emptyIfNull(donation.getComment()));
      builder.append("\t\t</td>\n");
      
      builder.append("\t\t<td>");
      builder.append(donation.getChipinTimeString());
      builder.append("</td>\n");
      
      builder.append("\t\t<td>");
      builder.append(donation.getAmount().toString());
      builder.append("\t\t</td>\n");
      
      builder.append("\t\t<td>??????</td>\n");
      builder.append("\t\t<td>donations@speeddemosarchive.com</td>\n");
      
      builder.append("\t</tr>\n\n");
    }
    
    builder.append("</table>\n");
    
    return builder.toString();
  }

  public static boolean checkAllDonationsAreInDatabase(List<ChipinDonation> sourceDonations, DonationDatabaseManager manager)
  {
    DonationData donations = manager.getDataAccess().getDonationData();

    // now try to get some of the donations by ID and check that everything checks out
    // also check that there are no duplicates of donors

    for (ChipinDonation cDonation : sourceDonations)
    {
      Donation donation = donations.getDonationByDomainId(DonationDomain.CHIPIN, cDonation.getChipinId());
      
      if (donation == null)
      {
        return false;
      }
      
      if (!cDonation.getAmount().equals(donation.getAmount()))
      {
        return false;
      }
      
      if (!cDonation.getChipinId().equals(donation.getDomainId()))
      {
        return false;
      }
      
      if (DonationBidState.PENDING != donation.getBidState())
      {
        return false;
      }
      
      Donor donor = donation.getDonor();
      
      if (!cDonation.getEmail().equals(donor.getEmail()))
      {
        return false;
      }
      
      String[] toks = cDonation.getName().trim().split("\\s+");
      
      if (toks.length == 2)
      {
        if (!toks[0].equals(donor.getFirstName()))
        {
          return false;
        }
        
        if (!toks[1].equals(donor.getLastName()))
        {
          return false;
        }
      }
    }
    
    return true;
  }
}
