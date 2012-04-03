package meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;

import lombok.Getter;

public class MetaEntityDescription
{
  @Getter
  private final List<MetaField> fields;
  
  private final Map<String, MetaField> fieldMap;
  
  public MetaEntityDescription(List<MetaField> fieldsList)
  {
    this.fields = Collections.unmodifiableList(new ArrayList<MetaField>(fieldsList));
    
    TreeMap<String, MetaField> fieldMap = new TreeMap<String, MetaField>(String.CASE_INSENSITIVE_ORDER);
    
    for (MetaField f : fieldsList)
    {
      if (fieldMap.get(f.getName()) != null)
      {
        throw new RuntimeException(String.format("Error, duplicate field name: %s", f.getName()));
      }
      else
      {
        fieldMap.put(f.getName(), f);
      }
    }
    
    this.fieldMap = Collections.unmodifiableMap(fieldMap);
  }
  
  public MetaField getField(String name)
  {
    return this.fieldMap.get(name);
  }
  
  public boolean validate(Object obj)
  {
    boolean result = true;
    
    for (MetaField field : this.fields)
    {
      try
      {
        Object value = BeanUtils.getProperty(obj, field.getName());
        if (!field.getFieldDescription().validate(value))
        {
          return false;
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
        return false;
      }
    }
    
    return result;
  }
}
