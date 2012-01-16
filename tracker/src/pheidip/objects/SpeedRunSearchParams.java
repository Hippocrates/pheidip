package pheidip.objects;

import java.util.Date;

import pheidip.model.ComparisonOperator;
import pheidip.model.EntitySpecification;
import pheidip.model.SearchProperty;
import pheidip.model.SearchSpecification;

public class SpeedRunSearchParams implements SearchEntity<SpeedRun>
{
  private static SearchSpecification specification;
  
  private String name;
  private String description;
  private Date startTimeAfter;
  private Date startTimeBefore;
  private Date endTimeAfter;
  private Date endTimeBefore;
  
  public SpeedRunSearchParams(String name, String description, Date startTimeAfter, Date startTimeBefore, Date endTimeAfter, Date endTimeBefore)
  {
    this.setName(name);
    this.setDescription(description);
    this.setStartTimeAfter(startTimeAfter);
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

  public void setStartTimeAfter(Date startTimeAfter)
  {
    this.startTimeAfter = startTimeAfter;
  }

  public Date getStartTimeAfter()
  {
    return startTimeAfter;
  }

  public void setStartTimeBefore(Date startTimeBefore)
  {
    this.startTimeBefore = startTimeBefore;
  }

  public Date getStartTimeBefore()
  {
    return startTimeBefore;
  }

  public void setEndTimeAfter(Date endTimeAfter)
  {
    this.endTimeAfter = endTimeAfter;
  }

  public Date getEndTimeAfter()
  {
    return endTimeAfter;
  }

  public void setEndTimeBefore(Date endTimeBefore)
  {
    this.endTimeBefore = endTimeBefore;
  }

  public Date getEndTimeBefore()
  {
    return endTimeBefore;
  }

  @Override
  public SearchSpecification getSearchSpecification()
  {
    if (specification == null)
    {
      EntitySpecification selfSpec = EntityMethods.getSpecification(this.getClass());
      EntitySpecification targetSpec = EntityMethods.getSpecification(SpeedRun.class);

      specification = new SearchSpecification(
          new SearchProperty(selfSpec.getProperty("name"), targetSpec.getProperty("name"), ComparisonOperator.INNERMATCH),
          new SearchProperty(selfSpec.getProperty("description"), targetSpec.getProperty("description"), ComparisonOperator.INNERMATCH));
    }
    
    return specification;
  }
}
