package test.util;

import pheidip.util.InnerStringMatchFilter;
import junit.framework.TestCase;

public class TestInnerStringMatchFilter extends TestCase
{
  public void testSimple()
  {
    InnerStringMatchFilter matcher = new InnerStringMatchFilter("bob");
    assertTrue(matcher.predicate("bob-omb"));
    assertTrue(matcher.predicate("bmo-bob-omb"));
    assertTrue(matcher.predicate("bmo-Bob-ZZZZZZOmb"));
    assertTrue(matcher.predicate("sHiShKaBoBs"));
  }
  
  public void testNullAlwaysNotEqual()
  {
    InnerStringMatchFilter matcher = new InnerStringMatchFilter("bob");
    assertFalse(matcher.predicate(null));
    InnerStringMatchFilter matcher2 = new InnerStringMatchFilter(null);
    assertFalse(matcher2.predicate(null));
  }
}
