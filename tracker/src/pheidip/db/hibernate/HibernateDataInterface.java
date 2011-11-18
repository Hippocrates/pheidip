package pheidip.db.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

abstract public class HibernateDataInterface 
{
	private HibernateDonationDataAccess manager;

	public HibernateDataInterface(HibernateDonationDataAccess manager)
	{
	  this.manager = manager;
	}

	public HibernateDonationDataAccess getManager()
	{
	  return this.manager;
	}
	
	public SessionFactory getSessionFactory()
	{
		return this.manager.getSessionFactory();
	}
	
	public Session getSession()
	{
	  return this.manager.getSession();
	}
	
	public Session beginTransaction()
	{
	  this.manager.getSession().beginTransaction();
	  return this.manager.getSession();
	}
	
	public void endTransaction()
	{
	  try
    {
	    this.manager.getSession().getTransaction().commit();
    }
	  catch(Exception e)
    {
	    this.manager.getSession().getTransaction().rollback();
      this.getManager().resetSession();
      throw new RuntimeException(e);
    }
	}
}
