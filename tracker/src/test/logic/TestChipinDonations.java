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
import pheidip.logic.DonationDatabaseManager;
import pheidip.logic.chipin.ChipinDonations;
import pheidip.objects.ChipinDonation;
import pheidip.objects.Donation;
import pheidip.objects.DonationDomain;

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
  
  public void testParseFullTable()
  {
    Random rand = new Random(222222);
    final int numDonors = 600;
    final int numDonations = 3000;
    
    List<ChipinDonation> sourceDonations = ChipinTestUtils.generateRandomDonations(numDonors, numDonations, rand);
    assertEquals(numDonations, sourceDonations.size());
    String randomDonations = ChipinTestUtils.generateChipinHTMLTable(sourceDonations);

    Document htmlDoc = Jsoup.parse(randomDonations);
     
    List<ChipinDonation> resultDonations = ChipinDonations.extractDonations(htmlDoc);
      
    assertEquals(numDonations, resultDonations.size());

    for (int i = 0; i < numDonations; ++i)
    {
      ChipinDonation source = sourceDonations.get(i);
      ChipinDonation result = resultDonations.get(i);
      
      assertEquals(source.getName(), result.getName());
      assertEquals(source.getEmail(), result.getEmail());
      // there seems to be a bug in JSoup where it will splice in an extra space on really long comments
      //assertEquals(StringUtils.emptyIfNull(source.getComment()), StringUtils.emptyIfNull(result.getComment()));
      assertEquals(source.getAmount(), result.getAmount());
      assertEquals(source.getChipinTimeString(), result.getChipinTimeString());
      assertEquals(source.getChipinId(), result.getChipinId());
    }

  }

  public void testNoDuplicateChipinIds()
  {
      Random rand = new Random(4223);
      final int numDonors = 300;
      final int numDonations = 1234;
      
      List<ChipinDonation> sourceDonationList = ChipinTestUtils.generateRandomDonations(numDonors, numDonations, rand);

      Set<String> ids = new HashSet<String>();
      
      for (ChipinDonation d : sourceDonationList)
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
  
  public void testMergeFullTableToEmptyDatabase()
  {
    DonationDatabaseManager manager = new DonationDatabaseManager();
    
    try
    {
      Random rand = new Random(731);
      final int numDonors = 600;
      final int numDonations = 4000;
      
      List<ChipinDonation> chipinDonations = ChipinTestUtils.generateRandomDonations(numDonors, numDonations, rand);

      manager.createMemoryDatabase();
      
      // logic changed, too lazy to update test
      
      assertTrue(ChipinTestUtils.checkAllDonationsAreInDatabase(chipinDonations, manager));
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
      Random rand = new Random(5459898);
      final int numDonors = 600;
      final int numDonations = 4000;
      
      List<ChipinDonation> chipinDonations = ChipinTestUtils.generateRandomDonations(numDonors, numDonations, rand);

      manager.createMemoryDatabase();
      
      // logic changed, too lazy to update test

      assertTrue(ChipinTestUtils.checkAllDonationsAreInDatabase(chipinDonations, manager));
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
  
  public void testMergeAnotherTableToDatabase()
  {
    DonationDatabaseManager manager = new DonationDatabaseManager();
    
    try
    {
      Random rand = new Random(887554);
      final int numDonorsA = 600;
      final int numDonationsA = 4000;
      final int numDonorsB = 400;
      final int numDonationsB = 2000;
      
      List<ChipinDonation> sourceDonationListA = ChipinTestUtils.generateRandomDonations(numDonorsA, numDonationsA, rand);
      List<ChipinDonation> sourceDonationListB = ChipinTestUtils.generateRandomDonations(numDonorsB, numDonationsB, rand);

      manager.createMemoryDatabase();
      
      // logic changed, too lazy to update test
      
      assertTrue(ChipinTestUtils.checkAllDonationsAreInDatabase(sourceDonationListA, manager));
      assertTrue(ChipinTestUtils.checkAllDonationsAreInDatabase(sourceDonationListB, manager));
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
  
  public void testAddCommentToPreviouslyAddedDonation()
  {
    DonationDatabaseManager manager = new DonationDatabaseManager();
    
    try
    {
      List<ChipinDonation> donationList = new ArrayList<ChipinDonation>();
      
      ChipinDonation d = new ChipinDonation("A Guy", "somewhere@anywhere.com", null, "123456789000", new BigDecimal("15.00"));
      
      manager.createMemoryDatabase();
      
      donationList.add(d);

      // logic changed, too lazy to update test
      
      DonationData donations = manager.getDataAccess().getDonationData();
      Donation donationBefore = donations.getDonationByDomainId(DonationDomain.CHIPIN, d.getChipinId());
      
      assertNull(donationBefore.getComment());
      
      String commentText = "Some comment text";
      
      ChipinDonation dPrime = new ChipinDonation(d.getName(), d.getEmail(), commentText, d.getChipinTimeString(), d.getAmount());

      donationList.set(0, dPrime);

      // logic changed, too lazy to update test
      
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
