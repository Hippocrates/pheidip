package test.db;

import java.util.List;

import pheidip.db.SpeedRunData;
import pheidip.objects.SpeedRun;


public class TestSpeedRunData extends DonationDatabaseTest
{
  private SpeedRunData speedRuns;
  
  public void setUp()
  {
    super.setUp();
    this.speedRuns = this.getDataAccess().getSpeedRuns();
  }
  
  public void testGetSpeedRunById()
  {
    final int runId = 1;
    SpeedRun s = this.speedRuns.getSpeedRunById(runId);
    
    assertNotNull(s);
    assertEquals(runId, s.getId());
    assertEquals("run 1", s.getName());
  }
  
  public void testInsertSpeedRun()
  {
    final int runId = 5;
    final String runName = "another run";
    
    this.speedRuns.insertSpeedRun(new SpeedRun(runId, runName));
    
    SpeedRun s = this.speedRuns.getSpeedRunById(runId);
    
    assertNotNull(s);
    assertEquals(runId, s.getId());
    assertEquals(runName, s.getName());
  }
  
  public void testDeleteSpeedRun()
  {
    final int runId = 3;
    
    assertNotNull(this.speedRuns.getSpeedRunById(runId));

    this.speedRuns.deleteSpeedRun(runId);
    
    assertNull(this.speedRuns.getSpeedRunById(runId));
  }
  
  public void testUpdateSpeedRun()
  {
    final int runId = 3;
    String newName = "something else";
    
    SpeedRun s = this.speedRuns.getSpeedRunById(runId);
    
    assertFalse(newName.equals(s.getName()));
    
    this.speedRuns.updateSpeedRun(new SpeedRun(runId, newName));
    
    SpeedRun sprime = this.speedRuns.getSpeedRunById(runId);
    
    assertEquals(newName, sprime.getName());
  }
  
  public void testGetAllSpeedRuns()
  {
    List<SpeedRun> all = this.speedRuns.getAllSpeedRuns();
    
    assertEquals(3, all.size());
  }
}
