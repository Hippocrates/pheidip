package pheidip.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pheidip.db.BidData;
import pheidip.db.DonationData;
import pheidip.db.DonationDataConstraintException;
import pheidip.objects.Challenge;
import pheidip.objects.ChallengeBid;

public class ChallengeControl
{
  private DonationDatabaseManager donationDatabase;
  private BidData bids;
  private DonationData donations;
  private int challengeId;
  private Challenge cachedData;

  public ChallengeControl(DonationDatabaseManager donationDatabase, int challengeId)
  {
    this.donationDatabase = donationDatabase;
    this.challengeId = challengeId;
    this.donations = this.donationDatabase.getDataAccess().getDonationData();
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
  
  public List<ChallengeBid> getAssociatedBids()
  {
    return new ArrayList<ChallengeBid>(this.getData().getBids());
  }

  public void deleteChallenge()
  {
    Challenge c = this.refreshData();
    
    for (ChallengeBid b : getAssociatedBids())
    {
      this.donations.deleteDonationBid(b);
    }
    
    this.bids.deleteChallenge(c);
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
    BigDecimal total = BigDecimal.ZERO;
    
    for (ChallengeBid b : getAssociatedBids())
    {
      total = total.add(b.getAmount());
    }
    
    return total.setScale(2);//this.bids.getChallengeTotal(this.challengeId);
  }
}
