package test.db;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllDBTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite(AllDBTests.class.getName());
    //$JUnit-BEGIN$
    suite.addTestSuite(TestSpeedRunData.class);
    suite.addTestSuite(TestBidData.class);
    suite.addTestSuite(TestDonationDataAccess.class);
    suite.addTestSuite(TestJDBCManager.class);
    suite.addTestSuite(TestDonationData.class);
    suite.addTestSuite(TestDonorData.class);
    suite.addTestSuite(TestDonationDataErrorParser.class);
    //$JUnit-END$
    return suite;
  }

}
