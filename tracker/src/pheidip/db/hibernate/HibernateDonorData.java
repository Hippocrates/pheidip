package pheidip.db.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import pheidip.db.DonorData;
import pheidip.objects.Donor;

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
		// TODO Auto-generated method stub
		return null;
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
}
