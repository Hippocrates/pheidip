package pheidip.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pheidip.db.BidData;
import pheidip.db.DonationDataConstraintException;
import pheidip.objects.BidState;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceOption;
import pheidip.util.IdUtils;
import pheidip.util.Pair;

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
    List<ChoiceOption> allOptions = this.getOptions();
    
    for (ChoiceOption option : allOptions)
    {
      this.bids.deleteChoiceOption(option.getId());
    }
    
    this.bids.deleteChoice(this.choiceId);
  }

  public void updateData(String name, String description, BidState newState)
  {
    try
    {
      Choice c = this.getData();
      this.bids.updateChoice(new Choice(this.choiceId, name, description, newState, c.getSpeedRunId()));
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

  public List<Pair<ChoiceOption, BigDecimal>> getOptionsWithTotals(boolean returnSorted)
  {
    List<ChoiceOption> options = this.getOptions();
    
    List<Pair<ChoiceOption,BigDecimal>> result = new ArrayList<Pair<ChoiceOption,BigDecimal>>();
    
    for (ChoiceOption o : options)
    {
      result.add(new Pair<ChoiceOption, BigDecimal>(o, this.getOptionTotal(o.getId())));
    }
    
    if (returnSorted)
    {
      Collections.sort(result, new Comparator<Pair<ChoiceOption, BigDecimal>>()
      {
        public int compare(Pair<ChoiceOption, BigDecimal> o1,
            Pair<ChoiceOption, BigDecimal> o2)
        {
          return o1.getSecond().compareTo(o2.getSecond());
        }
      });
    }
    
    return result;
  }
}
