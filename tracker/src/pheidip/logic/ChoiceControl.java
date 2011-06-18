package pheidip.logic;

import java.math.BigDecimal;
import java.util.List;

import pheidip.db.BidData;
import pheidip.db.DonationDataConstraintException;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceOption;
import pheidip.util.IdUtils;

public class ChoiceControl
{
  private DonationDatabaseManager donationDatabase;
  private BidData bids;
  private int choiceId;

  public ChoiceControl(DonationDatabaseManager donationDatabase, int choiceId)
  {
    this.donationDatabase = donationDatabase;
    this.choiceId = choiceId;
    this.bids = this.donationDatabase.getDataAccess().getBids();
  }
  
  public Choice getData()
  {
    return this.bids.getChoiceById(this.choiceId);
  }
  
  public List<ChoiceOption> getOptions()
  {
    return this.bids.getChoiceOptionsByChoiceId(this.choiceId);
  }
  
  public int createNewOption(String name)
  {
    try
    {
      int optionId = IdUtils.generateId();
      ChoiceOption op = new ChoiceOption(optionId, name, this.choiceId);
      this.bids.insertChoiceOption(op);
      return optionId;
    }
    catch (DonationDataConstraintException e)
    {
      this.donationDatabase.reportMessage(e.getMessage());
    }
    return 0;
  }
  
  public BigDecimal getOptionTotal(int choiceOptionId)
  {
    return this.bids.getChoiceOptionTotal(choiceOptionId);
  }
  
  public void renameOption(int id, String newName)
  {
    try
    {
      this.bids.updateChoiceOption(new ChoiceOption(id, newName, this.choiceId));
    }
    catch (DonationDataConstraintException e)
    {
      this.donationDatabase.reportMessage(e.getMessage());
    }
  }
  
  public void deleteOption(int id)
  {
     this.bids.deleteChoiceOption(id);
  }
  
  public void deleteChoice()
  {
    this.bids.deleteChoice(this.choiceId);
  }

  public void updateData(String text)
  {
    try
    {
      Choice c = this.getData();
      this.bids.updateChoice(new Choice(this.choiceId, text, c.getSpeedRunId()));
    }
    catch (DonationDataConstraintException e)
    {
      this.donationDatabase.reportMessage(e.getMessage());
    }
  }

  public int getChoiceId()
  {
    return this.choiceId;
  }
}
