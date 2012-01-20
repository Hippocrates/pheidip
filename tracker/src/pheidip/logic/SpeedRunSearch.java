package pheidip.logic;

import java.util.List;

import pheidip.db.SpeedRunData;
import pheidip.objects.SearchParameters;
import pheidip.objects.SpeedRun;
import pheidip.objects.SpeedRunSearchParams;

public class SpeedRunSearch extends EntitySearcher<SpeedRun>
{
  private DonationDatabaseManager manager;
  private SpeedRunData speedRuns;

  public SpeedRunSearch(DonationDatabaseManager manager)
  {
    this.manager = manager;
    this.speedRuns = this.manager.getDataAccess().getSpeedRunData();
  }
  
  public SpeedRun createIfAble(SpeedRunSearchParams params)
  {
    int id = SpeedRunControl.createNewSpeedRun(this.manager, params.getName());
    return this.speedRuns.getSpeedRunById(id);
  }

  @Override
  protected List<SpeedRun> implRunSearch(
      SearchParameters<SpeedRun> params, int searchOffset, int searchSize)
  {
    return this.speedRuns.searchSpeedRunsRange(params, searchOffset, searchSize);
  }
}
