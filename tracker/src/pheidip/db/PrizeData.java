package pheidip.db;

import java.util.List;

import pheidip.objects.Prize;
import pheidip.objects.PrizeSearchParams;

public interface PrizeData {

	public void insertPrize(Prize toAdd);

	public void updatePrize(Prize toUpdate);

	public Prize getPrizeById(int prizeId);

	public List<Prize> getAllPrizes();

	public Prize getPrizeByDonorId(int donorId);

	public void deletePrize(Prize prize);

	public List<Prize> searchPrizes(PrizeSearchParams params);

  public List<Prize> searchPrizesRange(PrizeSearchParams params, int offset, int size);
}