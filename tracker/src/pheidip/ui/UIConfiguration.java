package pheidip.ui;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UIConfiguration 
{
	public static void setDefaultConfiguration()
	{
	  UIConfiguration.setSwingLookAndFeel();
	}
	
	private static void setSwingLookAndFeel()
	{
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (UnsupportedLookAndFeelException e)
    {
      System.out.println("Could not load system UI Look and Feel, reverting to default: " + e.getMessage());
    } 
    catch (ClassNotFoundException e)
    {
      System.out.println(e.getMessage());
    } 
    catch (InstantiationException e) 
    {
      System.out.println(e.getMessage());
    } 
    catch (IllegalAccessException e) 
    {
      System.out.println(e.getMessage());
    }
	}
}
