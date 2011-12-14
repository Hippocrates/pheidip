package pheidip.logic;

import pheidip.db.DonationDataConstraintException;
import pheidip.db.PrizeData;
import pheidip.objects.Prize;
import pheidip.util.IdUtils;

public class PrizeControl
{
  private DonationDatabaseManager manager;
  private int prizeId;
  private PrizeData prizes;
  private Prize cachedData;
  
  public static int createNewPrize(DonationDatabaseManager manager)
  {
    return createNewPrize(manager, null);
  }
  
  public static int createNewPrize(DonationDatabaseManager manager, String name)
  {
    int newId = IdUtils.generateId();
    PrizeData prizes = manager.getDataAccess().getPrizeData();
    prizes.insertPrize(new Prize(newId, name, null, null, newId, null));
    return newId;
  }
  
  public PrizeControl(DonationDatabaseManager manager, int prizeId)
  {
    this.manager = manager;
    this.prizes = this.manager.getDataAccess().getPrizeData();
    this.prizeId = prizeId;
    this.cachedData = null;
  }

  public int getPrizeId()
  {
    return this.prizeId;
  }
  
  public Prize getData()
  {
    if (this.cachedData == null)
    {
      this.refreshData();
    }
    
    return this.cachedData;
  }
  
  public Prize refreshData()
  {
    this.cachedData = this.prizes.getPrizeById(this.prizeId);
    return this.cachedData;
  }

  public void removePrizeWinner()
  {
    this.getData().setWinner(null);
  }
  
  public void deletePrize()
  {
    try
    {
      Prize data = this.refreshData();
      
      if (data.getWinner() != null)
      {
        throw new RuntimeException("Error, remove the prize winner before deleting the prize.");
      }
      
      this.prizes.deletePrize(this.getData());
    }
    catch(DonationDataConstraintException e)
    {
      this.manager.reportMessage(e.getMessage());
    }
  }

  public void updateData(Prize data)
  {
    try
    {
      this.prizes.updatePrize(data);
    }
    catch(DonationDataConstraintException e)
    {
      this.manager.reportMessage(e.getMessage());
    }
  }
  
  public PrizeAssign getPrizeAssign()
  {
    return new PrizeAssign(this.manager);
  }
  
  public DonorSearch getDonorSearcher()
  {
    return new DonorSearch(this.manager);
  }
}
