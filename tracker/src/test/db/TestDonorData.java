package test.db;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import pheidip.db.ConnectionManager;
import pheidip.db.DonorData;
import pheidip.db.ScriptRunner;
import pheidip.objects.Donor;
import junit.framework.TestCase;

public class TestDonorData extends TestCase
{
  private ConnectionManager manager;
  private DonorData donors;

  public void setUp()
  {
    this.manager = new ConnectionManager();
    this.manager.createMemoryDatabase();
    
    ScriptRunner runner = new ScriptRunner(this.manager.getConnection(), true, true);
    
    try
    {
      runner.runScript(new FileReader(DBTestConfiguration.getTestDataDirectory() + "donation_bid_test_data_1.sql"));
    }
    catch (IOException e)
    {
      fail(e.getMessage());
    } 
    catch (SQLException e)
    {
      this.manager.handleSQLException(e);
    }
    
    this.donors = new DonorData(this.manager);
  }
  
  public void tearDown()
  {
    if (this.manager.isConnected())
    {
      this.manager.closeConnection();
    }
  }
  
  public void testGetDonorById()
  {
    Donor result = this.donors.getDonorById(1);
    
    assertNotNull(result);
    assertEquals(1, result.getId());
    assertEquals("test1@test.com", result.getEmail());
    assertNull(result.getAlias());
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
  
  public void testUpdateDonorEmail()
  {
    final String newEmail = "DifFerent@eMAil.com";
    this.donors.setDonorEmail(1, newEmail);
    
    Donor result = this.donors.getDonorById(1);
    
    assertEquals(newEmail.toLowerCase(), result.getEmail());
    
    try
    {
      this.donors.setDonorEmail(2, newEmail);
      
      fail("Error, update allowed duplicate.");
    }
    catch (Exception e)
    {
      // pass
    }
  }
  
  public void testUpdateDonorAlias()
  {
    final String newAlias = "sMk";
    this.donors.setDonorAlias(1, newAlias);
    
    Donor result = this.donors.getDonorById(1);
    
    assertEquals(newAlias.toLowerCase(), result.getAlias());
    
    try
    {
      this.donors.setDonorAlias(2, newAlias);
      
      fail("Error, update allowed duplicate.");
    }
    catch (Exception e)
    {
      // pass
    }
    
    try
    {
      this.donors.setDonorAlias(7, "something different");
      
      fail("Error, update non-existant allowed.");
    }
    catch (Exception e)
    {
      // pass
    }
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
