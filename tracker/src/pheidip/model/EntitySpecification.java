package pheidip.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EntitySpecification
{
  private List<EntityProperty> properties;

  public EntitySpecification(List<EntityProperty> properties)
  {
    this.properties = Collections.unmodifiableList(new ArrayList<EntityProperty>(properties));
  }

  public EntitySpecification(EntityProperty... properties)
  {
    this(Arrays.asList(properties));
  }
  
  public List<EntityProperty> getProperties()
  {
    return this.properties;
  }
  
  public EntityProperty getProperty(String name)
  {
    for (EntityProperty p : this.properties)
    {
      if (p.getName().equalsIgnoreCase(name))
      {
        return p;
      }
    }
    
    return null;
  }
}
