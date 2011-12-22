package pheidip.objects;

import pheidip.util.FilterFunction;
import pheidip.util.StringUtils;

public class ChoiceOptionSearchParams implements FilterFunction<ChoiceOption>
{
  public Choice owner;
  public String name;
  
  public ChoiceOptionSearchParams(String name, Choice owner)
  {
    this.name = name;
    this.owner = owner;
  }
  
  public boolean predicate(ChoiceOption x)
  {
    return
      (this.name == null || StringUtils.innerStringMatch(x.getName(), this.name) &&
      (this.owner == null || this.owner.equals(x.getChoice())));
  }
}
