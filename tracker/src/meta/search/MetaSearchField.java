package meta.search;

import lombok.Getter;
import meta.MetaField;
import meta.MetaFieldDescription;

public class MetaSearchField extends MetaField
{
  @Getter
  private final MetaField targetField;
  
  @Getter
  private final ComparisonOperator operator;
  
  public MetaSearchField(String name, MetaFieldDescription fieldDescription, ComparisonOperator operator, MetaField targetField)
  {
    super(name, fieldDescription);
    this.operator = operator;
    this.targetField = targetField;
  }
}
