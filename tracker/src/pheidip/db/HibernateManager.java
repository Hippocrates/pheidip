package pheidip.db;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateManager
{
  @SuppressWarnings("deprecation")
  public static SessionFactory createMemorySessionFactory()
  {
    Configuration cfg = new Configuration().configure();
    
    cfg.setProperty("hibernate.connection.url", "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1;MVCC=TRUE");
    cfg.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
    cfg.setProperty("hibernate.connection.username", "sa");
    cfg.setProperty("hibernate.connection.password", "");
    cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    cfg.setProperty("hibernate.hbm2ddl.auto", "create");

    return cfg.buildSessionFactory();
  }
  
  @SuppressWarnings("deprecation")
  public static SessionFactory createServerSessionFactory(String server, String dbName, String userName, String password)
  {
    Configuration cfg = new Configuration().configure();
    
    String location = "//" + server + "/" + dbName;
    
    String url = "jdbc:mysql:" + location;
    
    cfg.setProperty("hibernate.connection.url", url);
    cfg.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
    cfg.setProperty("hibernate.connection.username", userName);
    cfg.setProperty("hibernate.connection.password", password);
    cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

    return cfg.buildSessionFactory();
  }
  
  // other methods to create file or memory or whatever else here too....
}
