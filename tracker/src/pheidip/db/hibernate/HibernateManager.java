package pheidip.db.hibernate;

import java.io.File;

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
	  case H2:
		return "org.hibernate.dialect.H2Dialect";
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

    return cfg.buildSessionFactory();
  }
  
  // other methods to create file or memory or whatever else here too....
}
