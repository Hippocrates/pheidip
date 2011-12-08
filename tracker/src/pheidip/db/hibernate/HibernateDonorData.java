package pheidip.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import pheidip.db.DonorData;
import pheidip.objects.Donor;
import pheidip.objects.DonorSearchParams;
import pheidip.util.StringUtils;

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
	public Donor getDonorByEmail(String email) 
	{
	  Donor result = null;
	  
	  Session session = this.beginTransaction();

	  Query q = session.createQuery("from Donor d where d.email = :email");

	  q.setString("email", email);
	  
	  @SuppressWarnings("unchecked")
    List<Donor> listing = q.list();
	  
	  if (listing.size() == 1)
	  {
	    result = listing.get(0);
	  }
	  
	  this.endTransaction();
		  
	  return result;
	}

	@Override
	public Donor getDonorByAlias(String alias) 
	{
    Donor result = null;
    
    Session session = this.beginTransaction();

    Query q = session.createQuery("from Donor d where alias = :alias");

    q.setString("alias", alias);
    
    @SuppressWarnings("unchecked")
    List<Donor> listing = q.list();
    
    if (listing.size() == 1)
    {
      result = listing.get(0);
    }
    
    this.endTransaction();

    return result;
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
  public List<Donor> searchDonors(DonorSearchParams params)
  {
    String queryString = "from Donor d";
    List<String> whereClause = new ArrayList<String>();
    
    if (params.firstName != null)
      whereClause.add("d.firstName like :firstName");
    
    if (params.lastName != null)
      whereClause.add("d.lastName like :lastName");
    
    if (params.email != null)
      whereClause.add("d.email like :email");
    
    if (params.alias != null)
      whereClause.add("d.alias like :alias");
    
    if (whereClause.size() > 0)
    {
      queryString += " where " + StringUtils.joinSeperated(whereClause, " AND ");
    }
    
    StatelessSession dedicatedSession = this.beginBulkTransaction();
    
    Query q = dedicatedSession.createQuery(queryString + " order by d.alias, d.email, d.firstName, d.lastName");

    if (params.firstName != null)
      q.setString("firstName", StringUtils.sqlInnerStringMatch(params.firstName));
    
    if (params.lastName != null)
      q.setString("lastName", StringUtils.sqlInnerStringMatch(params.lastName));
    
    if (params.email != null)
      q.setString("email", StringUtils.sqlInnerStringMatch(params.email));
    
    if (params.alias != null)
      q.setString("alias", StringUtils.sqlInnerStringMatch(params.alias));
    
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
