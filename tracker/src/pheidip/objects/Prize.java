package pheidip.objects;

public class Prize
{
  private int id;
  private String name;
  private String imageURL;
  private String description;
  
  public Prize(int id, String name, String imageURL, String description)
  {
    this.id = id;
    this.name = name == null ? "" : name.toLowerCase();
    this.imageURL = imageURL;
    this.description = description;
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
  
  public String toString()
  {
    return this.name;
  }
}
