package pheidip.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import pheidip.util.StringUtils;

public class JDBCManager
{
  private static boolean[] driversLoaded = new boolean[DBType.size()];
  private static List<Map<Integer,SQLError> > errorTables = generateErrorTables();
  
  public static Connection createMemoryDatabase() throws SQLException
  {
    JDBCManager.loadDrivers(DBType.HSQLDB);
    String url = "jdbc:" + DBType.HSQLDB.getJDBCName() + ":mem:.";
    return DriverManager.getConnection(url, "", "");
  }
  
  public static Connection createFileDatabase(File fileLocation) throws SQLException
  {
    String fileBase = StringUtils.getFileBase(fileLocation.getAbsolutePath());
    JDBCManager.loadDrivers(DBType.HSQLDB);
    String url = "jdbc:" + DBType.HSQLDB.getJDBCName() + ":file:" + fileBase;
    return DriverManager.getConnection(url, "", "");
  }
  
  public static void shutdownHSQLConnection(Connection c) throws SQLException
  {
    c.createStatement().execute("SHUTDOWN COMPACT");
  }
  
  public static Connection connectToServer(DBType type, String server, String dbName, String userName, String password) throws SQLException
  {
    JDBCManager.loadDrivers(type);
    String url = createDriverServerURL(type, server, dbName);
    return DriverManager.getConnection(url, userName, password);
  }
  
  public static String getDriverName(DBType type)
  {
	switch (type)
    {
      case MYSQL:
        return "com.mysql.jdbc.Driver";
      case HSQLDB:
        return "org.hsqldb.jdbcDriver";
      default:
    	return null;
    }
  }
  
  public static String createDriverServerURL(DBType type, String server, String dbName)
  {
	String location = null;
	
	switch(type)
	{
	  case HSQLDB:
		location = "hsql://" + server + "/" + dbName;
		break;
	  case MYSQL:
	    location = "//" + server + "/" + dbName;
	  break;
	  default:
	    throw new RuntimeException("Error, unsupported database type : " + type.toString());
	 }

	 return "jdbc:" + type.getJDBCName() + ":" + location;
  }
  
  public static boolean loadDrivers(DBType type)
  {
    if (!driversLoaded[type.ordinal()])
    {
      try
      {
    	Class.forName(getDriverName(type)).newInstance();
    	driversLoaded[type.ordinal()] = true;
      }
      catch (ClassNotFoundException e)
      {
        throw new RuntimeException(e);
      }
      catch (InstantiationException e)
      {
        throw new RuntimeException(e);
      } 
      catch (IllegalAccessException e)
      {
    	  throw new RuntimeException(e);
      }
    }
    
    return driversLoaded[type.ordinal()];
  }
  
  private static List<Map<Integer,SQLError> > generateErrorTables()
  {
    List<Map<Integer,SQLError> > tables = new ArrayList<Map<Integer,SQLError> >();

    for (int i = 0; i < DBType.size(); ++i)
    {
      tables.add(null);
    }
    
    tables.set(DBType.HSQLDB.ordinal(), generateHSQLDBErrorTables());
    tables.set(DBType.MYSQL.ordinal(), generateMYSQLErrorTables());

    return tables;
  }
  
  private static Map<Integer,SQLError> generateHSQLDBErrorTables()
  {
    Map<Integer,SQLError> errorMap = new TreeMap<Integer,SQLError>();
    
    // here, just put in whatever mappings that can be found ...
    errorMap.put(-5501, SQLError.TABLE_NOT_FOUND);
    errorMap.put(-5504, SQLError.OBJECT_ALREADY_EXISTS);
    errorMap.put(-157, SQLError.INVALID_VALUE);
    errorMap.put(-104, SQLError.UNIQUE_VIOLATION);
    errorMap.put(-8, SQLError.FOREIGN_KEY_VIOLATION);
    errorMap.put(-424, SQLError.PREPARED_STATEMENT_PARAMETER_NOT_SET);
    
    return errorMap;
  }
  
  private static Map<Integer,SQLError> generateMYSQLErrorTables()
  {
    Map<Integer,SQLError> errorMap = new TreeMap<Integer,SQLError>();
    
    errorMap.put(1062, SQLError.UNIQUE_VIOLATION);
    errorMap.put(-8, SQLError.FOREIGN_KEY_VIOLATION);
    
    return errorMap;
  }

  public static SQLError getErrorForCode(DBType type, int errorCode)
  {
    Map<Integer,SQLError> table = errorTables.get(type.ordinal());
    SQLError result = null;
    
    if (table != null)
    {
      result = table.get(errorCode);
    }
    
    if (result == null)
    {
      result = SQLError.UNKNOWN;
    }
    
    return result;
  }

  public static int getCodeForError(DBType type, SQLError error)
  {
    Map<Integer,SQLError> table = errorTables.get(type.ordinal());
    
    for (Map.Entry<Integer, SQLError> entry : table.entrySet())
    {
      if (entry.getValue() == error)
      {
        return entry.getKey();
      }
    }
    
    return 0;
  }
}
