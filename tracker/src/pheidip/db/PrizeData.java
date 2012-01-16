package pheidip.db;

import java.util.List;

import pheidip.objects.Prize;
import pheidip.objects.SearchEntity;

public interface PrizeData {

	public void insertPrize(Prize toAdd);

	public void updatePrize(Prize toUpdate);

	public Prize getPrizeById(int prizeId);

	public List<Prize> getAllPrizes();

	public Prize getPrizeByDonorId(int donorId);

	public void deletePrize(Prize prize);

	public List<Prize> searchPrizes(SearchEntity<Prize> params);

  public List<Prize> searchPrizesRange(SearchEntity<Prize> params, int offset, int size);

  public void multiUpdatePrizes(List<Prize> prizesToUpdate);
}