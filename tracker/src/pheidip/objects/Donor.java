package pheidip.objects;

import java.util.HashSet;
import java.util.Set;

import pheidip.util.IdUtils;
import pheidip.util.StringUtils;

public class Donor
{
  private int id;
  private String email;
  private String firstName;
  private String lastName;
  private String alias;
  private Set<Donation> donations = new HashSet<Donation>();
  private Set<Prize> prizes = new HashSet<Prize>();

  public Donor()
  {
    this.id = IdUtils.generateId();
  }
  
  public Donor(int id, String email, String alias, String firstName, String lastName)
  {
    this.setId(id);
    this.setEmail(email);
    this.setFirstName(firstName);
    this.setLastName(lastName);
    this.setAlias(alias);
  }
  
  public String getAlias()
  {
    return alias;
  }
  
  public void setAlias(String alias)
  {
    this.alias = alias == null ? null : alias.toLowerCase();
  }

  public String getFirstName()
  {
    return firstName;
  }
  
  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email == null ? null : email.toLowerCase();
  }

  public int getId()
  {
    return id;
  }
  
  public void setId(int id)
  {
    this.id = id;
  }
  
  public void setPrizes(Set<Prize> prizes)
  {
    this.prizes = prizes;
  }

  public Set<Prize> getPrizes()
  {
    return prizes;
  }
  
  public Set<Donation> getDonations()
  {
    return this.donations;
  }
  
  public void setDonations(Set<Donation> donations)
  {
    this.donations = donations;
  }
  
  public int hashCode()
  {
    return this.getId();
  }
  
  public boolean equals(Object other)
  {
    if (other instanceof Donor)
    {
      return this.getId() == ((Donor)other).getId();
    }
    else
    {
      return false;
    }
  }

  public String toString()
  {
    if (this.getAlias() == null)
    {
      String nameId = StringUtils.emptyIfNull(this.getFirstName());
      
      if (nameId.length() > 0)
      {
        nameId += " ";
      }
      
      nameId += StringUtils.emptyIfNull(this.getLastName());
      
      if (nameId.length() > 0)
      {
        return nameId;
      }
      else if (StringUtils.isEmptyOrNull(this.getEmail()))
      {
        return "#" + this.getId();
      }
      else
      {
        return this.getEmail();
      }
    }
    else
    {
      return this.getAlias();
    }
  }
}
