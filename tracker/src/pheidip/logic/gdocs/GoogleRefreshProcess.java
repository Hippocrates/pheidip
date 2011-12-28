package pheidip.logic.gdocs;

import java.util.List;
import java.util.Map;

import pheidip.db.SpeedRunData;
import pheidip.logic.AbstractExternalProcess;
import pheidip.logic.DonationDatabaseManager;
import pheidip.logic.chipin.ExternalProcessState;
import pheidip.objects.SpeedRun;

public class GoogleRefreshProcess extends AbstractExternalProcess
{
  private DonationDatabaseManager database;
  private GoogleSpreadSheetLoginManager loginManager;
  
  private boolean intializationMode = false;

  public GoogleRefreshProcess(DonationDatabaseManager database, GoogleSpreadSheetLoginManager loginManager, boolean intializationMode)
  {
    this(database, loginManager, null);
    
    this.intializationMode = intializationMode;
  }
  
  public GoogleRefreshProcess(DonationDatabaseManager database, GoogleSpreadSheetLoginManager loginManager, ProcessStateCallback listener)
  {
    super(listener);
    
    this.database = database;
    this.loginManager = loginManager;
  }

  @Override
  public void run()
  {
    try
    {
      if (this.loginManager.isLoggedIn())
      {
        this.resetState(ExternalProcessState.RUNNING, 0.1, "Initializing");
        Thread.sleep(0);
        
        List<MarathonSpreadsheetEntry> entries = this.loginManager.retrieveSpreadSheetEntries();
  
        this.resetState(ExternalProcessState.RUNNING, 0.3, "Reading current database state");
        Thread.sleep(0);
        
        SpeedRunData speedRuns = database.getDataAccess().getSpeedRuns();
        
        List<SpeedRun> allRuns = speedRuns.getAllSpeedRuns();
        Map<String, SpeedRun> mappedRuns = GoogleSpreadSheetReader.nameMapRuns(allRuns);
        
        double processIncrement = (1.0 - 0.4) / entries.size();
        double processPercentage = 0.4;
        
        int currentIndex = 0;
        
        for (MarathonSpreadsheetEntry entry : entries)
        {
          this.resetState(ExternalProcessState.RUNNING, processPercentage, "Pushing run information");
          Thread.sleep(0);
          
          GoogleSpreadSheetReader.mergeRun(speedRuns, entry, mappedRuns, currentIndex, intializationMode);
          
          processPercentage += processIncrement;
          ++currentIndex;
        }
        
        this.resetState(ExternalProcessState.COMPLETED, 1.0, "All runs updated");
        Thread.sleep(0);
      }
      else
      {
        this.resetState(ExternalProcessState.FAILED, 0.0, "Error: not logged in.");
      }
    }
    catch (InterruptedException e)
    {
      this.resetState(ExternalProcessState.CANCELLED, 0.0, "Refresh operation cancelled.");
    }
    catch (Exception e)
    {
      this.resetState(ExternalProcessState.FAILED, 0.0, "The refresh operation failed.");
    }
  }

  @Override
  public String getProcessName()
  {
    return "Google Spreadsheet Refresh";
  }
}
