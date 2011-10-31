package pheidip.db;

import java.util.List;

import pheidip.objects.Donor;

public interface DonorData 
{

	public Donor getDonorById(int donorId);

	public Donor getDonorByEmail(String email);

	public Donor getDonorByAlias(String alias);

	public List<Donor> getAllDonors();

	public List<Donor> getDonorsWithoutPrizes();

	public void deleteDonor(int id);

	public void createDonor(Donor newDonor);

	public void updateDonor(Donor donor);

	public Donor getPrizeWinner(int prizeId);

	public void deleteAllDonors();

}