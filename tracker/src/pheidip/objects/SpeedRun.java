package pheidip.objects;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import pheidip.util.StringUtils;

public class SpeedRun extends Entity
{
  private String name = "" + this.getId();
  private String description = "";
  private String runners = "";
  private Date startTime = new Date();
  private Date endTime = new Date();
  private int sortKey = this.getId();
  private Set<Bid> bids = new HashSet<Bid>();
  private Set<Prize> prizeStartGame = new HashSet<Prize>();
  private Set<Prize> prizeEndGame = new HashSet<Prize>();
  
  public SpeedRun()
  {
  }
  
  public SpeedRun(int id, String name, String runners, int sortKey, Date startTime, Date endTime, String description)
  {
    this.setId(id);
    this.setName(name);
    this.setRunners(runners);
    this.setDescription(description);
    this.setStartTime(startTime);
    this.setEndTime(endTime);
    this.setSortKey(sortKey);
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    String oldName = this.name;
    this.name = StringUtils.isEmptyOrNull(name) ? "" + this.getId() : name.toLowerCase();
    this.firePropertyChange("name", oldName, this.name);
  }

  public String getDescription()
  {
    return this.description;
  }
  
  public void setDescription(String description)
  {
    String oldDescription = this.description;
    this.description = StringUtils.emptyIfNull(description);
    this.firePropertyChange("description", oldDescription, this.description);
  }

  public Set<Bid> getBids()
  {
    return bids;
  }
  
  public void setBids(Set<Bid> bids)
  {
    this.bids = bids;
  }

  public int getSortKey()
  {
    return sortKey;
  }
  
  public void setSortKey(int sortKey)
  {
    int oldSortKey = this.sortKey;
    this.sortKey = sortKey;
    this.firePropertyChange("sortKey", oldSortKey, this.sortKey);
  }

  public Set<Prize> getPrizeStartGame()
  {
    return prizeStartGame;
  }
  
  public void setPrizeStartGame(Set<Prize> prizeStartGame)
  {
    this.prizeStartGame = prizeStartGame;
  }
  
  public void setPrizeEndGame(Set<Prize> prizeEndGame)
  {
    this.prizeEndGame = prizeEndGame;
  }

  public Set<Prize> getPrizeEndGame()
  {
    return prizeEndGame;
  }

  public Date getStartTime()
  {
    return startTime;
  }

  public void setStartTime(Date startTime)
  {
    if (endTime == null)
    {
      throw new RuntimeException("Error, game time must not be null.");
    }
    
    Date oldStartTime = this.startTime;
    this.startTime = startTime;
    this.firePropertyChange("startTime", oldStartTime, this.startTime);
  }
  
  public Date getEndTime()
  {
    return endTime;
  }
  
  public void setEndTime(Date endTime)
  {
    if (endTime == null)
    {
      throw new RuntimeException("Error, game time must not be null.");
    }
    
    Date oldEndTime = this.endTime;
    this.endTime = endTime;
    this.firePropertyChange("endTime", oldEndTime, this.endTime);
  }

  public String getRunners()
  {
    return runners;
  }
 
  public void setRunners(String runners)
  {
    String oldRunners = this.runners;
    this.runners = StringUtils.emptyIfNull(runners);
    this.firePropertyChange("runners", oldRunners, this.runners);
  }

  public String toString()
  {
    return this.getName();
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
}
