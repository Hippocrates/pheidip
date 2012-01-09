package pheidip.logic;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import pheidip.db.DonationData;
import pheidip.db.DonorData;
import pheidip.objects.Donation;
import pheidip.objects.Donor;

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
  
  public List<PrizeDrawCandidate> getAllCandidatesInRange(Date loRange, Date hiRange)
  {
    List<Donation> donationsInTimeRange = this.donations.getDonationsInTimeRange(loRange, hiRange);
    
    Map<Integer, List<Donation> > donationMap = new HashMap<Integer, List<Donation> >();
   
    for (Donation d : donationsInTimeRange)
    {
      List<Donation> donorList = donationMap.get(d.getDonor().getId());
      
      if (donorList == null)
      {
        donorList = new ArrayList<Donation>();
        donationMap.put(d.getDonor().getId(), donorList);
      }
      
      donorList.add(d);
    }
    
    List<Donor> allDonors = this.donors.getAllDonors();
    
    Map<Integer, Donor> donorMap = new HashMap<Integer, Donor>();
    
    for (Donor d : allDonors)
    {
      donorMap.put(d.getId(), d);
    }
    
    ArrayList<PrizeDrawCandidate> candidates = new ArrayList<PrizeDrawCandidate>();
    
    for (List<Donation> donationList : donationMap.values())
    {
      candidates.add(new PrizeDrawCandidate(donorMap.get(donationList.get(0).getDonor().getId()), donationList));
    }
    
    return candidates;
  }
  
  public Donor selectFromFilteredList(List<Donor> filtered, boolean excludeIfWon)
  {
	    while (filtered.size() > 0)
	    {
	      Random rand = new SecureRandom();
	      int location = rand.nextInt(filtered.size());
	      Donor result = this.donors.getDonorById(filtered.get(location).getId());
	      
	      if (result.getPrizes().size() > 0 && excludeIfWon)
	      {
	    	  filtered.remove(location);
	    	  continue;
	      }
	      else
	      {
	    	  return result;
	      }
	    }

	    return null;
  }
  
  public Donor pickRandomCandidate(BigDecimal minimumAmount, boolean excludeIfWon, List<PrizeDrawCandidate> candidates)
  {
    List<Donor> filtered = new ArrayList<Donor>();

    for (PrizeDrawCandidate c : candidates)
    {
      if (c.getMaxDonation().compareTo(minimumAmount) >= 0)
      {
        filtered.add(c.getDonor());
      }
    }
    
    return this.selectFromFilteredList(filtered, excludeIfWon);
  }
  
  public Donor pickRandomCandidateWeighted(BigDecimal thresholdAmount, boolean excludeIfWon, int maxRaffleTickets, List<PrizeDrawCandidate> candidates)
  {
    List<Donor> filtered = new ArrayList<Donor>();
    
    for (PrizeDrawCandidate c : candidates)
    {
        int iterations = c.getDonationSum().divide(thresholdAmount, BigDecimal.ROUND_FLOOR).intValue();
        
        if (maxRaffleTickets != 0)
        {
          iterations = Math.min(iterations, maxRaffleTickets);
        }
        
        for (int i = 0; i < iterations; ++i)
        {
          filtered.add(c.getDonor());
        }
    }
    
    return this.selectFromFilteredList(filtered, excludeIfWon);
  }
  
  public Donor pickHighestSingleDonation(BigDecimal minimumAmount, boolean excludeIfWon, List<PrizeDrawCandidate> candidates)
  {
	  Collections.sort(candidates, new Comparator<PrizeDrawCandidate>()
			    {
					@Override
					public int compare(PrizeDrawCandidate o1, PrizeDrawCandidate o2) 
					{
						return o1.getMaxDonation().compareTo(o2.getMaxDonation());
					}
			    	
			    });
			    
			    for (int i = candidates.size() - 1; i >= 0; --i)
			    {
				    Donor result = this.donors.getDonorById(candidates.get(i).getDonor().getId());
				      
				    if (!(result.getPrizes().size() > 0 && excludeIfWon))
				    {
				      return result;
				    }
			    }
			    
			    return null;
  }
  
  public Donor pickHighestSumDonations(BigDecimal minimumAmount, boolean excludeIfWon, List<PrizeDrawCandidate> candidates)
  {
    Collections.sort(candidates, new Comparator<PrizeDrawCandidate>()
    {
		@Override
		public int compare(PrizeDrawCandidate o1, PrizeDrawCandidate o2) 
		{
			return o1.getDonationSum().compareTo(o2.getDonationSum());
		}
    	
    });
    
    for (int i = candidates.size() - 1; i >= 0; --i)
    {
	    Donor result = this.donors.getDonorById(candidates.get(i).getDonor().getId());
	      
	    if (!(result.getPrizes().size() > 0 && excludeIfWon))
	    {
	      return result;
	    }
    }
    
    return null;
  }

  public Donor selectWinner(PrizeAssignParams params)
  {
    List<PrizeDrawCandidate> candidates = this.getAllCandidatesInRange(params.donatedAfter, params.donatedBefore);
    
    Donor result = null;
    
    switch (params.method)
    {
    case RANDOM_UNIFORM_DRAW:
      result = this.pickRandomCandidate(params.targetAmount, params.excludeIfAlreadyWon, candidates);
      break;
    case SINGLE_HIGHEST_DONATION:
      result = this.pickHighestSingleDonation(params.targetAmount, params.excludeIfAlreadyWon, candidates);
      break;
    case RANDOM_WEIGHTED_DRAW:
      result = this.pickRandomCandidateWeighted(params.targetAmount, params.excludeIfAlreadyWon, params.maxRaffleTickets, candidates);
      break;
    case HIGHEST_SUM_DONATIONS:
      result = this.pickHighestSumDonations(params.targetAmount, params.excludeIfAlreadyWon, candidates);
      break;
    default:
      throw new RuntimeException("Error, unknown prize drawing method.");
    }
    
    if (result == null)
    {
      throw new RuntimeException("Error, no donors found that match this criteria.");
    }
    else
    {
      return result;
    }
  }
}
