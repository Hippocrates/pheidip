package pheidip.model;

public class SearchProperty
{
  private EntityProperty targetProperty;
  private EntityProperty searchProperty;
  private ComparisonOperator operator;
  
  public SearchProperty(EntityProperty searchProperty, EntityProperty targetProperty, ComparisonOperator operator)
  {
    this.targetProperty = targetProperty;
    this.searchProperty = searchProperty;
    this.operator = operator;
  }
  
  public EntityProperty getSearchProperty()
  {
    return this.searchProperty;
  }
  
  public EntityProperty getTargetProperty()
  {
    return this.targetProperty;
  }
  
  public ComparisonOperator getOperator()
  {
    return this.operator;
  }
}
