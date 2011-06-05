package pheidip.db;

import java.sql.Connection;

public abstract class DataInterface
{
  private Connection connection;
  private DonationDataAccess manager;

  abstract void rebuildPreparedStatements();
  
  public DataInterface(DonationDataAccess manager)
  {
    this.manager = manager;
    this.setConnection(manager.getConnection());
  }

  synchronized void setConnection(Connection connection)
  {
    this.connection = connection;
    
    if (this.connection != null)
    {
      this.rebuildPreparedStatements();
    }
  }

  Connection getConnection()
  {
    return connection;
  }

  DonationDataAccess getManager()
  {
    return manager;
  }
}