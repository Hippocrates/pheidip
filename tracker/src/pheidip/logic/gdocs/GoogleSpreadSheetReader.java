package pheidip.logic.gdocs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import meta.MetaEntity;
import meta.reflect.MetaEntityReflector;

import pheidip.db.DataAccess;
import pheidip.logic.DonationDatabaseManager;
import pheidip.logic.EntitySearchInstance;
import pheidip.logic.ProgramInstance;
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
  
  public static void mergeRun(DataAccess database, MarathonSpreadsheetEntry entry, Map<String, SpeedRun> mappedRuns, int currentIndex, boolean initializeMode)
  {
    if (!StringUtils.innerStringMatch(entry.getGameName(), "setup") && !entry.getGameName().equalsIgnoreCase("end"))
    {
      MetaEntity speedRunEntity = MetaEntityReflector.getMetaEntity(SpeedRun.class);
      
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
          
        database.saveInstance(speedRunEntity, newRun);
      }
      else
      {
        found.setRunners(StringUtils.joinLanguageSeperated(entry.getRunners()));
        found.setStartTime(entry.getStartTime());
        found.setEndTime(entry.getEstimatedFinish());
        
        database.updateInstance(speedRunEntity, found);
      }
    }
  }
  
  public static void mergeRuns(ProgramInstance instance, List<MarathonSpreadsheetEntry> entries, boolean initializeMode)
  {
    EntitySearchInstance<SpeedRun> searcher = instance.getEntitySearch(SpeedRun.class).createSearchInstance();
    searcher.setPageSize(Integer.MAX_VALUE);
    searcher.runSearch();
    List<SpeedRun> allRuns = searcher.getResults();
    
    Map<String, SpeedRun> mappedRuns = nameMapRuns(allRuns);
    
    int currentIndex = 0;
    
    for (MarathonSpreadsheetEntry entry : entries)
    {
      mergeRun(instance.getDataAccess(), entry, mappedRuns, currentIndex, initializeMode);
      
      ++currentIndex;
    }
  }
}
