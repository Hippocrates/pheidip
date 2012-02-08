package pheidip.main;

import javax.swing.JFrame;

import pheidip.logic.DonationDatabaseManager;
import pheidip.ui.DatabaseConnectDialog;
import pheidip.ui.UIConfiguration;

public class AddGrabBag
{
  public static void main(String[] args) throws java.io.IOException
  {
    UIConfiguration.setDefaultConfiguration();

    DonationDatabaseManager db = new DonationDatabaseManager();
    
    DatabaseConnectDialog dialog = new DatabaseConnectDialog(null, db);
    dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    dialog.setVisible(true);
    
    if (db.isConnected())
    {
      /*
      StringBuffer fileData = new StringBuffer(1000);
      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
      char[] buf = new char[1024];

      String line = null;
      
      while((line = reader.readLine()) != null)
      {
          String date = line.substring(0, line.indexOf(';'));

      }
      reader.close();
      */
      
    }
  }
}
