package test.logic;

import pheidip.logic.DonationDatabaseManager;
import junit.framework.TestCase;

public class TestDonationDatabaseManager extends TestCase
{
  public void testCreate()
  {
    DonationDatabaseManager manager = new DonationDatabaseManager();
    
    assertFalse(manager.isConnected());
  }
}
