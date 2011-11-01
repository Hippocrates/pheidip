package test.db;

import java.math.BigDecimal;
import java.util.List;

import pheidip.db.BidData;
import pheidip.db.SpeedRunData;
import pheidip.objects.BidState;
import pheidip.objects.Challenge;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceOption;

public class TestBidData extends DonationDatabaseTest
{
  private BidData bids;
  private SpeedRunData speedRuns;
  
  public void setUp()
  {
    super.setUp();
    this.bids = this.getDataAccess().getBids();
    this.speedRuns = this.getDataAccess().getSpeedRuns();
  }

  public void testGetChoiceById()
  {
    final int choiceId = 1;
    Choice choice = this.bids.getChoiceById(choiceId);
    
    assertEquals(choiceId, choice.getId());
    //assertEquals(1, (int)choice.getSpeedRunId());
    assertEquals("naming something", choice.getName());
  }
  
  public void testCreateChoice()
  {
    final int speedRunId = 1;
    final String name = "some thing";
    final int newId = 15;
    
    assertNull(this.bids.getChoiceById(newId));
    
    this.bids.insertChoice(new Choice(newId, name, null, BidState.OPENED, this.speedRuns.getSpeedRunById(speedRunId)));
  
    Choice choice = this.bids.getChoiceById(newId);
    
    assertEquals(newId, choice.getId());
    //assertEquals(speedRunId, (int)choice.getSpeedRunId());
    assertEquals(name, choice.getName());
    assertEquals(null, choice.getDescription());
  }
  
  public void testUpdateChoice()
  {
    final int choiceId = 1;
    final String newName = "some other thing";
    final int anotherRun = 2;
    
    Choice oldChoice = this.bids.getChoiceById(choiceId);
    
    assertFalse(newName.equals(oldChoice.getName()));
    //assertFalse(anotherRun == oldChoice.getSpeedRunId());
    
    this.bids.updateChoice(new Choice(choiceId, newName, null, BidState.OPENED, this.speedRuns.getSpeedRunById(anotherRun)));
    
    Choice choice = this.bids.getChoiceById(choiceId);
    
    assertEquals(choiceId, choice.getId());
    //assertEquals(anotherRun, (int)choice.getSpeedRunId());
    assertEquals(newName, choice.getName());
    assertEquals(null, choice.getDescription());
  }
  
  public void testSearchChoicesBySpeedRun()
  {
    final int speedRunId = 1;
    
    List<Choice> choices = this.bids.getChoicesBySpeedrun(speedRunId);
    
    assertEquals(3, choices.size());
  }
  
  public void testDeleteChoice()
  {
    final int choiceId = 4;
    
    assertNotNull(this.bids.getChoiceById(choiceId));
    
    this.bids.deleteChoice(choiceId);
    
    assertNull(this.bids.getChoiceById(choiceId));
  }
  

  public void testGetChoiceOptionById()
  {
    final int choiceOptionId = 1;
    ChoiceOption choiceOption = this.bids.getChoiceOptionById(choiceOptionId);
    
    assertEquals(choiceOptionId, choiceOption.getId());
    //assertEquals(1, choiceOption.getChoiceId());
    assertEquals("name 1", choiceOption.getName());
  }
  
  public void testCreateChoiceOption()
  {
    final int choiceId = 1;
    final String name = "some thing";
    final int newId = 15;
    
    assertNull(this.bids.getChoiceOptionById(newId));
    
    this.bids.insertChoiceOption(new ChoiceOption(newId, name, this.bids.getChoiceById(choiceId)));
  
    ChoiceOption choiceOption = this.bids.getChoiceOptionById(newId);
    
    assertEquals(newId, choiceOption.getId());
    assertEquals(choiceId, choiceOption.getChoice().getId());
    assertEquals(name, choiceOption.getName());
  }
  
