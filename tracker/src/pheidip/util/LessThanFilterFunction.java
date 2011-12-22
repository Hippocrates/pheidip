package pheidip.util;

public class LessThanFilterFunction<T extends Comparable<T> > implements FilterFunction<T>
{
  private T target;
  
  public LessThanFilterFunction(T target)
  {
    this.target = target;
  }
  
  public boolean predicate(T x)
  {
    return this.target.compareTo(x) > 0;
  }
}