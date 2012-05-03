package pheidip.ui;

import java.awt.Component;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import meta.MetaField;
import meta.StringFieldDescription;
import meta.TimeFieldDescription;

import pheidip.logic.ProgramInstance;
import pheidip.model.EntityProperty;
import pheidip.objects.Entity;
import pheidip.util.Pair;
import pheidip.ui.DateSpinner;
import pheidip.util.StringUtils;

public class UIGenerator
{
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static EntitySearchDialog<?> createEntitySearchDialog(Class<?> clazz, ProgramInstance instance, boolean allowMultiSelect)
  {
    return new EntitySearchDialog(instance, clazz, allowMultiSelect);
  }
  
  public JLabel generateLabel(MetaField field)
  {
    return new JLabel(StringUtils.javaToNatural(field.getName()) + ":");
  }
  
  public static Pair<Component, String> generateSearcherComponent(MetaField field)
  {
    Component component = null;
    String bindingProperty = null;
    
    if (field.getFieldDescription() instanceof StringFieldDescription)
    {
      if (field.getName().equalsIgnoreCase("password"))
      {
        component = new JPasswordField(31);
      }
      else
      {
        component = new BoundJTextField(31);
      }
      
      bindingProperty = "text";
    }
    else if (field.getFieldDescription() instanceof TimeFieldDescription)
    {
      component = new DateSpinner(((TimeFieldDescription)field.getFieldDescription()).getDateFormat());
      bindingProperty = "value";
    }
    else if (field.getFieldDescription().getFormatter() != null)
    {
      component = new JFormattedTextField(field.getFieldDescription().getFormatter());
      bindingProperty = "value";
    }
    
    return new Pair<Component, String>(component, bindingProperty);
  }
  
  public static Object[] getEnumValues(Class<? extends Enum<?>> enumClazz)
  {
    Object[] result = null;
    
    try
    {
      Method valuesMethod = enumClazz.getMethod("values");
      
      Object rawResult = valuesMethod.invoke(null);
      
      if (rawResult.getClass().isArray())
      {
        result = (Object[]) rawResult;
      }
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    
    return result;
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static Pair<Component, String> createPropertyComponent(EntityProperty property, ProgramInstance instance)
  {
    Component propertyComponent = null;
    String bindablePropertyName = null;
    
    if (property.isCollectionType())
    {
      if (property.getCollectionType().isEnum())
      {
        propertyComponent = new SetSelector(getEnumValues((Class<? extends Enum<?>>)property.getCollectionType()));
        bindablePropertyName = "selections";
      }
      else if (property.getCollectionType().isAssignableFrom(Entity.class))
      {
        throw new RuntimeException("Not implemented yet");
      }
      else
      {
        throw new RuntimeException("I guess this should be a JList of toString or something...");
      }
    }
    else if (Entity.class.isAssignableFrom(property.getStorageType()))
    {
      EntitySelector selector = new EntitySelector(instance, property.getStorageType());
      selector.setNullSelectionAllowed(true);
      selector.setNavigationAllowed(false);
      propertyComponent = selector;
      bindablePropertyName = "entity";
    }
    else if (property.getStorageType() == String.class)
    {
      propertyComponent = new BoundJTextField("");
      bindablePropertyName = "text";
    }
    else if (property.getStorageType() == Integer.class)
    {
      propertyComponent = new IntegerField();
      bindablePropertyName = "value";
    }
    else if (property.getStorageType() == BigDecimal.class)
    {
      propertyComponent = new MoneyTextField();
      bindablePropertyName = "value";
    }
    else if (property.getStorageType() == Date.class)
    {
      TimeControl calendar = new TimeControl();
      calendar.setValue(new Date());
      propertyComponent = calendar;
      bindablePropertyName = "date";
    }
    else
    {
      throw new RuntimeException("Not implemented yet");
    }
    
    return new Pair<Component, String>(propertyComponent, bindablePropertyName);
  }
}
