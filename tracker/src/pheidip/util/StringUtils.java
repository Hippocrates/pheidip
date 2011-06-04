package pheidip.util;

import java.util.Random;

public final class StringUtils
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

  public static String nullIfEmpty(String test)
  {
    return (test == null) ? null : ((test.length() == 0) ? null : test);
  }
  
  public static String randomStringOverAlphabet(int loRange, int hiRange, String alphabet, Random rand)
  {
    if (loRange <= 0 || hiRange <= 0 || loRange > hiRange)
    {
      return "";
    }
    
    char[] array = new char[rand.nextInt(hiRange - loRange + 1) + loRange];
    
    for (int i = 0; i < array.length; ++i)
    {
      array[i] = alphabet.charAt(rand.nextInt(alphabet.length()));
    }
    
    return new String(array);
  }
  
  public static String randomName(int loRange, int hiRange, Random rand)
  {
    return symbolToNatural(randomStringOverAlphabet(
        loRange, 
        hiRange,
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
        rand));
  }

  public static String randomEmail(int loNameRange, int hiNameRange,
      int loDomainRange, int hiDomainRange, String topLevelDomain, Random rand)
  {
    final String emailAlphabet = "abcdefghijklmnopqrstuvwxyz._";
    
    return randomStringOverAlphabet(loNameRange, hiNameRange, emailAlphabet, rand) + 
      "@" + randomStringOverAlphabet(loDomainRange, hiDomainRange, emailAlphabet, rand) + 
      "." + topLevelDomain;
  }

  public static String randomDollarAmountString(int loNumDigits, int hiNumDigits, Random rand)
  {
    final String digits = "0123456789";
    return randomStringOverAlphabet(loNumDigits, hiNumDigits, digits, rand) + "." + randomStringOverAlphabet(2,2,digits, rand);
  }
  
}
