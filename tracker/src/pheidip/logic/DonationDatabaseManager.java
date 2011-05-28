package pheidip.logic;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import pheidip.db.DBType;
import pheidip.db.DonationDataAccess;
import pheidip.db.ScriptRunner;

public class DonationDatabaseManager
{
  private DonationDataAccess dataAccess;
	
  public DonationDatabaseManager()
  {
    this.dataAccess = new DonationDataAccess();
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
    this.autoCloseConnection();
    
    this.dataAccess.createMemoryDatabase();
  }

  public void connectToServer(DBType type, String serverURL, String dbName, String userName, String password)
  {
    this.autoCloseConnection();
    
    this.dataAccess.connectToDatabaseServer(type, serverURL, dbName, userName, password);
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
      // TODO Auto-generated catch block
      // this is where the Reporter would be invoked
      e.printStackTrace();
    } 
    catch (SQLException e)
    {
      // TODO Auto-generated catch block
      // this is where the Reporter would be invoked
      e.printStackTrace();
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
}
