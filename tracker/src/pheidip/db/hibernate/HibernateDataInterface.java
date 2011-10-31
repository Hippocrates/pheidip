package pheidip.db.hibernate;

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
}
