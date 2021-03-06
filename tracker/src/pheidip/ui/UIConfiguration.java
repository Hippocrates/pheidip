package pheidip.ui;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import pheidip.logic.ConnectionType;

public class UIConfiguration 
{
	public static void setDefaultConfiguration()
	{
	  UIConfiguration.setSwingLookAndFeel();
	}
	
	public static ConnectionType getDefaultConnectionType()
	{
	  return ConnectionType.MYSQL_SERVER;
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
	
	public static void reportError(Exception e)
	{
	  reportError(e.getMessage());
	}
	
	public static void reportError(String message)
	{
	  JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
