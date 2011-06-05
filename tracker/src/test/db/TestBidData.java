package test.db;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import pheidip.db.BidData;
import pheidip.db.DonationDataAccess;
import pheidip.db.ScriptRunner;
import pheidip.objects.Choice;
import junit.framework.TestCase;

public class TestBidData extends TestCase
{
  private DonationDataAccess dataAccess;
  private BidData bids;

  public void setUp()
  {
    this.dataAccess = new DonationDataAccess();
    this.dataAccess.createMemoryDatabase();

    ScriptRunner runner = new ScriptRunner(this.dataAccess.getConnection(), true,
        true);

    try
    {
      runner.runScript(new FileReader(DBTestConfiguration
          .getTestDataDirectory() + "donation_bid_test_data_1.sql"));
    } 
    catch (IOException e)
    {
      fail(e.getMessage());
    } 
    catch (SQLException e)
    {
      this.dataAccess.handleSQLException(e);
    }

    this.bids = this.dataAccess.getBids();;
  }

  public void tearDown()
  {
    if (this.dataAccess.isConnected())
    {
      this.dataAccess.closeConnection();
    }
  }
  
  public void testGetChoiceById()
  {
    final int choiceId = 1;
    Choice choice = this.bids.getChoiceById(choiceId);
    
    assertEquals(choiceId, choice.getId());
    assertEquals(1, choice.getSpeedRunId());
    assertEquals("naming something", choice.getName());
  }
  
  public void testCreateChoice()
  {
    final int speedRunId = 1;
    final String name = "some thing";
    final int newId = 15;
    
    assertNull(this.bids.getChoiceById(newId));
    
    this.bids.insertChoice(new Choice(newId, name, speedRunId));
  
    Choice choice = this.bids.getChoiceById(newId);
    
    assertEquals(newId, choice.getId());
    assertEquals(speedRunId, choice.getSpeedRunId());
    assertEquals(name, choice.getName());
  }
  
  public void testChangeChoiceName()
  {
    final int choiceId = 1;
    final String newName = "some other thing";
    
    Choice oldChoice = this.bids.getChoiceById(choiceId);
    
    assertFalse(newName.equals(oldChoice.getName()));
    
    this.bids.setChoiceName(choiceId, newName);
    
    Choice choice = this.bids.getChoiceById(choiceId);
    
    assertEquals(newName, choice.getName());
  }
  
  public void testSearchChoicesBySpeedRun()
  {
    final int speedRunId = 1;
    
    List<Choice> choices = this.bids.getChoicesBySpeedrun(speedRunId);
    
    assertEquals(3, choices.size());
  }
}
