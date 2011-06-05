package test.logic;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllLogicTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite(AllLogicTests.class.getName());
    //$JUnit-BEGIN$
    suite.addTestSuite(TestDonationControl.class);
    suite.addTestSuite(TestDonationDatabaseManager.class);
    suite.addTestSuite(TestDonorControl.class);
    suite.addTestSuite(TestDonorSearch.class);
    suite.addTestSuite(TestChipinDonations.class);
    suite.addTestSuite(TestChipinLoginManager.class);
    suite.addTestSuite(TestChipinMergeProcess.class);
    //$JUnit-END$
    return suite;
  }

}
