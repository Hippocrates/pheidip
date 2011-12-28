package test.db;

import java.util.Date;
import java.util.List;

import pheidip.db.DonorData;
import pheidip.db.PrizeData;
import pheidip.objects.Donor;
import pheidip.objects.Prize;

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
    
    assertEquals(2, result.getDonations().size());
    
    assertEquals(1, result.getPrizes().size());
    
    assertNull(donors.getDonorById(0));
  }
  
  public void testGetAllDonors()
  {
    List<Donor> result = this.donors.getAllDonors();
    
    assertNotNull(result);
    assertEquals(6, result.size());
  }
  
  public void testCreateDonor()
  {
    Donor template = new Donor((int) new Date().getTime(), "some.email@test.com", "analiasthathasntbeenusedyet", "first", "last");
    
    this.donors.createDonor(template);
    
    Donor copycat1 = new Donor(template.getId(), "another-email@test.com", null, "first", "last");
    
    try
    {
      this.donors.createDonor(copycat1);

      fail("Did not refuse creation of duplicate donor.");
    }
    catch (Exception e)
    {
      // pass
    }
    
    Donor copycat2 = new Donor(template.getId() + 1, template.getEmail(), null, "first", "last");
    
    try
    {
      this.donors.createDonor(copycat2);

      fail("Did not refuse creation of duplicate email donor.");
    }
    catch (Exception e)
    {
      // pass
    }
    
    Donor copycat3 = new Donor(template.getId() + 1, null, template.getAlias(), "first", "last");
    
    try
    {
      this.donors.createDonor(copycat3);

      fail("Did not refuse creation of duplicate alias donor.");
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

	  Donor updated = this.donors.getDonorById(id);
	  updated.setEmail(email);
	  updated.setAlias(alias);
	  updated.setFirstName(firstName);
	  updated.setLastName(lastName);
	  
	  this.donors.updateDonor(updated);
	  
	  this.compareDonors(updated, this.donors.getDonorById(id));
  }
  
  public void testDeleteDonor()
  {
    Donor d = donors.getDonorById(5);
    this.donors.deleteDonor(d);
      
    assertNull(donors.getDonorById(5));
 
    try
    {
      Donor d2 = donors.getDonorById(2);
      this.donors.deleteDonor(d2);
      
      // check the the association nulls, and is not obliterated completely on delete,
      // since prizes exist as stand-alone entities
      PrizeData prizes = this.getDataAccess().getPrizeData();
      
      Prize p = prizes.getPrizeById(1);
      
      assertNotNull(p);
    }
    catch(Exception e)
    {
      // pass
    }
  }
}
