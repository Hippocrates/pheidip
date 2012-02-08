package pheidip.objects;

import pheidip.model.ComparisonOperator;
import pheidip.model.EntitySpecification;
import pheidip.model.SearchProperty;
import pheidip.model.SearchSpecification;

public class PrizeSearchParams extends SearchParameters<Prize>
{
  private static SearchSpecification<Prize> specification;
  
  private String name;
  private String description;

  public PrizeSearchParams(String name, String description, boolean excludeIfWon)
  {
    this.setName(name);
    this.setDescription(description);
  }

  public PrizeSearchParams()
  {
  }

  @Override
  public SearchSpecification<Prize> getSearchSpecification()
  {
    if (specification == null)
    {
      EntitySpecification selfSpec = EntityMethods.getSpecification(this.getClass());
      EntitySpecification targetSpec = EntityMethods.getSpecification(Prize.class);

      specification = new SearchSpecification<Prize>(
          new SearchProperty[]{ new SearchProperty(selfSpec.getProperty("name"), targetSpec.getProperty("name"), ComparisonOperator.INNERMATCH),
          new SearchProperty(selfSpec.getProperty("description"), targetSpec.getProperty("description"), ComparisonOperator.INNERMATCH) },
          new Class<?>[]{ Prize.class });
    }
    
    return specification;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getDescription()
  {
    return description;
  }
  
}
