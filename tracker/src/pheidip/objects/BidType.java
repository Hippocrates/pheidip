package pheidip.objects;

import pheidip.util.StringUtils;

public enum BidType
{
  CHOICE,
  CHALLENGE;
  
  public String toString()
  {
    return StringUtils.symbolToNatural(super.toString());
  }
}
