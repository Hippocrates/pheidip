package pheidip.objects;

import pheidip.util.IdUtils;
import lombok.Getter;
import lombok.Setter;

public abstract class Entity
{
  @Getter @Setter 
  private int id = IdUtils.generateId();
  
  @Override
  public int hashCode()
  {
    return this.getId();
  }
  
  @Override
  public boolean equals(Object other)
  {
    if (other != null && other.getClass() == this.getClass())
    {
      return this.id == ((Entity)other).id;
    }
    else
    {
      return false;
    }
  }
}
