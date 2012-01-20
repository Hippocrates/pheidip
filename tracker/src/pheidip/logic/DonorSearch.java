package pheidip.logic;

import java.util.List;

import pheidip.db.DonorData;
import pheidip.objects.Donor;
import pheidip.objects.DonorSearchParams;
import pheidip.objects.SearchParameters;

public class DonorSearch extends EntitySearcher<Donor>
{
  public static final int DEFAULT_SEARCH_SIZE = 20;
  
  private DonationDatabaseManager donationDatabase;
  private DonorData donors;

  public DonorSearch(DonationDatabaseManager donationDatabase)
  {
    this.donationDatabase = donationDatabase;
    this.donors = this.donationDatabase.getDataAccess().getDonorData();
  }
  
  public Donor createIfAble(DonorSearchParams params)
  {
    int createdDonor = DonorControl.createNewDonor(this.donationDatabase, params.getEmail(), params.getAlias(), params.getFirstName(), params.getLastName());
    return this.donors.getDonorById(createdDonor);
  }
 
  @Override
  public List<Donor> implRunSearch(SearchParameters<Donor> params, int searchOffset, int searchSize)
  {
    return this.donors.searchDonorsRange(params, searchOffset, searchSize);
  }

}
