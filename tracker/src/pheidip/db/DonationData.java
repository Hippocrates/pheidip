package pheidip.db;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import pheidip.objects.ChallengeBid;
import pheidip.objects.ChoiceBid;
import pheidip.objects.Donation;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationReadState;
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

	public List<ChallengeBid> getChallengeBidsByDonationId(int donationId);

	public List<ChoiceBid> getChoiceBidsByDonationId(int donationId);

	public void attachChallengeBid(ChallengeBid created);

	public void attachChoiceBid(ChoiceBid created);

	public void updateChallengeBidAmount(int challengeBidId,
			BigDecimal newAmount);

	public void updateChoiceBidAmount(int choiceBidId, BigDecimal newAmount);

	public void removeChallengeBid(int challengeBidId);

	public void removeChoiceBid(int choiceBidId);

	public void setDonationReadState(int donationId, DonationReadState readState);

	public List<Donation> getDonationsInTimeRange(Date lo, Date hi);

	public List<Donation> getAllDonations();

	public List<Donation> searchDonations(DonationSearchParams params);
}