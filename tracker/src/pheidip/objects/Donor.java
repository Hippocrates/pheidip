package pheidip.objects;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import pheidip.util.StringUtils;

public class Donor extends Entity
{
  private String email = "#" + this.getId();
  private String firstName = "";
  private String lastName = "";
  private String alias = null;
  private Set<Donation> donations = new HashSet<Donation>();
  private Set<Prize> prizes = new HashSet<Prize>();

  public Donor()
  {
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
    String oldAlias = alias;
    this.alias = StringUtils.canonicalize(alias);
    this.firePropertyChange("alias", oldAlias, this.alias);
  }

  public String getFirstName()
  {
    return firstName;
  }
  
  public void setFirstName(String firstName)
  {
    String oldFirstName = firstName;
    this.firstName = StringUtils.emptyIfNull(firstName);
    this.firePropertyChange("firstName", oldFirstName, this.firstName);
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName(String lastName)
  {
    String oldLastName = lastName;
    this.lastName = StringUtils.emptyIfNull(lastName);
    this.firePropertyChange("lastName", oldLastName, this.lastName);
  }

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    String oldEmail = this.email;
    this.email = StringUtils.isEmptyOrNull(email) ? "#" + this.getId() : email.toLowerCase();
    this.firePropertyChange("email", oldEmail, this.email);
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
  
  public BigDecimal getDonationTotal()
  {
    BigDecimal sum = BigDecimal.ZERO;
    
    for (Donation d : this.getDonations())
    {
      sum = sum.add(d.getAmount());
    }
    
    return sum;
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
