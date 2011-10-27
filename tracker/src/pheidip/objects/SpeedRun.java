package pheidip.objects;

import java.util.HashSet;
import java.util.Set;

import pheidip.util.IdUtils;

public class SpeedRun
{
  private int id = IdUtils.generateId();
  private String name;
  private String description;
  private Set<Bid> bids = new HashSet<Bid>();
  
  public SpeedRun()
  {
  }
  
  public SpeedRun(int id, String name, String description)
  {
    this.setId(id);
    this.setName(name);
    this.setDescription(description);
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
    this.name = name == null ? null : name.toLowerCase();
  }

  public String getDescription()
  {
    return this.description;
  }
  
  public void setDescription(String description)
  {
    this.description = description;
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
}
