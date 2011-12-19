package pheidip.db;

import java.util.Date;
import java.util.List;

import pheidip.objects.Donation;
import pheidip.objects.DonationBid;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationSearchParams;

public interface DonationData {

	public Donation getDonationById(int id);
	
	public void deleteDonation(Donation d);

	public void insertDonation(Donation d);

	public void updateDonation(Donation updated);

	public void addDonationBid(DonationBid added);
	
	public void updateDonationBid(DonationBid update);
	
	public void deleteDonationBid(DonationBid remove);

	public Donation getDonationByDomainId(DonationDomain domain, String domainId);

	public List<Donation> getDonationsInTimeRange(Date lo, Date hi);

	public List<Donation> getAllDonations();

	public List<Donation> searchDonations(DonationSearchParams params);

  public void insertMultipleDonations(List<Donation> donationsToInsert);
  public void updateMultipleDonations(List<Donation> donationsToUpdate);

  public List<Donation> searchDonationsRange(DonationSearchParams params, int searchOffset,
      int searchSize);
}