package pheidip.db.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import pheidip.db.DBType;
import pheidip.db.JDBCManager;

public class HibernateManager
{
  //public static final int JDBC_BATCH_SIZE = 20;
  
  private static String getDialect(DBType type)
  {
	switch (type)
	{
	  case H2:
		return "org.hibernate.dialect.H2Dialect";
	  case MYSQL:
		return "org.hibernate.dialect.MySQLDialect";
	  default:
		return null;
	}
  }
	
  @SuppressWarnings("deprecation")
  public static SessionFactory createMemorySessionFactory()
  {
    Configuration cfg = new Configuration().configure();
    
    cfg.setProperty("hibernate.connection.url", "jdbc:h2:mem:;DB_CLOSE_DELAY=-1;MVCC=TRUE");
    cfg.setProperty("hibernate.connection.driver_class", JDBCManager.getDriverName(DBType.H2));
    cfg.setProperty("hibernate.connection.username", "sa");
    cfg.setProperty("hibernate.connection.password", "");
    cfg.setProperty("hibernate.dialect", getDialect(DBType.H2));
    cfg.setProperty("hibernate.hbm2ddl.auto", "create");
    //cfg.setProperty("hibernate.jdbc.batch_size", "" + JDBC_BATCH_SIZE);

    return cfg.buildSessionFactory();
  }
  
  @SuppressWarnings("deprecation")
  public static SessionFactory createServerSessionFactory(DBType type, String server, String dbName, String userName, String password)
  {
    Configuration cfg = new Configuration().configure();

    String url = JDBCManager.createDriverServerURL(type, server, dbName);
    
    cfg.setProperty("hibernate.connection.url", url);
    cfg.setProperty("hibernate.connection.driver_class", JDBCManager.getDriverName(type));
    cfg.setProperty("hibernate.connection.username", userName);
    cfg.setProperty("hibernate.connection.password", password);
    cfg.setProperty("hibernate.dialect", getDialect(type));
    //cfg.setProperty("hibernate.jdbc.batch_size", "" + JDBC_BATCH_SIZE);

    return cfg.buildSessionFactory();
  }
  
  // other methods to create file or memory or whatever else here too....
}
