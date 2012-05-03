package pheidip.ui;
import java.awt.GridBagLayout;
import javax.swing.JProgressBar;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import pheidip.logic.ExternalProcess;
import pheidip.logic.chipin.ExternalProcessState;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ExternalProcessTab extends TabPanel
{
  private final static int MAXIMUM_PROGRESS = 1000;
  
  private JButton cancelButton;
  private JProgressBar loadingProgressBar;
  private JLabel loadingLabel;
  private ExternalProcess process;
  private ExternalProcessState currentState;
  private Thread loadingThread;

  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{0, 312, 85, 0, 0};
    gridBagLayout.rowHeights = new int[]{90, 28, 0, 0, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    loadingLabel = new JLabel("Loading...");
    loadingLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
    GridBagConstraints gbc_loadingLabel = new GridBagConstraints();
    gbc_loadingLabel.anchor = GridBagConstraints.WEST;
    gbc_loadingLabel.gridwidth = 2;
    gbc_loadingLabel.insets = new Insets(0, 0, 5, 5);
    gbc_loadingLabel.gridx = 1;
    gbc_loadingLabel.gridy = 1;
    add(loadingLabel, gbc_loadingLabel);
    
    loadingProgressBar = new JProgressBar();
    loadingProgressBar.setMaximum(MAXIMUM_PROGRESS);
    GridBagConstraints gbc_loadingProgressBar = new GridBagConstraints();
    gbc_loadingProgressBar.fill = GridBagConstraints.HORIZONTAL;
    gbc_loadingProgressBar.gridwidth = 2;
    gbc_loadingProgressBar.insets = new Insets(0, 0, 5, 5);
    gbc_loadingProgressBar.gridx = 1;
    gbc_loadingProgressBar.gridy = 2;
    add(loadingProgressBar, gbc_loadingProgressBar);
    
    cancelButton = new JButton("Cancel");
    cancelButton.setEnabled(false);
    GridBagConstraints gbc_cancelButton = new GridBagConstraints();
    gbc_cancelButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_cancelButton.insets = new Insets(0, 0, 0, 5);
    gbc_cancelButton.gridx = 2;
    gbc_cancelButton.gridy = 3;
    add(cancelButton, gbc_cancelButton);
  }
  
  private void initializeGUIEvents()
  {
    cancelButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent arg0)
      {
        ExternalProcessTab.this.confirmCancelOperation();
      }
    });
    
    this.process.setListener(new ExternalProcess.ProcessStateCallback()
    {
      @Override
      public void stateChanged(ExternalProcess updated)
      {
        onUpdateMergeState(updated.getState(), updated.getCompletionState(), updated.getStatusMessage());
      }
    });
  }
  
  /**
   * Create the panel.
   */
  public ExternalProcessTab(ExternalProcess process)
  {
    this.process = process;
    
    this.setHeaderText(process.getProcessName());
    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.loadingThread = new Thread(process);
    
    this.loadingThread.start();
  }

  @Override
  public boolean confirmClose()
  {
    if (this.currentState.isRunningState())
    {
      return this.confirmCancelOperation();
    }
    else
    {
      return true;
    }
  }
  
  private boolean confirmCancelOperation()
  {
    if (this.currentState.isRunningState())
    {
      int result = JOptionPane.showConfirmDialog(this, "Cancel merge operation?", "Are you sure you want to cancel this operation?", JOptionPane.YES_NO_OPTION);
      
      if (result == JOptionPane.YES_OPTION)
      {
        this.loadingThread.interrupt();
        return true;
      }
      else
      {
        return false;
      }
    }
    else
    {
      throw new RuntimeException("Error, this button shouldn't be pressable in this state");
    }
  }

  private void onUpdateMergeState(ExternalProcessState newState, double percentage, String message)
  {
    this.currentState = newState;
    this.loadingLabel.setText(message);
    this.loadingProgressBar.setValue((int)(percentage * MAXIMUM_PROGRESS));
    
    if (!this.currentState.isRunningState())
    {
      this.cancelButton.setEnabled(false);
      this.repaint();
    }
    else
    {
      this.cancelButton.setEnabled(true);
      this.repaint();
    }
  }
  
  public ExternalProcess getProcess()
  {
    return this.process;
  }

  @Override
  public void redrawContent()
  {
    // TODO: is this neccessary?
  }
}
