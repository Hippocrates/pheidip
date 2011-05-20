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
    initialize();
  }

  private void initialize()
  {
    setTitle("Connect to Database...");
    
    setBounds(100, 100, 389, 235);
    SpringLayout springLayout = new SpringLayout();
    getContentPane().setLayout(springLayout);
    
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
