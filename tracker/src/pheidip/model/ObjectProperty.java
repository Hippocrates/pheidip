package pheidip.model;

public class ObjectProperty
{
  private Object target;
  private String property;
  
  public ObjectProperty(Object target, String property)
  {
    this.target = target;
    this.property = property;
  }
  
  public Object getTarget()
  {
    return this.target;
  }
  
  public String getProperty()
  {
    return this.property;
  }
  
  public Object getValue()
  {
    return PropertyReflectSupport.getProperty(this.target, this.property);
  }
  
  void setValue(Object value)
  {
    PropertyReflectSupport.setProperty(this.target, this.property, value);
  }
  
  public boolean equals(Object other)
  {
    if (other instanceof ObjectProperty)
    {
      ObjectProperty casted = (ObjectProperty) other;
      
      return casted.getTarget() == this.target && casted.getProperty().equals(this.property);
    }
    else
    {
      return false;
    }
  }
}
