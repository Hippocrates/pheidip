package pheidip.db.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import pheidip.db.DonorData;
import pheidip.objects.Donor;
import pheidip.objects.Prize;
import pheidip.objects.SearchParameters;

public class HibernateDonorData extends HibernateDataInterface implements DonorData 
{
	public HibernateDonorData(HibernateDonationDataAccess manager) 
	{
		super(manager);
	}

	@Override
	public Donor getDonorById(int donorId) 
	{
	  Session session = this.beginTransaction();
	  
	  Donor d = (Donor) session.get(Donor.class, donorId);

	  this.endTransaction();
	  
	  return d;
	}

	@Override
	public List<Donor> getAllDonors() 
	{
	  StatelessSession dedicatedSession = this.beginBulkTransaction();
	  
    Query q = dedicatedSession.createQuery("from Donor order by id");

    @SuppressWarnings("unchecked")
    List<Donor> listing = q.list();
    
    this.endBulkTransaction(dedicatedSession);
    
    return listing;
	}

	@Override
	public List<Donor> getDonorsWithoutPrizes() 
	{
	  Session session = this.beginTransaction();
    Query q = session.createQuery("from Donor d where not exists elements(d.prizes) order by id");

    @SuppressWarnings("unchecked")
    List<Donor> listing = q.list();
    
    this.endTransaction();
    //session.close();
    
    return listing;
	}

	@Override
	public void deleteDonor(Donor d) 
	{
	  Session session = this.beginTransaction();
	  
	  session.merge(d);
	  // this is neccessary since the prizes aren't cascaded on deletion
    for (Prize p : d.getPrizes())
    {
      p.setWinner(null);
    }

    d.getPrizes().clear();

    session.delete(d);
    this.endTransaction();
	}

	@Override
	public void createDonor(Donor newDonor) 
	{
	  Session session = this.beginTransaction();
    session.save(newDonor);
      
    this.endTransaction();
	}

	@Override
	public void updateDonor(Donor donor) 
	{
	  Session session = this.beginTransaction();
    session.merge(donor);
    this.endTransaction();
	}

	@Override
	public void deleteAllDonors() 
	{
	  Session session = this.beginTransaction();
      
	  Query q = session.createQuery("delete Donor");
    q.executeUpdate();
      
    this.endTransaction();
	}
	

  @Override
  public List<Donor> searchDonors(SearchParameters<Donor> params)
  {
    return this.searchDonorsRange(params, 0, Integer.MAX_VALUE);
  }
	
	@Override
  public List<Donor> searchDonorsRange(SearchParameters<Donor> params, int offset, int size)
  {
    String queryString = SQLMethods.makeHQLSearchQueryString(params, "Donor");
    
    StatelessSession dedicatedSession = this.beginBulkTransaction();

    Query q = dedicatedSession.createQuery(queryString);

    SQLMethods.applyParametersToQuery(q, params);

    q.setFirstResult(offset);
    q.setMaxResults(size + 1);
    
    @SuppressWarnings("unchecked")
    List<Donor> listing = q.list();
    
    this.endBulkTransaction(dedicatedSession);

    return listing;
  }

  @Override
  public void insertMultipleDonors(List<Donor> toInsert)
  {
    StatelessSession dedicatedSession = this.beginBulkTransaction();

    for (Donor donor : toInsert)
    {
      try
      {
        dedicatedSession.insert(donor);
      }
      catch(Exception e)
      {
        throw new RuntimeException(e);
      }
    }
    
    this.endBulkTransaction(dedicatedSession);
  }
}
