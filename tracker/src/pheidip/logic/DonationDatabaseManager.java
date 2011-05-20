package pheidip.logic;

import pheidip.db.DonationDataAccess;

public class DonationDatabaseManager
{
  private DonationDataAccess dataAccess;
	
  public DonationDatabaseManager()
  {
    this.dataAccess = null;
  }
  
  public boolean isConnected()
  {
    return false;
  }
  
  DonationDataAccess getDataAccess()
  {
    return this.dataAccess;
  }
}
