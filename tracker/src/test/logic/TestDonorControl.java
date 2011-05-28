package test.logic;

import java.util.List;

import pheidip.logic.DonationDatabaseManager;
import pheidip.logic.DonorControl;
import pheidip.objects.Donation;
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
  
  //TODO: make a check if a field is allowed to be updated
}
