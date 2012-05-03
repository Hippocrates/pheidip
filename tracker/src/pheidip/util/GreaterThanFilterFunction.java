package pheidip.util;

public class GreaterThanFilterFunction<T extends Comparable<T> > implements FilterFunction<T>
{
  private T target;
  
  public GreaterThanFilterFunction(T target)
  {
    this.target = target;
  }
  
  public boolean predicate(T x)
  {
    return this.target.compareTo(x) < 0;
  }
}