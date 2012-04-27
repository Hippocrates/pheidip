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
    this.instance = this.control.load(this.instance.getId());
  }
  
  // the idea here is that there are methods to replace/merge the current instance in some 
  // fashion --> this will come later, for now, all updates are direct
  public void saveInstance()
  {
    this.control.save(this.instance);
  }
  
  public void deleteInstance()
  {
    this.control.delete(this.instance);
  }
}
