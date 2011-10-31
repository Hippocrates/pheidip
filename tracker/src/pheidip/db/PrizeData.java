package pheidip.db;

import java.util.List;

import pheidip.objects.Prize;

public interface PrizeData {

	public void insertPrize(Prize toAdd);

	public void updatePrize(Prize toUpdate);

	public Prize getPrizeById(int prizeId);

	public List<Prize> getAllPrizes();

	public Prize getPrizeByDonorId(int donorId);

	public void setPrizeWinner(int prizeId, int donorId);

	public void removePrizeWinner(int prizeId);

	public void deletePrize(int prizeId);

}