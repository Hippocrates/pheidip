package pheidip.db.hibernate;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import pheidip.db.DBType;
import pheidip.db.JDBCManager;
import pheidip.util.StringUtils;

public class HibernateManager
{

  private static String getDialect(DBType type)
  {
  	switch (type)
  	{
  	  case MYSQL:
  		return "org.hibernate.dialect.MySQLDialect";
  	  case HSQLDB:
  	  return "org.hibernate.dialect.HSQLDialect";
  	  default:
  		return null;
  	}
  }
	
  @SuppressWarnings("deprecation")
  public static SessionFactory createMemorySessionFactory()
  {
    Configuration cfg = new Configuration().configure();
    
    cfg.setProperty("hibernate.connection.url", "jdbc:" + DBType.HSQLDB.getJDBCName() + ":mem:.");
    cfg.setProperty("hibernate.connection.driver_class", JDBCManager.getDriverName(DBType.HSQLDB));
    cfg.setProperty("hibernate.connection.username", "sa");
    cfg.setProperty("hibernate.connection.password", "");
    cfg.setProperty("hibernate.dialect", getDialect(DBType.HSQLDB));
    cfg.setProperty("hibernate.hbm2ddl.auto", "create");

    return cfg.buildSessionFactory();
  }
  
  @SuppressWarnings("deprecation")
  public static SessionFactory createFileDatabase(File fileLocation)
  {
    String fileBase = StringUtils.getFileBase(fileLocation.getAbsolutePath());
    
    String url = "jdbc:" + DBType.HSQLDB.getJDBCName() + ":file:" + fileBase;
    
    Configuration cfg = new Configuration().configure();
    
    cfg.setProperty("hibernate.connection.url", url);
    cfg.setProperty("hibernate.connection.driver_class", JDBCManager.getDriverName(DBType.HSQLDB));
    cfg.setProperty("hibernate.connection.username", "sa");
    cfg.setProperty("hibernate.connection.password", "");
    cfg.setProperty("hibernate.dialect", getDialect(DBType.HSQLDB));
    
    if (!fileLocation.exists())
    {
      cfg.setProperty("hibernate.hbm2ddl.auto", "create");
    }

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

    cfg.setProperty("hibernate.connection.provider_class", "org.hibernate.connection.C3P0ConnectionProvider");
    cfg.setProperty("c3p0.acquire_increment", "1");
    cfg.setProperty("c3p0.idle_test_period", "50");
    cfg.setProperty("c3p0.max_size", "200");
    cfg.setProperty("c3p0.max_statements", "10");
    cfg.setProperty("c3p0.min_size", "10");
    cfg.setProperty("c3p0.timeout", "100");

    SessionFactory result = cfg.buildSessionFactory();
    Session session = null;
    
    try
    {
      session = result.openSession();
      // send a do-nothing query to confirm the database connection
      session.createSQLQuery("select 0;").list();
      session.close();
    }
    catch(Exception e)
    {
      session.close();
      result.close();
      throw new RuntimeException(e.getMessage());
    }
    
    return result;
  }
  
  // other methods to create file or memory or whatever else here too....
}
