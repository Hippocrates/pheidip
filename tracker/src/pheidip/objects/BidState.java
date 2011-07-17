package pheidip.objects;

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
}
