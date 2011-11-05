package pheidip.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pheidip.db.BidData;
import pheidip.db.DonationData;
import pheidip.objects.BidType;
import pheidip.objects.Challenge;
import pheidip.objects.ChallengeBid;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceBid;
import pheidip.objects.ChoiceOption;
import pheidip.objects.Donation;
import pheidip.objects.DonationBid;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationReadState;
import pheidip.objects.Donor;
import pheidip.objects.SpeedRun;
import pheidip.util.IdUtils;

public class DonationControl
{
  private DonationDatabaseManager donationDatabase;
  private DonationData donations;
  private BidData bids;
  
  private int donationId;
  private Donation cachedData;

  public DonationControl(DonationDatabaseManager donationDatabase, int donationId)
  {
    this.donationDatabase = donationDatabase;
    this.donations = this.donationDatabase.getDataAccess().getDonationData();
    this.bids = this.donationDatabase.getDataAccess().getBids();
    this.donationId = donationId;
    this.cachedData = null;
  }
  
  public int getDonationId()
  {
    return this.donationId;
  }
  
  public Donation getData()
  {
    if (this.cachedData == null)
    {
      return this.refreshData();
    }
    else
    {
      return this.cachedData;
    }
  }
  
  public Donor getDonationDonor()
  {
    return this.getData().getDonor();
  }
  
  public void updateData(Donation data)
  {
    if (this.allowUpdateData())
    {
      if (data.getAmount().compareTo(BigDecimal.ZERO) < 0)
      {
        throw new RuntimeException("Error, donation amonut must be non-negative.");
      }
      
      if (sumBids(this.getAttachedBids()).compareTo(data.getAmount()) > 0)
      {
        throw new RuntimeException("Error, change in amount would invalidate the sum of the attached bids.");
      }
    }
    
    this.donations.updateDonation(data);
    this.cachedData = this.refreshData();
  }
  
  public Donation refreshData()
  {
    return this.donations.getDonationById(this.donationId);
  }

  public void clearDonationRead()
  {
    this.donations.setDonationReadState(this.donationId, DonationReadState.PENDING);
  }

  public boolean allowUpdateData()
  {
    Donation data = this.getData();
    
    if (data.getDomain() == DonationDomain.LOCAL)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  public int attachNewChallengeBid(int challengeId, BigDecimal amount)
  {
    int newId = IdUtils.generateId();
    
    Donation data = this.getData();
    
    ChallengeBid created = new ChallengeBid(newId, amount, this.bids.getChallengeById(challengeId), data);

    if (sumBids(getAttachedBids()).add(amount).compareTo(data.getAmount()) <= 0)
    {
      data.getBids().add(created);
      this.donations.attachChallengeBid(created);
      this.donations.setDonationBidState(this.donationId, DonationBidState.PROCESSED);
    }
    else
    {
      throw new RuntimeException("Total of all bids cannot exceed donation amount.");
    }
    
    return newId;
  }
  
  public int attachNewChoiceBid(int choiceId, BigDecimal amount)
  {
    int newId = IdUtils.generateId();
    Donation data = this.getData();
    
    ChoiceBid created = new ChoiceBid(newId, amount, this.bids.getChoiceOptionById(choiceId), data);

    if (sumBids(getAttachedBids()).add(amount).compareTo(data.getAmount()) <= 0)
    {
      data.getBids().add(created);
      this.donations.attachChoiceBid(created);
      this.markAsBidsHandled();
    }
    else
    {
      throw new RuntimeException("Total of all bids cannot exceed donation amount.");
    }
    
    return newId;
  }
  
  public void updateChallengeBidAmount(int challengeBidId, BigDecimal newAmount)
  {
    if (checkChangeInBidIsBelowDonationAmount(challengeBidId, BidType.CHALLENGE, newAmount))
    {
      this.donations.updateChallengeBidAmount(challengeBidId, newAmount);
    }
    else
    {
      throw new RuntimeException("Total of all bids cannot exceed donation amount.");
    }
  }
  
  public void updateChoiceBidAmount(int choiceBidId, BigDecimal newAmount)
  {
    if (checkChangeInBidIsBelowDonationAmount(choiceBidId, BidType.CHOICE, newAmount))
    {
      this.donations.updateChoiceBidAmount(choiceBidId, newAmount);
    }
    else
    {
      throw new RuntimeException("Total of all bids cannot exceed donation amount.");
    }
  }
  
  public void removeChallengeBid(int challengeBidId)
  {
    this.donations.removeChallengeBid(challengeBidId);
  }
  
  public void removeChoiceBid(int choiceBidId)
  {
    this.donations.removeChoiceBid(choiceBidId);
  }
  
  private boolean checkChangeInBidIsBelowDonationAmount(int donationBidId, BidType type, BigDecimal newAmount)
  {
    Donation data = this.getData();
    List<DonationBid> currentBids = getAttachedBids();
    DonationBid found = null;

    for (DonationBid b : currentBids)
    {
      if (b.getType() == type && b.getId() == donationBidId)
      {
        found = b;
      }
    }
    
    if (found == null)
    {
      throw new RuntimeException("Error, donation bid not found.");
    }
    
    BigDecimal newTotal = sumBids(currentBids).subtract(found.getAmount()).add(newAmount);
    
    return newTotal.compareTo(data.getAmount()) <= 0;
  }

  public String getDonationBidDisplayName(DonationBid b)
  {
    if (b.getType() == BidType.CHOICE)
    {
      ChoiceBid c = (ChoiceBid) b;
      ChoiceOption option = c.getOption();
      Choice choice = option.getChoice();
      SpeedRun run = choice.getSpeedRun();
      return run.getName() + " : " + choice.getName() + " : " + option.getName();
    }
    else
    {
      ChallengeBid c = (ChallengeBid) b;
      Challenge challenge = c.getChallenge();
      SpeedRun run = challenge.getSpeedRun();
      return run.getName() + " : " + challenge.getName();
    }
  }

  public void deleteDonation()
  {
    List<DonationBid> attachedBids = getAttachedBids();
    
    for (DonationBid b : attachedBids)
    {
      if (b.getType() == BidType.CHALLENGE)
      {
        this.removeChallengeBid(b.getId());
      }
      else
      {
        this.removeChoiceBid(b.getId());
      }
    }
    
    this.cachedData = null;
    this.donations.deleteDonation(this.donationId);
  }
  
  public List<DonationBid> getAttachedBids()
  {
    return new ArrayList<DonationBid>(this.getData().getBids());
  }
  
  public BigDecimal getTotalUsed()
  {
    return sumBids(this.getAttachedBids());
  }
  
  public BigDecimal getTotalAvailiable()
  {
    return this.getData().getAmount().subtract(sumBids(this.getAttachedBids()));
  }
  
  private static BigDecimal sumBids(List<DonationBid> attachedBids)
  {
    BigDecimal sum = BigDecimal.ZERO;
    
    for (DonationBid b : attachedBids)
    {
      sum = sum.add(b.getAmount());
    }
    
    return sum;
  }
  
  public void markAsBidsHandled()
  {
    Donation d = this.getData();
    
    if (d.getComment() != null)
    {
      this.donations.setDonationBidState(d.getId(), DonationBidState.PROCESSED);
    }
  }
}
