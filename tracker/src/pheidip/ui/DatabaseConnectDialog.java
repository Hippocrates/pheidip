package pheidip.ui;

import javax.swing.JDialog;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class DatabaseConnectDialog extends JDialog
{
  private JPasswordField passwordField;
  private JComboBox userNameBox;
  private JComboBox serverURLBox;
  private JComboBox dbNameBox;
  private JButton connectButton;
  private JButton cancelButton;
  
  /**
   * Launch the application.
   */
  public static void main(String[] args)
  {
    try
    {
      DatabaseConnectDialog dialog = new DatabaseConnectDialog();
      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      dialog.setVisible(true);
    } 
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Create the dialog.
   */
  public DatabaseConnectDialog()
  {
    setTitle("Connect to Database...");
    
    setBounds(100, 100, 389, 235);
    SpringLayout springLayout = new SpringLayout();
    getContentPane().setLayout(springLayout);
    
    JLabel lblUserName = new JLabel("User Name:");
    springLayout.putConstraint(SpringLayout.WEST, lblUserName, 10, SpringLayout.WEST, getContentPane());
    lblUserName.setName("lblUserName");
    getContentPane().add(lblUserName);
    
    JLabel lblPassword = new JLabel("Password:");
    springLayout.putConstraint(SpringLayout.NORTH, lblPassword, 133, SpringLayout.NORTH, getContentPane());
    springLayout.putConstraint(SpringLayout.SOUTH, lblUserName, -13, SpringLayout.NORTH, lblPassword);
    springLayout.putConstraint(SpringLayout.EAST, lblUserName, 0, SpringLayout.EAST, lblPassword);
    springLayout.putConstraint(SpringLayout.WEST, lblPassword, 10, SpringLayout.WEST, getContentPane());
    lblPassword.setName("lblPassword");
    getContentPane().add(lblPassword);
    
    passwordField = new JPasswordField();
    springLayout.putConstraint(SpringLayout.EAST, lblPassword, -6, SpringLayout.WEST, passwordField);
    passwordField.setName("passwordField");
    getContentPane().add(passwordField);
    passwordField.setColumns(10);
    
    userNameBox = new JComboBox();
    userNameBox.setEditable(true);
    springLayout.putConstraint(SpringLayout.NORTH, passwordField, 6, SpringLayout.SOUTH, userNameBox);
    springLayout.putConstraint(SpringLayout.WEST, passwordField, 0, SpringLayout.WEST, userNameBox);
    springLayout.putConstraint(SpringLayout.EAST, passwordField, 0, SpringLayout.EAST, userNameBox);
    userNameBox.setName("userNameBox");
    getContentPane().add(userNameBox);
    
    serverURLBox = new JComboBox();
    serverURLBox.setEditable(true);
    springLayout.putConstraint(SpringLayout.WEST, userNameBox, 0, SpringLayout.WEST, serverURLBox);
    springLayout.putConstraint(SpringLayout.EAST, userNameBox, 0, SpringLayout.EAST, serverURLBox);
    springLayout.putConstraint(SpringLayout.WEST, serverURLBox, 139, SpringLayout.WEST, getContentPane());
    springLayout.putConstraint(SpringLayout.EAST, serverURLBox, -44, SpringLayout.EAST, getContentPane());
    serverURLBox.setName("serverURLBox");
    getContentPane().add(serverURLBox);
    
    dbNameBox = new JComboBox();
    dbNameBox.setEditable(true);
    springLayout.putConstraint(SpringLayout.NORTH, userNameBox, 6, SpringLayout.SOUTH, dbNameBox);
    springLayout.putConstraint(SpringLayout.NORTH, dbNameBox, 74, SpringLayout.NORTH, getContentPane());
    springLayout.putConstraint(SpringLayout.SOUTH, serverURLBox, -6, SpringLayout.NORTH, dbNameBox);
    springLayout.putConstraint(SpringLayout.WEST, dbNameBox, 0, SpringLayout.WEST, serverURLBox);
    springLayout.putConstraint(SpringLayout.EAST, dbNameBox, 0, SpringLayout.EAST, serverURLBox);
    dbNameBox.setName("dbNameBox");
    getContentPane().add(dbNameBox);
    
    JLabel lblServerUrl = new JLabel("Server URL:");
    springLayout.putConstraint(SpringLayout.WEST, lblServerUrl, 10, SpringLayout.WEST, getContentPane());
    springLayout.putConstraint(SpringLayout.EAST, lblServerUrl, -6, SpringLayout.WEST, serverURLBox);
    lblServerUrl.setName("lblServerUrl");
    getContentPane().add(lblServerUrl);
    
    JLabel lblDatabaseName = new JLabel("Database Name:");
    springLayout.putConstraint(SpringLayout.WEST, lblDatabaseName, 10, SpringLayout.WEST, getContentPane());
    springLayout.putConstraint(SpringLayout.SOUTH, lblDatabaseName, -14, SpringLayout.NORTH, lblUserName);
    springLayout.putConstraint(SpringLayout.EAST, lblDatabaseName, -6, SpringLayout.WEST, dbNameBox);
    springLayout.putConstraint(SpringLayout.SOUTH, lblServerUrl, -14, SpringLayout.NORTH, lblDatabaseName);
    lblDatabaseName.setName("lblDatabaseName");
    getContentPane().add(lblDatabaseName);
    
    connectButton = new JButton("Connect");
    springLayout.putConstraint(SpringLayout.WEST, connectButton, 10, SpringLayout.WEST, getContentPane());
    springLayout.putConstraint(SpringLayout.SOUTH, connectButton, -10, SpringLayout.SOUTH, getContentPane());
    connectButton.setName("connectButton");
    getContentPane().add(connectButton);
    
    cancelButton = new JButton("Cancel");
    springLayout.putConstraint(SpringLayout.NORTH, cancelButton, 0, SpringLayout.NORTH, connectButton);
    springLayout.putConstraint(SpringLayout.WEST, cancelButton, 6, SpringLayout.EAST, connectButton);
    cancelButton.setName("cancelButton");
    getContentPane().add(cancelButton);
  }

}