  public void testChangeChoiceOptionName()
  {
    final int choiceOptionId = 1;
    final String newName = "some other thing";
    final int anotherChoice = 4;
    
    ChoiceOption oldChoiceOption = this.bids.getChoiceOptionById(choiceOptionId);
    
    assertFalse(newName.equals(oldChoiceOption.getName()));
    assertFalse(anotherChoice == oldChoiceOption.getChoice().getId());
    
    this.bids.updateChoiceOption(new ChoiceOption(choiceOptionId, newName, this.bids.getChoiceById(anotherChoice)));
    
    ChoiceOption choiceOption = this.bids.getChoiceOptionById(choiceOptionId);
    
    assertEquals(newName, choiceOption.getName());
    assertEquals(anotherChoice, choiceOption.getChoice().getId());
    assertEquals(newName, choiceOption.getName());
  }
  
  public void testSearchChoiceOptionsByChoice()
  {
    final int choiceId = 1;
    
    List<ChoiceOption> choiceOptions = this.bids.getChoiceOptionsByChoiceId(choiceId);
    
    assertEquals(2, choiceOptions.size());
  }
  
  public void testDeleteChoiceOption()
  {
    final int choiceOptionId = 3;
    
    assertNotNull(this.bids.getChoiceOptionById(choiceOptionId));
    
    this.bids.deleteChoiceOption(choiceOptionId);
    
    assertNull(this.bids.getChoiceOptionById(choiceOptionId));
  }

  public void testGetChallengeById()
  {
    final int challengeId = 5;
    Challenge challenge = this.bids.getChallengeById(challengeId);
    
    assertEquals(challengeId, challenge.getId());
    //assertEquals(2, (int)challenge.getSpeedRunId());
    assertEquals("challenge 1", challenge.getName());
  }
  
  public void testCreateChallenge()
  {
    final int speedRunId = 5;
    final String name = "some thing";
    final BigDecimal amount = new BigDecimal("12.00");
    final int newId = 15;
    
    assertNull(this.bids.getChallengeById(newId));
    
    this.bids.insertChallenge(new Challenge(newId, name, amount, null, BidState.OPENED, this.speedRuns.getSpeedRunById(speedRunId)));
  
    Challenge challenge = this.bids.getChallengeById(newId);
    
    assertEquals(newId, challenge.getId());
    //assertEquals(speedRunId, (int)challenge.getSpeedRunId());
    assertEquals(amount, challenge.getGoalAmount());
    assertEquals(name, challenge.getName());
    assertEquals(null, challenge.getDescription());
  }
  
  public void updateChallenge()
  {
    final int challengeId = 5;
    final String newName = "some other thing";
    final int anotherRun = 2;
    final BigDecimal newAmount = new BigDecimal("15.56");
    
    Challenge oldChallenge = this.bids.getChallengeById(challengeId);    
    assertFalse(newName.equals(oldChallenge.getName()));
    assertFalse(newAmount.equals(oldChallenge.getGoalAmount()));
    assertFalse(anotherRun == oldChallenge.getSpeedRun().getId());
    
    this.bids.updateChallenge(new Challenge(challengeId, newName, newAmount, null, BidState.OPENED, this.speedRuns.getSpeedRunById(anotherRun)));
    
    Challenge challenge = this.bids.getChallengeById(challengeId);
    
    assertEquals(challengeId, challenge.getId());
    assertEquals(anotherRun, (int)challenge.getSpeedRun().getId());
    assertEquals(newAmount, challenge.getGoalAmount());
    assertEquals(newName, challenge.getName());
    assertEquals(null, challenge.getDescription());
  }
  
  public void testSearchChallengesBySpeedRun()
  {
    final int speedRunId = 1;
    
    List<Challenge> Challenges = this.bids.getChallengesBySpeedrun(speedRunId);
    
    assertEquals(1, Challenges.size());
  }
  
  public void testDeleteChallenge()
  {
    final int challengeId = 8;
    
    assertNotNull(this.bids.getChallengeById(challengeId));
    
    this.bids.deleteChallenge(challengeId);
    
    assertNull(this.bids.getChallengeById(challengeId));
  }
}
