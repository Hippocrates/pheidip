package pheidip.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pheidip.db.SpeedRunData;
import pheidip.objects.Bid;
import pheidip.objects.BidState;
import pheidip.objects.Challenge;
import pheidip.objects.Choice;
import pheidip.objects.SpeedRun;
import pheidip.util.IdUtils;

public class SpeedRunControl
{
  private DonationDatabaseManager donationDatabase;
  private SpeedRunData speedRuns;
  private int speedRunId;
  private SpeedRun cachedData;
  
  public SpeedRunControl(DonationDatabaseManager manager, int speedRunId)
  {
    this.donationDatabase = manager;
    this.speedRuns = this.donationDatabase.getDataAccess().getSpeedRunData();
    this.speedRunId = speedRunId;
    this.cachedData = null;
  }

  public SpeedRun getData()
  {
    if (this.cachedData == null)
    {
      this.refreshData();
    }
    
    return this.cachedData;
  }
  
  public SpeedRun refreshData()
  {
    this.cachedData = this.speedRuns.getSpeedRunById(this.speedRunId);
    return this.cachedData;
  }
  
  public void updateData(SpeedRun data)
  {
    this.speedRuns.updateSpeedRun(data);
  }

  public List<Bid> getAllBids()
  {
    return new ArrayList<Bid>(this.getData().getBids());
  }
  
  public static int createNewSpeedRun(DonationDatabaseManager manager, String name)
  {
    int id = IdUtils.generateId();
    manager.getDataAccess().getSpeedRunData().insertSpeedRun(new SpeedRun(id, name, "", id, new Date(), new Date(), ""));
    return id;
  }

  public static int createNewSpeedRun(DonationDatabaseManager manager)
  {
    return createNewSpeedRun(manager, null);
  }
  
  public int createNewChallenge(String result)
  {
    int id = IdUtils.generateId();

    Challenge inserted = new Challenge(id, result, BigDecimal.ZERO.setScale(2), null, BidState.HIDDEN, this.getData());
    this.getData().getBids().add(inserted);
    
    this.updateData(this.getData());
    
    return id;
  }
  
  public int createNewChoice(String defaultName)
  {
    int id = IdUtils.generateId();
    
    Choice inserted = new Choice(id, defaultName, null, BidState.OPENED, this.getData());
    this.getData().getBids().add(inserted);
    
    this.updateData(this.getData());

    return id;
  }

  public void deleteSpeedRun()
  {
    this.refreshData();

    this.speedRuns.deleteSpeedRun(this.cachedData);
    this.cachedData = null;
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
