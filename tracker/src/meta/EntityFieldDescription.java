package meta;

import pheidip.objects.Entity;

public class EntityFieldDescription<E extends Entity> extends MetaFieldDescription
{
  public EntityFieldDescription(Class<E> entityClass, boolean nullable)
  {
    super(entityClass, nullable, null);
  }
}
