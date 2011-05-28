package pheidip.logic;

import java.math.BigDecimal;
import java.util.List;

import pheidip.db.DonationData;
import pheidip.db.DonorData;
import pheidip.objects.Donation;
import pheidip.objects.Donor;

public class DonorControl
{
  private DonationDatabaseManager donationDatabase;
  private DonorData donors;
  private DonationData donations;
  private int donorId;

  public DonorControl(DonationDatabaseManager donationDatabase, int donorId)
  {
    this.donationDatabase = donationDatabase;
    this.donors = this.donationDatabase.getDataAccess().getDonorData();
    this.donations = this.donationDatabase.getDataAccess().getDonationData();
    this.donorId = donorId;
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
    this.donors.updateDonor(new Donor(this.donorId, email, alias, firstName, lastName));
  }
}
