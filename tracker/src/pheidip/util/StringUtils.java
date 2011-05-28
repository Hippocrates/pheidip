package pheidip.util;

public class StringUtils
{
  public static String symbolToNatural(String symbol)
  {
    char[] array = symbol.toCharArray();
    
    boolean prevSpace = true;
    
    for (int i = 0; i < array.length; ++i)
    {
      if (prevSpace)
      {
        array[i] = Character.toUpperCase(array[i]);
        prevSpace = false;
      }
      else
      {
        array[i] = Character.toLowerCase(array[i]);
      }
      
      if (array[i] == '_')
      {
        array[i] = ' ';
        prevSpace = true;
      }
    }
    
    return new String(array);
  }

  public static boolean isEmptyOrNull(String test)
  {
    return (test == null) ? true : test.length() == 0;
  }

  public static String emptyIfNull(String test)
  {
    return (test == null) ? "" : test;
  }
}
