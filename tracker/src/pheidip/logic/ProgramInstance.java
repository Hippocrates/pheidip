package pheidip.logic;

import pheidip.logic.chipin.ChipinLoginManager;
import pheidip.logic.gdocs.GoogleSpreadSheetLoginManager;
import pheidip.util.Reporter;

public class ProgramInstance
{
  private DonationDatabaseManager donationDatabase;
  private ChipinLoginManager chipinLogin;
  private GoogleSpreadSheetLoginManager googleLogin;
  
  public ProgramInstance()
  {
    this(null);
  }
  
  public ProgramInstance(Reporter reporter)
  {
    this.donationDatabase = new DonationDatabaseManager(reporter);
    this.chipinLogin = new ChipinLoginManager(reporter);
    this.googleLogin = new GoogleSpreadSheetLoginManager(reporter);
  }
  
  protected void finalize()
  {
    this.shutdownProgram();
  }
  
  public DonationDatabaseManager getDonationDatabase()
  {
    return this.donationDatabase;
  }
  
  public GoogleSpreadSheetLoginManager getGoogleLogin()
  {
    return this.googleLogin;
  }
  
  public ChipinLoginManager getChipinLogin()
  {
    return this.chipinLogin;
  }
  
  public DonorControl createDonorControl(int donorId)
  {
    return new DonorControl(this.donationDatabase, donorId);
  }
  
  public DonationControl createDonationControl(int donationId)
  {
    return new DonationControl(this.donationDatabase, donationId);
  }
  
  public SpeedRunControl createSpeedRunControl(int speedRunId)
  {
    return new SpeedRunControl(this.donationDatabase, speedRunId);
  }
  
  public ChoiceControl createChoiceControl(int choiceId)
  {
    return new ChoiceControl(this.donationDatabase, choiceId);
  }
  
  public ChallengeControl createChallengeControl(int challengeId)
  {
    return new ChallengeControl(this.donationDatabase, challengeId);
  }
  
  public PrizeControl createPrizeControl(int prizeId)
  {
    return new PrizeControl(this.donationDatabase, prizeId);
  }
  
  public DonorSearch createDonorSearch()
  {
    return new DonorSearch(this.donationDatabase);
  }
  
  public DonationSearch createDonationSearch()
  {
    return new DonationSearch(this.donationDatabase);
  }
  
  public BidSearch createBidSearch()
  {
    return new BidSearch(this.donationDatabase);
  }
  
  public SpeedRunSearch createSpeedRunSearch()
  {
    return new SpeedRunSearch(this.donationDatabase);
  }
  
  public void shutdownProgram()
  {
    this.googleLogin.logOut();
    this.chipinLogin.logOut();
    this.donationDatabase.closeConnection();
  }
}
