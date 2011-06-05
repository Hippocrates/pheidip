package pheidip.util;

import java.security.SecureRandom;

public final class IdUtils
{
  private static final SecureRandom rand = new SecureRandom();
  
  public static int generateId()
  {
    return rand.nextInt();
  }
}
