package test.logic;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;
import pheidip.logic.ChipinDonations;
import pheidip.logic.ChipinFileDocumentSource;
import pheidip.logic.ChipinLoginManager;
import pheidip.logic.ChipinMergeProcess;
import pheidip.logic.ChipinMergeState;
import pheidip.logic.ChipinTextDocumentSource;
import pheidip.logic.ChipinWebsiteDocumentSource;
import pheidip.logic.DonationDatabaseManager;
import pheidip.objects.ChipinDonation;
import test.db.DBTestConfiguration;

public class TestChipinMergeProcess extends TestCase
{  
  DonationDatabaseManager manager;

  public void setUp()
  {
    this.manager = new DonationDatabaseManager();
    this.manager.createMemoryDatabase();
    this.manager.runSQLScript(DBTestConfiguration.getTestDataDirectory() + "donation_bid_test_data_1.sql");
  }
  
  public void tearDown()
  {
    this.manager.closeConnection();
  }

  public void testRunStringMerge()
  {
    final int numDonors = 300;
    final int numDonations = 3000;
    Random rand = new Random();
    
    List<ChipinDonation> sourceDonations = ChipinTestUtils.generateRandomDonations(numDonors, numDonations, rand);
    String randomDonations = ChipinTestUtils.generateChipinHTMLTable(sourceDonations);

    testRunMergeOn(new ChipinMergeProcess(this.manager, new ChipinTextDocumentSource(randomDonations)));
  
    assertTrue(ChipinTestUtils.checkAllDonationsAreInDatabase(sourceDonations, manager));
  }
  
  public void testRunFileMerge()
  {
    final int numDonors = 300;
    final int numDonations = 3000;
    Random rand = new Random();

    List<ChipinDonation> sourceDonations = ChipinTestUtils.generateRandomDonations(numDonors, numDonations, rand);
    String randomDonations = ChipinTestUtils.generateChipinHTMLTable(sourceDonations);

    ByteArrayInputStream stream = new ByteArrayInputStream(randomDonations.getBytes());
    
    testRunMergeOn(new ChipinMergeProcess(this.manager, new ChipinFileDocumentSource(stream, new Runnable()
    {
      public void run()
      {
      }})));
    
    assertTrue(ChipinTestUtils.checkAllDonationsAreInDatabase(sourceDonations, manager));
  }
  
  public void testRunWebsiteMerge()
  {
    if (LogicTestConfiguration.chipinLoginTestsEnabled())
    {
      ChipinLoginManager chipinLogin = new ChipinLoginManager();
      
      chipinLogin.logIn(
          LogicTestConfiguration.getChipinLoginName(), 
          LogicTestConfiguration.getChipinLoginPassword(), 
          LogicTestConfiguration.getCGDQChipinId());
      
      List<ChipinDonation> sourceDonations = ChipinDonations.extractDonations(chipinLogin.getChipinPage());
      
      testRunMergeOn(new ChipinMergeProcess(this.manager, new ChipinWebsiteDocumentSource(chipinLogin)));
      
      assertTrue(ChipinTestUtils.checkAllDonationsAreInDatabase(sourceDonations, manager));
    
    }
    else
    {
      System.out.println("Skipped test 'testRunWebsiteMerge'");
    }
  }
  
  public void testCancelMerge()
  {
    final int numDonors = 300;
    final int numDonations = 3000;
    Random rand = new Random();

    List<ChipinDonation> sourceDonations = ChipinTestUtils.generateRandomDonations(numDonors, numDonations, rand);
    String randomDonations = ChipinTestUtils.generateChipinHTMLTable(sourceDonations);

    ChipinMergeProcess process = new ChipinMergeProcess(this.manager, new ChipinTextDocumentSource(randomDonations));
      
    Thread thread = new Thread(process);
      
    thread.start();
      
    try
    {
      Thread.sleep(10);
    } 
    catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
      
    thread.interrupt();
    
    try
    {
      thread.join(10000);
    } 
    catch (InterruptedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    assertEquals(ChipinMergeState.CANCELLED, process.getState());
      
    assertFalse(ChipinTestUtils.checkAllDonationsAreInDatabase(sourceDonations, manager));
  }
  
  private void testRunMergeOn(ChipinMergeProcess process)
  {
    try
    {
      assertEquals(ChipinMergeState.IDLE, process.getState());
      
      Thread thread = new Thread(process);
      
      thread.start();
      
      Thread.sleep(100);
      
      assertTrue(ChipinMergeState.IDLE != process.getState());
      assertTrue(ChipinMergeState.CANCELLED != process.getState());
      assertTrue(ChipinMergeState.FAILED != process.getState());
      
      final long testTimeout = 30000;
      long startTime = System.currentTimeMillis();
      
      while (process.getState() != ChipinMergeState.COMPLETED)
      {
        if (System.currentTimeMillis() - startTime > testTimeout)
        {
          fail();
        }
        Thread.sleep(10);
      }
    } 
    catch (Exception e)
    {
      this.manager.closeConnection();
      fail();
    }
  }
}
