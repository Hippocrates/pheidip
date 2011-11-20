package pheidip.objects;

import pheidip.util.IdUtils;
import pheidip.util.StringUtils;

public class Prize
{
  private int id = IdUtils.generateId();
  private String name;
  private String imageURL;
  private String description;
  private int sortKey;
  private Donor winner;
  
  public Prize()
  {
  }
  
  public Prize(int id, String name, String imageURL, String description, int sortKey, Donor winner)
  {
    this.setId(id);
    this.setName(name);
    this.setImageURL(imageURL);
    this.setDescription(description);
    this.setWinner(winner);
    this.setSortKey(sortKey);
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public void setId(int id)
  {
    this.id = id;
  }

  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = StringUtils.isEmptyOrNull(name) ? "#" + this.getId() : name.toLowerCase();
  }

  public String getImageURL()
  {
    return this.imageURL;
  }

  public void setImageURL(String imageURL)
  {
    this.imageURL = StringUtils.nullIfEmpty(imageURL);
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
    return this.getName() == null ? "Prize#" + this.getId() : this.getName();
  }
  
  public int hashCode()
  {
    return this.getId();
  }
  
  public boolean equals(Object other)
  {
    if (other instanceof Prize)
    {
      return this.getId() == ((Prize)other).getId();
    }
    else
    {
      return false;
    }
  }

  public void setWinner(Donor winner)
  {
    this.winner = winner;
  }

  public Donor getWinner()
  {
    return winner;
  }

  public void setSortKey(int sortKey)
  {
    this.sortKey = sortKey;
  }

  public int getSortKey()
  {
    return sortKey;
  }
}
