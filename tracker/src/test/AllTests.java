package test;

import test.db.AllDBTests;
import test.logic.AllLogicTests;
import test.util.AllUtilTests;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite(AllTests.class.getName());
    //$JUnit-BEGIN$
    
    suite.addTest(AllDBTests.suite());
    suite.addTest(AllLogicTests.suite());
    suite.addTest(AllUtilTests.suite());

    //$JUnit-END$
    return suite;
  }

}
