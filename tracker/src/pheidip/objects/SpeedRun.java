package pheidip.objects;

public class SpeedRun
{
  private int id;
  private String name;
  private String description;
  
  public SpeedRun(int id, String name, String description)
  {
    this.id = id;
    this.name = name == null ? null : name.toLowerCase();
    this.description = description;
  }

  public int getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    return this.description;
  }
  
  public String toString()
  {
    return this.getName();
  }
}
