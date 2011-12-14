package pheidip.objects;

import pheidip.util.FilterFunction;
import pheidip.util.StringUtils;

public class PrizeSearchParams implements FilterFunction<Prize>
{
  public String name;
  public boolean excludeIfWon;

  public PrizeSearchParams(String name, boolean excludeIfWon)
  {
    this.name = name;
    this.excludeIfWon = excludeIfWon;
  }

  @Override
  public boolean predicate(Prize x)
  {
    return StringUtils.innerStringMatch(x.getName(), this.name)
      && excludeIfWon ? (x.getWinner() == null) : true;
  }
}
