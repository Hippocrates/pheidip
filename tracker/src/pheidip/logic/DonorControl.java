package pheidip.logic;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import pheidip.db.DonationData;
import pheidip.db.DonorData;
import pheidip.objects.Donation;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationDomain;
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
    int id = IdUtils.generateId();
    donationDatabase.getDataAccess().getDonorData().createDonor(new Donor(id, null, null, null, null));
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
    this.donors.updateDonor(new Donor(this.donorId, StringUtils.nullIfEmpty(email), StringUtils.nullIfEmpty(alias), firstName, lastName));
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
