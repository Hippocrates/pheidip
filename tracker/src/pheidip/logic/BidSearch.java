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
