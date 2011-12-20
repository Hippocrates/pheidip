package pheidip.objects;

import java.util.HashSet;
import java.util.Set;

import pheidip.util.IdUtils;
import pheidip.util.StringUtils;

public class SpeedRun
{
  private int id;
  private String name;
  private String description;
  private int sortKey;
  private Set<Bid> bids = new HashSet<Bid>();
  private Set<Prize> prizeStartGame = new HashSet<Prize>();
  private Set<Prize> prizeEndGame = new HashSet<Prize>();
  
  public SpeedRun()
  {
    this.id = IdUtils.generateId();
  }
  
  public SpeedRun(int id, String name, int sortKey, String description)
  {
    this.setId(id);
    this.setName(name);
    this.setDescription(description);
    this.setSortKey(sortKey);
  }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = StringUtils.isEmptyOrNull(name) ? "" + this.getId() : name.toLowerCase();
  }

  public String getDescription()
  {
    return this.description;
  }
  
  public void setDescription(String description)
  {
    this.description = StringUtils.emptyIfNull(description);
  }

  public String toString()
  {
    return this.getName();
  }
  
  public int hashCode()
  {
    return this.getId();
  }
  
  public boolean equals(Object other)
  {
    if (other instanceof SpeedRun)
    {
      return this.getId() == ((SpeedRun)other).getId();
    }
    else
    {
      return false;
    }
  }

  public void setBids(Set<Bid> bids)
  {
    this.bids = bids;
  }

  public Set<Bid> getBids()
  {
    return bids;
  }

  public void setSortKey(int sortKey)
  {
    this.sortKey = sortKey;
  }

  public int getSortKey()
  {
    return sortKey;
  }

  public void setPrizeStartGame(Set<Prize> prizeStartGame)
  {
    this.prizeStartGame = prizeStartGame;
  }

  public Set<Prize> getPrizeStartGame()
  {
    return prizeStartGame;
  }

  public void setPrizeEndGame(Set<Prize> prizeEndGame)
  {
    this.prizeEndGame = prizeEndGame;
  }

  public Set<Prize> getPrizeEndGame()
  {
    return prizeEndGame;
  }
}
