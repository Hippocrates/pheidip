package pheidip.logic;

import java.math.BigDecimal;

import pheidip.db.BidData;
import pheidip.db.DonationDataConstraintException;
import pheidip.objects.Challenge;

public class ChallengeControl
{
  private DonationDatabaseManager donationDatabase;
  private BidData bids;
  private int challengeId;

  public ChallengeControl(DonationDatabaseManager donationDatabase, int challengeId)
  {
    this.donationDatabase = donationDatabase;
    this.challengeId = challengeId;
    this.bids = this.donationDatabase.getDataAccess().getBids();
  }
  
  public Challenge getData()
  {
    return this.bids.getChallengeById(this.challengeId);
  }

  public void deleteChallenge()
  {
    this.bids.deleteChallenge(this.challengeId);
  }

  public void updateData(String name, BigDecimal amount)
  {
    try
    {
      Challenge c = this.getData();
      this.bids.updateChallenge(new Challenge(this.challengeId, name, amount, c.getSpeedRunId()));
    }
    catch (DonationDataConstraintException e)
    {
      this.donationDatabase.reportMessage(e.getMessage());
    }
  }

  public int getChallengeId()
  {
    return this.challengeId;
  }
}
