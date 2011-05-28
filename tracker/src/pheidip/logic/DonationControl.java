package pheidip.logic;

import java.math.BigDecimal;

import pheidip.db.DonationData;
import pheidip.db.DonorData;
import pheidip.objects.Donation;
import pheidip.objects.DonationAnnounceState;
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

  public void updateComment(String comment)
  {
    this.donations.setDonationComment(this.donationId, StringUtils.nullIfEmpty(comment));
  }

  public void updateAmount(BigDecimal amount)
  {
    Donation d = this.getData();
    
    if (d.getDomain() == DonationDomain.LOCAL)
    {
      this.donations.setDonationAmount(this.donationId, amount);
    }
    else
    {
      throw new RuntimeException("Trying to modify the amount of a non-local donation.");
    }
  }
  
  public static boolean considerAsRead(Donation d)
  {
    if (d.getComment() == null)
    {
      return d.getAnnounceState() == DonationAnnounceState.AMOUNT_READ;
    }
    else
    {
      return d.getAnnounceState() == DonationAnnounceState.COMMENT_READ;
    }
  }
  
  public void markAsRead(boolean state)
  {
    Donation d = this.getData();
    
    if (state)
    {
      if (d.getComment() == null)
      {
        this.donations.setDonationAnnounceState(this.donationId, DonationAnnounceState.AMOUNT_READ);
      }
      else
      {
        this.donations.setDonationAnnounceState(this.donationId, DonationAnnounceState.COMMENT_READ);
      }
    }
    else
    {
      this.donations.setDonationAnnounceState(this.donationId, DonationAnnounceState.UNREAD);
    }
  }
}
