package pheidip.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pheidip.db.DonationData;
import pheidip.db.DonationDataConstraintException;
import pheidip.db.DonorData;
import pheidip.objects.Donation;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationCommentState;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationReadState;
import pheidip.objects.Donor;
import pheidip.objects.Prize;
import pheidip.util.IdUtils;
import pheidip.util.StringUtils;

public class DonorControl
{
  private DonationDatabaseManager donationDatabase;
  private DonorData donors;
  private DonationData donations;

  private int donorId;
  private Donor cachedDonor;
  
  public static int createNewDonor(DonationDatabaseManager donationDatabase)
  {
    return createNewDonor(donationDatabase, null, null, null, null);
  }
  
  public static int createNewDonor(DonationDatabaseManager donationDatabase, String eMail, String alias, String firstName, String lastName)
  {
    int id = IdUtils.generateId();
    donationDatabase.getDataAccess().getDonorData().createDonor(new Donor(id, 
        StringUtils.nullIfEmpty(eMail), 
        StringUtils.nullIfEmpty(alias), 
        StringUtils.nullIfEmpty(firstName), 
        StringUtils.nullIfEmpty(lastName)));
    return id;
  }
  
  public DonorControl(DonationDatabaseManager donationDatabase, int donorId)
  {
    this.donationDatabase = donationDatabase;
    this.donors = this.donationDatabase.getDataAccess().getDonorData();
    this.donations = this.donationDatabase.getDataAccess().getDonationData();
    this.donorId = donorId;
    this.cachedDonor = null;
  }
  
  public DonorControl(DonationDatabaseManager donationDatabase, Donor data)
  {
    this.donationDatabase = donationDatabase;
    this.donors = this.donationDatabase.getDataAccess().getDonorData();
    this.donations = this.donationDatabase.getDataAccess().getDonationData();
    this.donorId = data.getId();
    this.cachedDonor = data;
  }
  
  public int getDonorId()
  {
    return this.donorId;
  }
  
  public Donor getData()
  {
    if (this.cachedDonor == null)
    {
      return this.refreshData();
    }
    else
    {
      return this.cachedDonor;
    }
  }
  
  public Donor refreshData()
  {
    return this.cachedDonor = this.donors.getDonorById(this.donorId);
  }

  public List<Donation> getDonorDonations()
  {
    return new ArrayList<Donation>(this.getData().getDonations());
  }

  public BigDecimal getTotalDonated()
  {
    return this.getData().getDonationTotal();
  }

  public void updateData(Donor data)
  {
    try
    {
      this.donors.updateDonor(data);
      this.refreshData();
    }
    catch(DonationDataConstraintException e)
    {
      this.donationDatabase.reportMessage(e.getMessage());
    }
  }
  
  public Prize getPrizeWon()
  {
    Donor d = this.getData();
    
    if (!d.getPrizes().isEmpty())
    {
      return d.getPrizes().iterator().next();
    }
    else
    {
      return null;
    }
  }
  
  public void deleteDonor()
  {
    this.cachedDonor = null;
    this.donors.deleteDonor(this.donorId);
  }

  public int createNewLocalDonation()
  {
    int donationId = IdUtils.generateId();
    
    Donation toCreate = new Donation(
        donationId,
        DonationDomain.LOCAL,
        "" + donationId,
        DonationBidState.PENDING,
        DonationReadState.PENDING,
        DonationCommentState.PENDING,
        BigDecimal.ZERO,
        new Date(),
        this.getData(),
        "");
    
    this.getData().getDonations().add(toCreate);
    this.donations.insertDonation(toCreate);
    
    return donationId;
  }

  public boolean allowEmailUpdate()
  {
    Donor donor = this.getData();
    
    for (Donation d : donor.getDonations())
    {
      if (d.getDomain() == DonationDomain.CHIPIN)
      {
        return false;
      }
    }
    
    return true;
  }
}
