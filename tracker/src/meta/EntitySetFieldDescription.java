package meta;

import java.util.Set;

import lombok.Getter;

import pheidip.objects.Entity;

public class EntitySetFieldDescription<E extends Entity> extends MetaFieldDescription
{
  @Getter
  private final Class<E> entityClass;
  
  public EntitySetFieldDescription(Class<E> entityClass)
  {
    super(Set.class, false, null);

    this.entityClass = entityClass;
  }
}
