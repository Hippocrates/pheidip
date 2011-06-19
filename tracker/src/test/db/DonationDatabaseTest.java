package test.db;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import pheidip.db.DonationDataAccess;
import pheidip.db.ScriptRunner;
import junit.framework.TestCase;

public abstract class DonationDatabaseTest extends TestCase
{
  private DonationDataAccess dataAccess;
  
  public DonationDataAccess getDataAccess()
  {
    return this.dataAccess;
  }

  public DonationDatabaseTest()
  {
  }

  public void setUp()
  {
    this.dataAccess = new DonationDataAccess();
    this.dataAccess.createMemoryDatabase();
  
    ScriptRunner runner = new ScriptRunner(this.dataAccess.getConnection(), true,
        true);
  
    try
    {
      runner.runScript(new FileReader(DBTestConfiguration
          .getTestDataDirectory() + "donation_bid_test_data_1.sql"));
    } 
    catch (IOException e)
    {
      fail(e.getMessage());
    } 
    catch (SQLException e)
    {
      this.dataAccess.handleSQLException(e);
    }
  }

  public void tearDown()
  {
    if (this.dataAccess.isConnected())
    {
      this.dataAccess.closeConnection();
    }
  }

}