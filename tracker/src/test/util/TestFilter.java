package test.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pheidip.util.EqualsFilterFunction;
import pheidip.util.Filter;

import junit.framework.TestCase;

// This class tests the TypeFilter and ObjectFilter classes, which both
// use the 'FilterFunction' as a basis
public class TestFilter extends TestCase
{
  public void testTypeFilter()
  {
    List<String> tests = new ArrayList<String>(
        Arrays.asList("a","b","c","a","b","c","a","b")
            );
    
    List<String> result = Filter.filterList(tests, new EqualsFilterFunction<String>("a"));
    
    assertEquals(3, result.size());
    
    for (String s : result)
    {
      assertEquals("a", s);
    }
  }
  
  public void testObjectFilter()
  {
    List<String> tests = new ArrayList<String>(
        Arrays.asList("a","ab","abc","abcd","a","acb","aaa","ae","asas","asss")
        );
    
    try
    {
      List<String> result = Filter.filterList(tests, new EqualsFilterFunction<Integer>(3), String.class.getMethod("length"));
    
      assertEquals(3, result.size());
      
      for (String s : result)
      {
        assertEquals(3, s.length());
      }
    } 
    catch (SecurityException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } 
    catch (NoSuchMethodException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }    
  }
}
