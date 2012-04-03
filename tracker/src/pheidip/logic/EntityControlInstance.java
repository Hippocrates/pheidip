package pheidip.logic;

import lombok.Getter;
import pheidip.objects.Entity;

public class EntityControlInstance<E extends Entity>
{
  @Getter
  private E instance;
  
  @Getter
  private EntityControl<E> control;
  
  public EntityControlInstance(EntityControl<E> control, E instance)
  {
    this.instance = instance;
    this.control = control;
  }
  
  public boolean isPersistent()
  {
    return this.isValid() && this.instance.isPersistent();
  }
  
  public boolean isValid()
  {
    return this.instance != null;
  }
  
  public int getId()
  {
    return this.instance.getId();
  }

  public void refreshInstance()
  {
    if (this.instance.isPersistent())
    {
      this.instance = this.control.load(this.instance.getId());
    }
    else
    {
      throw new RuntimeException("Error, instance is not in database.");
    }
  }
  
  // the idea here is that there are methods to replace/merge the current instance in some 
  // fashion --> this will come later, for now, all updates are direct
  public void saveInstance()
  {
    this.control.save(this.instance);
  }
  
  public void deleteInstance()
  {
    if (this.instance.isPersistent())
    {
      this.control.delete(this.instance);
    }
    else
    {
      throw new RuntimeException("Error, instance is not in database.");
    }
  }
}
