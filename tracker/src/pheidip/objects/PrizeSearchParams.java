package pheidip.objects;

import pheidip.model.ComparisonOperator;
import pheidip.model.EntitySpecification;
import pheidip.model.SearchProperty;
import pheidip.model.SearchSpecification;

public class PrizeSearchParams implements SearchEntity<Prize>
{
  private static SearchSpecification specification;
  
  private String name;
  private String description;
  public boolean excludeIfWon;

  public PrizeSearchParams(String name, String description, boolean excludeIfWon)
  {
    this.setName(name);
    this.setDescription(description);
    this.excludeIfWon = excludeIfWon;
  }

  @Override
  public SearchSpecification getSearchSpecification()
  {
    if (specification == null)
    {
      EntitySpecification selfSpec = EntityMethods.getSpecification(this.getClass());
      EntitySpecification targetSpec = EntityMethods.getSpecification(Prize.class);

      specification = new SearchSpecification(
          new SearchProperty(selfSpec.getProperty("name"), targetSpec.getProperty("name"), ComparisonOperator.INNERMATCH),
          new SearchProperty(selfSpec.getProperty("description"), targetSpec.getProperty("description"), ComparisonOperator.INNERMATCH));
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

  private void setDescription(String description)
  {
    this.description = description;
  }

  public String getDescription()
  {
    return description;
  }
  
}
