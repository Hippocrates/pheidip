package test.logic;

import java.math.BigDecimal;
import java.util.List;

import pheidip.logic.DonationDatabaseManager;
import pheidip.logic.DonorControl;
import pheidip.objects.Donation;
import pheidip.objects.DonationDomain;
import pheidip.objects.Donor;
import test.db.DBTestConfiguration;
import junit.framework.TestCase;

public class TestDonorControl extends TestCase
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
  
  public void testCreateDonorWithDonorControl()
  {
    int newId = DonorControl.createNewDonor(this.manager);
    
    DonorControl control = new DonorControl(this.manager, newId);
    
    Donor result = control.getData();
    
    assertNotNull(result);
    assertNull(result.getAlias());
    assertNull(result.getEmail());
    assertNull(result.getFirstName());
    assertNull(result.getLastName());
    
    List<Donation> donations = control.getDonorDonations();
    
    assertEquals(0, donations.size());
  }
  
  public void testCreateDonorControl()
  {
    final int donorId = 1;
    DonorControl control = new DonorControl(this.manager, donorId);
    
    Donor result = control.getData();
    
    assertNotNull(result);
    assertEquals(donorId, result.getId());
    
    List<Donation> donations = control.getDonorDonations();
    
    assertEquals(1, donations.size());
    
    assertEquals(1, donations.get(0).getId());
  }
  
  public void testGetDonorDonationTotal()
  {
    final int donorId = 1;

    DonorControl control = new DonorControl(this.manager, donorId);
    
    assertEquals(control.getDonorDonations().get(0).getAmount(), control.getTotalDonated());
  }
  
  public void testUpdateDonorData()
  {
    final int donorId = 1;
    
    DonorControl control = new DonorControl(this.manager, donorId);
    
    Donor oldData = control.getData();
    
    final String newAlias = "asdkljasd";
    final String newEmail = "adkjasdhsdaj@kjsda.ad";
    
    assertFalse(newAlias.equalsIgnoreCase(oldData.getAlias()));
    assertFalse(newEmail.equalsIgnoreCase(oldData.getEmail()));
    
    control.updateData(newEmail, newAlias, oldData.getFirstName(), oldData.getLastName());
    
    Donor newData = control.getData();
    
    assertEquals(newData.getId(), oldData.getId());
    assertEquals(newData.getId(), oldData.getId());
    assertEquals(newData.getId(), oldData.getId());
  }
  
  public void testDeleteDonor()
  {
    final int donorId = 5;
    
    DonorControl control = new DonorControl(this.manager, donorId);
    
    control.deleteDonor();
    
    assertNull(control.getData());
  }
  
  public void testCreateNewDonation()
  {
    final int donorId = 5;
    
    DonorControl control = new DonorControl(this.manager, donorId);
    
    int donationId = control.createNewLocalDonation();
    
    List<Donation> donations = control.getDonorDonations();
    
    assertEquals(1, donations.size());
    
    assertEquals(donationId, donations.get(0).getId());
    assertEquals(donorId, donations.get(0).getDonor().getId());
    assertEquals(DonationDomain.LOCAL, donations.get(0).getDomain());
    assertEquals("" + donationId, donations.get(0).getDomainId());
    assertEquals(BigDecimal.ZERO.setScale(2), donations.get(0).getAmount());
  }
  
  public void testCheckEmailUpdateable()
  {
    final int localDonor = 1;
    final int chipinDonor = 3;
    
    DonorControl localDonorControl = new DonorControl(this.manager, localDonor);
    DonorControl chipinDonorControl = new DonorControl(this.manager, chipinDonor);
    
    assertTrue(localDonorControl.allowEmailUpdate());
    assertFalse(chipinDonorControl.allowEmailUpdate());
  }
}
