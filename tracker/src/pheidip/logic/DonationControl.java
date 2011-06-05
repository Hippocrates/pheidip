package pheidip.logic;

import java.math.BigDecimal;

import pheidip.db.DonationData;
import pheidip.db.DonorData;
import pheidip.objects.Donation;
import pheidip.objects.DonationDomain;
import pheidip.objects.Donor;

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

  public void deleteDonation()
  {
    this.donations.deleteDonation(this.donationId);
  }
}
