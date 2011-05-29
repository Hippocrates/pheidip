package test.util;

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
}
