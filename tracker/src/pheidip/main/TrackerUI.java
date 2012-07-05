package pheidip.main;

import java.util.TimeZone;

import pheidip.ui.MainWindow;
import pheidip.ui.UIConfiguration;

public class TrackerUI
{
  public static void main(String[] args)
  {
    System.out.println(TimeZone.getDefault().getDisplayName());

    UIConfiguration.setDefaultConfiguration();
    
    MainWindow window = new MainWindow();
    
    window.setVisible(true);
  }
}
