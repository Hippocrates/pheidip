package pheidip.logic.gdocs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import meta.MetaEntity;
import meta.reflect.MetaEntityReflector;

import pheidip.db.DataAccess;
import pheidip.logic.AbstractExternalProcess;
import pheidip.logic.EntitySearch;
import pheidip.logic.EntitySearchInstance;
import pheidip.logic.ProgramInstance;
import pheidip.logic.chipin.ExternalProcessState;
import pheidip.objects.Prize;
import pheidip.objects.SpeedRun;
import pheidip.util.StringUtils;

public class GoogleRefreshProcess extends AbstractExternalProcess
{
  private ProgramInstance instance;
  private GoogleSpreadSheetLoginManager loginManager;
  private EntitySearch<SpeedRun> speedRunSearch;
  private EntitySearch<Prize> prizeSearch;
  private MetaEntity speedRunEntity;
  private MetaEntity prizeEntity;
  
  public GoogleRefreshProcess(ProgramInstance instance, GoogleSpreadSheetLoginManager loginManager)
  {
    this(instance, loginManager, null);
  }
  
  public GoogleRefreshProcess(ProgramInstance instance, GoogleSpreadSheetLoginManager loginManager, ProcessStateCallback listener)
  {
    super(listener);
    
    this.instance = instance;
    this.loginManager = loginManager;
    
    this.speedRunSearch = this.instance.getEntitySearch(SpeedRun.class);
    this.prizeSearch = this.instance.getEntitySearch(Prize.class);
    
    this.speedRunEntity = MetaEntityReflector.getMetaEntity(SpeedRun.class);
    this.prizeEntity = MetaEntityReflector.getMetaEntity(Prize.class);
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
        
        EntitySearchInstance<SpeedRun> speedRunSearchInstance = this.speedRunSearch.createSearchInstance();
        EntitySearchInstance<Prize> prizeSearchInstance = this.prizeSearch.createSearchInstance();
        
        speedRunSearchInstance.setPageSize(Integer.MAX_VALUE);
        speedRunSearchInstance.runSearch();
        prizeSearchInstance.setPageSize(Integer.MAX_VALUE);
        prizeSearchInstance.runSearch();

        List<SpeedRun> allRuns = speedRunSearchInstance.getResults();
        List<Prize> allPrizes = prizeSearchInstance.getResults();
        Map<String, SpeedRun> mappedRuns = GoogleSpreadSheetReader.nameMapRuns(allRuns);
        
        double processIncrement = (0.8 - 0.4) / (entries.size() * 2 + allRuns.size());
        double processPercentage = 0.4;
        
        int currentRunIndex = 0;
        int currentPrizeIndex = 0;
        
        List<SpeedRun> runsToInsert = new ArrayList<SpeedRun>();
        List<SpeedRun> runsToUpdate = new ArrayList<SpeedRun>();
        
        List<Prize> prizesToUpdate = new ArrayList<Prize>();

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
              
              List<Prize> runPrizes = new ArrayList<Prize>();
              
              for (Prize p : allPrizes)
              {
                if (p.getEndGame() != null && p.getEndGame().getId() == found.getId())
                {
                  runPrizes.add(p);
                }
              }
              
              Collections.sort(runPrizes, new Comparator<Prize>()
              {
                @Override
                public int compare(Prize arg0, Prize arg1)
                {
                  return arg0.getSortKey() - arg1.getSortKey();
                }
              });
              
              for (Prize p : runPrizes)
              {
                p.setSortKey(currentPrizeIndex * 100);
                ++currentPrizeIndex;
                prizesToUpdate.add(p);
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
          s = this.instance.getEntityControl(SpeedRun.class).load(s.getId());
          s.setSortKey(currentRunIndex * 100);
          ++currentRunIndex;
          
          for (Prize p : s.getPrizeEndGame())
          {
            p.setSortKey(currentPrizeIndex * 100);
            ++currentPrizeIndex;
          }
          runsToUpdate.add(s);
        }
        
        DataAccess database = this.instance.getDataAccess();
        
        for (SpeedRun s : runsToInsert)
        {
          database.saveInstance(this.speedRunEntity, s);
          this.resetState(ExternalProcessState.RUNNING, processPercentage, "Pushing run information");
          Thread.sleep(0);
          processPercentage += processIncrement;
        }
        
        database.updateMultiple(this.speedRunEntity, runsToUpdate);

        this.resetState(ExternalProcessState.RUNNING, 0.8, "Pushing prize information");

        database.updateMultiple(this.prizeEntity, prizesToUpdate);
        
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
