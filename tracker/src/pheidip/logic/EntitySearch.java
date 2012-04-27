package pheidip.logic;

import java.util.EnumSet;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import meta.EnumSetFieldDescription;
import meta.MetaEntity;
import meta.search.MetaSearchEntity;
import meta.search.MetaSearchField;
import pheidip.db.DataAccess;
import pheidip.model.PropertyReflectSupport;
import pheidip.objects.Entity;

public class EntitySearch<E extends Entity>
{
  @Getter @NotNull
  private MetaEntity entityDescription;
  
  @Getter @NotNull
  private DataAccess dataAccess;
  
  @Getter @NotNull
  private MetaSearchEntity searchDescription;
  
  public EntitySearch(DataAccess dataAccess, MetaEntity entityDescription, MetaSearchEntity searchDescription)
  {
    this.dataAccess = dataAccess;
    this.entityDescription = entityDescription;
    this.searchDescription = searchDescription;
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public Object createDefaultSearchParameters()
  {
    try
    {
      Object params = this.searchDescription.getStorageClass().newInstance();
      
      for (MetaSearchField field : this.searchDescription.getSearchEntityDescription().getSearchFields())
      {
        if (field.getFieldDescription() instanceof EnumSetFieldDescription)
        {
          PropertyReflectSupport.setProperty(params, field.getName(), EnumSet.allOf(((EnumSetFieldDescription)field.getFieldDescription()).getEnumClass()));
        }
      }
      
      return params;
    }
    catch (InstantiationException e)
    {
      throw new RuntimeException("Error, searcher does not have a default no-args constructor.");
    }
    catch (IllegalAccessException e)
    {
      throw new RuntimeException("Security exception.");
    }
  }
  
  public EntitySearchInstance<E> createSearchInstance()
  {
    return this.createSearchInstance(this.createDefaultSearchParameters());
  }
  
  public EntitySearchInstance<E> createSearchInstance(Object params)
  {
    return new EntitySearchInstance<E>(this.dataAccess, this.entityDescription, this.searchDescription, params);
    /*
    if (this.searchDescription.validate(params))
    {
      return new EntitySearchInstance<E>(this.dataAccess, this.entityDescription, this.searchDescription, params);
    }
    else
    {
      return null;
    }*/
  }
}
