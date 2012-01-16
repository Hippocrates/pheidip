package pheidip.objects;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import pheidip.model.EntitySpecification;
import pheidip.util.IdUtils;

public abstract class Entity
{
  private int id = IdUtils.generateId();
  private PropertyChangeSupport propertyChangeManager = new PropertyChangeSupport(this);
  
  public EntitySpecification getSpecification()
  {
    return EntityMethods.getSpecification(this.getClass());
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public void setId(int id)
  {
    int oldId = this.id;
    this.id = id;
    this.firePropertyChange("id", oldId, this.id);
  }

  public int hashCode()
  {
    return this.getId();
  }
  
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    this.propertyChangeManager.addPropertyChangeListener(listener);
  }
  
  public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    this.propertyChangeManager.addPropertyChangeListener(propertyName, listener);
  }
  
  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    this.propertyChangeManager.removePropertyChangeListener(listener);
  }
  
  public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
  {
    this.propertyChangeManager.removePropertyChangeListener(propertyName, listener);
  }
  
  protected void firePropertyChange(String propertyName, Object oldValue, Object newValue)
  {
    this.propertyChangeManager.firePropertyChange(propertyName, oldValue, newValue);
  }
}
