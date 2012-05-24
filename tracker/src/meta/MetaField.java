package meta;

import pheidip.model.PropertyReflectSupport;

import lombok.Data;

@Data
public class MetaField
{
  private final String name;
  private final MetaFieldDescription fieldDescription;
  
  public Object getValue(Object instance)
  {
    try
    {
      return PropertyReflectSupport.getProperty(instance, this.name);
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void setValue(Object instance, Object value)
  {
    try
    {
      PropertyReflectSupport.setProperty(instance, this.name, value);
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}
