package pheidip.db.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.exception.ConstraintViolationException;

import pheidip.db.DonationDataConstraint;
import pheidip.db.DonationDataConstraintException;
import pheidip.db.DonationDataErrorParser;

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
	
	public StatelessSession beginBulkTransaction()
	{
	  StatelessSession dedicatedSession = this.getSessionFactory().openStatelessSession();
    dedicatedSession.beginTransaction();
    return dedicatedSession;
	}
	
	public void endBulkTransaction(StatelessSession dedicatedSession)
	{
	  try
    {
  	  dedicatedSession.getTransaction().commit();
      dedicatedSession.close();
    }
	  catch(Exception e)
    {
	    dedicatedSession.getTransaction().rollback();
	    dedicatedSession.close();
      throw new RuntimeException(e);
    }
	}

	public Session beginTransaction()
	{

	  try
          {
	    if (this.manager.getSession().isOpen() && this.manager.getSession().getTransaction().isActive())
  	    {
  	      this.manager.getSession().getTransaction().rollback();
  	    }
  	  
  	    this.manager.getSession().beginTransaction();
  	   
  	    return this.manager.getSession();
	  }
	  catch(Exception e)
	  {
	    throw new RuntimeException(e);
	  }
	  
          this.manager.resetSession();
	  
	  this.manager.getSession().beginTransaction();
	  
	  return this.manager.getSession();
	}
	
	public void endTransaction()
	{
	  try
    {
	    this.manager.getSession().getTransaction().commit();
    }
	  catch(ConstraintViolationException e)
	  {
	    this.manager.getSession().getTransaction().rollback();
      this.getManager().resetSession();
      
      String errorMessage = e.getLocalizedMessage();
      
      DonationDataConstraint knownConstraint = DonationDataErrorParser.parseError(errorMessage);
      
      if (knownConstraint != null)
      {
        throw new DonationDataConstraintException(knownConstraint);
      }
      else
      {
        throw new RuntimeException(e.getLocalizedMessage());
      }
	  }
	  catch(Exception e)
    {
	    this.manager.getSession().getTransaction().rollback();
      this.getManager().resetSession();
      throw new RuntimeException(e);
    }
	}
}
