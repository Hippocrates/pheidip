package pheidip.db.hibernate;

public class HQLAliasProvider
{
  private int numAliases;
  
  public HQLAliasProvider()
  {
    this.numAliases = 0;
  }
  
  public String generateNewAlias()
  {
    String alias = "a" + this.numAliases;
    ++this.numAliases;
    return alias;
  }
}
