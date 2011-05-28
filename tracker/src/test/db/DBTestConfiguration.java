package test.db;

public class DBTestConfiguration
{
  public static boolean testMYSQLConnectToServer()
  {
    return true;
  }
  
  public static String mysqlServerName()
  {
    return "127.0.0.1";
  }
  
  public static String mysqlDatabaseName()
  {
    return "test";
  }
  
  public static String mysqlServerUser()
  {
    return "default";
  }
  
  public static String mysqlServerPassword()
  {
    return "default";
  }

  public static String getTestDataDirectory()
  {
    return "../database/test/data/";
  }
}
