package pheidip.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import pheidip.db.DonationData;
import pheidip.db.DonorData;
import pheidip.objects.ChallengeBid;
import pheidip.objects.ChoiceBid;
import pheidip.objects.Donation;
import pheidip.objects.DonationBid;
import pheidip.objects.DonationDomain;
import pheidip.objects.Donor;
import pheidip.util.IdUtils;

public class DonationControl
{
  private DonationDatabaseManager donationDatabase;
  private DonationData donations;
  private DonorData donors;
  private int donationId;

  public DonationControl(DonationDatabaseManager donationDatabase, int donationId)
  {
    this.donationDatabase = donationDatabase;
    this.donations = this.donationDatabase.getDataAccess().getDonationData();
    this.donors = this.donationDatabase.getDataAccess().getDonorData();
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
  
  public void updateData(BigDecimal amount, String comment)
  {
    if (this.allowUpdateData())
    {
      this.donations.setDonationAmount(this.donationId, amount);
      this.donations.setDonationComment(this.donationId, comment);
    }
    else
    {
      throw new RuntimeException("Raw updates not allowed on non-local donations.");
    }
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
    List<DonationBid> currentBids = getAttachedBids();
    currentBids.add(created);
    
    if (sumIsUnderAmount(currentBids, data.getAmount()))
    {
      this.donations.attachChallengeBid(created);
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
    List<DonationBid> currentBids = getAttachedBids();
    currentBids.add(created);
    
    if (sumIsUnderAmount(currentBids, data.getAmount()))
    {
      this.donations.attachChoiceBid(created);
    }
    else
    {
      throw new RuntimeException("Total of all bids cannot exceed donation amount.");
    }
    
    return newId;
  }

  public void deleteDonation()
  {
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
  
  private static BigDecimal sumBids(List<DonationBid> attachedBids)
  {
    BigDecimal sum = BigDecimal.ZERO;
    
    for (DonationBid b : attachedBids)
    {
      sum.add(b.getAmount());
    }
    
    return sum;
  }
  
  private static boolean sumIsUnderAmount(List<DonationBid> attachedBids, BigDecimal total)
  {
    return (sumBids(attachedBids).compareTo(total) <= 0);
  }
}
