package pheidip.logic;

import java.util.ArrayList;
import java.util.List;

import pheidip.db.BidData;
import pheidip.objects.Bid;
import pheidip.objects.BidSearchParams;
import pheidip.objects.Challenge;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceOption;
import pheidip.objects.ChoiceOptionSearchParams;
import pheidip.objects.SpeedRun;
import pheidip.util.Filter;

public class BidSearch
{
  private DonationDatabaseManager manager;
  private BidData bids;

  public BidSearch(DonationDatabaseManager manager)
  {
    this.manager = manager;
    this.bids = this.manager.getDataAccess().getBids();
  }
  
  public SpeedRunSearch createSpeedRunSearch()
  {
    return new SpeedRunSearch(this.manager);
  }
  
  public Challenge createChallengeIfAble(SpeedRun speedRun, String name)
  {
    Challenge result = new Challenge();
    result.setSpeedRun(speedRun);
    result.setName(name);
    this.bids.insertChallenge(result);
    
    return result;
  }
  
  public Choice createChoiceIfAble(SpeedRun speedRun, String name)
  {
    Choice result = new Choice();
    result.setSpeedRun(speedRun);
    result.setName(name);
    this.bids.insertChoice(result);
    
    return result;
  }
  
  public ChoiceOption createOptionIfAble(int choiceId, String optionName)
  {
    ChoiceControl control = new ChoiceControl(this.manager, choiceId);
    int id = control.createNewOption(optionName);
    return this.bids.getChoiceOptionById(id);
  }
  
  public List<Bid> searchBids(BidSearchParams params)
  {
    return this.bids.searchBids(params);
  }
  
  public List<Bid> filterBids(BidSearchParams params)
  {
    return Filter.filterList(new ArrayList<Bid>(params.owner.getBids()), params);
  }
  
  // params.owner must not be null
  public List<ChoiceOption> filterChoiceOptions(ChoiceOptionSearchParams params)
  {
    return Filter.filterList(new ArrayList<ChoiceOption>(params.owner.getOptions()), params);
  }
}
