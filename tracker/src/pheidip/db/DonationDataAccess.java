package pheidip.db;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DonationDataAccess
{
  private Connection connection;
  private DBType connectionType;
  private boolean isMemoryConnection;
  
  private DonorData donors;
  private DonationData donations;
  
  public DonationDataAccess()
  {
    this.connection = null;
    this.connectionType = null;
    
    this.donors = null;
    this.donations = null;
  }
  
  public DonorData getDonorData()
  {
    if (this.donors == null)
    {
      this.donors = new DonorData(this);
    }
    
    return this.donors;
  }
  
  public DonationData getDonationData()
  {
    if (this.donations == null)
    {
      this.donations = new DonationData(this);
    }
    
    return this.donations;
  }
  
  protected void finalize()
  {
    if (this.isConnected())
    {
      this.closeConnection();
    }
  }
  
  public Connection getConnection()
  {
    return this.connection;
  }
  
  private void setConnection(Connection connection)
  {
    this.connection = connection;
    
    if (this.donors != null)
    {
      this.donors.setConnection(this.connection);
    }
    
    if (this.donations != null)
    {
      this.donations.setConnection(this.connection);
    }
  }
  
  public void connectToDatabaseServer(DBType type, String server, String dbname, String user, String password)
  {
    if (this.isConnected())
    {
      throw new RuntimeException("Error, database is already connected.");
    }
    
    try
    {
      this.setConnection(JDBCManager.connectToServer(type, server, dbname, user, password));
      this.connectionType = type;
    } 
    catch (SQLException e)
    {
      this.handleSQLException(e);
    }
  }
  
  public void createMemoryDatabase()
  {
    if (this.isConnected())
    {
      throw new RuntimeException("Error, database is already connected.");
    }
    
    try
    {
      this.setConnection(JDBCManager.createMemoryDatabase());
      this.connectionType = DBType.HSQLDB;
      this.isMemoryConnection = true;

      ScriptRunner runner = new ScriptRunner(this.connection, true, true);

      runner.runScript(new FileReader(DBConfiguration.getDonationSchemaFilename()));
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage());
    } 
    catch (SQLException e)
    {
      this.handleSQLException(e);
    }
  }
  
  public DBType getConnectionType()
  {
    return this.connectionType;
  }
  
  public boolean isConnected()
  {
    boolean result = false;
    
    try
    {
      result = (this.connection != null) && (!this.connection.isClosed());
    } 
    catch (SQLException e)
    {
      this.handleSQLException(e);
    }
    
    return result;
  }
  
  public void handleSQLException(SQLException error)
  {
    System.out.println("Error Code = " + error.getErrorCode() + " : " + error.getMessage());
    throw new RuntimeException(error.getMessage());
  }

  public void closeConnection()
  {
    if (this.isConnected())
    {
      try
      {
        // if it's an in-mem db, then we want to destroy it before closing it
        if (this.isMemoryConnection)
        {
          JDBCManager.shutdownHSQLConnection(this.connection);
        }
        
        this.connection.close();
        this.setConnection(null);
      } 
      catch (SQLException e)
      {
        this.handleSQLException(e);
      }
      finally
      {
        this.connection = null;
        this.connectionType = null;
      }
    }
    else
    {
      throw new RuntimeException("Error, no connection is active.");
    }
  }
}
