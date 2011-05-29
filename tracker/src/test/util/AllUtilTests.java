package test.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllUtilTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite(AllUtilTests.class.getName());
    //$JUnit-BEGIN$
    suite.addTestSuite(TestStringUtils.class);
    suite.addTestSuite(TestFilter.class);
    suite.addTestSuite(TestInnerStringMatchFilter.class);
    //$JUnit-END$
    return suite;
  }

}
