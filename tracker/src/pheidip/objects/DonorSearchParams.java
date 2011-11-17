package pheidip.objects;

import pheidip.util.StringUtils;

public class DonorSearchParams
{
  public DonorSearchParams(String firstName, String lastName, String email, String alias)
  {
    this.firstName = StringUtils.nullIfEmpty(firstName);
    this.lastName = StringUtils.nullIfEmpty(lastName);
    this.email = StringUtils.nullIfEmpty(email);
    this.alias = StringUtils.nullIfEmpty(alias);
  }
  
  public String firstName;
  public String lastName;
  public String email;
  public String alias;
}
