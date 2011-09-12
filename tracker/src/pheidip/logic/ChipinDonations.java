package pheidip.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import pheidip.db.DonationData;
import pheidip.db.DonationDataConstraint;
import pheidip.db.DonationDataConstraintException;
import pheidip.db.DonorData;
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
  private static final int NUM_ID_RETRIES = 5;
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
          donationElements.get(NAME_INDEX).ownText().trim(),
          donationElements.get(EMAIL_INDEX).ownText().trim(),
          donationElements.get(COMMENT_INDEX).ownText().trim(),
          donationElements.get(CHIPIN_ID_INDEX).ownText().trim(),
          new BigDecimal(donationElements.get(AMOUNT_INDEX).ownText().trim())
          )
      );
    }
    
    return result;
  }
  
  public static Map<String,Donation> generateDonationSet(DonationData donations)
  {
    Map<String,Donation> table = new HashMap<String,Donation>();
    
    List<Donation> all = donations.getAllDonations();
    
    for (Donation d : all)
    {
      if (d.getDomain() == DonationDomain.CHIPIN)
      {
        table.put(d.getDomainId(), d);
      }
    }
    
    return table;
  }
  
  public static Map<String,Donor> generateDonorSet(DonorData donors)
  {
    Map<String,Donor> table = new HashMap<String,Donor>();
    
    List<Donor> all = donors.getAllDonors();
    
    for (Donor d : all)
    {
      table.put(d.getEmail(), d);
    }
    
    return table;
  }
  
  public static void mergeDonations(DonationDatabaseManager manager, List<ChipinDonation> chipinDonations)
  {
    DonationData donations = manager.getDataAccess().getDonationData();
    DonorData donors = manager.getDataAccess().getDonorData();
    
    Map<String,Donation> donationTable = generateDonationSet(donations);
    Map<String,Donor> donorTable = generateDonorSet(donors);
    
    for (ChipinDonation d : chipinDonations)
    {
      mergeDonation(manager, d, donorTable, donationTable);
    }
  }
  
  public static void mergeDonation(DonationDatabaseManager manager, ChipinDonation chipinDonation, Map<String, Donor> donorTable, Map<String, Donation> donationTable)
  {
    DonationData donations = manager.getDataAccess().getDonationData();
    DonorData donors = manager.getDataAccess().getDonorData();

    Donation found = donationTable.get(chipinDonation.getChipinId());
    
    if (found == null)
    {
      Donor donor = donorTable.get(chipinDonation.getEmail());
      
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
          
        int retryCount = 0;

        while (retryCount < NUM_ID_RETRIES)
        {
          try
          {
            int newDonorId = IdUtils.generateId();
              
            donor = new Donor(newDonorId, chipinDonation.getEmail(), null, firstName, lastName);
            donors.createDonor(donor);
            donorTable.put(chipinDonation.getEmail(), donor);
            retryCount = NUM_ID_RETRIES;
          }
          catch(DonationDataConstraintException e)
          {
            if (e.getConstraintViolation() == DonationDataConstraint.DonorPK)
            {
              donor = null;
              ++retryCount;
            }
            else
            {
              retryCount = NUM_ID_RETRIES;
              donor = null;
            }
          }
        }
      }
      
      if (donor != null)
      {
        String commentString = chipinDonation.getComment();
        
        if (StringUtils.emptyIfNull(chipinDonation.getComment()).length() > ChipinDonation.maxCommentLength())
        {
          commentString = commentString.substring(0, ChipinDonation.maxCommentLength() - 1);
          System.out.println("Warning, truncating comment with length > " + ChipinDonation.maxCommentLength());
        }
        
        int retryCount = 0;

        while (retryCount < NUM_ID_RETRIES)
        {
          try
          {
            int newDonationId = IdUtils.generateId();
            
            donations.insertDonation(
              new Donation(
                newDonationId,
                DonationDomain.CHIPIN, 
                chipinDonation.getChipinId(),
                DonationBidState.PENDING, 
                DonationReadState.PENDING,
                DonationCommentState.PENDING,
                chipinDonation.getAmount(),
                chipinDonation.getTimeStamp(),
                donor.getId(),
                StringUtils.nullIfEmpty(commentString)
                )
              );
            retryCount = NUM_ID_RETRIES;
          }
          catch(DonationDataConstraintException e)
          {
            if (e.getConstraintViolation() == DonationDataConstraint.DonationPK)
            {
              ++retryCount;
            }
            else
            {
              retryCount = NUM_ID_RETRIES;
            }
          }
        }
      }
    }
    else
    {
      if (StringUtils.isEmptyOrNull(found.getComment()) && !StringUtils.isEmptyOrNull(chipinDonation.getComment()))
      {
        donations.setDonationComment(found.getId(), chipinDonation.getComment());
      }
    }
  }
}
