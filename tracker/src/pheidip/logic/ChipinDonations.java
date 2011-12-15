package pheidip.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import pheidip.objects.ChipinDonation;
import pheidip.objects.Donation;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationCommentState;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationReadState;
import pheidip.objects.Donor;
import pheidip.util.IdUtils;
import pheidip.util.StringUtils;

public final class ChipinDonations
{
  public static final String CONTRIBUTOR_TABLE_ID = "contributortable";
  public static final BigDecimal SIGNIFIGANCE_THRESHOLD = new BigDecimal("1.00");

  private static final int NAME_INDEX = 0;
  private static final int EMAIL_INDEX = 1;
  // I have no idea what goes in the second slot
  private static final int COMMENT_INDEX = 3;
  private static final int CHIPIN_ID_INDEX = 4;
  private static final int AMOUNT_INDEX = 5;

  public static List<ChipinDonation> extractDonations(Document htmlDoc)
  {
    List<ChipinDonation> result = new ArrayList<ChipinDonation>();

    Element contributorTable = htmlDoc.getElementById(CONTRIBUTOR_TABLE_ID);

    List<Element> rowElements = contributorTable.getElementsByTag("tr");

    for (Element element : rowElements)
    {
      List<Element> donationElements = element.getElementsByTag("td");

      result.add(new ChipinDonation(donationElements.get(NAME_INDEX).ownText()
          .trim(), donationElements.get(EMAIL_INDEX).ownText().trim(),
          donationElements.get(COMMENT_INDEX).ownText().trim(),
          donationElements.get(CHIPIN_ID_INDEX).ownText().trim(),
          new BigDecimal(donationElements.get(AMOUNT_INDEX).ownText().trim())));
    }

    return result;
  }
  
  public static Map<String, ChipinDonation> mapDonations(List<ChipinDonation> donations)
  {
    Map<String, ChipinDonation> result = new HashMap<String, ChipinDonation>();
    
    for (ChipinDonation donation : donations)
    {
      result.put(donation.getChipinId(), donation);
    }
    
    return result;
  }
  
  public static void buildMergeTables(List<ChipinDonation> chipinDonations, List<Donation> databaseDonations, List<Donor> allDonors, List<Donor> donorsToInsert, List<Donation> donationsToInsert, List<Donation> donationsToUpdate)
  {
    Map<String, Donor> donorTable = generateDonorSet(allDonors);
    Map<String, ChipinDonation> chipinDonationMap = mapDonations(chipinDonations);

    for (Donation donation : databaseDonations)
    {
      ChipinDonation found = chipinDonationMap.remove(donation.getDomainId());
      
      if (found != null && !StringUtils.isEmptyOrNull(found.getComment()) && StringUtils.isEmptyOrNull(donation.getComment()))
      {
        donation.setComment(found.getComment());
        donation.setReadState(DonationReadState.PENDING);
        donation.setBidState(DonationBidState.PENDING);
        donationsToUpdate.add(donation);
      }
    }
    
    for (ChipinDonation chipinDonation : chipinDonationMap.values())
    {
      Donor donor = donorTable.get(chipinDonation.getEmail());
      
      if (donor == null)
      {
        String[] toks = chipinDonation.getName().trim().split("\\s+");
        donor = new Donor();
        donor.setEmail(chipinDonation.getEmail());
        
        if (toks.length > 0)
        {
          StringBuilder builder = new StringBuilder();
          
          for (int i = 0; i < toks.length - 1; ++i)
          {
            builder.append(toks[i]);
          }
          donor.setFirstName(builder.toString());

          donor.setLastName(toks[toks.length - 1]);
        }
        
        donorTable.put(donor.getEmail(), donor);
        donorsToInsert.add(donor);
      }
      
      String commentString = chipinDonation.getComment();

      if (StringUtils.emptyIfNull(chipinDonation.getComment()).length() > ChipinDonation.MAX_COMMENT_LENGTH)
      {
        commentString = commentString.substring(0,
            ChipinDonation.MAX_COMMENT_LENGTH - 1);
        System.out.println("Warning, truncating comment with length > "
            + ChipinDonation.MAX_COMMENT_LENGTH);
      }
      
      boolean signifigant = chipinDonation.getAmount().compareTo(SIGNIFIGANCE_THRESHOLD) >= 0;
      boolean hasComment = !StringUtils.isEmptyOrNull(commentString);
      
      donationsToInsert.add(new Donation(IdUtils.generateId(),
          DonationDomain.CHIPIN, 
          chipinDonation.getChipinId(),
          signifigant && hasComment ? DonationBidState.PENDING : DonationBidState.IGNORED, 
          signifigant || hasComment ? DonationReadState.PENDING : DonationReadState.IGNORED,
          DonationCommentState.PENDING, 
          chipinDonation.getAmount(),
          chipinDonation.getTimeStamp(), 
          donor, StringUtils.nullIfEmpty(commentString)));
    }
  }

  private static Map<String, Donor> generateDonorSet(List<Donor> all)
  {
    Map<String, Donor> table = new HashMap<String, Donor>();

    for (Donor d : all)
    {
      table.put(d.getEmail(), d);
    }

    return table;
  }
}
