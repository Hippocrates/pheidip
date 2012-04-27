package meta.reflect;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;

import net.sf.cglib.beans.BeanGenerator;

import pheidip.objects.Entity;

import meta.EntityFieldDescription;
import meta.EntitySetFieldDescription;
import meta.EnumFieldDescription;
import meta.EnumSetFieldDescription;
import meta.IdFieldDescription;
import meta.IntegerFieldDescription;
import meta.MetaEntity;
import meta.MetaEntityDescription;
import meta.MetaField;
import meta.MoneyFieldDescription;
import meta.StringFieldDescription;
import meta.TimeFieldDescription;

public class MetaEntityReflector
{
  private static Map<Class<?>, MetaEntity> cachedMetaEntities = new HashMap<Class<?>, MetaEntity>();
  
  private static Class<?> getCollectionType(Method readMethod)
  {
    Type rawType = readMethod.getGenericReturnType();
      
    if (rawType instanceof ParameterizedType)
    {
      Type result = ((ParameterizedType)rawType).getActualTypeArguments()[0];
        
      if (result instanceof Class)
      {
        return (Class<?>)result;
      }
    }

    return null;
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static <T> MetaEntity getMetaEntity(Class<T> clazz)
  {
    MetaEntity result = cachedMetaEntities.get(clazz);
    
    if (result == null)
    {
      
      try
      {
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        
        ValidatorFactory validatorFactory = Validation.byDefaultProvider().configure().buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        BeanDescriptor beanDescriptor = validator.getConstraintsForClass(clazz);
        
        List<MetaField> fields = new ArrayList<MetaField>();
        
        boolean nullable = true;
        Object maxValue = null;
        Object minValue = null;
        Integer maxSize = null;
        Integer minSize = null;
        
        for (PropertyDescriptor d : beanInfo.getPropertyDescriptors())
        {
          javax.validation.metadata.PropertyDescriptor constraints = beanDescriptor.getConstraintsForProperty(d.getName());
          
          if (constraints != null)
          {
            for (ConstraintDescriptor s : constraints.getConstraintDescriptors())
            {
              Class<?> annotationClass = s.getAnnotation().annotationType();
              if (annotationClass == NotNull.class)
              {
                  nullable = false;
              }
              else if (annotationClass == Size.class)
              {
                minSize = ((Size)s.getAnnotation()).min();
                maxSize = ((Size)s.getAnnotation()).max();
              }
              else if (annotationClass == Min.class)
              {
                minValue = ((Min)s.getAnnotation()).value();
              }
              else if (annotationClass == Max.class)
              {
                minValue = ((Max)s.getAnnotation()).value();
              }
            }
          }
  
          if (d.getPropertyType() == BigDecimal.class)
          {
            fields.add(new MetaField(d.getName(), new MoneyFieldDescription(nullable)));
          }
          else if (d.getPropertyType() == Integer.class || d.getPropertyType() == Integer.TYPE)
          {
            if (d.getPropertyType() == Integer.TYPE)
            {
              nullable = false;
            }
            if (d.getName().equalsIgnoreCase("id"))
            {
              fields.add(new MetaField(d.getName(), new IdFieldDescription()));
            }
            else
            {
              fields.add(new MetaField(d.getName(), new IntegerFieldDescription(nullable, (Integer)minValue, (Integer)maxValue)));
            }
          }
          else if (d.getPropertyType() == EnumSet.class)
          {
            fields.add(new MetaField(d.getName(), new EnumSetFieldDescription(getCollectionType(d.getReadMethod()))));
          }
          else if (d.getPropertyType().isEnum())
          {
            fields.add(new MetaField(d.getName(), new EnumFieldDescription(d.getPropertyType(), nullable)));
          }
          else if (d.getPropertyType() == Date.class)
          {
            fields.add(new MetaField(d.getName(), new TimeFieldDescription(nullable, TimeFieldDescription.DEFAULT_DATE_FORMAT, (Date)minValue, (Date)maxValue)));
          }
          else if (d.getPropertyType() == String.class)
          {
            fields.add(new MetaField(d.getName(), new StringFieldDescription(nullable, true, minSize, maxSize)));
          }
          else if (Entity.class.isAssignableFrom(d.getPropertyType()))
          {
            fields.add(new MetaField(d.getName(), new EntityFieldDescription(d.getPropertyType(), nullable)));
          }
          else if (d.getPropertyType() == Set.class)
          {
            fields.add(new MetaField(d.getName(), new EntitySetFieldDescription(getCollectionType(d.getReadMethod()))));
          }
        }
  
        String className = clazz.getSimpleName();
        result = new MetaEntity(className.substring(className.lastIndexOf(".") + 1), clazz, new MetaEntityDescription(fields));
        cachedMetaEntities.put(beanInfo.getClass(), result);
      }
      catch (IntrospectionException e)
      {
        throw new RuntimeException(e);
      }
    }
    
    return result;
  }
  
  public static Class<?> createClassForMetaEntity(MetaEntityDescription description)
  {
    BeanGenerator generator = new BeanGenerator();
    
    for (MetaField field : description.getFields())
    {
      generator.addProperty(field.getName(), field.getFieldDescription().getStorageClass());
    }

    Object createdClass = generator.createClass();
    
    return (Class<?>)createdClass;
  }
  
  public static MetaEntity createNewMetaEntity(String name, MetaEntityDescription description)
  {
    return new MetaEntity(name, createClassForMetaEntity(description), description);
  }
}
