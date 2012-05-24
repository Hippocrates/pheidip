package pheidip.objects;

import pheidip.util.StringUtils;

public enum BidState
{
  HIDDEN,
  OPENED,
  CLOSED;
  
  private static BidState[] _list = BidState.values();
  
  public static BidState get(int i)
  {
    return _list[i];
  }
  
  @Override
  public String toString()
  {
    return StringUtils.symbolToNatural(super.toString());
  }
}
