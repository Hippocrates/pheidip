package pheidip.logic;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;

public class ChipinLoginManager
{
  private static final String LOGIN_NAME_INPUT_ID = "loginEmail";
  private static final String LOGIN_PASSWORD_INPUT_ID = "loginPassword";

  private static final String SITE_BASE_URL = "http://www.chipin.com";
  private static final String LOGIN_POST_PAGE = "/loginsubmit";
  private static final String LOGOUT_PAGE = "/logout";
  private static final String CHIPIN_PAGE_BASE = "/contributors/private/id/";
  private static final String SUCCESSFUL_LOGIN_TARGET = "/dashboard";
  
  private static final String SESSION_ID_COOKIE_NAME = "JSESSIONID";
  
  private String loginName;
  private String password;
  private String chipinId;
  private String sessionId;
  
  
  public ChipinLoginManager()
  {
    this.sessionId = null;
  }
  
  public boolean isLoggedIn()
  {
    return (this.sessionId != null);
  }
  
  private void retryLogin()
  {
    Connection loginConnection = HttpConnection.connect(SITE_BASE_URL + LOGIN_POST_PAGE);
    
    loginConnection.data(LOGIN_NAME_INPUT_ID, this.loginName);
    loginConnection.data(LOGIN_PASSWORD_INPUT_ID, this.password);
    loginConnection.method(Method.POST);
    
    try
    {
      Response result = loginConnection.execute();
      
      if (result.statusCode() == 200 && result.url().getFile().equals(SUCCESSFUL_LOGIN_TARGET))
      {
        this.sessionId = result.cookie(SESSION_ID_COOKIE_NAME);
      }
      else
      {
        this.cleanFields();
      }
    } 
    catch (IOException e)
    {
      this.cleanFields();
      
      throw new RuntimeException(e);
    }
  }
 
  // so insecure...
  public void logIn(String loginName, String password, String chipinId)
  {
    this.loginName = loginName;
    this.password = password;
    this.chipinId = chipinId;
    
    this.retryLogin();
  }
  
  private void cleanFields()
  {
    this.loginName = null;
    this.password = null;
    this.sessionId = null;
    this.chipinId = null;
  }
  
  public void logOut()
  {
    if (this.isLoggedIn())
    {
      Connection logoutConnection = HttpConnection.connect(SITE_BASE_URL + LOGOUT_PAGE);
      
      logoutConnection.cookie(SESSION_ID_COOKIE_NAME, this.sessionId);
      logoutConnection.method(Method.GET);
      //logoutConnection.followRedirects(false);
      
      this.cleanFields();
      
      try
      {
        logoutConnection.execute();
      } 
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }
  }
  
  public Document getChipinPage()
  {
    if (!this.isLoggedIn())
    {
      return null;
    }
    
    Connection pageConnection = HttpConnection.connect(SITE_BASE_URL + CHIPIN_PAGE_BASE + this.chipinId);
    
    pageConnection.cookie(SESSION_ID_COOKIE_NAME, this.sessionId);
    pageConnection.method(Method.GET);
    pageConnection.timeout(1000000);
    
    Response response;
    
    try
    {
      response = pageConnection.execute();

      if (response.statusCode() == 200)
      {
        return response.parse();
      }
      else
      {
        this.logOut();
        return null;
      }
    } 
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
