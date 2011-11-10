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
  private Challenge cachedData;

  public ChallengeControl(DonationDatabaseManager donationDatabase, int challengeId)
  {
    this.donationDatabase = donationDatabase;
    this.challengeId = challengeId;
    this.bids = this.donationDatabase.getDataAccess().getBids();
    this.cachedData = null;
  }
  
  public Challenge refreshData()
  {
    this.cachedData = this.bids.getChallengeById(this.challengeId);
    return this.cachedData;
  }
  
  public Challenge getData()
  {
    if (this.cachedData == null)
    {
      this.refreshData();
    }
    
    return this.cachedData;
  }

  public void deleteChallenge()
  {
    this.bids.deleteChallenge(this.challengeId);
  }

  public void updateData(Challenge data)
  {
    try
    {
      this.bids.updateChallenge(data);
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

  public BigDecimal getTotalCollected()
  {
    return this.bids.getChallengeTotal(this.challengeId);
  }
}
