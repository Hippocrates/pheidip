package test.db;

import pheidip.db.DBType;
import pheidip.db.JDBCManager;
import pheidip.db.SQLError;
import junit.framework.TestCase;

public class TestJDBCManager extends TestCase
{
  public void testGetErrorCodes()
  {
    assertEquals(SQLError.TABLE_NOT_FOUND, JDBCManager.getErrorForCode(DBType.HSQLDB, -5501));
    assertEquals(SQLError.UNKNOWN, JDBCManager.getErrorForCode(DBType.HSQLDB, 42));
    assertEquals(SQLError.UNKNOWN, JDBCManager.getErrorForCode(DBType.MYSQL, 1234));
  }
}
