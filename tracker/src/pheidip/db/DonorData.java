package pheidip.db;

import java.util.List;

import pheidip.objects.Donor;
import pheidip.objects.DonorSearchParams;

public interface DonorData 
{

	public Donor getDonorById(int donorId);

	public Donor getDonorByEmail(String email);

	public Donor getDonorByAlias(String alias);

	public List<Donor> getAllDonors();

	public List<Donor> getDonorsWithoutPrizes();

	public void deleteDonor(Donor id);

	public void createDonor(Donor newDonor);
	
	public void insertMultipleDonors(List<Donor> toInsert);

	public void updateDonor(Donor donor);

	public void deleteAllDonors();

	public List<Donor> searchDonors(DonorSearchParams params);
}