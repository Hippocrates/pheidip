package test;

import test.db.AllDBTests;
import test.logic.AlLogicTests;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite(AllTests.class.getName());
    //$JUnit-BEGIN$
    
    suite.addTest(AllDBTests.suite());
    suite.addTest(AlLogicTests.suite());

    //$JUnit-END$
    return suite;
  }

}
