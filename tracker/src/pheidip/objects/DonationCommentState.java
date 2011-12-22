package pheidip.objects;

import pheidip.util.StringUtils;

public enum DonationCommentState
{
  PENDING,
  DENIED,
  APPROVED,
  FLAGGED;
  
  private static DonationCommentState[] _list = DonationCommentState.values();
  
  public static DonationCommentState get(int i)
  {
    return _list[i];
  }
  
  @Override
  public String toString()
  {
    return StringUtils.symbolToNatural(super.toString());
  }
}
