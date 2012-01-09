package pheidip.db.hibernate;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;

import pheidip.db.BidData;
import pheidip.db.DBType;
import pheidip.db.DonationData;
import pheidip.db.DonationDataAccess;
import pheidip.db.DonorData;
import pheidip.db.PrizeData;
import pheidip.db.SpeedRunData;

public class HibernateDonationDataAccess implements DonationDataAccess 
{
	private SessionFactory sessionFactory;
	private DBType databaseConnectionType;
	
	private HibernateDonorData donors;
	private HibernateSpeedRunData speedRuns;
	private HibernateDonationData donations;
	private HibernateBidData bids;
	private HibernatePrizeData prizes;
  private Session session;
	
	public HibernateDonationDataAccess()
	{
	  this.sessionFactory = null;
	  this.session = null;
	  this.databaseConnectionType = null;
	  
	  this.donors = null;
	}
	
	@Override
	public DonorData getDonorData() 
	{
	  if (this.donors == null)
	  {
	    this.donors = new HibernateDonorData(this);
	  }
	
	  return this.donors;
	}

	@Override
	public DonationData getDonationData() 
	{
		if (this.donations == null)
		{
		  this.donations = new HibernateDonationData(this);
		}
		
		return this.donations;
	}

  @Override
	public BidData getBidData() 
	{
		if (this.bids == null)
		{
		  this.bids = new HibernateBidData(this);
		}
		
		return this.bids;
	}

	@Override
	public SpeedRunData getSpeedRunData() 
	{
		if (this.speedRuns == null)
		{
		  this.speedRuns = new HibernateSpeedRunData(this);
		}
		
		return this.speedRuns;
	}

	@Override
	public PrizeData getPrizeData() 
	{
		if (this.prizes == null)
		{
		  this.prizes = new HibernatePrizeData(this);
		}
		
		return this.prizes;
	}

	@Override
	public void connectToDatabaseServer(DBType type, String server,
			String dbname, String user, String password) 
	{
	  this.sessionFactory = HibernateManager.createServerSessionFactory(type, server, dbname, user, password);
	  this.session = this.sessionFactory.openSession();
	  this.databaseConnectionType = type;
	}

	@Override
	public void openFileDatabase(File location) 
	{
	  this.sessionFactory = HibernateManager.createFileDatabase(location);
    this.session = this.sessionFactory.openSession();
    this.databaseConnectionType = DBType.HSQLDB;
	}

	@Override
	public void createMemoryDatabase() 
	{
		this.sessionFactory = HibernateManager.createMemorySessionFactory();
		this.session = this.sessionFactory.openSession();
		this.databaseConnectionType = DBType.HSQLDB;
	}

	@Override
	public DBType getConnectionType() 
	{
		return this.databaseConnectionType;
	}

	@Override
	public boolean isConnected() 
	{
		return this.sessionFactory != null && !this.sessionFactory.isClosed();
	}

	@Override
	public void closeConnection() 
	{
	  if (this.databaseConnectionType == DBType.HSQLDB)
	  {
	    this.session.doWork(new Work()
	    {
        public void execute(Connection c) throws SQLException
        {
          c.createStatement().execute("SHUTDOWN COMPACT");
        }
	    });
	  }
	  
	  this.session.close();
		this.sessionFactory.close();
	}

	public SessionFactory getSessionFactory() 
	{
		return this.sessionFactory;
	}
	
	public Session getSession()
	{
	  return this.session;
	}
	
	public void resetSession()
	{
	  //this.session.flush();
      if (this.session.isOpen())
      {
    	  this.session.close();
      }
      
	  this.session = this.sessionFactory.openSession();
	}
}
