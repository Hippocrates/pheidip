package pheidip.logic.gdocs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pheidip.db.SpeedRunData;
import pheidip.logic.DonationDatabaseManager;
import pheidip.objects.SpeedRun;
import pheidip.util.StringUtils;

public class GoogleSpreadSheetReader
{
  public static Map<String, SpeedRun> nameMapRuns(List<SpeedRun> allRuns)
  {
    Map<String, SpeedRun> mappedRuns = new HashMap<String, SpeedRun>();
    
    for (SpeedRun run : allRuns)
    {
      mappedRuns.put(run.getName(), run);
    }
    
    return mappedRuns;
  }
  
  public static void mergeRun(SpeedRunData speedRuns, MarathonSpreadsheetEntry entry, Map<String, SpeedRun> mappedRuns, int currentIndex, boolean initializeMode)
  {
    if (!StringUtils.innerStringMatch(entry.getGameName(), "setup") && !entry.getGameName().equalsIgnoreCase("end"))
    {
      SpeedRun found = mappedRuns.get(entry.getGameName().toLowerCase());
      
      if (found == null)
      {
        SpeedRun newRun = new SpeedRun();
        newRun.setName(entry.getGameName());
        newRun.setRunners(StringUtils.joinLanguageSeperated(entry.getRunners()));
        newRun.setDescription(entry.getComments());
        newRun.setSortKey(currentIndex);
        newRun.setStartTime(entry.getStartTime());
        newRun.setEndTime(entry.getEstimatedFinish());
        
        String description = StringUtils.joinLanguageSeperated(entry.getRunners());
        
        if (!StringUtils.isEmptyOrNull(entry.getComments()))
        {
          description += (StringUtils.isEmptyOrNull(description) ? "" : " - ") + entry.getComments();
        }
        
        newRun.setDescription(description);
          
        speedRuns.insertSpeedRun(newRun);
      }
      else
      {
        found.setRunners(StringUtils.joinLanguageSeperated(entry.getRunners()));
        found.setStartTime(entry.getStartTime());
        found.setEndTime(entry.getEstimatedFinish());
        
        speedRuns.updateSpeedRun(found);
      }
    }
  }
  
  public static void mergeRuns(DonationDatabaseManager database, List<MarathonSpreadsheetEntry> entries, boolean initializeMode)
  {
    SpeedRunData speedRuns = database.getDataAccess().getSpeedRunData();
    
    List<SpeedRun> allRuns = speedRuns.getAllSpeedRuns();
    
    Map<String, SpeedRun> mappedRuns = nameMapRuns(allRuns);
    
    int currentIndex = 0;
    
    for (MarathonSpreadsheetEntry entry : entries)
    {
      mergeRun(speedRuns, entry, mappedRuns, currentIndex, initializeMode);
      
      ++currentIndex;
    }
  }
}
