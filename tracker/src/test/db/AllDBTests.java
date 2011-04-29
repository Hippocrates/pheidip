package test.db;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllDBTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite(AllDBTests.class.getName());
    //$JUnit-BEGIN$
    suite.addTestSuite(TestConnectionManager.class);
    suite.addTestSuite(TestJDBCManager.class);
    suite.addTestSuite(TestDonationData.class);
    suite.addTestSuite(TestDonorData.class);
    //$JUnit-END$
    return suite;
  }

}
