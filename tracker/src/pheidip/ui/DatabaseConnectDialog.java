package pheidip.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import pheidip.logic.ConnectionType;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class DatabaseConnectDialog extends JDialog
{
  private ConnectionType currentConnectionType;
  private JButton connectButton;
  private JButton cancelButton;
  private JLabel connectionTypeLabel;
  private JComboBox connectionTypeBox;
  private JPanel connectPanel;

  public static void main(String[] args)
  {
    UIConfiguration.setDefaultConfiguration();
    DatabaseConnectDialog dialog = new DatabaseConnectDialog();
    dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    dialog.setVisible(true);
  }
  
  public DatabaseConnectDialog()
  {
    setTitle("Connect to Database...");
    setBounds(100, 100, 389, 235);
    
    getContentPane().setLayout(new BorderLayout(10, 10));
    
    JPanel buttonsPanel = new JPanel();
    getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    GridBagLayout gbl_panel = new GridBagLayout();
    gbl_panel.columnWidths = new int[]{153, 73, 65, 0};
    gbl_panel.rowHeights = new int[]{23, 0};
    gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
    gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
    buttonsPanel.setLayout(gbl_panel);
    
    connectButton = new JButton("Connect");
    connectButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e) 
      {
        DatabaseConnectDialog.this.connectButtonClicked();
      }
    });
    connectButton.setHorizontalAlignment(SwingConstants.LEFT);
    GridBagConstraints gbc_connectionButton = new GridBagConstraints();
    gbc_connectionButton.anchor = GridBagConstraints.NORTH;
    gbc_connectionButton.insets = new Insets(0, 0, 0, 5);
    gbc_connectionButton.gridx = 0;
    gbc_connectionButton.gridy = 0;
    buttonsPanel.add(connectButton, gbc_connectionButton);
    
    cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e) 
      {
        DatabaseConnectDialog.this.cancelButtonClicked();
      }
    });
    GridBagConstraints gbc_cancelButton = new GridBagConstraints();
    gbc_cancelButton.insets = new Insets(0, 0, 0, 5);
    gbc_cancelButton.anchor = GridBagConstraints.NORTH;
    gbc_cancelButton.gridx = 1;
    gbc_cancelButton.gridy = 0;
    buttonsPanel.add(cancelButton, gbc_cancelButton);
    
    JPanel connectTypePanel = new JPanel();
    getContentPane().add(connectTypePanel, BorderLayout.NORTH);
    GridBagLayout gbl_panel_1 = new GridBagLayout();
    gbl_panel_1.columnWidths = new int[]{166, 85, 28, 0};
    gbl_panel_1.rowHeights = new int[]{20, 0};
    gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
    gbl_panel_1.rowWeights = new double[]{0.0, Double.MIN_VALUE};
    connectTypePanel.setLayout(gbl_panel_1);
    
    connectionTypeLabel = new JLabel("Connection Type:");
    connectionTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);
    GridBagConstraints gbc_connectionTypeLabel = new GridBagConstraints();
    gbc_connectionTypeLabel.anchor = GridBagConstraints.EAST;
    gbc_connectionTypeLabel.insets = new Insets(0, 0, 0, 5);
    gbc_connectionTypeLabel.gridx = 0;
    gbc_connectionTypeLabel.gridy = 0;
    connectTypePanel.add(connectionTypeLabel, gbc_connectionTypeLabel);
    
    connectionTypeBox = new JComboBox(ConnectionType.values());
    connectionTypeBox.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        DatabaseConnectDialog.this.connectionTypeChanged();
      }
    });
    connectionTypeBox.setSelectedItem(UIConfiguration.getDefaultConnectionType());
    
    GridBagConstraints gbc_connectionTypeBox = new GridBagConstraints();
    gbc_connectionTypeBox.insets = new Insets(0, 0, 0, 5);
    gbc_connectionTypeBox.anchor = GridBagConstraints.NORTHWEST;
    gbc_connectionTypeBox.gridx = 1;
    gbc_connectionTypeBox.gridy = 0;
    connectTypePanel.add(connectionTypeBox, gbc_connectionTypeBox);
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
    }
    
    this.closeDialog();
  }
  
  private void connectToSever()
  {
    DatabaseServerConnectPanel panel = (DatabaseServerConnectPanel) this.connectPanel;
    
    // ... need a database logic element to actually do anything...
  }
  
  private void createMemoryConnection()
  {
   DatabaseMemoryConnectPanel panel = (DatabaseMemoryConnectPanel) this.connectPanel;
    
    // ...
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
    }
    
    this.getContentPane().add(this.connectPanel, BorderLayout.CENTER);
    this.getContentPane().validate();
  }
}
