package pheidip.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import pheidip.db.DonorData;
import pheidip.objects.Donor;
import pheidip.objects.DonorSearchParams;
import pheidip.objects.Prize;
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
	  Session session = this.getSessionFactory().openSession();
	  session.beginTransaction();
	  
	  Donor d = (Donor) session.get(Donor.class, donorId);

	  session.getTransaction().commit();
	  session.close();
	  
	  return d;
	}

	@Override
	public Donor getDonorByEmail(String email) 
	{
	  Donor result = null;
	  
	  Session session = this.getSessionFactory().openSession();
	  session.beginTransaction();
	  
	  Query q = session.createQuery("from Donor d where d.email = :email");

	  q.setString("email", email);
	  
	  @SuppressWarnings("unchecked")
    List<Donor> listing = q.list();
	  
	  if (listing.size() == 1)
	  {
	    result = listing.get(0);
	  }
	  
	  session.getTransaction().commit();
	  session.close();
		  
	  return result;
	}

	@Override
	public Donor getDonorByAlias(String alias) 
	{
    Donor result = null;
    
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Query q = session.createQuery("from Donor d where alias = :alias");

    q.setString("alias", alias);
    
    @SuppressWarnings("unchecked")
    List<Donor> listing = q.list();
    
    if (listing.size() == 1)
    {
      result = listing.get(0);
    }
    
    session.getTransaction().commit();
    session.close();
      
    return result;
	}

	@Override
	public List<Donor> getAllDonors() 
	{
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Query q = session.createQuery("from Donor order by id");

    @SuppressWarnings("unchecked")
    List<Donor> listing = q.list();
    
    session.getTransaction().commit();
    session.close();
      
    return listing;
	}

	@Override
	public List<Donor> getDonorsWithoutPrizes() 
	{
	  Session session = this.getSessionFactory().openSession();
	  session.beginTransaction();
	  
    Query q = session.createQuery("from Donor d where exists elements(d.prizes) order by id");

    @SuppressWarnings("unchecked")
    List<Donor> listing = q.list();
    
    session.getTransaction().commit();
    session.close();
    
    return listing;
	}

	@Override
	public void deleteDonor(int id) 
	{
	  Donor d = this.getDonorById(id);
	  
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    session.delete(d);
    
    session.getTransaction().commit();
    session.close();
	}

	@Override
	public void createDonor(Donor newDonor) 
	{
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    session.save(newDonor);
    
    session.getTransaction().commit();
    session.close();
	}

	@Override
	public void updateDonor(Donor donor) 
	{
	  Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    session.merge(donor);
    
    session.getTransaction().commit();
    session.close();
	}

	@Override
	public Donor getPrizeWinner(int prizeId) 
	{
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Prize p = (Prize) session.load(Prize.class, prizeId);
    Donor d = p.getWinner();
    
    session.getTransaction().commit();
    session.close();
    
    return d;
	}

	@Override
	public void deleteAllDonors() 
	{
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Query q = session.createQuery("delete Donor");
    q.executeUpdate();
    
    session.getTransaction().commit();
    session.close();
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
    
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Query q = session.createQuery(queryString + " order by d.alias, d.email, d.firstName, d.lastName");

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
    
    session.getTransaction().commit();
    session.close();
    
    return listing;
  }
}
