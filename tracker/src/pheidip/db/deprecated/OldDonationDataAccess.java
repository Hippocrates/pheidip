package pheidip.db.deprecated;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import pheidip.db.BidData;
import pheidip.db.DBConfiguration;
import pheidip.db.DBType;
import pheidip.db.DonationData;
import pheidip.db.DonationDataAccess;
import pheidip.db.DonationDataConstraint;
import pheidip.db.DonationDataConstraintException;
import pheidip.db.DonationDataErrorParser;
import pheidip.db.DonorData;
import pheidip.db.JDBCManager;
import pheidip.db.PrizeData;
import pheidip.db.ScriptRunner;
import pheidip.db.SpeedRunData;

public class OldDonationDataAccess implements DonationDataAccess
{
  private Connection connection;
  private DBType connectionType;
  private boolean isMemoryConnection;
  
  private OldDonorData donors;
  private OldDonationData donations;
  private OldBidData bids;
  private OldSpeedRunData speedRuns;
  private OldPrizeData prizes;
  
  public OldDonationDataAccess()
  {
    this.connection = null;
    this.connectionType = null;
    
    this.donors = null;
    this.donations = null;
    this.speedRuns = null;
    this.bids = null;
    this.prizes = null;
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationDataAccess#getDonorData()
 */
@Override
synchronized public DonorData getDonorData()
  {
    if (this.donors == null)
    {
      this.donors = new OldDonorData(this);
    }
    
    return this.donors;
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationDataAccess#getDonationData()
 */
@Override
synchronized public DonationData getDonationData()
  {
    if (this.donations == null)
    {
      this.donations = new OldDonationData(this);
    }
    
    return this.donations;
  }

  /* (non-Javadoc)
 * @see pheidip.db.DonationDataAccess#getBids()
 */
@Override
synchronized public BidData getBids()
  {
    if (this.bids == null)
    {
      this.bids = new OldBidData(this);
    }
    
    return this.bids;
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationDataAccess#getSpeedRuns()
 */
@Override
synchronized public SpeedRunData getSpeedRuns()
  {
    if (this.speedRuns == null)
    {
      this.speedRuns = new OldSpeedRunData(this);
    }
    
    return this.speedRuns;
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationDataAccess#getPrizeData()
 */
@Override
synchronized public PrizeData getPrizeData()
  {
    if (this.prizes == null)
    {
      this.prizes = new OldPrizeData(this);
    }
    
    return this.prizes;
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
  
  synchronized private void setConnection(Connection connection)
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
    
    if (this.speedRuns != null)
    {
      this.speedRuns.setConnection(this.connection);
    }
    
    if (this.bids != null)
    {
      this.bids.setConnection(this.connection);
    }
    
    if (this.prizes != null)
    {
      this.prizes.setConnection(this.connection);
    }
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationDataAccess#connectToDatabaseServer(pheidip.db.DBType, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
 */
@Override
synchronized public void connectToDatabaseServer(DBType type, String server, String dbname, String user, String password)
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
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationDataAccess#openFileDatabase(java.io.File)
 */
@Override
synchronized public void openFileDatabase(File location)
  {      
    boolean dbAlreadyExists = location.exists();
    
    try
    {
      Connection connection = JDBCManager.createFileDatabase(location);
      
      if (!dbAlreadyExists)
      {
        ScriptRunner runner = new ScriptRunner(connection, true, true);
        runner.runScript(new FileReader(DBConfiguration.getDonationSchemaFilename()));
      }
      
      this.setConnection(connection);
      this.connectionType = DBType.HSQLDB;
      this.isMemoryConnection = true;
    }
    catch (SQLException e)
    {
      this.handleSQLException(e);
    } 
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationDataAccess#createMemoryDatabase()
 */
@Override
synchronized public void createMemoryDatabase()
  {
    if (this.isConnected())
    {
      throw new RuntimeException("Error, database is already connected.");
    }
    
    try
    {
      Connection newConnection = JDBCManager.createMemoryDatabase();
      
      ScriptRunner runner = new ScriptRunner(newConnection, true, true);
      runner.runScript(new FileReader(DBConfiguration.getDonationSchemaFilename()));
      
      this.setConnection(newConnection);
      this.connectionType = DBType.HSQLDB;
      this.isMemoryConnection = true;
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
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationDataAccess#getConnectionType()
 */
@Override
synchronized public DBType getConnectionType()
  {
    return this.connectionType;
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationDataAccess#isConnected()
 */
@Override
synchronized public boolean isConnected()
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
    DonationDataConstraint violatedConstraint = DonationDataErrorParser.parseError(error.getMessage());
    
    if (violatedConstraint != null)
    {
      throw new DonationDataConstraintException(violatedConstraint);
    }
    else
    {
      throw new RuntimeException(error.getMessage());
    }
  }

  /* (non-Javadoc)
 * @see pheidip.db.DonationDataAccess#closeConnection()
 */
@Override
synchronized public void closeConnection()
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
