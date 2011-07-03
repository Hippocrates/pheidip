package pheidip.logic;

import java.lang.reflect.Method;
import java.util.List;

import pheidip.db.DonorData;
import pheidip.objects.Donor;
import pheidip.util.Filter;
import pheidip.util.InnerStringMatchFilter;
import pheidip.util.StringUtils;

public class DonorSearch
{
  private DonationDatabaseManager donationDatabase;
  private DonorData donors;
  // This assumes that there won't be too many donors, caching 
  // this to a file (or only allowing searching within the db)
  // in the future may be warranted
  private List<Donor> cachedDonors;
  
  private Method firstNameMethod;
  private Method lastNameMethod;
  private Method aliasMethod;
  private Method emailMethod;
  
  public DonorSearch(DonationDatabaseManager donationDatabase)
  {
    this.donationDatabase = donationDatabase;
    this.donors = this.donationDatabase.getDataAccess().getDonorData();
    
    this.cachedDonors = this.donors.getAllDonors();
    
    try
    {
      this.firstNameMethod = Donor.class.getMethod("getFirstName");
      this.lastNameMethod = Donor.class.getMethod("getLastName");
      this.aliasMethod = Donor.class.getMethod("getAlias");
      this.emailMethod = Donor.class.getMethod("getEmail");
    } 
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public Donor createIfAble(String firstName, String lastName, String email, String alias)
  {
    int createdDonor = DonorControl.createNewDonor(this.donationDatabase, email, alias, firstName, lastName);
    return this.donors.getDonorById(createdDonor);
  }
  
  public List<Donor> searchDonors(String firstName, String lastName, String email, String alias)
  {
    List<Donor> filtered = this.cachedDonors;
    
    if (!StringUtils.isEmptyOrNull(firstName))
    {
      filtered = this.searchByStringField(firstName, filtered, this.firstNameMethod);
    }
    
    if (!StringUtils.isEmptyOrNull(lastName))
    {
      filtered = this.searchByStringField(lastName, filtered, this.lastNameMethod);
    }
    
    if (!StringUtils.isEmptyOrNull(email))
    {
      filtered = this.searchByStringField(email, filtered, this.emailMethod);
    }
    
    if (!StringUtils.isEmptyOrNull(alias))
    {
      filtered = this.searchByStringField(alias, filtered, this.aliasMethod);
    }
    
    return filtered;
  }
  
  private List<Donor> searchByStringField(String firstName, List<Donor> input, Method getter)
  {
    return Filter.filterList(input, new InnerStringMatchFilter(firstName), getter);
  }
}
