package meta.search;

import meta.MetaEntity;

public class MetaSearchEntity extends MetaEntity
{
  public MetaSearchEntity(String name, Class<?> storageClass, MetaSearchEntityDescription entityDescription)
  {
    super(name, storageClass, entityDescription);
  }

  public MetaSearchEntityDescription getSearchEntityDescription()
  {
    return (MetaSearchEntityDescription) super.getEntityDescription();
  }
}
