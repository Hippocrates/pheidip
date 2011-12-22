package pheidip.main;

import java.util.List;

import pheidip.logic.DonationDatabaseManager;
import pheidip.logic.gdocs.GoogleSpreadSheetLoginManager;
import pheidip.logic.gdocs.GoogleSpreadSheetReader;
import pheidip.logic.gdocs.MarathonSpreadsheetEntry;
import pheidip.objects.SpeedRun;

public class RetreiveGoogleSpreadSheet
{
  public static void main(String[] args)
  {
    DonationDatabaseManager manager = new DonationDatabaseManager();
    
    manager.createMemoryDatabase();
    
    GoogleSpreadSheetLoginManager login = new GoogleSpreadSheetLoginManager();
    
    login.logIn("sdatracker@gmail.com", "hq3ct0ak", "0Alb3Dj0u13H7dG1BTS1mdGVHcXZudUE0SXk5dEk2LXc");
    
    if (login.isLoggedIn())
    {
      List<MarathonSpreadsheetEntry> entries = login.retrieveSpreadSheetEntries();
      
      for (MarathonSpreadsheetEntry entry : entries)
      {
        System.out.println(entry.getStartTime().toString());
        System.out.println(entry.getGameName());
        System.out.println(entry.getRunners().toString());
        System.out.println(entry.getEstimatedFinish().toString());
      }
      
      GoogleSpreadSheetReader.mergeRuns(manager, entries, true);
    }
    
    List<SpeedRun> allRuns = manager.getDataAccess().getSpeedRuns().getAllSpeedRuns();
    
    System.out.println(allRuns.toString());
  }
}
