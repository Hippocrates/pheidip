package pheidip.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pheidip.db.BidData;
import pheidip.db.DonationData;
import pheidip.db.DonationDataConstraintException;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceBid;
import pheidip.objects.ChoiceOption;
import pheidip.util.IdUtils;
import pheidip.util.Pair;

public class ChoiceControl
{
  private DonationDatabaseManager donationDatabase;
  private BidData bids;
  private DonationData donations;
  private int choiceId;
  private Choice cachedData;

  public ChoiceControl(DonationDatabaseManager donationDatabase, int choiceId)
  {
    this.donationDatabase = donationDatabase;
    this.choiceId = choiceId;
    this.donations = this.donationDatabase.getDataAccess().getDonationData();
    this.bids = this.donationDatabase.getDataAccess().getBids();
    this.cachedData = null;
  }
  
  public Choice refreshData()
  {
    this.cachedData = this.bids.getChoiceById(this.choiceId);
    return this.cachedData;
  }
  
  public Choice getData()
  {
    if (this.cachedData == null)
    {
      this.refreshData();
    }
    
    return this.cachedData;
  }
  
  public List<ChoiceOption> getOptions()
  {
    return new ArrayList<ChoiceOption>(this.getData().getOptions());
  }
  
  public void createNewOption(String name)
  {
    try
    {
      int optionId = IdUtils.generateId();
      
      Choice data = this.getData();

      ChoiceOption op = new ChoiceOption(optionId, name, this.getData());
      data.getOptions().add(op);
      this.bids.updateChoice(data);
    }
    catch (DonationDataConstraintException e)
    {
      this.donationDatabase.reportMessage(e.getMessage());
    }
  }
  
  public List<ChoiceBid> getAssociatedBids(ChoiceOption option)
  {
    return new ArrayList<ChoiceBid>(option.getBids());
  }
  
  public BigDecimal getOptionTotal(ChoiceOption option)
  {
    BigDecimal result = BigDecimal.ZERO;
    
    for (ChoiceBid b : getAssociatedBids(option))
    {
      result = result.add(b.getAmount());
    }
    
    return result.setScale(2);//this.bids.getChoiceOptionTotal(choiceOptionId);
  }
  
  public void deleteOption(ChoiceOption option)
  {
    for (ChoiceBid b : getAssociatedBids(option))
    {
      this.donations.deleteDonationBid(b);
    }

    this.bids.deleteChoiceOption(option);
  }
  
  public void deleteChoice()
  {
    Choice c = this.refreshData();
    
    for (ChoiceOption o : c.getOptions())
    {
      this.deleteOption(o);
    }

    this.bids.deleteChoice(this.refreshData());
    this.cachedData = null;
  }

  public void updateData(Choice data)
  {
    try
    {
      this.bids.updateChoice(data);
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
    Choice data = this.getData();

    List<ChoiceOption> options = new ArrayList<ChoiceOption>(data.getOptions());
    
    List<Pair<ChoiceOption,BigDecimal>> result = new ArrayList<Pair<ChoiceOption,BigDecimal>>();
    
    for (ChoiceOption o : options)
    {
      result.add(new Pair<ChoiceOption, BigDecimal>(o, this.getOptionTotal(o)));
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
