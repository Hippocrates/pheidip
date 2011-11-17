package pheidip.logic;

import java.util.List;

import pheidip.db.DonationData;
import pheidip.objects.Donation;
import pheidip.objects.DonationSearchParams;

public class DonationSearch
{
  private DonationDatabaseManager manager;
  private DonationData donations;
  List<Donation> cachedDonations;

  public DonationSearch(DonationDatabaseManager manager)
  {
    this.manager = manager;
    this.donations = this.manager.getDataAccess().getDonationData();
  }
  
  public DonorSearch createDonorSearch()
  {
    return new DonorSearch(this.manager);
  }
  
  public List<Donation> searchDonations(DonationSearchParams param)
  {
    return this.donations.searchDonations(param);
  }
}
