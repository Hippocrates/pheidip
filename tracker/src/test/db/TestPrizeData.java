package test.db;

import pheidip.db.PrizeData;
import pheidip.db.SpeedRunData;
import pheidip.objects.Prize;
import pheidip.objects.PrizeDrawMethod;

public class TestPrizeData extends DonationDatabaseTest
{
  private PrizeData prizes;
  private SpeedRunData speedRuns;

  public void setUp()
  {
    super.setUp();
    this.prizes = this.getDataAccess().getPrizeData();
    this.speedRuns = this.getDataAccess().getSpeedRunData();
  }
  
  public void testGetPrize()
  {
    Prize p = this.prizes.getPrizeById(3);
    
    assertNotNull(p);
    
    assertEquals(3, p.getId());
    assertEquals("one more prize", p.getName());
    assertEquals("sample@url.com", p.getImageURL());
    assertEquals(2, p.getStartGame().getId());
    assertEquals(3, p.getEndGame().getId());
  }
  
  public void comparePrizes(Prize a, Prize b)
  {
    assertEquals(a.getId(), b.getId());
    assertEquals(a.getName(), b.getName());
    assertEquals(a.getImageURL(), b.getImageURL());
    assertEquals(a.getDescription(), b.getDescription());
    assertEquals(a.getSortKey(), b.getSortKey());
    assertEquals(a.getStartGame(), b.getStartGame());
    assertEquals(a.getEndGame(), b.getEndGame());
    assertEquals(a.getDrawMethod(), b.getDrawMethod());
    assertEquals(a.getWinner(), b.getWinner());
  }
  
  public void testCreatePrize()
  {
    Prize p = new Prize();
    p.setId(4);
    p.setName("a name");
    p.setDrawMethod(PrizeDrawMethod.RANDOM_WEIGHTED_DRAW);
    p.setStartGame(this.speedRuns.getSpeedRunById(1));
    p.setEndGame(this.speedRuns.getSpeedRunById(1));
    p.setDescription("A Description.");
    p.setImageURL("a.url@somewhere.org");
    p.setSortKey(66);
    
    this.prizes.insertPrize(p);
    
    Prize result = this.prizes.getPrizeById(p.getId());
    
    assertNotNull(result);
    
    comparePrizes(p, result);
  }
  
  public void testUpdatePrize()
  {
    Prize p = this.prizes.getPrizeById(3);
    
    assertNotNull(p);
    
    p.setName("a name");
    p.setDrawMethod(PrizeDrawMethod.RANDOM_WEIGHTED_DRAW);
    p.setStartGame(this.speedRuns.getSpeedRunById(1));
    p.setEndGame(this.speedRuns.getSpeedRunById(1));
    p.setDescription("A Description.");
    p.setImageURL("a.url@somewhere.org");
    p.setSortKey(66);
    
    this.prizes.updatePrize(p);
    
    Prize result = this.prizes.getPrizeById(p.getId());
    
    comparePrizes(p, result);
  }
  
  public void testDeletePrize()
  {
    Prize p = this.prizes.getPrizeById(3);
    
    this.prizes.deletePrize(p);
    
    assertNull(this.prizes.getPrizeById(p.getId()));
  }
  
  public void testGetPrizeByWinner()
  {
    Prize p = this.prizes.getPrizeByDonorId(2);
    
    assertEquals(1, p.getId());
  }
}
