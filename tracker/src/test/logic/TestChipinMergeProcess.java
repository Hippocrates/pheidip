package test.logic;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;
import pheidip.logic.DonationDatabaseManager;
import pheidip.logic.chipin.ChipinDonation;
import pheidip.logic.chipin.ChipinDonations;
import pheidip.logic.chipin.ChipinFileDonationSource;
import pheidip.logic.chipin.ChipinLoginManager;
import pheidip.logic.chipin.ChipinMergeProcess;
import pheidip.logic.chipin.ExternalProcessState;
import pheidip.logic.chipin.ChipinTextDonationSource;
import pheidip.logic.chipin.ChipinWebsiteDonationSource;
import pheidip.logic.chipin.RawChipinDonationSource;
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

    testRunMergeOn(new ChipinMergeProcess(this.manager, new ChipinTextDonationSource(randomDonations)));

    ChipinTestHelper helper = new ChipinTestHelper();
    
    helper.checkAllDonationsAreInDatabase(sourceDonations, manager);
  }
  
  public void testRunFileMerge()
  {
    final int numDonors = 300;
    final int numDonations = 3000;
    Random rand = new Random();

    List<ChipinDonation> sourceDonations = ChipinTestUtils.generateRandomDonations(numDonors, numDonations, rand);
    String randomDonations = ChipinTestUtils.generateChipinHTMLTable(sourceDonations);

    ByteArrayInputStream stream = new ByteArrayInputStream(randomDonations.getBytes());
    
    testRunMergeOn(new ChipinMergeProcess(this.manager, new ChipinFileDonationSource(stream, new Runnable()
    {
      public void run()
      {
      }})));
    
ChipinTestHelper helper = new ChipinTestHelper();
    
    helper.checkAllDonationsAreInDatabase(sourceDonations, manager);
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
      
      testRunMergeOn(new ChipinMergeProcess(this.manager, new ChipinWebsiteDonationSource(chipinLogin)));
      
      ChipinTestHelper helper = new ChipinTestHelper();
      
      helper.checkAllDonationsAreInDatabase(sourceDonations, manager);
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

    ChipinMergeProcess process = new ChipinMergeProcess(this.manager, new RawChipinDonationSource(sourceDonations));
      
    Thread thread = new Thread(process);
      
    thread.start();
      
    try
    {
      Thread.sleep(10);
    } 
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
      
    thread.interrupt();
    
    try
    {
      thread.join(10000);
    } 
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
    
    assertEquals(ExternalProcessState.CANCELLED, process.getState());

    assertTrue(this.manager.getDataAccess().getDonationData().getAllDonations().size() < sourceDonations.size());
  }
  
  private void testRunMergeOn(ChipinMergeProcess process)
  {
    try
    {
      assertEquals(ExternalProcessState.IDLE, process.getState());
      
      Thread thread = new Thread(process);
      
      thread.start();
      
      Thread.sleep(200);
      
      assertTrue(ExternalProcessState.IDLE != process.getState());
      assertTrue(ExternalProcessState.CANCELLED != process.getState());
      assertTrue(ExternalProcessState.FAILED != process.getState());
      
      final long testTimeout = 30000;
      long startTime = System.currentTimeMillis();
      
      while (process.getState() != ExternalProcessState.COMPLETED)
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
