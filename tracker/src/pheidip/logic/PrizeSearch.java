package pheidip.logic;

import java.util.List;

import pheidip.db.PrizeData;
import pheidip.objects.Prize;
import pheidip.objects.PrizeSearchParams;
import pheidip.util.Filter;

public class PrizeSearch
{
  private DonationDatabaseManager manager;
  private PrizeData prizes;
  private List<Prize> cachedPrizes;

  public PrizeSearch(DonationDatabaseManager manager)
  {
    this.manager = manager;
    this.prizes = this.manager.getDataAccess().getPrizeData();
    
    this.cachedPrizes = this.prizes.getAllPrizes();
  }
  
  public List<Prize> searchPrizes(String name)
  {
    PrizeSearchParams params = new PrizeSearchParams();
    params.name = name;
    
    return Filter.filterList(this.cachedPrizes, params);
  }
  
  public Prize createIfAble(String name)
  {
    int newId = PrizeControl.createNewPrize(this.manager, name);
    return this.prizes.getPrizeById(newId);
  }
}
