package pheidip.logic;

import pheidip.db.DBType;
import pheidip.util.StringUtils;

public enum ConnectionType
{
  MYSQL_SERVER(DBType.MYSQL),
  HSQLDB_MEMORY(DBType.HSQLDB);
  
  private DBType dbType;
  
  public DBType getDBType()
  {
    return dbType;
  }
  
  public String toString()
  {
    return StringUtils.symbolToNatural(super.toString());
  }

  ConnectionType(DBType dbType)
  {
    this.dbType = dbType;
  }
  
  private static ConnectionType[] _list = ConnectionType.values();
  
  public static int size()
  {
    return _list.length;
  }
  
  public static ConnectionType get(int i)
  {
    return _list[i];
  }
}
