package pheidip.objects;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import pheidip.model.ComparisonOperator;
import pheidip.model.SearchProperty;
import pheidip.model.SearchSpecification;

public abstract class SearchParameters<T extends Entity>
{
  private PropertyChangeSupport propertyChangeManager = new PropertyChangeSupport(this);
  
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
  
  public abstract SearchSpecification getSearchSpecification();
  
  public T createInstanceFromParameters(Class<T> clazz)
  {
    T result = null;
    
    try
    {
      result = clazz.newInstance();

      for (SearchProperty property : this.getSearchSpecification().getProperties())
      {
        if (property.getOperator() == ComparisonOperator.EQUALS || property.getOperator() == ComparisonOperator.INNERMATCH || property.getOperator() == ComparisonOperator.LIKE)
        {
          Object value = property.getSearchProperty().getReadMethod().invoke(this);
          property.getTargetProperty().getWriteMethod().invoke(result, value);
        }
      }
    }
    catch (Exception e)
    {
      result = null;
    }
    
    return result;
  }
}
