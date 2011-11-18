package pheidip.db;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import pheidip.objects.Donation;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationSearchParams;

public interface DonationData {

	public Donation getDonationById(int id);

	public List<Donation> getDonorDonations(int donorId);

	public BigDecimal getDonorDonationTotal(int id);

	public void setDonationComment(int id, String comment);

	public void deleteDonation(int id);

	public void setDonationBidState(int id, DonationBidState bidState);

	public void setDonationAmount(int donationId, BigDecimal amount);

	public void insertDonation(Donation d);

	public void updateDonation(Donation updated);

	public Donation getDonationByDomainId(DonationDomain domain, String domainId);

	public void updateChallengeBidAmount(int challengeBidId,
			BigDecimal newAmount);

	public void updateChoiceBidAmount(int choiceBidId, BigDecimal newAmount);

	public List<Donation> getDonationsInTimeRange(Date lo, Date hi);

	public List<Donation> getAllDonations();

	public List<Donation> searchDonations(DonationSearchParams params);

  public void insertMultipleDonations(List<Donation> donationsToInsert);
  public void updateMultiplDonations(List<Donation> donationsToUpdate);
}