package pheidip.logic;

import java.util.ArrayList;
import java.util.List;

import pheidip.db.BidData;
import pheidip.db.SpeedRunData;
import pheidip.objects.Bid;
import pheidip.objects.SpeedRun;
import pheidip.util.IdUtils;

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
  
  public void updateData(String newName)
  {
    this.speedRuns.updateSpeedRun(new SpeedRun(this.speedRunId, newName));
  }

  public List<Bid> getAllBids()
  {
    List<Bid> bids = new ArrayList<Bid>();
    
    bids.addAll(this.bids.getChallengesBySpeedrun(this.speedRunId));
    bids.addAll(this.bids.getChoicesBySpeedrun(this.speedRunId));
    
    return bids;
  }

  public static int createNewSpeedRun(DonationDatabaseManager manager)
  {
    int id = IdUtils.generateId();
    manager.getDataAccess().getSpeedRuns().insertSpeedRun(new SpeedRun(id, null));
    return id;
  }

  public void deleteSpeedRun()
  {
    //TODO: clear all related bids
    
    this.speedRuns.deleteSpeedRun(this.speedRunId);
  }
  
  public int getSpeedRunId()
  {
    return this.speedRunId;
  }
}
