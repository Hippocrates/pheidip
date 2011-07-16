package pheidip.objects;

public class Prize
{
  private int id;
  private String name;
  private String imageURL;
  private String description;
  private Integer winner;
  
  public Prize(int id, String name, String imageURL, String description, Integer winner)
  {
    this.id = id;
    this.name = name == null ? "" : name.toLowerCase();
    this.imageURL = imageURL;
    this.description = description;
    this.winner = winner;
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getImageURL()
  {
    return this.imageURL;
  }
  
  public String getDescription()
  {
    return this.description;
  }
  
  public Integer getWinner()
  {
    return this.winner;
  }
  
  public String toString()
  {
    return this.name;
  }
}
