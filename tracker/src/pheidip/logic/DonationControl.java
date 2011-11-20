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
  
  public DonationControl(DonationDatabaseManager donationDatabase, Donation donation)
  {
    this.donationDatabase = donationDatabase;
    this.donations = this.donationDatabase.getDataAccess().getDonationData();
    this.bids = this.donationDatabase.getDataAccess().getBids();
    this.donationId = donation.getId();
    this.cachedData = null;
  }
  
  public int getDonationId()
  {
    return this.donationId;
  }
  
  
  public Donation refreshData()
  {
    this.cachedData = this.donations.getDonationById(this.donationId);
    return this.cachedData;
  }
  
  public Donation getData()
  {
    if (this.cachedData == null)
    {
      this.refreshData();
    }

    return this.cachedData;
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

  public void clearDonationRead()
  {
    Donation d = this.getData();
    d.setReadState(DonationReadState.PENDING);
    this.donations.updateDonation(d);
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
      data.setBidState(DonationBidState.PROCESSED);
      this.donations.updateDonation(data);
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
      this.markAsBidsHandled();
      this.donations.updateDonation(data);
    }
    else
    {
      throw new RuntimeException("Total of all bids cannot exceed donation amount.");
    }
    
    return newId;
  }
  
  public void updateDonationBidAmount(DonationBid choiceBid, BigDecimal newAmount)
  {
    if (checkChangeInBidIsBelowDonationAmount(choiceBid, newAmount))
    {
      choiceBid.setAmount(newAmount);
      this.donations.updateDonation(this.getData());
    }
    else
    {
      throw new RuntimeException("Total of all bids cannot exceed donation amount.");
    }
  }
  
  public void removeBid(DonationBid bid)
  {
    this.getData().getBids().remove(bid);
    this.donations.updateDonation(this.getData());
  }

  private boolean checkChangeInBidIsBelowDonationAmount(DonationBid donationBid, BigDecimal newAmount)
  {
    Donation data = this.getData();
    List<DonationBid> currentBids = getAttachedBids();
    
    BigDecimal newTotal = sumBids(currentBids).subtract(donationBid.getAmount()).add(newAmount);
    
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
      this.removeBid((ChallengeBid)b);
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
