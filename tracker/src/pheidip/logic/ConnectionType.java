package pheidip.logic;

import lombok.Getter;
import pheidip.db.DBType;
import pheidip.util.StringUtils;

public enum ConnectionType
{
  MYSQL_SERVER(DBType.MYSQL),
  HSQLDB_MEMORY(DBType.HSQLDB),
  HSQLDB_FILE(DBType.HSQLDB);
  
  ConnectionType(DBType type)
  {
    this.type = type;
  }
  
  public String toString()
  {
    return StringUtils.symbolToNatural(super.toString());
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
  
  @Getter
  private DBType type;
}
