package pheidip.logic;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import pheidip.db.DonationData;
import pheidip.db.DonorData;
import pheidip.objects.Donation;
import pheidip.objects.Donor;
import pheidip.util.Filter;
import pheidip.util.FilterFunction;
import pheidip.util.Pair;

public class PrizeAssign
{
  private DonationDatabaseManager manager;
  private DonationData donations;
  private DonorData donors;

  public PrizeAssign(DonationDatabaseManager manager)
  {
    this.manager = manager;
    this.donations = this.manager.getDataAccess().getDonationData();
    this.donors = this.manager.getDataAccess().getDonorData();
  }
  
  public List<Donor> getAllCandidates(final PrizeAssignParams params)
  {
    // get all donations inside the time frame
    List<Donation> donationsInTimeRange = this.donations.getDonationsInTimeRange(params.donatedAfter, params.donatedBefore);
    
    // filter that list by the donations that match the single donation amount
    List<Donation> donationsSingleAboveAmount = Filter.filterList(
        donationsInTimeRange, new FilterFunction<Donation>()
        {
          public boolean predicate(Donation x)
          {
            return params.singleDonationsAbove == null ? true : x.getAmount().compareTo(params.singleDonationsAbove) >= 0;
          }
        });
    
    // set all donors to consider, based on whether or not secondard prizing is allowed
    List<Donor> donorSet = params.excludeIfAlreadyWon ? this.donors.getDonorsWithoutPrizes() : this.donors.getAllDonors();
    
    // build a Donor -> List<Donation> dictionary
    Map<Integer, Pair<Donor,List<Donation> > > donorMap = new HashMap<Integer, Pair<Donor,List<Donation> > >();
    
    for (Donor d : donorSet)
    {
      donorMap.put(d.getId(), new Pair<Donor,List<Donation> >(d, new ArrayList<Donation>()));
    }
    
    for (Donation d : donationsSingleAboveAmount)
    {
      Pair<Donor,List<Donation> > pair = donorMap.get(d.getDonor().getId());
      if (pair != null)
      {
        List<Donation> targetList = donorMap.get(d.getDonor().getId()).getSecond();
        targetList.add(d);
      }
    }
    
    List<Donor> finalFilter = new ArrayList<Donor>();
    
    // get the sum of each donor's contribution over this time-frame
    // all donors with > 0 donations and with contribution total over the threshold are returned
    for (Pair<Donor, List<Donation>> it : donorMap.values())
    {
      BigDecimal sum = BigDecimal.ZERO;
      
      if (params.totalDoantionsAbove != null)
      {
        for (Donation d : it.getSecond())
        {
          sum = sum.add(d.getAmount());
        }
      }
      
      if (it.getSecond().size() > 0 && (params.totalDoantionsAbove == null || sum.compareTo(params.totalDoantionsAbove) >= 0))
      {
        finalFilter.add(it.getFirst());
      }
    }
    
    return finalFilter;
  }
  
  public Donor pickRandomCandidate(List<Donor> candidates)
  {
    if (candidates.size() > 0)
    {
      // select a random number in the range (using _secure_ random this time)
      Random rand = new SecureRandom();
      return candidates.get(rand.nextInt(candidates.size()));
    }
    else
    {
      throw new RuntimeException("Error, no donors found that match the criteria.");
    }
  }
}
