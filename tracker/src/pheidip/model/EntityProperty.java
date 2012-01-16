package pheidip.model;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class EntityProperty
{
  private String name;
  private Method readMethod;
  private Method writeMethod;
  private Class<?> storageType;
  
  public EntityProperty(PropertyDescriptor descriptor)
  {
    this(descriptor.getName(), descriptor.getReadMethod(), descriptor.getWriteMethod(), descriptor.getPropertyType());
  }
  
  public EntityProperty(String name, Method readMethod, Method writeMethod, Class<?> storageType)
  {
    this.name = name;
    this.readMethod = readMethod;
    this.writeMethod = writeMethod;
    this.storageType = storageType;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public Method getReadMethod()
  {
    return this.readMethod;
  }
  
  public Method getWriteMethod()
  {
    return this.writeMethod;
  }
  
  public Class<?> getStorageType()
  {
    return this.storageType;
  }

  public boolean isReadOnly()
  {
    return this.writeMethod != null;
  }
  
  public Object getProperty(Object target)
  {
    try
    {
      return this.readMethod.invoke(target);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void setProperty(Object target, Object value)
  {
    if (this.isReadOnly())
    {
      throw new RuntimeException("Error: property is read only.");
    }
    
    try
    {
      this.writeMethod.invoke(target, value);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
