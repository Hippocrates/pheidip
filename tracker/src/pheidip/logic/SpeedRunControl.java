package pheidip.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pheidip.db.BidData;
import pheidip.db.SpeedRunData;
import pheidip.objects.Bid;
import pheidip.objects.BidState;
import pheidip.objects.BidType;
import pheidip.objects.Challenge;
import pheidip.objects.Choice;
import pheidip.objects.SpeedRun;
import pheidip.util.IdUtils;
import pheidip.util.StringUtils;

public class SpeedRunControl
{
  private DonationDatabaseManager donationDatabase;
  private SpeedRunData speedRuns;
  private BidData bids;
  private int speedRunId;
  
  public SpeedRunControl(DonationDatabaseManager manager, int speedRunId)
  {
    this.donationDatabase = manager;
    this.speedRuns = this.donationDatabase.getDataAccess().getSpeedRuns();
    this.bids = this.donationDatabase.getDataAccess().getBids();
    this.speedRunId = speedRunId;
  }

  public SpeedRun getData()
  {
    return this.speedRuns.getSpeedRunById(this.speedRunId);
  }
  
  public void updateData(String newName, String description)
  {
    this.speedRuns.updateSpeedRun(new SpeedRun(this.speedRunId, newName, description));
  }

  public List<Bid> getAllBids()
  {
    List<Bid> bids = new ArrayList<Bid>();
    
    bids.addAll(this.bids.getChallengesBySpeedrun(this.speedRunId));
    bids.addAll(this.bids.getChoicesBySpeedrun(this.speedRunId));
    
    return bids;
  }
  
  public static int createNewSpeedRun(DonationDatabaseManager manager, String name)
  {
    int id = IdUtils.generateId();
    manager.getDataAccess().getSpeedRuns().insertSpeedRun(new SpeedRun(id, StringUtils.nullIfEmpty(name), null));
    return id;
  }

  public static int createNewSpeedRun(DonationDatabaseManager manager)
  {
    return createNewSpeedRun(manager, null);
  }
  
  public int createNewChallenge(String result)
  {
    int id = IdUtils.generateId();
    
    this.bids.insertChallenge(new Challenge(id, result, BigDecimal.ZERO.setScale(2), null, BidState.OPENED, this.speedRunId));
    
    return id;
  }
  
  public int createNewChoice(String defaultName)
  {
    int id = IdUtils.generateId();
    
    this.bids.insertChoice(new Choice(id, defaultName, null, BidState.OPENED, this.speedRunId));
    
    return id;
  }

  public void deleteSpeedRun()
  {
    List<Bid> allBids = this.getAllBids();
    
    for (Bid b : allBids)
    {
      if (b.getType() == BidType.CHALLENGE)
      {
        this.bids.deleteChallenge(b.getId());
      }
      else
      {
        ChoiceControl control = new ChoiceControl(this.donationDatabase, b.getId());
        control.deleteChoice();
      }
    }
    
    this.speedRuns.deleteSpeedRun(this.speedRunId);
  }
  
  public ChoiceControl getChoiceControl(int choiceId)
  {
    return new ChoiceControl(this.donationDatabase, choiceId);
  }
  
  public ChallengeControl getChallengeControl(int challengeId)
  {
    return new ChallengeControl(this.donationDatabase, challengeId);
  }
  
  public int getSpeedRunId()
  {
    return this.speedRunId;
  }
}
