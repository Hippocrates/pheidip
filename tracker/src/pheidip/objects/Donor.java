package pheidip.objects;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import pheidip.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

public class Donor extends Entity
{
  @Getter @Setter @NotNull @Size(min=0, max=255)
  private String firstName = "";
  
  @Getter @Setter @NotNull @Size(min=0, max=255)
  private String lastName = "";
  
  @Getter @Setter @Size(min=0, max=255)
  private String alias;
  
  @Getter @Setter @NotNull @Size(min=1, max=255)
  private String email;
  
  @Getter @Setter @NotNull
  private Set<Donation> donations = new HashSet<Donation>();
  
  @Getter @Setter @NotNull
  private Set<Prize> prizes = new HashSet<Prize>();
  
  @Override
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
