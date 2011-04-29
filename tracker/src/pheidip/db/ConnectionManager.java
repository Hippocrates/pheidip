package pheidip.db;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager
{
  private Connection connection;
  private DBType connectionType;
  private boolean isMemoryConnection;
  
  public ConnectionManager()
  {
    this.connection = null;
    this.connectionType = null;
  }
  
  public Connection getConnection()
  {
    return this.connection;
  }
  
  public void connectToDatabaseServer(DBType type, String server, String dbname, String user, String password)
  {
    String location = null;
    
    switch(type)
    {
    case HSQLDB:
      location = "hsql://" + server + "/" + dbname;
      break;
    case MYSQL:
      location = "//" + server + "/" + dbname;
      break;
    }
    
    this.createConnection(type, location, user, password);
  }
  
  public void createMemoryDatabase()
  {
    this.createConnection(DBType.HSQLDB, "mem:.", "", "");
    
    this.isMemoryConnection = true;
    
    if (this.isConnected())
    {
      ScriptRunner runner = new ScriptRunner(this.connection, true, true);
    
      try
      {
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
  }
 
  private void createConnection(DBType type, String location, String user, String password)
  {
    if (this.isConnected())
    {
      throw new RuntimeException("Error, please close the existing connection before opening a new one.");
    }
    
    JDBCManager.loadDrivers(type);
    
    String url = "jdbc:" + type.getJDBCName() + ":" + location;

    try
    {
      this.connection = DriverManager.getConnection(url, user, password);
      this.connectionType = type;
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
          this.connection.createStatement().execute("SHUTDOWN");
        }
        
        this.connection.close();
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
