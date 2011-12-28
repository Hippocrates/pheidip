package pheidip.logic;

import java.math.BigDecimal;

import pheidip.db.DonationDataConstraintException;
import pheidip.db.PrizeData;
import pheidip.objects.Donor;
import pheidip.objects.Prize;
import pheidip.objects.PrizeDrawMethod;
import pheidip.objects.SpeedRun;
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
    prizes.insertPrize(new Prize(newId, name, null, null, newId, PrizeDrawMethod.RANDOM_UNIFORM_DRAW, new BigDecimal("5.00"), null, null, null));
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
    Prize data = this.getData();
    
    Donor donor = data.getWinner();
    
    if (donor != null)
    {
      donor.getPrizes().remove(data);
    }
    
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
  
  public void setStartGame(SpeedRun startGame)
  {
    this.getData().setStartGame(startGame);
    if (startGame != null)
    {
      startGame.getPrizeStartGame().add(this.getData());
    }
    this.updateData(this.getData());
  }
  
  public void setEndGame(SpeedRun endGame)
  {
    this.getData().setEndGame(endGame);
    if (endGame != null)
    {
      endGame.getPrizeEndGame().add(this.getData());
    }
    this.updateData(this.getData());
  }
  
  public PrizeAssign getPrizeAssign()
  {
    return new PrizeAssign(this.manager);
  }
  
  public DonorSearch getDonorSearcher()
  {
    return new DonorSearch(this.manager);
  }
  
  public SpeedRunSearch getSpeedRunSearch()
  {
    return new SpeedRunSearch(this.manager);
  }
}
