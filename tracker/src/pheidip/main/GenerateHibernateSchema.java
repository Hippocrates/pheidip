package pheidip.main;

import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.MySQLDialect;

public class GenerateHibernateSchema
{
  public static void main(String[] args)
  {
    Configuration cfg = new Configuration().configure();
    
    String[] lines = cfg.generateSchemaCreationScript(new MySQLDialect());

    for (int i = 0; i < lines.length; i++) 
    {
      System.out.println(lines[i] + ";");
    }
  }
}
