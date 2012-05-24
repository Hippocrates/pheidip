package pheidip.util;


public class InnerStringMatchFilter implements FilterFunction<String>
{
  private String matcher;

  public InnerStringMatchFilter(String matcher)
  {
    if (!StringUtils.isEmptyOrNull(matcher))
    {
      this.matcher = matcher.toUpperCase();
    }
  }

  public boolean predicate(String x)
  {
    if (StringUtils.isEmptyOrNull(x))
    {
      return false;
    }
    else
    {
      return x.toUpperCase().contains(this.matcher);
    }
  }
}
