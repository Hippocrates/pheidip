package pheidip.db.deprecated;

import java.sql.Connection;


public abstract class OldDataInterface
{
  private Connection connection;
  private OldDonationDataAccess manager;

  abstract void rebuildPreparedStatements();
  
  public OldDataInterface(OldDonationDataAccess manager)
  {
    this.manager = manager;
    this.setConnection(manager.getConnection());
  }

  synchronized public void setConnection(Connection connection)
  {
    this.connection = connection;
    
    if (this.connection != null)
    {
      this.rebuildPreparedStatements();
    }
  }

  public Connection getConnection()
  {
    return connection;
  }

  public OldDonationDataAccess getManager()
  {
    return manager;
  }
}