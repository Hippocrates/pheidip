package pheidip.objects;

import pheidip.util.StringUtils;

public enum DonationBidState
{
  PENDING,
  IGNORED,
  PROCESSED,
  FLAGGED;

  private static DonationBidState[] _list = DonationBidState.values();
  
  public static DonationBidState get(int i)
  {
    return _list[i];
  }
  
  @Override
  public String toString()
  {
    return StringUtils.symbolToNatural(super.toString());
  }
}
