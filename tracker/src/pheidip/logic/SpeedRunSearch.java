package pheidip.logic;

import java.lang.reflect.Method;
import java.util.List;

import pheidip.db.SpeedRunData;
import pheidip.objects.SpeedRun;
import pheidip.util.Filter;
import pheidip.util.InnerStringMatchFilter;
import pheidip.util.StringUtils;

public class SpeedRunSearch
{
  private DonationDatabaseManager manager;
  private SpeedRunData speedRuns;
  private List<SpeedRun> cachedRuns;
  private Method nameMethod;

  public SpeedRunSearch(DonationDatabaseManager manager)
  {
    this.manager = manager;
    this.speedRuns = this.manager.getDataAccess().getSpeedRuns();
    
    this.cachedRuns = this.speedRuns.getAllSpeedRuns();
    
    try
    {
      this.nameMethod = SpeedRun.class.getMethod("getName");
      
    } 
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public List<SpeedRun> searchSpeedRuns(String name)
  {
    List<SpeedRun> filtered = this.cachedRuns;
    
    if (!StringUtils.isEmptyOrNull(name))
    {
      filtered = this.searchByStringField(name, filtered, this.nameMethod);
    }
    
    return filtered;
  }
  
  private List<SpeedRun> searchByStringField(String firstName, List<SpeedRun> input, Method getter)
  {
    return Filter.filterList(input, new InnerStringMatchFilter(firstName), getter);
  }
}
