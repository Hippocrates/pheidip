package pheidip.db.hibernate;

import java.io.File;

import org.hibernate.SessionFactory;

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
	
	public HibernateDonationDataAccess()
	{
	  this.sessionFactory = null;
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
		// TODO Auto-generated method stub
		throw new RuntimeException("Method not supported.");
	}

	@Override
	public BidData getBids() 
	{
		// TODO Auto-generated method stub
		throw new RuntimeException("Method not supported.");
	}

	@Override
	public SpeedRunData getSpeedRuns() 
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
		// TODO Auto-generated method stub
		throw new RuntimeException("Method not supported.");
	}

	@Override
	public void connectToDatabaseServer(DBType type, String server,
			String dbname, String user, String password) 
	{
	  this.sessionFactory = HibernateManager.createServerSessionFactory(type, server, dbname, user, password);
	  this.databaseConnectionType = type;
	}

	@Override
	public void openFileDatabase(File location) 
	{
		// TODO Auto-generated method stub
		throw new RuntimeException("Method not supported.");
	}

	@Override
	public void createMemoryDatabase() 
	{
		this.sessionFactory = HibernateManager.createMemorySessionFactory();
		this.databaseConnectionType = DBType.H2;
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
		this.sessionFactory.close();
	}

	public SessionFactory getSessionFactory() 
	{
		return this.sessionFactory;
	}

}
