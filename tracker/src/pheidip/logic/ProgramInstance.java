package pheidip.logic;

public class ProgramInstance
{
  private DonationDatabaseManager donationDatabase;
  private ChipinLoginManager chipinLogin;
  
  public ProgramInstance()
  {
    this.donationDatabase = new DonationDatabaseManager();
    this.chipinLogin = new ChipinLoginManager();
  }
  
  protected void finalize()
  {
    this.shutdownProgram();
  }
  
  public DonationDatabaseManager getDonationDatabase()
  {
    return this.donationDatabase;
  }
  
  public ChipinLoginManager getChipinLogin()
  {
    return this.chipinLogin;
  }
  
  public void shutdownProgram()
  {
    this.chipinLogin.logOut();
    this.donationDatabase.closeConnection();
  }
}
