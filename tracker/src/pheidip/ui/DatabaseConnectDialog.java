package pheidip.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import pheidip.logic.ConnectionType;
import pheidip.logic.DonationDatabaseManager;
import pheidip.util.StringUtils;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

@SuppressWarnings("serial")
public class DatabaseConnectDialog extends JDialog
{
  private ConnectionType currentConnectionType;
  private JButton connectButton;
  private JButton cancelButton;
  private JLabel connectionTypeLabel;
  private JComboBox connectionTypeBox;
  private JPanel connectPanel;
  private DonationDatabaseManager databaseManager;
  private JPanel buttonPanel;
  private JPanel connectionTypePanel;
  private JPanel contentPanel;
  private ActionHandler actionHandler;
  
  private void initializeGUI()
  {
    setTitle("Connect to Database...");
    setBounds(100, 100, 389, 235);
    
    this.contentPanel = new JPanel();
    
    this.setContentPane(this.contentPanel);
    this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    
    this.contentPanel.setLayout(new BorderLayout(10, 10));
    
    buttonPanel = new JPanel();
    this.contentPanel.add(buttonPanel, BorderLayout.SOUTH);
    GridBagLayout gbl_panel = new GridBagLayout();
    gbl_panel.columnWidths = new int[]{0, 73, 73, 0};
    gbl_panel.rowHeights = new int[]{23, 0};
    gbl_panel.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
    gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
    buttonPanel.setLayout(gbl_panel);
    
    connectButton = new JButton("Connect");
    connectButton.setHorizontalAlignment(SwingConstants.LEFT);
    GridBagConstraints gbc_connectionButton = new GridBagConstraints();
    gbc_connectionButton.anchor = GridBagConstraints.NORTHEAST;
    gbc_connectionButton.insets = new Insets(0, 0, 0, 5);
    gbc_connectionButton.gridx = 1;
    gbc_connectionButton.gridy = 0;
    buttonPanel.add(connectButton, gbc_connectionButton);
    getRootPane().setDefaultButton(connectButton);
    
    cancelButton = new JButton("Cancel");
    GridBagConstraints gbc_cancelButton = new GridBagConstraints();
    gbc_cancelButton.anchor = GridBagConstraints.NORTHEAST;
    gbc_cancelButton.gridx = 2;
    gbc_cancelButton.gridy = 0;
    buttonPanel.add(cancelButton, gbc_cancelButton);
    
    connectionTypePanel = new JPanel();
    this.contentPanel.add(connectionTypePanel, BorderLayout.NORTH);
    GridBagLayout gbl_panel_1 = new GridBagLayout();
    gbl_panel_1.columnWidths = new int[]{166, 85, 28, 0};
    gbl_panel_1.rowHeights = new int[]{20, 0};
    gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
    gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
    connectionTypePanel.setLayout(gbl_panel_1);
    
    connectionTypeLabel = new JLabel("Connection Type:");
    connectionTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);
    GridBagConstraints gbc_connectionTypeLabel = new GridBagConstraints();
    gbc_connectionTypeLabel.anchor = GridBagConstraints.EAST;
    gbc_connectionTypeLabel.insets = new Insets(0, 0, 0, 5);
    gbc_connectionTypeLabel.gridx = 0;
    gbc_connectionTypeLabel.gridy = 0;
    connectionTypePanel.add(connectionTypeLabel, gbc_connectionTypeLabel);
    
