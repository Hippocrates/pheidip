package pheidip.logic;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;

import pheidip.db.DBType;
import pheidip.db.DonationDataAccess;
import pheidip.db.DonationDataErrorParser;
import pheidip.db.SQLScriptRunner;
import pheidip.db.hibernate.HibernateDonationDataAccess;
import pheidip.util.Reporter;

public class DonationDatabaseManager
{
  private HibernateDonationDataAccess dataAccess;
  private Reporter reporter;
	
  public DonationDatabaseManager()
  {
    this.dataAccess = new HibernateDonationDataAccess();
  }
  
  public DonationDatabaseManager(Reporter reporter)
  {
    this.dataAccess = new HibernateDonationDataAccess();
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
    this.autoCloseConnection();
    this.dataAccess.createMemoryDatabase();
  }
  
  public void openFileDatabase(File location)
  {
    this.autoCloseConnection();
    this.dataAccess.openFileDatabase(location);
  }

  public void connectToServer(DBType type, String serverURL, String dbName, String userName, String password)
  {
    this.autoCloseConnection();
    this.dataAccess.connectToDatabaseServer(type, serverURL, dbName, userName, password);
  }
  
  public void runSQLScript(final String filename)
  {
    // Does not work
    Session session = this.dataAccess.getSessionFactory().openSession();

    try
    {
      session.beginTransaction();
      session.doWork(
          new Work() {
              public void execute(Connection connection) throws SQLException
              {
                FileReader reader = null;
                
                try
                {
                  reader = new FileReader(filename);
                  SQLScriptRunner.runScript(connection, reader);
                }
                catch (SQLException e)
                {
                  DonationDatabaseManager.this.reportMessage(DonationDataErrorParser.parseError(e.getMessage()).getErrorMessage());
                }
                catch (Exception e)
                {
                  e.printStackTrace();
                  DonationDatabaseManager.this.reportMessage(e.getMessage());
                }
                finally
                {
                  if (reader != null)
                  {
                    try
                    {
                      reader.close();
                    }
                    catch (IOException e)
                    {
                      DonationDatabaseManager.this.reportMessage(e.getMessage());
                    }
                  }
                }
                
              }
          }
      );
      session.getTransaction().commit();
    }
    finally
    {
      if (session != null)
      {
        session.close();
      }
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
