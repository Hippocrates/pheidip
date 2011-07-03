package pheidip.logic;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import pheidip.db.DonationData;
import pheidip.db.DonationDataConstraintException;
import pheidip.db.DonorData;
import pheidip.objects.Donation;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationReadState;
import pheidip.objects.Donor;
import pheidip.util.IdUtils;
import pheidip.util.StringUtils;

public class DonorControl
{
  private DonationDatabaseManager donationDatabase;
  private DonorData donors;
  private DonationData donations;
  private int donorId;
  
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
  }
  
  public int getDonorId()
  {
    return this.donorId;
  }
  
  public Donor getData()
  {
    return this.donors.getDonorById(this.donorId);
  }

  public List<Donation> getDonorDonations()
  {
    return this.donations.getDonorDonations(this.donorId);
  }

  public BigDecimal getTotalDonated()
  {
    return this.donations.getDonorDonationTotal(this.donorId);
  }

  public void updateData(String email, String alias, String firstName, String lastName)
  {
    try
    {
      this.donors.updateDonor(new Donor(this.donorId, StringUtils.nullIfEmpty(email), StringUtils.nullIfEmpty(alias), firstName, lastName));
    }
    catch(DonationDataConstraintException e)
    {
      this.donationDatabase.reportMessage(e.getMessage());
    }
  }
  
  public void deleteDonor()
  {
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
        BigDecimal.ZERO,
        new Date(),
        this.donorId,
        "");
    
    this.donations.insertDonation(toCreate);
    
    return donationId;
  }

  public boolean allowEmailUpdate()
  {
    List<Donation> donations = this.getDonorDonations();
    
    for (Donation d : donations)
    {
      if (d.getDomain() == DonationDomain.CHIPIN)
      {
        return false;
      }
    }
    
    return true;
  }
}
