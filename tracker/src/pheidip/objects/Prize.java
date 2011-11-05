package pheidip.objects;

import pheidip.util.IdUtils;

public class Prize
{
  private int id = IdUtils.generateId();
  private String name;
  private String imageURL;
  private String description;
  private Donor winner;
  
  public Prize()
  {
    //this.id
  }
  
  public Prize(int id, String name, String imageURL, String description, Donor winner)
  {
    this.setId(id);
    this.setName(name);
    this.setImageURL(imageURL);
    this.setDescription(description);
    this.setWinner(winner);
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
    this.name = name == null ? null : name.toLowerCase();
  }

  public String getImageURL()
  {
    return this.imageURL;
  }

  public void setImageURL(String imageURL)
  {
    this.imageURL = imageURL;
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
}
