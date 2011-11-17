package pheidip.logic;

import java.util.List;

import pheidip.db.DonorData;
import pheidip.objects.Donor;
import pheidip.objects.DonorSearchParams;

public class DonorSearch
{
  private DonationDatabaseManager donationDatabase;
  private DonorData donors;
  
  public DonorSearch(DonationDatabaseManager donationDatabase)
  {
    this.donationDatabase = donationDatabase;
    this.donors = this.donationDatabase.getDataAccess().getDonorData();
  }
  
  public Donor createIfAble(DonorSearchParams params)
  {
    int createdDonor = DonorControl.createNewDonor(this.donationDatabase, params.email, params.alias, params.firstName, params.lastName);
    return this.donors.getDonorById(createdDonor);
  }
  
  public List<Donor> searchDonors(DonorSearchParams params)
  {
    return this.donors.searchDonors(params);
  }
}
