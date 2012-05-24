package pheidip.objects;

import pheidip.util.StringUtils;

public enum PrizeDrawMethod
{
  RANDOM_UNIFORM_DRAW,
  RANDOM_WEIGHTED_DRAW,
  SINGLE_HIGHEST_DONATION,
  HIGHEST_SUM_DONATIONS;
  
  private static PrizeDrawMethod[] _list = PrizeDrawMethod.values();
  
  public static PrizeDrawMethod get(int i)
  {
    return _list[i];
  }
  
  @Override
  public String toString()
  {
    return StringUtils.symbolToNatural(super.toString());
  }
}
