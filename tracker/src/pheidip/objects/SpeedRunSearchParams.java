package pheidip.objects;

import pheidip.util.FilterFunction;
import pheidip.util.StringUtils;

public class SpeedRunSearchParams implements FilterFunction<SpeedRun>
{
  public SpeedRunSearchParams(String name)
  {
    this.name = name;
  }
  
  public String name;

  @Override
  public boolean predicate(SpeedRun x)
  {
    return (this.name == null || StringUtils.innerStringMatch(x.getName(), this.name));
  }
  
  
}
