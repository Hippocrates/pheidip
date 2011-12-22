package pheidip.main;

import pheidip.ui.MainWindow;
import pheidip.ui.UIConfiguration;

public class TrackerUI
{
  public static void main(String[] args)
  {
    UIConfiguration.setDefaultConfiguration();
    
    MainWindow window = new MainWindow();
    
    window.setVisible(true);
  }
}
