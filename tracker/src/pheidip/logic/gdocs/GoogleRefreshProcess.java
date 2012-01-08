package pheidip.logic.gdocs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import pheidip.db.SpeedRunData;
import pheidip.logic.AbstractExternalProcess;
import pheidip.logic.DonationDatabaseManager;
import pheidip.logic.chipin.ExternalProcessState;
import pheidip.objects.Prize;
import pheidip.objects.SpeedRun;
import pheidip.util.StringUtils;

public class GoogleRefreshProcess extends AbstractExternalProcess
{
  private DonationDatabaseManager database;
  private GoogleSpreadSheetLoginManager loginManager;
  
  public GoogleRefreshProcess(DonationDatabaseManager database, GoogleSpreadSheetLoginManager loginManager)
  {
    this(database, loginManager, null);
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
        
        SpeedRunData speedRuns = database.getDataAccess().getSpeedRunData();
        
        List<SpeedRun> allRuns = speedRuns.getAllSpeedRuns();
        Map<String, SpeedRun> mappedRuns = GoogleSpreadSheetReader.nameMapRuns(allRuns);
        
        double processIncrement = (1.0 - 0.4) / (entries.size() * 2 + allRuns.size());
        double processPercentage = 0.4;
        
        int currentRunIndex = 0;
        int currentPrizeIndex = 0;
        
        List<SpeedRun> runsToInsert = new ArrayList<SpeedRun>();
        List<SpeedRun> runsToUpdate = new ArrayList<SpeedRun>();

        for (MarathonSpreadsheetEntry entry : entries)
        {
          this.resetState(ExternalProcessState.RUNNING, processPercentage, "Pushing run information");
          Thread.sleep(0);
          
          if (!StringUtils.innerStringMatch(entry.getGameName(), "setup") && !entry.getGameName().equalsIgnoreCase("end"))
          {
            SpeedRun found = mappedRuns.remove(entry.getGameName().toLowerCase());
            
            if (found == null)
            {
              SpeedRun newRun = new SpeedRun();
              newRun.setName(entry.getGameName());
              newRun.setRunners(StringUtils.joinLanguageSeperated(entry.getRunners()));
              newRun.setDescription(entry.getComments());
              newRun.setSortKey(currentRunIndex * 100);
              newRun.setStartTime(entry.getStartTime());
              newRun.setEndTime(entry.getEstimatedFinish());
              
              String description = StringUtils.joinLanguageSeperated(entry.getRunners());
              
              if (!StringUtils.isEmptyOrNull(entry.getComments()))
              {
                description += (StringUtils.isEmptyOrNull(description) ? "" : " - ") + entry.getComments();
              }
              
              newRun.setDescription(description);
                
              runsToInsert.add(newRun);
            }
            else
            {
              found.setSortKey(currentRunIndex);
              found.setRunners(StringUtils.joinLanguageSeperated(entry.getRunners()));
              found.setStartTime(entry.getStartTime());
              found.setEndTime(entry.getEstimatedFinish());
              
              List<Prize> prizes = new ArrayList<Prize>(found.getPrizeEndGame());
              
              Collections.sort(prizes, new Comparator<Prize>()
              {
                @Override
                public int compare(Prize arg0, Prize arg1)
                {
                  return arg0.getSortKey() - arg1.getSortKey();
                }
              });
              
              for (Prize p : prizes)
              {
                p.setSortKey(currentPrizeIndex * 100);
                ++currentPrizeIndex;
              }
                
              runsToUpdate.add(found);
            }
          }

          processPercentage += processIncrement;
          ++currentRunIndex;
        }
        
        currentRunIndex += 1000;
        currentPrizeIndex += 1000;
        
        for (SpeedRun s : mappedRuns.values())
        {
          s.setSortKey(currentRunIndex * 100);
          ++currentRunIndex;
          
          for (Prize p : s.getPrizeEndGame())
          {
            p.setSortKey(currentPrizeIndex * 100);
            ++currentPrizeIndex;
          }
          runsToUpdate.add(s);
        }
        
        for (SpeedRun s : runsToInsert)
        {
          speedRuns.insertSpeedRun(s);
          this.resetState(ExternalProcessState.RUNNING, processPercentage, "Pushing run information");
          Thread.sleep(0);
          processPercentage += processIncrement;
        }
        
        speedRuns.multiUpdateSpeedRuns(runsToUpdate);
        /*
        for (SpeedRun s : runsToUpdate)
        {
          speedRuns.updateSpeedRun(s);
          this.resetState(ExternalProcessState.RUNNING, processPercentage, "Pushing run information");
          Thread.sleep(0);
          processPercentage += processIncrement;
        }
        */
        
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
