package pheidip.logic;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import pheidip.db.DBType;
import pheidip.db.DonationDataAccess;
import pheidip.db.DonationDataErrorParser;
import pheidip.db.deprecated.OldDonationDataAccess;
import pheidip.db.deprecated.ScriptRunner;
import pheidip.util.Reporter;

public class DonationDatabaseManager
{
  private OldDonationDataAccess dataAccess;
  private Reporter reporter;
	
  public DonationDatabaseManager()
  {
    this.dataAccess = new OldDonationDataAccess();
  }
  
  public DonationDatabaseManager(Reporter reporter)
  {
    this.dataAccess = new OldDonationDataAccess();
    this.reporter = reporter;
  }
  
  protected void finalize()
  {
    this.closeConnection();
  }
  
  public boolean isConnected()
  {
    return (this.dataAccess.isConnected());
  }
  
  public DonationDataAccess getDataAccess()
  {
    return this.dataAccess;
  }

  public void createMemoryDatabase()
  {
    try
    {
      this.autoCloseConnection();
      this.dataAccess.createMemoryDatabase();
    }
    catch (Exception e)
    {
      this.reportMessage(e.getMessage());
    }
  }
  
  public void openFileDatabase(File location)
  {
    try
    {
      this.autoCloseConnection();
      this.dataAccess.openFileDatabase(location);
    }
    catch (Exception e)
    {
      this.reportMessage(e.getMessage());
    }
  }

  public void connectToServer(DBType type, String serverURL, String dbName, String userName, String password)
  {
    try
    {
      this.autoCloseConnection();
      this.dataAccess.connectToDatabaseServer(type, serverURL, dbName, userName, password);
    }
    catch (Exception e)
    {
      this.reportMessage(e.getMessage());
    }
  }
  
  public void runSQLScript(String filename)
  {
    try
    {
      FileReader reader = new FileReader(filename);
      ScriptRunner runner = new ScriptRunner(this.dataAccess.getConnection(), true, true);
      
      runner.runScript(reader);
    } 
    catch (IOException e)
    {
      this.reportMessage(e.getMessage());
    } 
    catch (SQLException e)
    {
      this.reportMessage(DonationDataErrorParser.parseError(e.getMessage()).getErrorMessage());
    }
  }
  
  private void autoCloseConnection()
  {
    if (this.isConnected())
    {
      this.dataAccess.closeConnection();
    }
  }

  public void closeConnection()
  {
    this.autoCloseConnection();
  }
  
  public void reportMessage(String message)
  {
    if (this.reporter != null)
    {
      this.reporter.report(message);
    }
  }
}
