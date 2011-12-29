package pheidip.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import pheidip.db.PrizeData;
import pheidip.objects.Prize;
import pheidip.objects.PrizeSearchParams;
import pheidip.util.StringUtils;

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
  }

  @Override
  public void updatePrize(Prize toUpdate)
  {
    Session session = this.beginTransaction();
    
    session.merge(toUpdate);
    
    this.endTransaction();
  }

  @Override
  public Prize getPrizeById(int prizeId)
  {
    Session session = this.beginTransaction();

    Prize p = (Prize) session.get(Prize.class, prizeId);
    
    this.endTransaction();
    
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
    
    return result;
  }

  @Override
  public void deletePrize(Prize prize)
  {
    Session session = this.beginTransaction();
    
    session.delete(prize);
    
    this.endTransaction();
  }

  @Override
  public List<Prize> searchPrizes(PrizeSearchParams params)
  {
    return this.searchPrizesRange(params, 0, Integer.MAX_VALUE);
  }
  
  @Override
  public List<Prize> searchPrizesRange(PrizeSearchParams params, int offset, int size)
  {
    String queryString = "from Prize p";
    List<String> whereClause = new ArrayList<String>();
    
    if (params.name != null)
      whereClause.add("lower(p.name) like :name");
    
    if (params.excludeIfWon)
      whereClause.add("p.winner is null");
    
    if (whereClause.size() > 0)
    {
      queryString += " where " + StringUtils.joinSeperated(whereClause, " AND ");
    }
    
    StatelessSession dedicatedSession = this.beginBulkTransaction();
    
    Query q = dedicatedSession.createQuery(queryString + " order by p.name");

    if (params.name != null)
      q.setString("name", StringUtils.sqlInnerStringMatch(params.name));

    q.setFirstResult(offset);
    q.setMaxResults(size);
    
    @SuppressWarnings("unchecked")
    List<Prize> listing = q.list();
    
    this.endBulkTransaction(dedicatedSession);
    
    return listing;
  }

}
