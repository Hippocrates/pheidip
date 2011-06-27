package pheidip.objects;

import pheidip.util.StringUtils;

public class ChoiceOption 
{
	private int choiceId;
	private String name;
  private int id;
	
	public ChoiceOption(int id, String name, int choiceId)
	{
	  this.id = id;
		this.choiceId = choiceId;
		this.name = name == null ? null : name.toLowerCase();
	}
	
	public int getChoiceId()
	{
		return this.choiceId;
	}
	
	public String getName()
	{
		return this.name;
	}

  public int getId()
  {
    return id;
  }
  
  public String toString()
  {
    return StringUtils.isEmptyOrNull(this.name) ? "#" + this.id : this.name;
  }
}
