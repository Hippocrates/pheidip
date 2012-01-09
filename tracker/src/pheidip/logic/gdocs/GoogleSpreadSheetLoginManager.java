package pheidip.logic.gdocs;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pheidip.util.Reporter;
import pheidip.util.StringUtils;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class GoogleSpreadSheetLoginManager
{
  private static final String LIST_FEED_URL_FORMAT = "https://spreadsheets.google.com/feeds/list/%s/od6/private/full";
  private static final String GOOGLE_APP_NAME = "sda-donationtracker-1.0";
  
  private static final String DATE_FIELD_TAG = "dateandtimeestgmt-5";
  private static final String GAME_FIELD_TAG = "gamecategoryifapplicable";
  private static final String RUNNERS_FIELD_TAG = "runners";
  private static final String ESTIMATE_FIELD_TAG = "estimateincludessetup";
  private static final String COMMENTATORS_FIELD_TAG = "couchcommentators";
  private static final String COMMENTS_FIELD_TAG = "comments";
  private static final String PRIZES_FIELD_TAG = "prizes";
  
  private SpreadsheetService service;
  private String loginName;
  private String password;
  private String spreadSheetId;
  private Reporter reporter;
  
  public GoogleSpreadSheetLoginManager(Reporter reporter)
  {
    this.loginName = null;
    this.password = null;
    this.spreadSheetId = null;
    this.service = null;
    this.reporter = reporter;
  }
  
  public GoogleSpreadSheetLoginManager()
  {
    this(null);
  }
  
  public boolean isLoggedIn()
  {
    return this.service != null;
  }
  
  private void cleanFields()
  {
    this.loginName = null;
    this.password = null;
    this.spreadSheetId = null;
    this.service = null;
  }
  
  public void logIn(String loginName, String password, String spreadSheetId)
  {
    this.loginName = loginName;
    this.password = password;
    this.spreadSheetId = spreadSheetId;
    
    this.retryLogin();
  }
  
  public List<MarathonSpreadsheetEntry> retrieveSpreadSheetEntries()
  {
    if (this.isLoggedIn())
    {
      List<MarathonSpreadsheetEntry> results = new ArrayList<MarathonSpreadsheetEntry>();
      
      try
      {
        ListFeed feed = service.getFeed(new URL(generateListFeedURL(this.spreadSheetId)), ListFeed.class);

        for (ListEntry entry : feed.getEntries()) 
        {
          results.add(parseEntry(entry));
        }
        
        return results;
      }
      catch (MalformedURLException e)
      {
        e.printStackTrace();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      catch (ServiceException e)
      {
        e.printStackTrace();
      }
      catch (ParseException e)
      {
        e.printStackTrace();
      } 
    }

    return null;
  }
  
  private static MarathonSpreadsheetEntry parseEntry(ListEntry entry) throws ParseException
  {
    final SimpleDateFormat dateFormatter = new SimpleDateFormat ("MM/dd/yyyy hh:mm:ss");
    final SimpleDateFormat secondFormatter = new SimpleDateFormat("MM/dd/yyyy");
    dateFormatter.setLenient(true);
    
    Date startTime = new Date(0);
    String gameName = null;
    List<String> runners = new ArrayList<String>();
    long estimatedTime = 0;
    List<String> commentators = new ArrayList<String>();
    String comments = "";
    List<String> prizes = new ArrayList<String>();
    
    for (String tag : entry.getCustomElements().getTags()) 
    {
      final String value = entry.getCustomElements().getValue(tag);
      if (tag.equalsIgnoreCase(DATE_FIELD_TAG))
      {
    	try
    	{
          startTime = dateFormatter.parse(value);
    	}
    	catch (ParseException e)
    	{
          startTime = secondFormatter.parse(value);
    	}
      }
      else if (tag.equalsIgnoreCase(GAME_FIELD_TAG))
      {
        gameName = value;
      }
      else if (tag.equalsIgnoreCase(RUNNERS_FIELD_TAG))
      {
        runners.addAll(Arrays.asList(StringUtils.heuristicSplit(StringUtils.emptyIfNull(value))));
      }
      else if (tag.equalsIgnoreCase(ESTIMATE_FIELD_TAG))
      {
        String[] toks = value.split(":");
        
        if (toks.length == 3)
        {
          try
          {
            // hours:
            estimatedTime = Integer.parseInt(toks[0]);
            estimatedTime *= 60;
            // minutes:
            estimatedTime += Integer.parseInt(toks[1]);
            estimatedTime *= 60;
            // seconds:
            estimatedTime += Integer.parseInt(toks[2]);
            estimatedTime *= 1000;
          }
          catch(NumberFormatException e)
          {
            estimatedTime = 0;
          }
        }
        else
        {
          estimatedTime = 0;
        }
      }
      else if (tag.equalsIgnoreCase(COMMENTATORS_FIELD_TAG))
      {
        commentators.addAll(Arrays.asList(StringUtils.heuristicSplit(StringUtils.emptyIfNull(value))));
      }
      else if (tag.equalsIgnoreCase(COMMENTS_FIELD_TAG))
      {
        comments = value;
      }
      else if (tag.equalsIgnoreCase(PRIZES_FIELD_TAG))
      {
        prizes.addAll(Arrays.asList(StringUtils.heuristicSplit(StringUtils.emptyIfNull(value))));
      }
    }
    
    return new MarathonSpreadsheetEntry(startTime, gameName, runners, new Date(startTime.getTime() + estimatedTime), commentators, comments, prizes);
  }
  
  private void retryLogin()
  {
    this.service = new SpreadsheetService(GOOGLE_APP_NAME);
    
    try
    {
      service.setUserCredentials(this.loginName, this.password);
    }
    catch (AuthenticationException e)
    {
      this.cleanFields();
      this.reportMessage(e.getMessage());
    }
  }
  
  private static String generateListFeedURL(String key)
  {
    return String.format(LIST_FEED_URL_FORMAT, key);
  }
  
  public void logOut()
  {
    if (this.service != null)
    {
      this.service = null;
      
      this.cleanFields();
    }
  }
  
  private void reportMessage(String toReport)
  {
    if (this.reporter != null)
    {
      this.reporter.report(toReport);
    }
    else
    {
      throw new RuntimeException(toReport);
    }
  }
}
