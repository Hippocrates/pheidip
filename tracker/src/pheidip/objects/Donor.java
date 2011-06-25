package pheidip.objects;

import pheidip.util.StringUtils;

public class Donor
{
  private int id;
  private String email;
  private String firstName;
  private String lastName;
  private String alias;

  public Donor(int id, String email, String alias, String firstName, String lastName)
  {
    this.id = id;
    this.email = email == null ? null : email.toLowerCase();
    this.firstName = firstName;
    this.lastName = lastName;
    this.alias = alias == null ? null : alias.toLowerCase();
  }
  
  public String getAlias()
  {
    return alias;
  }
  
  public String getFirstName()
  {
    return firstName;
  }
  
  public String getLastName()
  {
    return lastName;
  }

  public String getEmail()
  {
    return email;
  }

  public int getId()
  {
    return id;
  }
  
  public String toString()
  {
    if (this.alias == null)
    {
      String nameId = StringUtils.emptyIfNull(this.firstName);
      
      if (nameId.length() > 0)
      {
        nameId += " ";
      }
      
      nameId += StringUtils.emptyIfNull(this.lastName);
      
      if (nameId.length() > 0)
      {
        return nameId;
      }
      else if (StringUtils.isEmptyOrNull(this.email))
      {
        return "#" + this.id;
      }
      else
      {
        return this.email;
      }
    }
    else
    {
      return this.alias;
    }
  }
}
