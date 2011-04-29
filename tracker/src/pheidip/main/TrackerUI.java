package pheidip.main;

import javax.swing.JPanel;

import pheidip.ui.MainWindow;
import pheidip.ui.UIConfiguration;

public class TrackerUI
{
  public static void main(String[] args)
  {
    UIConfiguration.setDefaultConfiguration();
    
    MainWindow window = new MainWindow();
    
    for (int i = 0; i < 20; ++i)
    {
      JPanel x = new JPanel();
      x.setName("title" + i);
      window.insertTab(x);
      x.setName("titl" + i);
    }
    
    window.setVisible(true);
  }
}
