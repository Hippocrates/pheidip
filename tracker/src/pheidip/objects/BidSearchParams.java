package pheidip.objects;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import pheidip.model.ComparisonOperator;
import pheidip.model.EntitySpecification;
import pheidip.model.SearchProperty;
import pheidip.model.SearchSpecification;

public class BidSearchParams implements SearchEntity<Bid>
{
  private static SearchSpecification specification;
  
  private String name;
  private String description;
  private SpeedRun owner;
  private Set<BidState> states;

  public BidSearchParams(String name, String description, SpeedRun owner, Set<BidState> states)
  {
    this.setName(name);
    this.setDescription(description);
    this.setOwner(owner);
    this.setStates(states);
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getName()
  {
    return name;
  }

  public void setOwner(SpeedRun owner)
  {
    this.owner = owner;
  }

  public SpeedRun getOwner()
  {
    return owner;
  }

  public void setStates(Set<BidState> states)
  {
    this.states = new HashSet<BidState>(states);
  }

  public Set<BidState> getStates()
  {
    return Collections.unmodifiableSet(states);
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getDescription()
  {
    return description;
  }

  @Override
  public SearchSpecification getSearchSpecification()
  {
    if (specification == null)
    {
      EntitySpecification selfSpec = EntityMethods.getSpecification(this.getClass());
      EntitySpecification targetSpec = EntityMethods.getSpecification(Bid.class);

      specification = new SearchSpecification(
          new SearchProperty(selfSpec.getProperty("name"), targetSpec.getProperty("name"), ComparisonOperator.INNERMATCH),
          new SearchProperty(selfSpec.getProperty("description"), targetSpec.getProperty("description"), ComparisonOperator.INNERMATCH),
          new SearchProperty(selfSpec.getProperty("owner"), targetSpec.getProperty("speedRun"), ComparisonOperator.EQUALS),
          new SearchProperty(selfSpec.getProperty("states"), targetSpec.getProperty("bidState"), ComparisonOperator.IN));
    }
    
    return specification;
  }
  
  
}
