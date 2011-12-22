package test.logic;

import java.util.List;

import pheidip.logic.DonationDatabaseManager;
import pheidip.logic.SpeedRunSearch;
import pheidip.objects.SpeedRun;
import pheidip.objects.SpeedRunSearchParams;
import test.db.DBTestConfiguration;
import junit.framework.TestCase;

public class TestSpeedRunSearch extends TestCase
{
  private DonationDatabaseManager manager;
  private SpeedRunSearch searcher;
  
  public void setUp()
  {
    this.manager = new DonationDatabaseManager();
    this.manager.createMemoryDatabase();
    this.manager.runSQLScript(DBTestConfiguration.getTestDataDirectory() + "donation_bid_test_data_1.sql");
  
    this.searcher = new SpeedRunSearch(this.manager);
  }
  
  public void tearDown()
  {
    this.manager.closeConnection();
  }
  
  public void testSearchSpeedRunsNoParam()
  {
    List<SpeedRun> runs = this.searcher.runSearch(new SpeedRunSearchParams(null));
    
    assertEquals(3, runs.size());
  }
  
  public void testSearchSpeedRunsName()
  {
    List<SpeedRun> runs = this.searcher.runSearch(new SpeedRunSearchParams("yet"));
    
    assertEquals(1, runs.size());
    
    assertEquals(3, runs.get(0).getId());
    assertEquals("yet another run", runs.get(0).getName());
  }
}
