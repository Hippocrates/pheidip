package pheidip.objects;

import pheidip.util.StringUtils;

public enum DonationReadState
{
  PENDING,
  IGNORED,
  READ,
  FLAGGED;
  
  private static DonationReadState[] _list = DonationReadState.values();
  
  public static DonationReadState get(int i)
  {
    return _list[i];
  }
  
  @Override
  public String toString()
  {
    return StringUtils.symbolToNatural(super.toString());
  }
}
