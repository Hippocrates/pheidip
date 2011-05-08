package test.db;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import pheidip.db.DonationDataAccess;
import pheidip.db.DBType;
import pheidip.db.ScriptRunner;
import junit.framework.TestCase;

public class TestDonationDataAccess extends TestCase
{
  private DonationDataAccess dataAccess;
  
  public void setUp()
  {
    this.dataAccess = new DonationDataAccess();
  }
  
  public void tearDown()
  {
    if (this.dataAccess.isConnected())
    {
      this.dataAccess.closeConnection();
    }
  }
  
  public void testCreate()
  {
    assertFalse(this.dataAccess.isConnected());
  }
  
  public void testCreateMemoryConnection()
  {
    try
    {
      this.dataAccess.createMemoryDatabase();
      
      assertTrue(this.dataAccess.isConnected());
      assertNotNull(this.dataAccess.getConnection());
      
      // only HSQLDB memory connections are supported atm
      assertEquals(DBType.HSQLDB, this.dataAccess.getConnectionType());
    }
    catch(Exception e)
    {
      fail(e.getMessage());
    }
  }
  
  public void testCanOpenCloseOpen()
  {
    try
    {
      this.dataAccess.createMemoryDatabase();
      
      this.dataAccess.closeConnection();
      
      this.dataAccess.createMemoryDatabase();
    }
    catch(Exception e)
    {
      fail(e.getMessage());
    }
  }
  
  public void testCannotOpenMultipleTimes()
  {
    try
    {
      this.dataAccess.createMemoryDatabase();
      
      this.dataAccess.createMemoryDatabase();
      
      fail("Error, allowed opening another connection while the current one is still active.");
    }
    catch(Exception e)
    {
      // pass
    }
  }
  
  
  public void testBasicQuery()
  {
    try
    {
      this.dataAccess.createMemoryDatabase();
      
      Connection c = this.dataAccess.getConnection();
      
      Statement s = c.createStatement();
      
      ResultSet r1 = s.executeQuery("SELECT * FROM Donor");
      
      assertFalse(r1.next());
      
      ResultSet r2 = s.executeQuery("SELECT * FROM Donation");
      
      assertFalse(r2.next());
    }
    catch(SQLException e)
    {
      fail("Error code : " + e.getErrorCode() + "\t" + e.getMessage());
    }
  }
  
  
  public void testFailedQuery()
  {
    try
    {
      this.dataAccess.createMemoryDatabase();
      
      Connection c = this.dataAccess.getConnection();
      
      Statement s = c.createStatement();
      
      ResultSet r1 = s.executeQuery("SELECT * FROM Donors"); // Donors is misspelled
      
      assertFalse(r1.next());
      
      fail("Did not fail on missing table");
    }
    catch(SQLException e)
    {
      // pass
    }
  }
  
  public void testRunSQLScript()
  {
    try
    {
      this.dataAccess.createMemoryDatabase();
      
      Connection c = this.dataAccess.getConnection();
      
      ScriptRunner runner = new ScriptRunner(c, true, true);

      runner.runScript(new FileReader(DBTestConfiguration.getTestDataDirectory() + "donation_bid_test_data_1.sql"));

      Statement s = c.createStatement();
      
      ResultSet r1 = s.executeQuery("SELECT * FROM Donor");
      
      assertTrue(r1.next());
    }
    catch(SQLException e)
    {
      fail(e.getMessage());
    }
    catch (FileNotFoundException e)
    {
      fail("DB test file not found: " + e.getMessage());
    } 
    catch (IOException e)
    {
      fail("Could not read DB test file: " + e.getMessage());
    }
  }
  
  public void testConnectToSever()
  {
    if (DBTestConfiguration.testMYSQLConnectToServer())
    {
      try
      {
        this.dataAccess.connectToDatabaseServer(
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
      System.out.println("Skipped 'testConnectToSever'.");
    }
  }
}
