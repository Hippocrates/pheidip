package pheidip.logic;

import java.util.Set;

import javax.validation.ConstraintViolation;

import lombok.Getter;
import meta.MetaEntity;

import pheidip.db.DataAccess;
import pheidip.objects.Entity;
import pheidip.util.StringUtils;

// This is a basic 'CRUD' interface for entity objects
public class EntityControl<E extends Entity>
{
  @Getter
  private MetaEntity entityDescription;
  
  @Getter
  private DataAccess dataAccess;
  
  public EntityControl(DataAccess dataAccess, MetaEntity entityDescription)
  {
    this.dataAccess = dataAccess;
    this.entityDescription = entityDescription;
  }
  
  public void delete(E instance)
  {
    try
    {
      this.dataAccess.deleteInstance(this.entityDescription, instance);
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void save(E instance)
  {
    Set<ConstraintViolation<E>> violations = this.validate(instance);
    
    if (violations.size() > 0)
    {
      ConstraintViolation<E> first = violations.iterator().next();
      
      throw new RuntimeException("Error : " + StringUtils.javaToNatural(first.getPropertyPath().toString()) + " : " + first.getMessage());
    }
    else
    {
      try
      {
        this.dataAccess.updateInstance(this.entityDescription, instance);
      }
      catch(Exception e)
      {
        throw new RuntimeException(e);
      }
    }
  }
  
  public E load(int id)
  {
    try
    {
      return this.dataAccess.loadInstance(this.entityDescription, id);
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  
  public Set<ConstraintViolation<E>> validate(E instance)
  {
    return ValidationUtils.<E>validate(instance);
  }
}
