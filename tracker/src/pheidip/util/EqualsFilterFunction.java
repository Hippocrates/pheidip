package pheidip.util;


public class EqualsFilterFunction<T extends Comparable<T> > implements FilterFunction<T>
{
  private T target;
  
  public EqualsFilterFunction(T target)
  {
    this.target = target;
  }
  
  public boolean predicate(T x)
  {
    return this.target.equals(x);
  }
}
