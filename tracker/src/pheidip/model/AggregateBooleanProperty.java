package pheidip.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class AggregateSource
{
  ObjectProperty source;
  ConverterMethod converter;
  
  public AggregateSource(ObjectProperty source)
  {
    this(source, DefaultConverterMethod.getInstance());
  }
  
  public AggregateSource(ObjectProperty source, ConverterMethod converter)
  {
    this.source = source;
    this.converter = converter;
  }
  
  public ObjectProperty getSource()
  {
    return this.source;
  }
  
  public ConverterMethod getConverter()
  {
    return this.converter;
  }
  
  public boolean getValue()
  {
    return (Boolean) this.converter.convert(this.source.getValue());
  }
}

public class AggregateBooleanProperty implements PropertyChangeListener
{
  private boolean cachedValue;
  private PropertyChangeSupport propertyChangeManager = new PropertyChangeSupport(this);
  private List<AggregateSource> sources = new ArrayList<AggregateSource>();

  public AggregateBooleanProperty()
  {
    cachedValue = false;
  }
  
  public void addSource(ObjectProperty source)
  {
    PropertyReflectSupport.addPropertyChangeListener(source.getTarget(), source.getProperty(), this);
    this.sources.add(new AggregateSource(source));
    
    this.recalculateValue();
  }
  
  public void addSource(ObjectProperty source, ConverterMethod converter)
  {
    PropertyReflectSupport.addPropertyChangeListener(source.getTarget(), source.getProperty(), this);
    this.sources.add(new AggregateSource(source, converter));
    
    this.recalculateValue();
  }
  
  public void removeSource(ObjectProperty source)
  {
    for (AggregateSource entry : Collections.unmodifiableList(this.sources))
    {
      if (entry.getSource().equals(source))
      {
        this.sources.remove(entry);
      }
    }
    
    this.recalculateValue();
  }
  
  public void removeObject(Object sourceObject)
  {
    for (AggregateSource entry : Collections.unmodifiableList(this.sources))
    {
      if (entry.getSource().getTarget().equals(sourceObject))
      {
        this.sources.remove(entry);
      }
    }
    
    this.recalculateValue();
  }
  
  public boolean getValue()
  {
    return this.cachedValue;
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
  
  private void recalculateValue()
  {
    boolean result = true;
    
    for (AggregateSource source : this.sources)
    {
      result &= source.getValue();
    }
    
    boolean oldValue = this.cachedValue;
    this.cachedValue = result;
    this.propertyChangeManager.firePropertyChange("value", oldValue, this.cachedValue);
  }

  @Override
  public void propertyChange(PropertyChangeEvent ev)
  {
    boolean changed = false;
    for (AggregateSource source : this.sources)
    {
      if (ev.getSource().equals(source.getSource().getTarget()) && ev.getPropertyName().equals(source.getSource().getProperty()))
      {
        changed = true;
      }
    }
    
    if (changed)
    {
      this.recalculateValue();
    }
  }
}
