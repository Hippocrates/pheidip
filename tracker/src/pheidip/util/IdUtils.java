package pheidip.util;

import java.util.Date;

public final class IdUtils
{
  public static int generateId()
  {
    Date d = new Date();
    
    return (int) d.getTime();
  }
}