    connectionTypeBox = new JComboBox(ConnectionType.values());
    GridBagConstraints gbc_connectionTypeBox = new GridBagConstraints();
    gbc_connectionTypeBox.insets = new Insets(0, 0, 0, 5);
    gbc_connectionTypeBox.anchor = GridBagConstraints.NORTHWEST;
    gbc_connectionTypeBox.gridx = 1;
    gbc_connectionTypeBox.gridy = 0;
    connectionTypePanel.add(connectionTypeBox, gbc_connectionTypeBox);
  }
  
  private class ActionHandler implements ActionListener
  {
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == connectButton)
        {
          DatabaseConnectDialog.this.connectButtonClicked();
        }
        else if (ev.getSource() == cancelButton)
        {
          DatabaseConnectDialog.this.cancelButtonClicked();
        }
        else if (ev.getSource() == connectionTypeBox)
        {
          DatabaseConnectDialog.this.connectionTypeChanged();
        }
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(DatabaseConnectDialog.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    connectButton.addActionListener(this.actionHandler);
    cancelButton.addActionListener(this.actionHandler);
    connectionTypeBox.addActionListener(this.actionHandler);
    
    this.getRootPane().setDefaultButton(this.connectButton);
  }
  
  public DatabaseConnectDialog(JFrame parent, DonationDatabaseManager databaseManager)
  {
    super(parent, true);
    
    this.databaseManager = databaseManager;
    
    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.connectionTypeBox.setSelectedItem(UIConfiguration.getDefaultConnectionType());
  }
  
  private void connectButtonClicked()
  {
    switch(currentConnectionType)
    {
    case MYSQL_SERVER:
      this.connectToSever();
      break;
    case HSQLDB_MEMORY:
      this.createMemoryConnection();
      break;
    case HSQLDB_FILE:
      this.createFileConnection();
      break;
    }
    
    this.closeDialog();
  }
  
  private void createFileConnection()
  {
    DatabaseFileConnectPanel panel = (DatabaseFileConnectPanel) this.connectPanel;

    String initFilename = panel.getDBFileName();
    
    this.databaseManager.openFileDatabase(new File(initFilename));

    if (!this.databaseManager.isConnected())
    {
      JOptionPane.showMessageDialog(this, "Could not create memory connection.", "Failed to connect...", JOptionPane.ERROR_MESSAGE);
    }
    
    this.closeDialog();
  }

  private void connectToSever()
  {
    DatabaseServerConnectPanel panel = (DatabaseServerConnectPanel) this.connectPanel;
    
     this.databaseManager.connectToServer(
          this.currentConnectionType.getDBType(),
          panel.getServerURL(),
          panel.getDBName(),
          panel.getUserName(),
          panel.getPassword());
    
    if (!this.databaseManager.isConnected())
    {
      JOptionPane.showMessageDialog(this, "Could not connect to database server.", "Failed to connect...", JOptionPane.ERROR_MESSAGE);
    }
    
    this.closeDialog();
  }
  
  private void createMemoryConnection()
  {
   DatabaseMemoryConnectPanel panel = (DatabaseMemoryConnectPanel) this.connectPanel;
    
   this.databaseManager.createMemoryDatabase();
     
   String initFilename = panel.getInitializeScriptFilename();
     
   if (!StringUtils.isEmptyOrNull(initFilename))
   {
     this.databaseManager.runSQLScript(initFilename);
   }
   
   if (!this.databaseManager.isConnected())
   {
     JOptionPane.showMessageDialog(this, "Could not create memory connection.", "Failed to connect...", JOptionPane.ERROR_MESSAGE);
   }
   
   this.closeDialog();
  }
  
  private void cancelButtonClicked()
  {
    this.closeDialog();
  }
  
  private void closeDialog()
  {
    this.setVisible(false);
    this.dispose();
  }
  
  private void connectionTypeChanged()
  {
    currentConnectionType = (ConnectionType) this.connectionTypeBox.getSelectedItem();
  
    if (this.connectPanel != null)
    {
      this.remove(this.connectPanel);
    }
    
    switch (currentConnectionType)
    {
    case MYSQL_SERVER:
      this.connectPanel = new DatabaseServerConnectPanel();
      break;
    case HSQLDB_MEMORY:
      this.connectPanel = new DatabaseMemoryConnectPanel();
      break;
    case HSQLDB_FILE:
      this.connectPanel = new DatabaseFileConnectPanel();
      break;
    }
    
    this.contentPanel.add(this.connectPanel, BorderLayout.CENTER);
    this.contentPanel.validate();
  }
}
