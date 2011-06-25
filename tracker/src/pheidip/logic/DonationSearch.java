package pheidip.logic;

import java.util.List;

import pheidip.db.DonationData;
import pheidip.objects.Donation;
import pheidip.util.Filter;

public class DonationSearch
{
  private DonationDatabaseManager manager;
  private DonationData donations;
  List<Donation> cachedDonations;

  public DonationSearch(DonationDatabaseManager manager)
  {
    this.manager = manager;
    this.donations = this.manager.getDataAccess().getDonationData();
    this.cachedDonations = this.donations.getAllDonations();
  }
  
  public DonorSearch createDonorSearch()
  {
    return new DonorSearch(this.manager);
  }
  
  public List<Donation> searchDonations(DonationSearchParams param)
  {
    return Filter.filterList(this.cachedDonations, param);
  }
}
