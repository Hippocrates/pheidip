package pheidip.logic;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
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
      candidates.add(new PrizeDrawCandidate(this.donors.getDonorById(donationList.get(0).getDonor().getId()), donationList));
    }
    
    return candidates;
  }
  
  public Donor pickRandomCandidate(BigDecimal minimumAmount, boolean excludeIfWon, List<PrizeDrawCandidate> candidates)
  {
    List<Donor> filtered = new ArrayList<Donor>();
    
    for (PrizeDrawCandidate c : candidates)
    {
      if (c.getMaxDonation().compareTo(minimumAmount) >= 0 && (!excludeIfWon || !c.alreadyHasPrize()))
      {
        filtered.add(c.getDonor());
      }
    }
    
    if (filtered.size() > 0)
    {
      Random rand = new SecureRandom();
      return filtered.get(rand.nextInt(filtered.size()));
    }
    else
    {
      return null;
    }
  }
  
  public Donor pickRandomCandidateWeighted(BigDecimal thresholdAmount, boolean excludeIfWon, List<PrizeDrawCandidate> candidates)
  {
    List<Donor> filtered = new ArrayList<Donor>();
    
    for (PrizeDrawCandidate c : candidates)
    {
      if (!excludeIfWon || !c.alreadyHasPrize())
      {
        int iterations = c.getDonationSum().divide(thresholdAmount, BigDecimal.ROUND_FLOOR).intValue();
        
        for (int i = 0; i < iterations; ++i)
        {
          filtered.add(c.getDonor());
        }
      }
    }
    
    if (filtered.size() > 0)
    {
      Random rand = new SecureRandom();
      return filtered.get(rand.nextInt(filtered.size()));
    }
    else
    {
      return null;
    }
  }
  
  public Donor pickHighestSingleDonation(BigDecimal minimumAmount, boolean excludeIfWon, List<PrizeDrawCandidate> candidates)
  {
    Donor maxDonor = null;
    BigDecimal currentMax = minimumAmount;
    
    for (PrizeDrawCandidate c : candidates)
    {
      if (c.getMaxDonation().compareTo(currentMax) >= 0 && (!excludeIfWon || !c.alreadyHasPrize()))
      {
        maxDonor = c.getDonor();
        currentMax = c.getMaxDonation();
      }
    }
    
    return maxDonor;
  }
  
  public Donor pickHighestSumDonations(BigDecimal minimumAmount, boolean excludeIfWon, List<PrizeDrawCandidate> candidates)
  {
    Donor maxDonor = null;
    BigDecimal currentMax = minimumAmount;
    
    for (PrizeDrawCandidate c : candidates)
    {
      if (c.getDonationSum().compareTo(currentMax) >= 0 && (!excludeIfWon || !c.alreadyHasPrize()))
      {
        maxDonor = c.getDonor();
        currentMax = c.getDonationSum();
      }
    }
    
    return maxDonor;
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
      result = this.pickRandomCandidateWeighted(params.targetAmount, params.excludeIfAlreadyWon, candidates);
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
