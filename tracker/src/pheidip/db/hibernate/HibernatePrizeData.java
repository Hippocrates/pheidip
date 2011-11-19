package pheidip.db.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import pheidip.db.PrizeData;
import pheidip.objects.Donor;
import pheidip.objects.Prize;

public class HibernatePrizeData extends HibernateDataInterface implements PrizeData
{
  public HibernatePrizeData(HibernateDonationDataAccess manager)
  {
    super(manager);
  }

  @Override
  public void insertPrize(Prize toAdd)
  {
    Session session = this.beginTransaction();
    
    
    session.save(toAdd);
    
    this.endTransaction();
    //session.close();
  }

  @Override
  public void updatePrize(Prize toUpdate)
  {
    Session session = this.beginTransaction();
    
    
    session.merge(toUpdate);
    
    this.endTransaction();
    //session.close();
  }

  @Override
  public Prize getPrizeById(int prizeId)
  {
    Session session = this.beginTransaction();
    
    
    Prize p = (Prize) session.get(Prize.class, prizeId);
    
    this.endTransaction();
    //session.close();
    
    return p;
  }

  @Override
  public List<Prize> getAllPrizes()
  {
    Session session = this.beginTransaction();
    
    
    Query q = session.createQuery("from Prize");

    @SuppressWarnings("unchecked")
    List<Prize> listing = q.list();
    
    this.endTransaction();
    //session.close();
    
    return listing;
  }

  @Override
  public Prize getPrizeByDonorId(int donorId)
  {
    Prize result = null;
    
    Session session = this.beginTransaction();
    
    
    Query q = session.createQuery("from Prize as p where p.winner.id = :donorid");

    q.setInteger("donorid", donorId);
    
    @SuppressWarnings("unchecked")
    List<Prize> listing = q.list();
    
    if (listing.size() == 1)
    {
      result = listing.get(0);
    }
    
    this.endTransaction();
    //session.close();
    
    return result;
  }

  @Override
  public void setPrizeWinner(int prizeId, int donorId)
  {
    Session session = this.beginTransaction();
    
    
    Prize p = (Prize) session.load(Prize.class, prizeId);
    Donor d = (Donor) session.load(Donor.class, donorId);
    
    p.setWinner(d);
    
    session.save(p);
    
    this.endTransaction();
    //session.close();
  }

  @Override
  public void removePrizeWinner(int prizeId)
  {
    Session session = this.beginTransaction();
    
    
    Prize p = (Prize) session.load(Prize.class, prizeId);

    p.setWinner(null);
    
    this.endTransaction();
    //session.close();
  }

  @Override
  public void deletePrize(int prizeId)
  {
    Session session = this.beginTransaction();
    
    
    Prize p = (Prize) session.load(Prize.class, prizeId);

    session.delete(p);
    
    this.endTransaction();
    //session.close();
  }

}
