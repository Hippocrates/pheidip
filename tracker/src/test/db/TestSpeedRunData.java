package test.db;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pheidip.db.SpeedRunData;
import pheidip.objects.SpeedRun;


public class TestSpeedRunData extends DonationDatabaseTest
{
  private SpeedRunData speedRuns;
  
  public void setUp()
  {
    super.setUp();
    this.speedRuns = this.getDataAccess().getSpeedRunData();
  }
  
  public void testGetSpeedRunById()
  {
    final int runId = 3;
    SpeedRun s = this.speedRuns.getSpeedRunById(runId);
    
    assertNotNull(s);
    assertEquals(runId, s.getId());
    assertEquals("yet another run", s.getName());
    assertEquals(runId, s.getSortKey());
    
    assertEquals(2, s.getBids().size());
    
    assertEquals(0, s.getPrizeStartGame().size());
    assertEquals(1, s.getPrizeEndGame().size());
  }
  
  public void testInsertSpeedRun()
  {
    final int runId = 5;
    final String runName = "another run";
    
    Calendar c = Calendar.getInstance();
    
    c.set(2007,11,15,03,11,00);
    Date startTime = c.getTime();
    c.set(2007,11,15,05,19,24);
    Date endTime = c.getTime();
    this.speedRuns.insertSpeedRun(new SpeedRun(runId, runName, "", runId, startTime, endTime, ""));
    
    SpeedRun s = this.speedRuns.getSpeedRunById(runId);
    
    assertNotNull(s);
    assertEquals(runId, s.getId());
    assertEquals(runName, s.getName());
    assertEquals("", s.getDescription());
    assertEquals(startTime, s.getStartTime());
    assertEquals(endTime, s.getEndTime());
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
    
    this.speedRuns.updateSpeedRun(new SpeedRun(runId, newName, "", runId, new Date(), new Date(), null));
    
    SpeedRun sprime = this.speedRuns.getSpeedRunById(runId);
    
    assertEquals(newName, sprime.getName());
  }
  
  public void testGetAllSpeedRuns()
  {
    List<SpeedRun> all = this.speedRuns.getAllSpeedRuns();
    
    assertEquals(3, all.size());
  }
}
