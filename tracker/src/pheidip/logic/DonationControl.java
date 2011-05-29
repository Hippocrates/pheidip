package pheidip.logic;

import java.math.BigDecimal;

import pheidip.db.DonationData;
import pheidip.db.DonorData;
import pheidip.objects.Donation;
import pheidip.objects.DonationDomain;
import pheidip.objects.Donor;
import pheidip.util.StringUtils;

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
    this.donations.setDonationComment(this.donationId, StringUtils.nullIfEmpty(comment));
  
    Donation d = this.getData();
    
    if (!this.allowUpdateAmount() && !d.getAmount().equals(amount))
    {
      throw new RuntimeException("Update of amount not allowed on " + StringUtils.symbolToNatural(d.getDomain().toString()) + " donations.");
    }
    
    this.donations.setDonationAmount(this.donationId, amount);
    this.donations.setDonationComment(this.donationId, comment);
  }
  
  public boolean allowUpdateAmount()
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
