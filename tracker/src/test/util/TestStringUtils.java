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
}
