package pheidip.logic;

import java.lang.reflect.Method;
import java.util.List;

import pheidip.db.BidData;
import pheidip.objects.Bid;
import pheidip.objects.Challenge;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceOption;
import pheidip.util.EqualsFilterFunction;
import pheidip.util.Filter;
import pheidip.util.InnerStringMatchFilter;
import pheidip.util.StringUtils;

public class BidSearch
{
  private DonationDatabaseManager manager;
  private BidData bids;
  private List<Bid> cachedBids;
  private Method nameMethod;
  private Method speedRunIdMethod;
  private Method optionNameMethod;
  private Method choiceIdMethod;
  private List<ChoiceOption> cachedOptions;

  public BidSearch(DonationDatabaseManager manager)
  {
    this.manager = manager;
    this.bids = this.manager.getDataAccess().getBids();
    this.cachedBids = this.bids.getAllBids();
    this.cachedOptions = this.bids.getAllChoiceOptions();

    try
    {
      this.speedRunIdMethod = Bid.class.getMethod("getSpeedRunId");
      this.nameMethod = Bid.class.getMethod("getName");
      this.optionNameMethod = ChoiceOption.class.getMethod("getName");
      this.choiceIdMethod = ChoiceOption.class.getMethod("getChoiceId");
    } 
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public Challenge createChallengeIfAble(int speedRunId, String name)
  {
    SpeedRunControl control = new SpeedRunControl(this.manager, speedRunId);
    int id = control.createNewChallenge(name);
    return this.bids.getChallengeById(id);
  }
  
  public Choice createChoiceIfAble(int speedRunId, String name)
  {
    SpeedRunControl control = new SpeedRunControl(this.manager, speedRunId);
    int id = control.createNewChoice(name);
    return this.bids.getChoiceById(id);
  }
  
  public ChoiceOption createOptionIfAble(int choiceId, String optionName)
  {
    ChoiceControl control = new ChoiceControl(this.manager, choiceId);
    int id = control.createNewOption(optionName);
    return this.bids.getChoiceOptionById(id);
  }
  
  public List<Bid> searchBids(Integer speedRunId, String bidName)
  {
    List<Bid> filtered = this.cachedBids;
    
    if (speedRunId != null)
    {
      filtered = Filter.filterList(filtered, new EqualsFilterFunction<Integer>(speedRunId), this.speedRunIdMethod);
    }
    
    if (!StringUtils.isEmptyOrNull(bidName))
    {
      filtered = Filter.filterList(filtered, new InnerStringMatchFilter(bidName), this.nameMethod);
    }
    
    return filtered;
  }
  
  public List<ChoiceOption> searchChoiceOptions(Integer choiceId, String optionName)
  {
    List<ChoiceOption> filtered = this.cachedOptions;
    
    if (choiceId != null)
    {
      filtered = Filter.filterList(filtered, new EqualsFilterFunction<Integer>(choiceId), this.choiceIdMethod);
    }
    
    if (!StringUtils.isEmptyOrNull(optionName))
    {
      filtered = Filter.filterList(filtered, new InnerStringMatchFilter(optionName), this.optionNameMethod);
    }
    
    return filtered;
  }
}
