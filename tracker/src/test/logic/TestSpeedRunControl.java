package test.logic;

import java.util.List;

import pheidip.logic.DonationDatabaseManager;
import pheidip.logic.SpeedRunControl;
import pheidip.objects.Bid;
import pheidip.objects.Choice;
import pheidip.objects.SpeedRun;
import test.db.DBTestConfiguration;
import junit.framework.TestCase;

public class TestSpeedRunControl extends TestCase
{
  DonationDatabaseManager manager;
  
  public void setUp()
  {
    this.manager = new DonationDatabaseManager();
    this.manager.createMemoryDatabase();
    this.manager.runSQLScript(DBTestConfiguration.getTestDataDirectory() + "donation_bid_test_data_1.sql");
  }
  
  public void tearDown()
  {
    this.manager.closeConnection();
  }
  
  public void testCreateSpeedrunControl()
  {
    final int speedRunId = 1;
    
    SpeedRunControl control = new SpeedRunControl(this.manager, speedRunId);
    
    SpeedRun data = control.getData();
    
    assertNotNull(data);
    assertEquals(speedRunId, control.getSpeedRunId());
    assertEquals(speedRunId, data.getId());
    assertEquals("run 1", data.getName());
  }
  
  public void testGetAssociatedBids()
  {
    final int speedRunId = 1;
    
    SpeedRunControl control = new SpeedRunControl(this.manager, speedRunId);
   
    List<Bid> bids = control.getAllBids();
    
    assertEquals(2, bids.size());
    
    for (Bid b : bids)
    {
      if (b instanceof Choice)
      {
        assertEquals("naming something", b.getName());
      }
      else
      {
        assertEquals("challenge 2", b.getName());
      }
    }
  }
  
  public void testCreateNewSpeedRun()
  {
    int newId = SpeedRunControl.createNewSpeedRun(this.manager);
    
    SpeedRunControl control = new SpeedRunControl(this.manager, newId);
    
    SpeedRun result = control.getData();
    
    assertNotNull(result);
    assertNull(result.getName());
    
    List<Bid> donations = control.getAllBids();
    
    assertEquals(0, donations.size());
  }
  
  public void testDeleteSpeedRun()
  {
    final int id = 3;
    SpeedRunControl control = new SpeedRunControl(this.manager, id);
    
    assertNotNull(control.getData());
    
    control.deleteSpeedRun();
    
    control = new SpeedRunControl(this.manager, id);

    assertNull(control.getData());
  }
}
