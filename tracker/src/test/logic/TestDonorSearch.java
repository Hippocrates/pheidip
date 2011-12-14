package test.logic;

import java.util.List;

import pheidip.logic.DonationDatabaseManager;
import pheidip.logic.DonorSearch;
import pheidip.objects.Donor;
import pheidip.objects.DonorSearchParams;
import test.db.DBTestConfiguration;
import junit.framework.TestCase;

public class TestDonorSearch extends TestCase
{
  private DonorSearch searcher;
  private DonationDatabaseManager manager;
  
  public void setUp()
  {
    this.manager = new DonationDatabaseManager();
    this.manager.createMemoryDatabase();
    this.manager.runSQLScript(DBTestConfiguration.getTestDataDirectory() + "donation_bid_test_data_1.sql");
    
    this.searcher = new DonorSearch(this.manager);
  }
  
  public void tearDown()
  {
    this.manager.closeConnection();
  }
  
  public void testBasicSearch()
  {
    List<Donor> result = this.searcher.runSearch(new DonorSearchParams("ste", "", "", ""));
    
    assertEquals(2, result.size());
  }
  
  public void testBothNamesSearch()
  {
    List<Donor> result = this.searcher.runSearch(new DonorSearchParams("bro", "cra", "", ""));
    
    assertEquals(1, result.size());
    assertEquals(3, result.get(0).getId());
  }
  
  public void testAliasSearch()
  {
    List<Donor> result = this.searcher.runSearch(new DonorSearchParams("", "", "", "alias"));
    
    assertEquals(2, result.size());
  }
  
  public void testEmailSearch()
  {
    List<Donor> result = this.searcher.runSearch(new DonorSearchParams("", "", "@", null));
    
    assertEquals(4, result.size());
  }
}
