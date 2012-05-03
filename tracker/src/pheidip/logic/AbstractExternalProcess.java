package pheidip.logic;

import pheidip.logic.chipin.ExternalProcessState;

public abstract class AbstractExternalProcess implements ExternalProcess
{
  private ExternalProcessState currentState;
  private double completionState;
  private String statusMessage;
  private ProcessStateCallback listener;

  public AbstractExternalProcess(ProcessStateCallback listener)
  {
    this.currentState = ExternalProcessState.IDLE;
    this.completionState = 0.0;
    this.statusMessage = "Idle";
    
    this.setListener(listener);
  }
  
  protected synchronized void resetState(ExternalProcessState newState, double newPercentage, String newStatus)
  {
    this.currentState = newState;
    this.completionState = newPercentage;
    this.statusMessage = newStatus;
    
    if (this.listener != null)
    {
      this.listener.stateChanged(this);
    }
  }

  @Override
  public synchronized void setListener(ProcessStateCallback listener)
  {
    this.listener = listener;
    this.resetState(this.currentState, this.completionState, this.statusMessage);
  }

  @Override
  public ExternalProcessState getState()
  {
    return this.currentState;
  }

  @Override
  public String getStatusMessage()
  {
    return this.statusMessage;
  }

  @Override
  public double getCompletionState()
  {
    return this.completionState;
  }
}
