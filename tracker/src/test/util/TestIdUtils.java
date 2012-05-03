package test.util;

import pheidip.util.IdUtils;
import junit.framework.TestCase;

public class TestIdUtils extends TestCase
{
  public void testConsecutiveIdsDifferent()
  {
    int id0 = IdUtils.generateId();
    int id1 = IdUtils.generateId();
    
    assertTrue(id0 != id1);
  }
  
  public void testListOfIdsIsDifferent()
  {
    int list[] = new int[5000];
    
    for (int i = 0; i < list.length; ++i)
    {
      list[i] = IdUtils.generateId();
    }
    
    for (int i = 0; i < list.length; ++i)
    {
      for (int j = 0; j < i; ++j)
      {
        assertTrue(list[j] != list[i]);
      }
    }
  }
}
