package pheidip.objects;

import pheidip.util.FilterFunction;
import pheidip.util.StringUtils;

public class BidSearchParams implements FilterFunction<Bid>
{
  public String name;
  public SpeedRun owner;

  public BidSearchParams(String name, SpeedRun owner)
  {
    this.name = name;
    this.owner = owner;
  }

  @Override
  public boolean predicate(Bid x)
  {
    return
      (StringUtils.nullIfEmpty(this.name) == null || StringUtils.innerStringMatch(x.getName(), this.name)) &&
      (this.owner == null || this.owner.equals(x.getSpeedRun()));
  }
}
