package pheidip.model;

import java.beans.*;
import java.lang.reflect.Method;

public class PropertyReflectSupport
{
  public static boolean supportsBoundProperties(Class<?> clazz)
  {
    return (getPCLAdder(clazz) != null) && (getPCLRemover(clazz) != null);
  }

  private static final Class<?>[] PCL_PARAMS = new Class<?>[]
  { PropertyChangeListener.class };

  private static final Class<?>[] NAMED_PCL_PARAMS = new Class<?>[]
  { String.class, PropertyChangeListener.class };

  public static boolean hasPCLAdder(Class<?> clazz)
  {
    return getPCLAdder(clazz) != null;
  }
  
  public static boolean hasNamedPCLAdder(Class<?> clazz)
  {
    return getNamedPCLAdder(clazz) != null;
  }
  
  public static boolean hasPCLRemover(Class<?> clazz)
  {
    return getPCLRemover(clazz) != null;
  }
  
  public static boolean hasNamedPCLRemover(Class<?> clazz)
  {
    return getNamedPCLRemover(clazz) != null;
  }
  
  public static Method getPCLAdder(Class<?> clazz)
  {
    try
    {
      return clazz.getMethod("addPropertyChangeListener", PCL_PARAMS);
    }
    catch (NoSuchMethodException e)
    {
      return null;
    }
  }

  public static Method getPCLRemover(Class<?> clazz)
  {
    try
    {
      return clazz.getMethod("removePropertyChangeListener", PCL_PARAMS);
    }
    catch (NoSuchMethodException e)
    {
      return null;
    }
  }

  public static Method getNamedPCLAdder(Class<?> clazz)
  {
    try
    {
      return clazz.getMethod("addPropertyChangeListener", NAMED_PCL_PARAMS);
    }
    catch (NoSuchMethodException e)
    {
      return null;
    }
  }

  public static Method getNamedPCLRemover(Class<?> clazz)
  {
    try
    {
      return clazz.getMethod("removePropertyChangeListener", NAMED_PCL_PARAMS);
    }
    catch (NoSuchMethodException e)
    {
      return null;
    }
  }

  public static void addPropertyChangeListener(Object bean, PropertyChangeListener listener)
  {
    Class<?> objectClass = bean.getClass();

    if (listener == null)
    {
      throw new NullPointerException("The listener must not be null.");
    }

    // Check whether the bean supports bound properties.
    if (!supportsBoundProperties(objectClass))
    {
      throw new RuntimeException(
          "Error, object does not support bound properties");
    }

    Method multicastPCLAdder = getPCLAdder(objectClass);
    try
    {
      multicastPCLAdder.invoke(bean, listener);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public static void addPropertyChangeListener(Object bean, String propertyName, PropertyChangeListener listener)
  {
    Class<?> objectClass = bean.getClass();
    if (propertyName == null)
    {
      throw new NullPointerException("The property name must not be null.");
    }

    if (listener == null)
    {
      throw new NullPointerException("The listener must not be null.");
    }

    Method namedPCLAdder = getNamedPCLAdder(objectClass);

    if (namedPCLAdder == null)
    {
      throw new RuntimeException(
          "Could not find the bean method"
              + "/npublic void addPropertyChangeListener(String, PropertyChangeListener);"
              + "/nin bean:" + bean);
    }

    try
    {
      namedPCLAdder.invoke(bean, propertyName, listener);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }

  public static void removePropertyChangeListener(Object bean,
      PropertyChangeListener listener)
  {
    Class<?> objectClass = bean.getClass();

    if (listener == null)
      throw new NullPointerException("The listener must not be null.");

    Method multicastPCLRemover = getPCLRemover(objectClass);
    
    if (multicastPCLRemover == null)
    {
      throw new RuntimeException(
          "Could not find the method:"
              + "\npublic void removePropertyChangeListener(String, PropertyChangeListener x);"
              + "\nfor bean:" + bean);
    }

    try
    {
      multicastPCLRemover.invoke(bean, listener);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }

  public static void removePropertyChangeListener(Object bean, String propertyName, PropertyChangeListener listener)
  {
    Class<?> objectClass = bean.getClass();
    
    if (propertyName == null)
      throw new NullPointerException("The property name must not be null.");
    if (listener == null)
      throw new NullPointerException("The listener must not be null.");

    Method namedPCLRemover = getNamedPCLRemover(objectClass);
    if (namedPCLRemover == null)
    {
      throw new RuntimeException(
          "Could not find the bean method"
              + "/npublic void removePropertyChangeListener(String, PropertyChangeListener);"
              + "/nin bean:" + bean);
    }

    try
    {
      namedPCLRemover.invoke(bean, propertyName, listener);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }
  
  public static Object getProperty(Object object, String property)
  {
    try
    {
      BeanInfo info = Introspector.getBeanInfo(object.getClass());
      Method getMethod = null;

      for (PropertyDescriptor descriptor : info.getPropertyDescriptors())
      {
        if (descriptor.getName().equalsIgnoreCase(property))
        {
          getMethod = descriptor.getReadMethod();
        }
      }
      
      return getMethod.invoke(object);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }
  
  public static void setProperty(Object object, String property, Object value)
  {
    try
    {
      BeanInfo info = Introspector.getBeanInfo(object.getClass());
      Method setMethod = null;
  
      for (PropertyDescriptor descriptor : info.getPropertyDescriptors())
      {
        if (descriptor.getName().equalsIgnoreCase(property))
        {
          setMethod = descriptor.getWriteMethod();
        }
      }

      setMethod.invoke(object, value);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e.getMessage());
    }
  }
}
