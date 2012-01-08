package pheidip.db;

import java.util.List;

import pheidip.objects.SpeedRun;
import pheidip.objects.SpeedRunSearchParams;

public interface SpeedRunData {

	public SpeedRun getSpeedRunById(int runId);

	public void insertSpeedRun(SpeedRun speedRun);

	public void deleteSpeedRun(SpeedRun speedRun);

	public void updateSpeedRun(SpeedRun run);

	public List<SpeedRun> getAllSpeedRuns();

	public List<SpeedRun> searchSpeedRuns(SpeedRunSearchParams params);

  public List<SpeedRun> searchSpeedRunsRange(SpeedRunSearchParams params, int offset,
      int size);

void multiUpdateSpeedRuns(List<SpeedRun> toUpdate);
}