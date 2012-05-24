package pheidip.objects;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pheidip.model.EntityProperty;
import pheidip.model.EntitySpecification;

public final class EntityMethods
{
  private static Map<Class<?>, EntitySpecification> cachedSpecifications = new HashMap<Class<?>, EntitySpecification>();
  
  public static <T> EntitySpecification getSpecification(Class<T> clazz)
  {
    if (cachedSpecifications.get(clazz) == null)
    {
      cachedSpecifications.put(clazz, new EntitySpecification(buildProperties(clazz)));
    }
    
    return cachedSpecifications.get(clazz);
  }
  
  public static <T> List<EntityProperty> buildProperties(Class<T> clazz)
  {
    BeanInfo info;
    try
    {
      info = Introspector.getBeanInfo(clazz);
    }
    catch (IntrospectionException e)
    {
      throw new RuntimeException(e);
    }
    
    List<EntityProperty> properties = new ArrayList<EntityProperty>();
    
    for (PropertyDescriptor desc : info.getPropertyDescriptors())
    {
      properties.add(new EntityProperty(desc));
    }
    
    return properties;
  }
}
