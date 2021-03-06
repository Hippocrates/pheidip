package pheidip.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class StringUtils
{
  public static String[] heuristicSplit(String toSplit)
  {
    final String splitRegex = "(([;,])|(\\s+and\\s+))+";
    String[] results = toSplit.split(splitRegex);
    
    for (int i = 0; i < results.length; ++i)
    {
      results[i] = results[i].trim();
    }
      
    return results;
  }
  
  public static boolean innerStringMatch(String whole, String part)
  {
    if (whole != null)
    {
      return whole.toUpperCase().contains(part.toUpperCase());
    }
    else
    {
      return false;
    }
  }
  
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
  
  public static String naturalToSymbol(String natural)
  {
    return natural.trim().toUpperCase().replace(' ', '_');
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
    if (loRange < 0 || hiRange <= 0 || loRange > hiRange)
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
  
  public static String getFileBase(String filename)
  {
    int dotlocation = filename.lastIndexOf('.');
    
    if (dotlocation == -1)
    {
      return filename;
    }
    else
    {
      return filename.substring(0, dotlocation);
    }
  }
  
  public static String getFileExtension(String filename)
  {
    int dotlocation = filename.lastIndexOf('.');
    
    if (dotlocation == -1)
    {
      return "";
    }
    else
    {
      return filename.substring(dotlocation + 1);
    }
  }
  
  public static String canonicalize(String s)
  {
    return nullIfEmpty(s) == null ? null : s.toLowerCase();
  }
  
  public static String sqlInnerStringMatch(String s)
  {
    return "%" + emptyIfNull(s).toLowerCase() + "%";
  }
  
  public static String joinLanguageSeperated(List<String> strings)
  {
    List<String> subList = new ArrayList<String>(strings);
    
    if (subList.size() == 0)
    {
      return "";
    }
    else if (subList.size() == 1)
    {
      return subList.get(0);
    }
    else
    {
      String last = subList.remove(subList.size() - 1);
      
      String first = joinSeperated(subList, ", ");
      
      return first + ", and " + last;
    }
  }
  
  public static String joinSeperated(List<String> strings, String seperator)
  {
    StringBuilder acculm = new StringBuilder();
    
    boolean started = false;
    
    for (String s : strings)
    {
      if (started)
      {
        acculm.append(seperator);
      }
      else
      {
        started = true;
      }
      
      acculm.append(s);
    }
    
    return acculm.toString();
  }

  public static String javaToNatural(String java) 
  {
    StringBuilder output = new StringBuilder();
    char[] array = java.toCharArray();
    
    output.append(Character.toUpperCase(array[0]));
    
    for (int i = 1; i < array.length; ++i)
    {
      if (Character.isUpperCase(array[i]))
      {
        output.append(' ');
      }
      
      output.append(array[i]);
    }
    
    return output.toString();
  }

  public static String joinSeperated(String[] orderingProperties, String seperator)
  {
    return joinSeperated(Arrays.asList(orderingProperties), seperator);
  }
}
