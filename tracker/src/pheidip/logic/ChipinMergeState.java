package pheidip.logic;

public enum ChipinMergeState
{
  IDLE,
  RETRIEVING,
  EXTRACTING,
  MERGING,
  COMPLETED,
  CANCELLED, 
  FAILED;
  
  private static ChipinMergeState[] _list = ChipinMergeState.values();
  
  public static ChipinMergeState get(int i)
  {
    return _list[i];
  }
  
  public static int size()
  {
    return _list.length;
  }
}
