package meta.search;

import java.util.ArrayList;
import java.util.List;

import meta.EntityFieldDescription;
import meta.EnumFieldDescription;
import meta.EnumSetFieldDescription;
import meta.MetaEntityDescription;
import meta.MetaField;
import meta.MetaFieldDescription;
import meta.MoneyFieldDescription;
import meta.StringFieldDescription;
import meta.TimeFieldDescription;
import meta.reflect.MetaEntityReflector;

public final class MetaSearchUtils
{
  public static String getUpperRangeName(String fieldName)
  {
    return "hi_" + fieldName;
  }
  
  public static String getLowerRangeName(String fieldName)
  {
    return "lo_" + fieldName;
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public static MetaSearchEntityDescription createSearchDescription(MetaEntityDescription target)
  {
    List<MetaSearchField> fields = new ArrayList<MetaSearchField>();
    
    for (MetaField field : target.getFields())
    {
      MetaFieldDescription description = field.getFieldDescription();
      
      if (description instanceof MoneyFieldDescription)
      {
        MoneyFieldDescription created = new MoneyFieldDescription(true);
        fields.add(new MetaSearchField(getLowerRangeName(field.getName()), created, ComparisonOperator.GEQUALS, field));
        fields.add(new MetaSearchField(getUpperRangeName(field.getName()), created, ComparisonOperator.LEQUALS, field));
      }
      else if (description instanceof TimeFieldDescription)
      {
        TimeFieldDescription cast = (TimeFieldDescription) description;
        TimeFieldDescription created = new TimeFieldDescription(true, cast.getDateFormat(), null, null);
        fields.add(new MetaSearchField(getLowerRangeName(field.getName()), created, ComparisonOperator.GEQUALS, field));
        fields.add(new MetaSearchField(getUpperRangeName(field.getName()), created, ComparisonOperator.LEQUALS, field));
      }
      else if (description instanceof StringFieldDescription)
      {
        StringFieldDescription cast = (StringFieldDescription) description;
        fields.add(new MetaSearchField(field.getName(), new StringFieldDescription(true, cast.isCaseSentitive(), cast.getMinLength(), cast.getMaxLength()), ComparisonOperator.INNERMATCH, field));
      }
      else if (description instanceof EnumFieldDescription)
      {
        EnumFieldDescription<?> cast = (EnumFieldDescription<?>) description;
        fields.add(new MetaSearchField(field.getName(), new EnumSetFieldDescription(cast.getStorageClass()), ComparisonOperator.IN, field));
      }
      else if (description instanceof EntityFieldDescription)
      {
        EntityFieldDescription<?> cast = (EntityFieldDescription) description;
        fields.add(new MetaSearchField(field.getName(), new EntityFieldDescription(cast.getStorageClass(), true), ComparisonOperator.EQUALS, field));
      }
    }
    
    return new MetaSearchEntityDescription(fields);
  }

  public static MetaSearchEntity createMetaSearchEntity(String name, MetaSearchEntityDescription description)
  {
    return new MetaSearchEntity(name, MetaEntityReflector.createClassForMetaEntity(description), description);
  }
}
