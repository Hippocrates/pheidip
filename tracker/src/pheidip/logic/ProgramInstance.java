package pheidip.logic;

public class ProgramInstance
{
  private DonationDatabaseManager donationDatabase;
  
  public ProgramInstance()
  {
    this.donationDatabase = new DonationDatabaseManager();
  }
  
  protected void finalize()
  {
    this.shutdownProgram();
  }
  
  public DonationDatabaseManager getDonationDatabase()
  {
    return this.donationDatabase;
  }
  
  public void shutdownProgram()
  {
    this.donationDatabase.closeConnection();
  }
}
