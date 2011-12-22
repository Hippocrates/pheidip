package pheidip.ui;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class DatabaseServerConnectPanel extends JPanel
{
  private JTextField serverURLBox;
  private JTextField databaseNameBox;
  private JTextField userNameBox;
  private JPasswordField passwordBox;

  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{0, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 1.0};
    gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0};
    setLayout(gridBagLayout);
    
    JLabel lblServerUrl = new JLabel("Server URL:");
    GridBagConstraints gbc_lblServerUrl = new GridBagConstraints();
    gbc_lblServerUrl.insets = new Insets(0, 0, 5, 5);
    gbc_lblServerUrl.anchor = GridBagConstraints.EAST;
    gbc_lblServerUrl.gridx = 0;
    gbc_lblServerUrl.gridy = 0;
    add(lblServerUrl, gbc_lblServerUrl);
    
    serverURLBox = new JTextField();
    serverURLBox.setEditable(true);
    GridBagConstraints gbc_serverURLBox = new GridBagConstraints();
    gbc_serverURLBox.insets = new Insets(0, 0, 5, 0);
    gbc_serverURLBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_serverURLBox.gridx = 1;
    gbc_serverURLBox.gridy = 0;
    add(serverURLBox, gbc_serverURLBox);
    
    JLabel lblDatabaseName = new JLabel("Database Name:");
    GridBagConstraints gbc_lblDatabaseName = new GridBagConstraints();
    gbc_lblDatabaseName.anchor = GridBagConstraints.EAST;
    gbc_lblDatabaseName.insets = new Insets(0, 0, 5, 5);
    gbc_lblDatabaseName.gridx = 0;
    gbc_lblDatabaseName.gridy = 1;
    add(lblDatabaseName, gbc_lblDatabaseName);
    
    databaseNameBox = new JTextField();
    databaseNameBox.setEditable(true);
    GridBagConstraints gbc_databaseNameBox = new GridBagConstraints();
    gbc_databaseNameBox.insets = new Insets(0, 0, 5, 0);
    gbc_databaseNameBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_databaseNameBox.gridx = 1;
    gbc_databaseNameBox.gridy = 1;
    add(databaseNameBox, gbc_databaseNameBox);
    
    JLabel lblUserName = new JLabel("User Name:");
    GridBagConstraints gbc_lblUserName = new GridBagConstraints();
    gbc_lblUserName.anchor = GridBagConstraints.EAST;
    gbc_lblUserName.insets = new Insets(0, 0, 5, 5);
    gbc_lblUserName.gridx = 0;
    gbc_lblUserName.gridy = 2;
    add(lblUserName, gbc_lblUserName);
    
    userNameBox = new JTextField();
    GridBagConstraints gbc_userNameBox = new GridBagConstraints();
    gbc_userNameBox.insets = new Insets(0, 0, 5, 0);
    gbc_userNameBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_userNameBox.gridx = 1;
    gbc_userNameBox.gridy = 2;
    add(userNameBox, gbc_userNameBox);
    
    JLabel lblPassword = new JLabel("Password");
    GridBagConstraints gbc_lblPassword = new GridBagConstraints();
    gbc_lblPassword.anchor = GridBagConstraints.EAST;
    gbc_lblPassword.insets = new Insets(0, 0, 0, 5);
    gbc_lblPassword.gridx = 0;
    gbc_lblPassword.gridy = 3;
    add(lblPassword, gbc_lblPassword);
    
    passwordBox = new JPasswordField();
    GridBagConstraints gbc_passwordField = new GridBagConstraints();
    gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
    gbc_passwordField.gridx = 1;
    gbc_passwordField.gridy = 3;
    add(passwordBox, gbc_passwordField);
  }
  
  private void initializeGUIEvents()
  {
    // empty (for now)
  }
  
  /**
   * Create the panel.
   */
  public DatabaseServerConnectPanel()
  {
    this.initializeGUI();
    this.initializeGUIEvents();
  }
  
  public String getServerURL()
  {
    return emptyIfNull(this.serverURLBox.getText());
  }
  
  public String getDBName()
  {
    return emptyIfNull(this.databaseNameBox.getText());
  }
  
  public String getUserName()
  {
    return emptyIfNull(this.userNameBox.getText());
  }
  
  // so very insecure...
  public String getPassword()
  {
    return emptyIfNull(new String(this.passwordBox.getPassword()));
  }
  
  public static String emptyIfNull(Object input)
  {
    return (input == null) ? "" : input.toString();
  }

}
