package pheidip.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityPropertiesSync implements PropertyChangeListener
{
  private List<SyncrhonizationEntry> synchedProperties;
  
  public EntityPropertiesSync()
  {
    this.synchedProperties = new ArrayList<SyncrhonizationEntry>();
  }

  public void synchronizeProperties(ObjectProperty source, ObjectProperty destination)
  {
    this.synchronizeProperties(source, destination, DefaultConverterMethod.getInstance());
  }
  
  public void synchronizeProperties(ObjectProperty source, ObjectProperty destination, ConverterMethod converter)
  {
    this.removeSync(source, destination);
    
    SyncrhonizationEntry sync = new SyncrhonizationEntry(source, destination, converter);
    sync.synchronize();
    PropertyReflectSupport.addPropertyChangeListener(source.getTarget(), source.getProperty(), this);
    this.synchedProperties.add(sync);
  }
  
  public void synchronizeProperties2Way(ObjectProperty left, ObjectProperty right)
  {
    this.synchronizeProperties(left, right);
    this.synchronizeProperties(right, left);
  }
  
  public void synchronizeProperties2Way(ObjectProperty left, ObjectProperty right, ConverterMethod leftToRight, ConverterMethod rightToLeft)
  {
    this.synchronizeProperties(left, right, leftToRight);
    this.synchronizeProperties(right, left, rightToLeft);
  }
  
  public void removeSync(ObjectProperty source, ObjectProperty destination)
  {
    SyncrhonizationEntry found = null;
    for (SyncrhonizationEntry entry : Collections.unmodifiableList(this.synchedProperties))
    {
      if (entry.getSource().equals(source) && entry.getDestination().equals(destination))
      {
        if (found != null)
        {
          System.out.println("Error, multiple entries");
        }
        else
        {
          found = entry;
          break;
        }
        
        PropertyReflectSupport.removePropertyChangeListener(source.getTarget(), source.getProperty(), this);
        this.synchedProperties.remove(entry);
      }
    }
  }
  
  public void removeSync2Way(ObjectProperty left, ObjectProperty right)
  {
    this.removeSync(left, right);
    this.removeSync(right, left);
  }
  
  public void removeObject(Object target)
  {
    for (SyncrhonizationEntry entry : Collections.unmodifiableList(this.synchedProperties))
    {
      if (entry.getSource().getTarget() == target || entry.getDestination().getTarget() == target)
      {
        this.synchedProperties.remove(entry);
      }
    }
  }
  
  @Override
  public void propertyChange(PropertyChangeEvent ev)
  {
    if (ev.getOldValue() == null && ev.getNewValue() == null || (ev.getOldValue() != null && ev.getOldValue().equals(ev.getNewValue())))
    {
      System.out.println("Anger!");
    }
    else
    {
      for (SyncrhonizationEntry entry : this.synchedProperties)
      {
        if (entry.getSource().getTarget() == ev.getSource() && entry.getSource().getProperty().equals(ev.getPropertyName()))
        {
          entry.synchronize();
        }
      }
    }
  }

}
