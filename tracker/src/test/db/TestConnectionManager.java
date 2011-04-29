package test.db;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import pheidip.db.ConnectionManager;
import pheidip.db.DBType;
import pheidip.db.ScriptRunner;
import junit.framework.TestCase;

public class TestConnectionManager extends TestCase
{
  private ConnectionManager manager;
  
  public void setUp()
  {
    this.manager = new ConnectionManager();
  }
  
  public void tearDown()
  {
    if (this.manager.isConnected())
    {
      this.manager.closeConnection();
    }
  }
  
  public void testCreate()
  {
    assertFalse(this.manager.isConnected());
  }
  
  public void testCreateMemoryConnection()
  {
    try
    {
      this.manager.createMemoryDatabase();
      
      assertTrue(this.manager.isConnected());
      assertNotNull(this.manager.getConnection());
      
      // only HSQLDB memory connections are supported atm
      assertEquals(DBType.HSQLDB, this.manager.getConnectionType());
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
      this.manager.createMemoryDatabase();
      
      this.manager.closeConnection();
      
      this.manager.createMemoryDatabase();
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
      this.manager.createMemoryDatabase();
      
      this.manager.createMemoryDatabase();
      
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
      this.manager.createMemoryDatabase();
      
      Connection c = this.manager.getConnection();
      
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
      this.manager.createMemoryDatabase();
      
      Connection c = this.manager.getConnection();
      
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
      this.manager.createMemoryDatabase();
      
      Connection c = this.manager.getConnection();
      
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
        this.manager.connectToDatabaseServer(
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
