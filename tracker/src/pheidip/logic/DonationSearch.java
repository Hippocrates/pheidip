package pheidip.logic;

import java.util.List;

import pheidip.db.DonationData;
import pheidip.objects.Donation;
import pheidip.objects.DonationSearchParams;

public class DonationSearch extends EntitySearcher<Donation, DonationSearchParams>
{
  public final static int DEFAULT_SEARCH_SIZE = 20;
  
  private DonationDatabaseManager manager;
  private DonationData donations;

  public DonationSearch(DonationDatabaseManager manager)
  {
    this.manager = manager;
    this.donations = this.manager.getDataAccess().getDonationData();
  }
  
  public DonorSearch createDonorSearch()
  {
    return new DonorSearch(this.manager);
  }

  @Override
  protected List<Donation> implRunSearch(DonationSearchParams params, int searchOffset, int searchSize)
  {
    return this.donations.searchDonationsRange(params, searchOffset, searchSize);
  }
}
