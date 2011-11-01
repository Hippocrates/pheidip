package pheidip.logic;

import pheidip.db.DonationDataConstraintException;
import pheidip.db.DonorData;
import pheidip.db.PrizeData;
import pheidip.objects.Donor;
import pheidip.objects.Prize;
import pheidip.util.IdUtils;
import pheidip.util.StringUtils;

public class PrizeControl
{
  private DonationDatabaseManager manager;
  private int prizeId;
  private PrizeData prizes;
  private DonorData donors;
  
  public static int createNewPrize(DonationDatabaseManager manager)
  {
    return createNewPrize(manager, null);
  }
  
  public static int createNewPrize(DonationDatabaseManager manager, String name)
  {
    int newId = IdUtils.generateId();
    PrizeData prizes = manager.getDataAccess().getPrizeData();
    prizes.insertPrize(new Prize(newId, name, null, null, null));
    return newId;
  }
  
  public PrizeControl(DonationDatabaseManager manager, int prizeId)
  {
    this.manager = manager;
    this.prizes = this.manager.getDataAccess().getPrizeData();
    this.prizeId = prizeId;
    this.donors = this.manager.getDataAccess().getDonorData();
  }

  public int getPrizeId()
  {
    return this.prizeId;
  }
  
  public Prize getData()
  {
    return this.prizes.getPrizeById(this.prizeId);
  }
  
  public Donor getPrizeWinner()
  {
    return this.donors.getPrizeWinner(this.prizeId);
  }
  
  public void setPrizeWinner(int donorId)
  {
    this.prizes.setPrizeWinner(this.prizeId, donorId);
  }
  
  public void removePrizeWinner()
  {
    this.prizes.removePrizeWinner(this.prizeId);
  }
  
  public void deletePrize()
  {
    try
    {
      this.prizes.deletePrize(this.prizeId);
    }
    catch(DonationDataConstraintException e)
    {
      this.manager.reportMessage(e.getMessage());
    }
  }

  public void updateData(String name, String imageURL, String description, Integer winner)
  {
    try
    {
      Donor d = null;
      if (winner != null)
      {
        d = this.donors.getDonorById(winner);
      }
      
      this.prizes.updatePrize(new Prize(
          this.prizeId, 
          StringUtils.nullIfEmpty(name), 
          StringUtils.nullIfEmpty(imageURL), 
          StringUtils.nullIfEmpty(description),
          d));
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
