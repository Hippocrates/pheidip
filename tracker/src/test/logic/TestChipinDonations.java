package test.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import pheidip.db.DonationData;
import pheidip.db.DonorData;
import pheidip.logic.ChipinDonations;
import pheidip.logic.DonationDatabaseManager;
import pheidip.objects.ChipinDonation;
import pheidip.objects.Donation;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationDomain;
import pheidip.objects.Donor;

public class TestChipinDonations extends TestCase
{
  public void testParseSimpleTable()
  {
    final String html = 
      "<table id=\"contributortable\" style=\"display:none\">\n" +
      "\t<tr>\n" + 
      "\t\t<td>Some Guy</td>\n" + 
      "\t\t<td>some@guy.com</td>\n" + 
      "\t\t<td></td>\n" + 
      "\t\t<td>A Chipin comment</td>\n" + 
      "\t\t<td>1262272179000</td>\n" + 
      "\t\t<td>1.00</td>\n" + 
      "\t\t<td>/profileimage/</td>\n" + 
      "\t\t<td>donations@speeddemosarchive.com</td>\n" + 
      "\t</tr>\n" + 
      "</table>\n";
    
    Document htmlDoc = Jsoup.parse(html);

    List<ChipinDonation> donations = ChipinDonations.extractDonations(htmlDoc);
    
    assertEquals(1, donations.size());
    assertEquals("Some Guy", donations.get(0).getName());
    assertEquals("some@guy.com", donations.get(0).getEmail());
    assertEquals("A Chipin comment", donations.get(0).getComment());
    assertEquals("1262272179000", donations.get(0).getChipinTimeString());
    assertEquals(new BigDecimal("1.00"), donations.get(0).getAmount());
  }
  /*
  public void testParseFullTable()
  {
    try
    {
      //TODO: replace this test w/ the random data
      Document htmlDoc = Jsoup.parse(new File("src/test/logic/agdq-chipin.html"), null);
      
      List<ChipinDonation> donations = ChipinDonations.extractDonations(htmlDoc);
      
      final int donationCount = 3253;
      
      assertEquals(donationCount, donations.size());

      //TODO: write the rest of this test
    }
    catch (IOException e)
    {
      fail();
    }
  }
  */
  public void testNoDuplicateChipinIds()
  {
      Random rand = new Random(4223);
      final int numDonors = 500;
      final int numDonations = 1234;
      
      String randomDonations = ChipinDonationTableGenerator.generateChipinHTMLTable(ChipinDonationTableGenerator.generateRandomDonations(numDonors, numDonations, rand), rand);

      Document htmlDoc = Jsoup.parse(randomDonations);
      List<ChipinDonation> donations = ChipinDonations.extractDonations(htmlDoc);
    
      Set<String> ids = new HashSet<String>();
      
      for (ChipinDonation d : donations)
      {
        if (ids.contains(d.getChipinId()))
        {
          fail();
        }
        else
        {
          ids.add(d.getChipinId());
        }
      }
  }
  /*
  public void testMergeFullTableToEmptyDatabase()
  {
    DonationDatabaseManager manager = new DonationDatabaseManager();
    
    try
    {
      //TODO: replace this test w/ the random data
      Document htmlDoc = Jsoup.parse(new File("src/test/logic/agdq-chipin.html"), null);
      
      List<ChipinDonation> chipinDonations = ChipinDonations.extractDonations(htmlDoc);
      
      manager.createMemoryDatabase();
      
      ChipinDonations.mergeDonations(manager, chipinDonations);
      
      DonationData donations = manager.getDataAccess().getDonationData();
      DonorData donors = manager.getDataAccess().getDonorData();
      
      // now try to get some of the donations by ID and check that everything checks out
      // also check that there are no duplicates of donors

      for (ChipinDonation cDonation : chipinDonations)
      {
        Donation donation = donations.getDonationByDomainId(DonationDomain.CHIPIN, cDonation.getChipinId());
        
        assertNotNull(donation);
        
        assertEquals(cDonation.getAmount(), donation.getAmount());
        assertEquals(cDonation.getChipinId(), donation.getDomainId());
        assertEquals(DonationBidState.PENDING, donation.getBidState());
        
        Donor donor = donors.getDonorById(donation.getDonorId());
        
        assertEquals(cDonation.getEmail(), donor.getEmail());
        String[] toks = cDonation.getName().trim().split("\\s+");
        
        if (toks.length == 2)
        {
          assertEquals(toks[0], donor.getFirstName());
          assertEquals(toks[1], donor.getLastName());
        }
      }
      
    }
    catch (Exception e)
    {
      fail();
    }
    finally
    {
      manager.closeConnection();
    }
  }
  
  public void testMergeFullTableToFullDatabase()
  {
    DonationDatabaseManager manager = new DonationDatabaseManager();
    
    try
    {
      //TODO: replace this test w/ the random data
      Document htmlDoc = Jsoup.parse(new File("src/test/logic/agdq-chipin.html"), null);
      
      List<ChipinDonation> chipinDonations = ChipinDonations.extractDonations(htmlDoc);
      
      manager.createMemoryDatabase();
      
      ChipinDonations.mergeDonations(manager, chipinDonations);
      ChipinDonations.mergeDonations(manager, chipinDonations);
      
      DonationData donations = manager.getDataAccess().getDonationData();
      DonorData donors = manager.getDataAccess().getDonorData();
      
      // now try to get some of the donations by ID and check that everything checks out
      // also check that there are no duplicates of donors

      for (ChipinDonation cDonation : chipinDonations)
      {
        Donation donation = donations.getDonationByDomainId(DonationDomain.CHIPIN, cDonation.getChipinId());
        
        assertNotNull(donation);
        
        assertEquals(cDonation.getAmount(), donation.getAmount());
        assertEquals(cDonation.getChipinId(), donation.getDomainId());
        assertEquals(DonationBidState.PENDING, donation.getBidState());
        
        Donor donor = donors.getDonorById(donation.getDonorId());
        
        assertEquals(cDonation.getEmail(), donor.getEmail());
        String[] toks = cDonation.getName().trim().split("\\s+");
        
        if (toks.length == 2)
        {
          assertEquals(toks[0], donor.getFirstName());
          assertEquals(toks[1], donor.getLastName());
        }
      }
      
    }
    catch (Exception e)
    {
      fail();
    }
    finally
    {
      manager.closeConnection();
    }
  }
  */
  public void testAddCommentToPreviouslyAddedDonation()
  {
    DonationDatabaseManager manager = new DonationDatabaseManager();
    
    try
    {
      ChipinDonation d = new ChipinDonation("A Guy", "somewhere@anywhere.com", null, "123456789000", new BigDecimal("15.00"));
    
      List<ChipinDonation> chipinDonations = new ArrayList<ChipinDonation>();
      
      chipinDonations.add(d);
      
      manager.createMemoryDatabase();
     
      ChipinDonations.mergeDonations(manager, chipinDonations);
      
      DonationData donations = manager.getDataAccess().getDonationData();
      Donation donationBefore = donations.getDonationByDomainId(DonationDomain.CHIPIN, d.getChipinId());
      
      assertNull(donationBefore.getComment());
      
      String commentText = "Some comment text";
      
      ChipinDonation dPrime = new ChipinDonation(d.getName(), d.getEmail(), commentText, d.getChipinTimeString(), d.getAmount());
      
      chipinDonations.set(0, dPrime);
      
      ChipinDonations.mergeDonations(manager, chipinDonations);
      
      Donation donationAfter = donations.getDonationByDomainId(DonationDomain.CHIPIN, dPrime.getChipinId());
    
      assertEquals(commentText, donationAfter.getComment());
    }
    catch (Exception e)
    {
      fail();
    }
    finally
    {
      manager.closeConnection();
    }
  }
}
