package pheidip.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public final class Filter
{
  public static <T> List<T> filterList(List<T> input, FilterFunction<T> filterFunc)
  {
    ArrayList<T> output = new ArrayList<T>();
      
    for (T x : input)
    {
      if (filterFunc.predicate(x))
      {
        output.add(x);
      }
    }
    
    return output;
  }
  
  @SuppressWarnings("unchecked")
  public static <T,O> List<O> filterList(List<O> input, FilterFunction<T> filterFunc, Method invokeMethod)
  {
    ArrayList<O> output = new ArrayList<O>();
    
    try
    {
      for (O x : input)
      {
        if (filterFunc.predicate((T)invokeMethod.invoke(x)))
        {
          output.add(x);
        }
      }
    } 
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    
    return output;
  }
}
