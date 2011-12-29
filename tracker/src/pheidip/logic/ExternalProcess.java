package pheidip.logic;

import pheidip.logic.chipin.ExternalProcessState;

public interface ExternalProcess extends Runnable
{
  public interface ProcessStateCallback
  {
    void stateChanged(ExternalProcess updated);
  }
  
  public void setListener(ProcessStateCallback listener);
  
  public ExternalProcessState getState();
  public String getStatusMessage();
  public double getCompletionState();
  public String getProcessName();
}
