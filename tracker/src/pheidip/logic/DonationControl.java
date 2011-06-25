package pheidip.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pheidip.db.BidData;
import pheidip.db.DonationData;
import pheidip.db.DonorData;
import pheidip.db.SpeedRunData;
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
  private DonorData donors;
  private SpeedRunData speedRuns;
  private int donationId;
  private BidData bids;

  public DonationControl(DonationDatabaseManager donationDatabase, int donationId)
  {
    this.donationDatabase = donationDatabase;
    this.donations = this.donationDatabase.getDataAccess().getDonationData();
    this.donors = this.donationDatabase.getDataAccess().getDonorData();
    this.bids = this.donationDatabase.getDataAccess().getBids();
    this.speedRuns = this.donationDatabase.getDataAccess().getSpeedRuns();
    this.donationId = donationId;
  }
  
  public int getDonationId()
  {
    return this.donationId;
  }
  
  public Donation getData()
  {
    return this.donations.getDonationById(this.donationId);
  }
  
  public Donor getDonationDonor()
  {
    return this.donors.getDonorById(this.getData().getDonorId());
  }
  
  public void updateData(BigDecimal amount, String comment, boolean markAsRead)
  {
    if (this.allowUpdateData())
    {
      if (amount.compareTo(BigDecimal.ZERO) < 0)
      {
        throw new RuntimeException("Error, donation amonut must be non-negative.");
      }
      
      if (sumBids(this.getAttachedBids()).compareTo(amount) > 0)
      {
        throw new RuntimeException("Error, change in amount would invalidate the sum of the attached bids.");
      }

      this.donations.setDonationAmount(this.donationId, amount);
      this.donations.setDonationComment(this.donationId, comment);
    }
    
    if (markAsRead)
    {
      this.markDonationAsRead();
    }
    else
    {
      this.clearDonationRead();
    }
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
    ChallengeBid created = new ChallengeBid(newId, amount, challengeId, this.donationId);
    
    Donation data = this.getData();

    if (sumBids(getAttachedBids()).add(amount).compareTo(data.getAmount()) <= 0)
    {
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
    ChoiceBid created = new ChoiceBid(newId, amount, choiceId, this.donationId);
    
    Donation data = this.getData();

    if (sumBids(getAttachedBids()).add(amount).compareTo(data.getAmount()) <= 0)
    {
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
      ChoiceOption option = this.bids.getChoiceOptionById(c.getOptionId());
      Choice choice = this.bids.getChoiceById(option.getChoiceId());
      SpeedRun run = this.speedRuns.getSpeedRunById(choice.getSpeedRunId());
      return run.getName() + " : " + choice.getName() + " : " + option.getName();
    }
    else
    {
      ChallengeBid c = (ChallengeBid) b;
      Challenge challenge = this.bids.getChallengeById(c.getChallengeId());
      SpeedRun run = this.speedRuns.getSpeedRunById(challenge.getSpeedRunId());
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
    
    this.donations.deleteDonation(this.donationId);
  }
  
  public List<DonationBid> getAttachedBids()
  {
    List<DonationBid> attachedBids = new ArrayList<DonationBid>();
    
    attachedBids.addAll(this.donations.getChallengeBidsByDonationId(this.donationId));
    attachedBids.addAll(this.donations.getChoiceBidsByDonationId(this.donationId));
    
    return attachedBids;
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

  public void markDonationAsRead()
  {
    Donation d = this.getData();
    
    if (d.getComment() != null)
    {
      this.donations.setDonationReadState(this.donationId, DonationReadState.COMMENT_READ);
    }
    else
    {
      this.donations.setDonationReadState(this.donationId, DonationReadState.AMOUNT_READ);
    }
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
