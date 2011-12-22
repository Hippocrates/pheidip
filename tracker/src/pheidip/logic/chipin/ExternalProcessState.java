package pheidip.logic.chipin;

public enum ExternalProcessState
{
  IDLE,
  RUNNING,
  COMPLETED,
  CANCELLED, 
  FAILED;

  
  public boolean isRunningState()
  {
    return this == RUNNING;
  }
  
  private static ExternalProcessState[] _list = ExternalProcessState.values();
  
  public static ExternalProcessState get(int i)
  {
    return _list[i];
  }
  
  public static int size()
  {
    return _list.length;
  }
}
