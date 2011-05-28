package test.logic;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AlLogicTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite(AlLogicTests.class.getName());
    //$JUnit-BEGIN$
    suite.addTestSuite(TestDonationControl.class);
    suite.addTestSuite(TestDonationDatabaseManager.class);
    suite.addTestSuite(TestDonorControl.class);
    suite.addTestSuite(TestDonorSearch.class);
    //$JUnit-END$
    return suite;
  }

}
