package test.hdb;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllHibernateTests
{

  public static Test suite()
  {
    TestSuite suite = new TestSuite(AllHibernateTests.class.getName());
    
    //$JUnit-BEGIN$
    suite.addTestSuite(TestCreateSession.class);
    //$JUnit-END$
    return suite;
  }

}
