package pheidip.logic;

import java.math.BigDecimal;
import java.util.Date;

public class PrizeAssignParams
{
  public PrizeDrawMethod method;
  public boolean excludeIfAlreadyWon;
  public Date donatedAfter;
  public Date donatedBefore;
  public BigDecimal targetAmount;
}
