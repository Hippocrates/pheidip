package pheidip.logic;

import pheidip.logic.chipin.ChipinLoginManager;
import pheidip.logic.gdocs.GoogleSpreadSheetLoginManager;
import pheidip.objects.Bid;
import pheidip.objects.BidSearchParams;
import pheidip.objects.Donation;
import pheidip.objects.DonationSearchParams;
import pheidip.objects.Donor;
import pheidip.objects.DonorSearchParams;
import pheidip.objects.Prize;
import pheidip.objects.PrizeSearchParams;
import pheidip.objects.SearchParameters;
import pheidip.objects.SpeedRun;
import pheidip.objects.SpeedRunSearchParams;
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
  
  public PrizeSearch createPrizeSearch()
  {
    return new PrizeSearch(this.donationDatabase);
  }
  
  public EntitySearcher<?> createEntitySearcher(Class<?> clazz)
  {
    if (clazz == Donor.class)
    {
      return this.createDonorSearch();
    }
    else if (clazz == Donation.class)
    {
      return this.createDonationSearch();
    }
    else if (clazz == Bid.class)
    {
      return this.createBidSearch();
    }
    else if (clazz == Prize.class)
    {
      return this.createPrizeSearch();
    }
    else if (clazz == SpeedRun.class)
    {
      return this.createSpeedRunSearch();
    }
    else
    {
      return null;
    }
  }
  
  public SearchParameters<?> createSearchParameters(Class<?> clazz)
  {
    if (clazz == Donor.class)
    {
      return new DonorSearchParams();
    }
    else if (clazz == Donation.class)
    {
      return new DonationSearchParams();
    }
    else if (clazz == Bid.class)
    {
      return new BidSearchParams();
    }
    else if (clazz == Prize.class)
    {
      return new PrizeSearchParams();
    }
    else if (clazz == SpeedRun.class)
    {
      return new SpeedRunSearchParams();
    }
    else
    {
      return null;
    }
  }
  
  public void shutdownProgram()
  {
    this.googleLogin.logOut();
    this.chipinLogin.logOut();
    this.donationDatabase.closeConnection();
  }
}
