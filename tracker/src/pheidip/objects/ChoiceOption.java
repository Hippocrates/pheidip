package pheidip.objects;

import pheidip.util.IdUtils;
import pheidip.util.StringUtils;

public class ChoiceOption 
{
	private int choiceId;
	private String name;
  private int id = IdUtils.generateId();
  private Choice choice;
	
  public ChoiceOption()
  {
  }
  
	public ChoiceOption(int id, String name, int choiceId)
	{
	  this.setId(id);
		this.setChoiceId(choiceId);
		this.setName(name);
	}
	
	public int getChoiceId()
	{
		return this.choiceId;
	}
	
	private void setChoiceId(int choiceId)
  {
    this.choiceId = choiceId;
  }

  public String getName()
	{
		return this.name;
	}

  private void setName(String name)
  {
    this.name = name == null ? null : name.toLowerCase();
  }

  public int getId()
  {
    return id;
  }
  
  private void setId(int id)
  {
    this.id = id;
  }

  public void setChoice(Choice choice)
  {
    this.choice = choice;
  }

  public Choice getChoice()
  {
    return choice;
  }

  public String toString()
  {
    return StringUtils.isEmptyOrNull(this.getName()) ? "#" + this.getId() : this.getName();
  }
}
