package test.util;

import java.math.BigDecimal;
import java.util.Random;

import pheidip.util.StringUtils;
import junit.framework.TestCase;

public class TestStringUtils extends TestCase
{
  public void testSymbolToNatural()
  {
    String symbol = "A_SYMBOL_STRING"; // i.e. all caps, underscore seperated
    
    String natural = StringUtils.symbolToNatural(symbol);
    
    assertEquals("A Symbol String", natural);
  }
  
  public void testIsEmptyOrNull()
  {
    assertTrue(StringUtils.isEmptyOrNull(""));
    assertTrue(StringUtils.isEmptyOrNull(null));
    assertFalse(StringUtils.isEmptyOrNull("not empty"));
  }
  
  public void testEmptyIfNull()
  {
    String notEmpty = "blah";
    
    assertEquals("", StringUtils.emptyIfNull(null));
    assertEquals(notEmpty, StringUtils.emptyIfNull(notEmpty));
    assertEquals("", StringUtils.emptyIfNull(""));
  }
  
  public void testNullIfEmpty()
  {
    String notEmpty = "blah";
    assertEquals(notEmpty, StringUtils.nullIfEmpty(notEmpty));
    assertEquals(null, StringUtils.nullIfEmpty(""));
    assertEquals(null, StringUtils.nullIfEmpty(null));
  }
  
  public void testCreateRandomName()
  {
    Random rand = new Random(15542);
    final int loRange = 10;
    final int hiRange = 20;
    String randomString = StringUtils.randomName(loRange, hiRange, rand);
    
    assertTrue(randomString.length() >= loRange);
    assertTrue(randomString.length() <= hiRange);
    
    for(int i = 0; i < randomString.length(); ++i)
    {
      assertTrue(Character.isLetter(randomString.charAt(i)));
    }
  }
  
  public void testCreateRandomEmail()
  {
    Random rand = new Random(994555);
    final int loNameRange = 10;
    final int hiNameRange = 20;
    final int loDomainRange = 10;
    final int hiDomainRange = 20;
    final String topLevelDomain = "com";
    
    String randomEmail = StringUtils.randomEmail(loNameRange, hiNameRange, loDomainRange, hiDomainRange, topLevelDomain, rand);
    
    assertTrue(randomEmail.length() >= loNameRange + loDomainRange + topLevelDomain.length() + 2);
    assertTrue(randomEmail.length() <= hiNameRange + hiDomainRange + topLevelDomain.length() + 2);
    
    assertTrue(randomEmail.indexOf('@') != -1);
    assertFalse(randomEmail.indexOf('@') == 0);
    assertFalse(randomEmail.indexOf('@') == randomEmail.length() - 1);
    assertTrue(randomEmail.indexOf('@', randomEmail.indexOf('@') + 1) == -1);
    
    assertTrue(randomEmail.lastIndexOf('.') != -1);
    assertFalse(randomEmail.indexOf('.') == randomEmail.length() - 1);
    assertTrue(randomEmail.lastIndexOf('.') > randomEmail.lastIndexOf('@'));
    
    for(int i = 0; i < randomEmail.lastIndexOf('.'); ++i)
    {
      if (i != randomEmail.indexOf('@'))
      {
        assertTrue(
          Character.isLetter(randomEmail.charAt(i)) ||
          Character.isDigit(randomEmail.charAt(i)) ||
          randomEmail.charAt(i) == '_' ||
          randomEmail.charAt(i) == '.');
      }
    }
    
    assertEquals(topLevelDomain, randomEmail.substring(randomEmail.lastIndexOf('.') + 1));
  }
  
  public void testCreateRandomDollarAmountString()
  {
    Random rand = new Random(994555);
    final int loNumDigits = 1;
    final int hiNumDigits = 5;
    
    String amountString = StringUtils.randomDollarAmountString(loNumDigits, hiNumDigits, rand);
  
    BigDecimal amount = new BigDecimal(amountString);
    
    assertTrue(amount.compareTo(BigDecimal.ZERO) > 0);
    assertTrue(amount.compareTo(BigDecimal.TEN.pow(hiNumDigits + 1)) < 0);
    assertTrue(amount.scale() == 2);
  }
}
