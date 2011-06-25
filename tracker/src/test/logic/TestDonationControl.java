package test.logic;

import java.math.BigDecimal;

import pheidip.logic.DonationControl;
import pheidip.logic.DonationDatabaseManager;
import pheidip.objects.Donation;
import pheidip.objects.DonationDomain;
import pheidip.objects.Donor;
import test.db.DBTestConfiguration;
import junit.framework.TestCase;

public class TestDonationControl extends TestCase
{
  DonationDatabaseManager manager;
  
  public void setUp()
  {
    this.manager = new DonationDatabaseManager();
    this.manager.createMemoryDatabase();
    this.manager.runSQLScript(DBTestConfiguration.getTestDataDirectory() + "donation_bid_test_data_1.sql");
  }
  
  public void tearDown()
  {
    this.manager.closeConnection();
  }
  
  public void testCreateDonationControl()
  {
    final int donationId = 1;
    
    DonationControl control = new DonationControl(this.manager, donationId);
    
    Donation d = control.getData();
    
    assertEquals(donationId, d.getId());
    assertEquals(1, d.getDonorId());
    assertEquals(DonationDomain.LOCAL, d.getDomain());
  }
  
  public void testGetDonationDonor()
  {
    final int donationId = 1;
    
    DonationControl control = new DonationControl(this.manager, donationId);
    
    Donor d = control.getDonationDonor();

    assertEquals(1, d.getId());
    assertEquals("smk", d.getAlias());
  }
  
  public void testUpdateDonation()
  {
    final int donationId = 1;
    
    DonationControl control = new DonationControl(this.manager, donationId);
    
    String commentText = "Some new comment text";
    BigDecimal amount = new BigDecimal("3.50");
    
    control.updateData(amount, commentText, false);
    
    Donation newData = control.getData();
    
    assertEquals(commentText, newData.getComment());
  }
  
  public void testDeleteDonation()
  {
    final int donationId = 7;
    
    DonationControl control = new DonationControl(this.manager, donationId);
    
    control.deleteDonation();
    
    assertNull(control.getData());
  }
}
