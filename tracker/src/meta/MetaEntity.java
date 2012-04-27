package meta;

import java.util.List;

import lombok.Data;

@Data
public class MetaEntity
{
  private final String name;
  private final Class<?> storageClass;
  private final MetaEntityDescription entityDescription;
  
  public MetaField getField(String name)
  {
    return this.entityDescription.getField(name);
  }
  
  public List<MetaField> getFields()
  {
    return this.entityDescription.getFields();
  }
  
  public boolean validate(Object obj)
  {
    return this.entityDescription.validate(obj);
  }
}
