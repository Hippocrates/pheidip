package pheidip.objects;

import pheidip.model.ComparisonOperator;
import pheidip.model.EntitySpecification;
import pheidip.model.SearchProperty;
import pheidip.model.SearchSpecification;
import pheidip.util.StringUtils;

public class DonorSearchParams extends SearchParameters<Donor>
{
  private static SearchSpecification<Donor> specification;
  
  public DonorSearchParams(String firstName, String lastName, String email, String alias)
  {
    this.setFirstName(StringUtils.nullIfEmpty(firstName));
    this.setLastName(StringUtils.nullIfEmpty(lastName));
    this.setEmail(StringUtils.nullIfEmpty(email));
    this.setAlias(StringUtils.nullIfEmpty(alias));
  }
  
  public DonorSearchParams()
  {
  }

  private String firstName;
  private String lastName;
  private String email;
  private String alias;
  
  @Override
  public SearchSpecification<Donor> getSearchSpecification()
  {
    if (specification == null)
    {
      EntitySpecification selfSpec = EntityMethods.getSpecification(this.getClass());
      EntitySpecification targetSpec = EntityMethods.getSpecification(Donor.class);

      specification = new SearchSpecification<Donor>(
          new SearchProperty[]{ new SearchProperty(selfSpec.getProperty("firstName"), targetSpec.getProperty("firstName"), ComparisonOperator.INNERMATCH),
          new SearchProperty(selfSpec.getProperty("lastName"), targetSpec.getProperty("lastName"), ComparisonOperator.INNERMATCH),
          new SearchProperty(selfSpec.getProperty("email"), targetSpec.getProperty("email"), ComparisonOperator.INNERMATCH),
          new SearchProperty(selfSpec.getProperty("alias"), targetSpec.getProperty("alias"), ComparisonOperator.INNERMATCH) },
          new Class<?>[]{ Donor.class });
    }
    
    return specification;
  }

  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getEmail()
  {
    return email;
  }

  public void setAlias(String alias)
  {
    this.alias = alias;
  }

  public String getAlias()
  {
    return alias;
  }
}
