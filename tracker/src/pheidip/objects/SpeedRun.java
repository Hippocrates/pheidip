package pheidip.objects;

public class SpeedRun
{
  private int id;
  private String name;
  
  public SpeedRun(int id, String name)
  {
    this.id = id;
    this.name = name;
  }

  public int getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }
  
  public String toString()
  {
    return this.getName();
  }
}
