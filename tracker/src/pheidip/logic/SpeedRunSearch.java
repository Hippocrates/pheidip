package pheidip.logic;

import java.util.List;

import pheidip.db.SpeedRunData;
import pheidip.objects.SpeedRun;
import pheidip.objects.SpeedRunSearchParams;

public class SpeedRunSearch
{
  private DonationDatabaseManager manager;
  private SpeedRunData speedRuns;

  public SpeedRunSearch(DonationDatabaseManager manager)
  {
    this.manager = manager;
    this.speedRuns = this.manager.getDataAccess().getSpeedRuns();
  }
  
  public SpeedRun createIfAble(SpeedRunSearchParams params)
  {
    int id = SpeedRunControl.createNewSpeedRun(this.manager, params.name);
    return this.speedRuns.getSpeedRunById(id);
  }
  
  public List<SpeedRun> searchSpeedRuns(SpeedRunSearchParams params)
  {
    return this.speedRuns.searchSpeedRuns(params);
  }
}
