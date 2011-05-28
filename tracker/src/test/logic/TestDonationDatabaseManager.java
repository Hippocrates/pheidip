package test.logic;

import pheidip.db.DBType;
import pheidip.logic.DonationDatabaseManager;
import test.db.DBTestConfiguration;
import junit.framework.TestCase;

public class TestDonationDatabaseManager extends TestCase
{
  private DonationDatabaseManager manager;
  
  public void setUp()
  {
    this.manager = new DonationDatabaseManager();
  }
  
  public void tearDown()
  {
    this.manager.closeConnection();
  }
  
  public void testCreate()
  {
    assertFalse(manager.isConnected());
  }
  
  public void testConnectHSQLMemory()
  {
    manager.createMemoryDatabase();
    
    assertTrue(manager.isConnected());
  }
  
  public void testRunInitScript()
  {
    manager.createMemoryDatabase();
    
    manager.runSQLScript(DBTestConfiguration.getTestDataDirectory() + "donation_bid_test_data_1.sql");
  
    assertNotNull(manager.getDataAccess().getDonorData().getDonorById(1));
  }
  
  public void testConnectMySQL()
  {
    if (DBTestConfiguration.testMYSQLConnectToServer())
    {
      try
      {
        this.manager.connectToServer(
            DBType.MYSQL, 
            DBTestConfiguration.mysqlServerName(),
            DBTestConfiguration.mysqlDatabaseName(),
            DBTestConfiguration.mysqlServerUser(),
            DBTestConfiguration.mysqlServerPassword() );
      }
      catch(Exception e)
      {
        fail(e.getMessage());
      }
    }
    else
    {
      System.out.println("Skipped 'testConnectMySQL'.");
    }
  }
}
