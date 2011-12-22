package test.logic;

import java.util.List;

import org.jsoup.nodes.Document;

import junit.framework.TestCase;
import pheidip.logic.chipin.ChipinDonations;
import pheidip.logic.chipin.ChipinLoginManager;
import pheidip.objects.ChipinDonation;

public class TestChipinLoginManager extends TestCase
{
  public void testLogInToChipin()
  {
    if (LogicTestConfiguration.chipinLoginTestsEnabled())
    {
      ChipinLoginManager loginManager = new ChipinLoginManager();
      
      loginManager.logIn(
          LogicTestConfiguration.getChipinLoginName(), 
          LogicTestConfiguration.getChipinLoginPassword(), 
          LogicTestConfiguration.getCGDQChipinId());
      
      assertTrue(loginManager.isLoggedIn());
      
      loginManager.logOut();
    }
    else
    {
      System.out.println("Skipped test 'testLogInToChipin'");
    }
  }
  
  public void testFailOnPassword()
  {
    if (LogicTestConfiguration.chipinLoginTestsEnabled())
    {
      ChipinLoginManager loginManager = new ChipinLoginManager();
      
      loginManager.logIn(
          LogicTestConfiguration.getChipinLoginName(), 
          "superman", // fun fact: the most commonly used password is 'superman' 
          LogicTestConfiguration.getCGDQChipinId());
      
      assertFalse(loginManager.isLoggedIn());
      Document result = loginManager.getChipinPage();
      assertNull(result);
    }
    else
    {
      System.out.println("Skipped test 'testFailOnPassword'");
    }
  }
  
  public void testGetChipinPage()
  {
    if (LogicTestConfiguration.chipinLoginTestsEnabled())
    {
      ChipinLoginManager loginManager = new ChipinLoginManager();
      
      loginManager.logIn(
          LogicTestConfiguration.getChipinLoginName(), 
          LogicTestConfiguration.getChipinLoginPassword(), 
          LogicTestConfiguration.getAGDQChipinId());
      
      assertTrue(loginManager.isLoggedIn());
      
      Document result = loginManager.getChipinPage();
      
      assertNotNull(result);
      
      List<ChipinDonation> donations = ChipinDonations.extractDonations(result);
      
      assertEquals(3253, donations.size());
    }
    else
    {
      System.out.println("Skipped test 'testGetChipinPage'");
    }
  }
}
