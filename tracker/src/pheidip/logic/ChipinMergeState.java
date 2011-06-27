package pheidip.logic;

public enum ChipinMergeState
{
  IDLE(true),
  RETRIEVING(true),
  EXTRACTING(true),
  COMPARING(true),
  MERGING(true),
  COMPLETED(false),
  CANCELLED(false), 
  FAILED(false);
  
  private boolean isRunningState;

  ChipinMergeState(boolean isRunningState)
  {
    this.isRunningState = isRunningState;
  }
  
  public boolean isRunningState()
  {
    return isRunningState;
  }
  
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
