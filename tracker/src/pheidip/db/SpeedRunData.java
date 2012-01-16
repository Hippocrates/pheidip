package pheidip.db;

import java.util.List;

import pheidip.objects.SearchEntity;
import pheidip.objects.SpeedRun;

public interface SpeedRunData {

	public SpeedRun getSpeedRunById(int runId);

	public void insertSpeedRun(SpeedRun speedRun);

	public void deleteSpeedRun(SpeedRun speedRun);

	public void updateSpeedRun(SpeedRun run);

	public List<SpeedRun> getAllSpeedRuns();

	public List<SpeedRun> searchSpeedRuns(SearchEntity<SpeedRun> params);

  public List<SpeedRun> searchSpeedRunsRange(SearchEntity<SpeedRun> params, int offset,
      int size);

void multiUpdateSpeedRuns(List<SpeedRun> toUpdate);
}