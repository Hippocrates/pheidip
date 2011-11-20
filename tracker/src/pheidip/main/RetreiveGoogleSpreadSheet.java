package pheidip.main;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.helper.HttpConnection;

public class RetreiveGoogleSpreadSheet
{
  public static void main(String[] args)
  {
    String url = "https://spreadsheets.google.com/feeds/list/0Alb3Dj0u13H7dG1BTS1mdGVHcXZudUE0SXk5dEk2LXc/1/private/full";
    Connection loginConnection = HttpConnection.connect(url);
    
    loginConnection.method(Method.GET);
    
    try
    {
      Response result = loginConnection.execute();
      System.out.println(result.body());
    } 
    catch (IOException e)
    {
      System.out.println("Failed: " + e.getMessage());
    }
  }
}
