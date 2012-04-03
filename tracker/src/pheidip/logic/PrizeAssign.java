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

import meta.MetaEntity;
import meta.reflect.MetaEntityReflector;
import meta.search.MetaSearchUtils;

import pheidip.model.PropertyReflectSupport;
import pheidip.objects.Donation;
import pheidip.objects.Donor;

public class PrizeAssign
{
  private MetaEntity donorEntity;
  private ProgramInstance instance;
  private EntitySearch<Donation> donationSearch;
  private EntitySearch<Donor> donorSearch;

  public PrizeAssign(ProgramInstance instance)
  {
    this.instance = instance;
    this.donationSearch = this.instance.getEntitySearch(Donation.class);
    this.donorSearch = this.instance.getEntitySearch(Donor.class);
    
    this.donorEntity = MetaEntityReflector.getMetaEntity(Donor.class);
  }
  
  public List<PrizeDrawCandidate> getAllCandidatesInRange(Date loRange, Date hiRange)
  {
    EntitySearchInstance<Donation> searchInstance = donationSearch.createSearchInstance();
    
    PropertyReflectSupport.setProperty(searchInstance.getSearchParams(), MetaSearchUtils.getLowerRangeName("timeReceived"), loRange);
    PropertyReflectSupport.setProperty(searchInstance.getSearchParams(), MetaSearchUtils.getUpperRangeName("timeReceived"), hiRange);
    searchInstance.setPageSize(Integer.MAX_VALUE);
    searchInstance.runSearch();
    
    List<Donation> donationsInTimeRange = searchInstance.getResults();
    
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
    
    EntitySearchInstance<Donor> donorSearchInstance = this.donorSearch.createSearchInstance();
    donorSearchInstance.setPageSize(Integer.MAX_VALUE);
    donorSearchInstance.runSearch();
    List<Donor> allDonors = donorSearchInstance.getResults();
    
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
	      Donor result = this.instance.getDataAccess().loadInstance(this.donorEntity, filtered.get(location).getId());
	      
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
				    Donor result = this.instance.getDataAccess().loadInstance(this.donorEntity, candidates.get(i).getDonor().getId());
				      
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
	    Donor result = this.instance.getDataAccess().loadInstance(this.donorEntity, candidates.get(i).getDonor().getId());
	      
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
