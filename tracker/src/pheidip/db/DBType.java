package pheidip.db;

public enum DBType
{
  MYSQL,
  HSQLDB;
  
  private static final DBType[] _list = DBType.values();
  
  public String getJDBCName()
  {
    return this.toString().toLowerCase();
  }
  
  public static int size()
  {
    return _list.length;
  }
  
  public static DBType get(int i)
  {
    return _list[i];
  }
}
