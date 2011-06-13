package test.db;

import java.util.Date;
import java.util.List;

import pheidip.db.DonorData;
import pheidip.objects.Donor;

public class TestDonorData extends DonationDatabaseTest
{
  private DonorData donors;

  public void setUp()
  {
    super.setUp();
    this.donors = this.getDataAccess().getDonorData();
  }
  
  public void testGetDonorById()
  {
    Donor result = this.donors.getDonorById(1);
    
    assertNotNull(result);
    assertEquals(1, result.getId());
    assertEquals("test1@test.com", result.getEmail());
    assertEquals("smk", result.getAlias());
    assertNull(donors.getDonorById(0));
  }
  
  public void testGetDonorByEmail()
  {
    Donor result = this.donors.getDonorByEmail("test2@test.com");
    
    assertNotNull(result);
    assertEquals(2, result.getId());
    assertEquals("test2@test.com", result.getEmail());
    assertEquals("analias", result.getAlias());
    
    assertNull(donors.getDonorByEmail("test5@test.com"));
  }
  
  public void testGetDonorByAlias()
  {
    Donor result = this.donors.getDonorByAlias("anotheralias");
    
    assertNotNull(result);
    assertEquals(4, result.getId());
    assertEquals("test4@test.com", result.getEmail());
    assertEquals("anotheralias", result.getAlias());
    
    assertNull(donors.getDonorByAlias("test5@test.com"));
  }
  
  public void testGetAllDonors()
  {
    List<Donor> result = this.donors.getAllDonors();
    
    assertNotNull(result);
    assertEquals(6, result.size());
  }
  
  public void testCreateDonor()
  {
    Donor template = new Donor((int) new Date().getTime(), "some.email@test.com", null, "first", "last");
    
    this.donors.createDonor(template);
    
    Donor result = this.donors.getDonorById(template.getId());
    
    this.compareDonors(template, result);
    
    try
    {
      this.donors.createDonor(template);
      fail("Did not refuse creation of duplicate donor.");
    }
    catch (Exception e)
    {
      // pass
    }
  }
  
  private void compareDonors(Donor a, Donor b)
  {
    assertEquals(a.getId(), b.getId());
    assertEquals(a.getEmail(), b.getEmail());
    assertEquals(a.getAlias(), b.getAlias());
    assertEquals(a.getAlias(), b.getAlias());
    assertEquals(a.getFirstName(), b.getFirstName());
    assertEquals(a.getLastName(), b.getLastName());
  }
  
  public void testUpdateDonor()
  {
	  final int id = 1;
	  final String email = "ANewEMaiL@asdlkj.adslih";
	  final String alias = "ANewAlias";
	  final String firstName = "fn";
	  final String lastName = "ln";
	  
	  Donor updated = new Donor(id, email, alias, firstName, lastName);
	  
	  this.donors.updateDonor(updated);
	  
	  this.compareDonors(updated, this.donors.getDonorById(id));
  }
  
  public void testDeleteDonor()
  {
    // can't delete one of them due to fk constraint, try again
    this.donors.deleteDonor(5);
      
    assertNull(donors.getDonorById(5));
      
    try
    {
      this.donors.deleteDonor(5);
      fail("Did not throw on non-present donor.");
    }
    catch(Exception e)
    {
      // pass
    }
  }
}
