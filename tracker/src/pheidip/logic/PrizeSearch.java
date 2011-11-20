package pheidip.logic;

import java.util.List;

import pheidip.db.PrizeData;
import pheidip.objects.Prize;
import pheidip.objects.PrizeSearchParams;

public class PrizeSearch
{
  private DonationDatabaseManager manager;
  private PrizeData prizes;

  public PrizeSearch(DonationDatabaseManager manager)
  {
    this.manager = manager;
    this.prizes = this.manager.getDataAccess().getPrizeData();
  }
  
  public List<Prize> searchPrizes(String name, boolean excludeIfWon)
  {
    PrizeSearchParams params = new PrizeSearchParams();
    params.name = name;
    params.excludeIfWon = excludeIfWon;
    
    return this.prizes.searchPrizes(params);
  }
  
  public Prize createIfAble(String name)
  {
    int newId = PrizeControl.createNewPrize(this.manager, name);
    return this.prizes.getPrizeById(newId);
  }
}
