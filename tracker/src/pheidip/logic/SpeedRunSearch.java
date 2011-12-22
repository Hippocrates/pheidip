package pheidip.logic;

import java.util.List;

import pheidip.db.SpeedRunData;
import pheidip.objects.SpeedRun;
import pheidip.objects.SpeedRunSearchParams;

public class SpeedRunSearch extends AbstractSearcher<SpeedRun, SpeedRunSearchParams>
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

  @Override
  protected List<SpeedRun> implRunSearch(
      SpeedRunSearchParams params, int searchOffset, int searchSize)
  {
    return this.speedRuns.searchSpeedRunsRange(params, searchOffset, searchSize);
  }
}
