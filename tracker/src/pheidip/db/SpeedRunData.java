package pheidip.db;

import java.util.List;

import pheidip.objects.SpeedRun;

public interface SpeedRunData {

	public SpeedRun getSpeedRunById(int runId);

	public void insertSpeedRun(SpeedRun speedRun);

	public void deleteSpeedRun(int runId);

	public void updateSpeedRun(SpeedRun run);

	public List<SpeedRun> getAllSpeedRuns();

}