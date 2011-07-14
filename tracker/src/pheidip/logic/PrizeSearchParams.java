package pheidip.logic;

import pheidip.objects.Prize;
import pheidip.util.FilterFunction;
import pheidip.util.StringUtils;

public class PrizeSearchParams implements FilterFunction<Prize>
{
  public String name;

  @Override
  public boolean predicate(Prize x)
  {
    return StringUtils.isEmptyOrNull(this.name) ? true : x.getName().toUpperCase().contains(name.toUpperCase());
  }
}
