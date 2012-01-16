package pheidip.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pheidip.db.BidData;
import pheidip.objects.Bid;
import pheidip.objects.BidState;
import pheidip.objects.Challenge;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceOption;
import pheidip.objects.ChoiceOptionSearchParams;
import pheidip.objects.SearchEntity;
import pheidip.objects.SpeedRun;
import pheidip.util.Filter;

public class BidSearch extends EntitySearcher<Bid>
{
  private DonationDatabaseManager manager;
  private BidData bids;

  public BidSearch(DonationDatabaseManager manager)
  {
    this.manager = manager;
    this.bids = this.manager.getDataAccess().getBidData();
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
    result.setBidState(BidState.HIDDEN);
    result.setGoalAmount(BigDecimal.ZERO);
    speedRun.getBids().add(result);

    this.bids.insertChallenge(result);
    
    return result;
  }
  
  public Choice createChoiceIfAble(SpeedRun speedRun, String name)
  {
    Choice result = new Choice();
    result.setSpeedRun(speedRun);
    result.setName(name);
    result.setBidState(BidState.HIDDEN);
    speedRun.getBids().add(result);

    this.bids.insertChoice(result);
    
    return result;
  }
  
  public ChoiceOption createOptionIfAble(Choice owner, String optionName)
  {
    // this is neccessary since the speedRun in question will have been a bulk transaction result
    owner = this.bids.getChoiceById(owner.getId());
    ChoiceOption toAdd = new ChoiceOption();
    toAdd.setName(optionName);
    toAdd.setChoice(owner);
    owner.getOptions().add(toAdd);

    this.bids.insertChoiceOption(toAdd);

    return toAdd;
  }

  @Override
  protected List<Bid> implRunSearch(SearchEntity<Bid> params, int searchOffset, int searchSize)
  {
    return this.bids.searchBidsRange(params, searchOffset, searchSize);
  }
  
  // params.owner must not be null
  public List<ChoiceOption> filterChoiceOptions(ChoiceOptionSearchParams params)
  {
    return Filter.filterList(new ArrayList<ChoiceOption>(params.owner.getOptions()), params);
  }
}
