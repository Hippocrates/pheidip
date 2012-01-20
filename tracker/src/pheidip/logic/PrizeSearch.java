package pheidip.logic;

import java.util.List;

import pheidip.db.PrizeData;
import pheidip.objects.Prize;
import pheidip.objects.SearchParameters;

public class PrizeSearch extends EntitySearcher<Prize>
{
  private DonationDatabaseManager manager;
  private PrizeData prizes;

  public PrizeSearch(DonationDatabaseManager manager)
  {
    this.manager = manager;
    this.prizes = this.manager.getDataAccess().getPrizeData();
  }
  
  public Prize createIfAble(String name)
  {
    int newId = PrizeControl.createNewPrize(this.manager, name);
    return this.prizes.getPrizeById(newId);
  }

  @Override
  protected List<Prize> implRunSearch(SearchParameters<Prize> params,
      int searchOffset, int searchSize)
  {
    return this.prizes.searchPrizesRange(params, searchOffset, searchSize);
  }
}
