package pheidip.db;

import java.util.List;

import pheidip.objects.Donor;
import pheidip.objects.SearchEntity;

public interface DonorData 
{

	public Donor getDonorById(int donorId);

	public List<Donor> getAllDonors();

	public List<Donor> getDonorsWithoutPrizes();

	public void deleteDonor(Donor id);

	public void createDonor(Donor newDonor);
	
	public void insertMultipleDonors(List<Donor> toInsert);

	public void updateDonor(Donor donor);

	public void deleteAllDonors();

	public List<Donor> searchDonors(SearchEntity<Donor> params);

  public List<Donor> searchDonorsRange(SearchEntity<Donor> params, int searchOffset, int searchSize);
}