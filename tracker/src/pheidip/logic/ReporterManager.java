package pheidip.logic;

import java.util.ArrayList;
import java.util.List;

import pheidip.util.Reporter;

public class ReporterManager
{
  private List<Reporter> reporters = new ArrayList<Reporter>();
  
  public ReporterManager()
  {
  }
  
  public void addReporter(Reporter r)
  {
    if (!reporters.contains(r))
    {
      reporters.add(r);
    }
  }
  
  public void removeReporter(Reporter r)
  {
    if (reporters.contains(r))
    {
      reporters.remove(r);
    }
  }
}
