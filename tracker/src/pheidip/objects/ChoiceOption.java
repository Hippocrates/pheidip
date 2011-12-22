package pheidip.objects;

import java.util.HashSet;
import java.util.Set;

import pheidip.util.IdUtils;
import pheidip.util.StringUtils;

public class ChoiceOption 
{
	private String name;
  private int id;
  private Choice choice;
  private Set<ChoiceBid> bids = new HashSet<ChoiceBid>();
	
  public ChoiceOption()
  {
    this.id = IdUtils.generateId(); 
  }

	public ChoiceOption(int id, String name, Choice choice)
  {
    this.setId(id);
    this.setName(name);
    this.setChoice(choice);
  }

  public String getName()
	{
		return this.name;
	}

  public void setName(String name)
  {
    this.name = StringUtils.isEmptyOrNull(name) ? "#" + this.getId() : name.toLowerCase();
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

  public void setBids(Set<ChoiceBid> bids)
  {
    this.bids = bids;
  }

  public Set<ChoiceBid> getBids()
  {
    return bids;
  }
  public String toString()
  {
    return StringUtils.isEmptyOrNull(this.getName()) ? "#" + this.getId() : this.getName();
  }

}
