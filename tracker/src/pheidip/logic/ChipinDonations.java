package pheidip.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import pheidip.db.DonationData;
import pheidip.db.DonorData;
import pheidip.objects.ChipinDonation;
import pheidip.objects.Donation;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationDomain;
import pheidip.objects.Donor;
import pheidip.util.IdUtils;
import pheidip.util.StringUtils;

public final class ChipinDonations
{
  public static final String CONTRIBUTOR_TABLE_ID = "contributortable";
  
  private static final int NAME_INDEX = 0;
  private static final int EMAIL_INDEX = 1;
  // I have no idea what goes in the second slot
  private static final int COMMENT_INDEX = 3;
  private static final int CHIPIN_ID_INDEX = 4;
  private static final int AMOUNT_INDEX = 5;
  
  static public List<ChipinDonation> extractDonations(Document htmlDoc)
  {
    List<ChipinDonation> result = new ArrayList<ChipinDonation>();
    
    Element contributorTable = htmlDoc.getElementById(CONTRIBUTOR_TABLE_ID);
    
    List<Element> rowElements = contributorTable.getElementsByTag("tr");
    
    for (Element element : rowElements)
    {
      List<Element> donationElements = element.getElementsByTag("td");

      result.add(new ChipinDonation(
          donationElements.get(NAME_INDEX).ownText(),
          donationElements.get(EMAIL_INDEX).ownText(),
          donationElements.get(COMMENT_INDEX).ownText(),
          donationElements.get(CHIPIN_ID_INDEX).ownText(),
          new BigDecimal(donationElements.get(AMOUNT_INDEX).ownText())
          )
      );
    }
    
    return result;
  }

  public static void mergeDonations(DonationDatabaseManager manager, List<ChipinDonation> chipinDonations)
  {
    DonationData donations = manager.getDataAccess().getDonationData();
    DonorData donors = manager.getDataAccess().getDonorData();

    int baseDonorId = IdUtils.generateId();
    int baseDonationId = IdUtils.generateId();
    
    for (ChipinDonation chipinDonation : chipinDonations)
    {
      Donation found = donations.getDonationByDomainId(DonationDomain.CHIPIN, chipinDonation.getChipinId());
      
      if (found == null)
      {
        Donor donor = donors.getDonorByEmail(chipinDonation.getEmail());
        
        if (donor == null)
        {
          String[] toks = chipinDonation.getName().trim().split("\\s+");
          String firstName = "";
          String lastName = "";
          
          if (toks.length > 0)
          {
            firstName = toks[0];
          }
          
          if (toks.length > 1)
          {
            lastName = toks[1];
          }
          
          donor = new Donor(baseDonorId, chipinDonation.getEmail(), null, firstName, lastName);
          donors.createDonor(donor);
          ++baseDonorId;
        }
        
        String commentString = chipinDonation.getComment();
        
        if (StringUtils.emptyIfNull(chipinDonation.getComment()).length() > ChipinDonation.maxCommentLength())
        {
          commentString = commentString.substring(0, ChipinDonation.maxCommentLength() - 1);
          System.out.println("Warning, truncating comment with length > " + ChipinDonation.maxCommentLength());
        }
        
        donations.insertDonation(
          new Donation(
              baseDonationId,
            DonationDomain.CHIPIN, 
            chipinDonation.getChipinId(),
            DonationBidState.PENDING, 
            chipinDonation.getAmount(),
            new Date(),
            donor.getId(),
            commentString
            )
          );
        ++baseDonationId;
      }
      else if (found.getComment() == null && chipinDonation.getComment() != null)
      {
        donations.setDonationComment(found.getId(), chipinDonation.getComment());
      }
    }
  }
  
}
