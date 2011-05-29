package pheidip.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.List;

public class FocusTraversalManager extends FocusTraversalPolicy
{
  List<Component> components;
  
  public FocusTraversalManager(Component[] components)
  {
    this.components = new ArrayList<Component>();
    
    for (Component c : components)
    {
      this.components.add(c);
    }
  }
  
  public FocusTraversalManager(List<Component> components)
  {
    this.components = new ArrayList<Component>(components);
  }
  
  private int clampToSize(int i)
  {
    if (i < 0)
    {
      i = this.components.size() - 1;
    }
    
    return i % this.components.size();
  }
  
  @Override
  public Component getComponentAfter(Container container, Component component)
  {
    return this.components.get(this.clampToSize(this.components.indexOf(component) + 1));
  }

  @Override
  public Component getComponentBefore(Container container, Component component)
  {
    return this.components.get(this.clampToSize(this.components.indexOf(component) - 1));
  }

  @Override
  public Component getDefaultComponent(Container arg0)
  {
    return this.components.get(0);
  }

  @Override
  public Component getFirstComponent(Container arg0)
  {
    return this.components.get(0);
  }

  @Override
  public Component getLastComponent(Container arg0)
  {
    return this.components.get(this.components.size() - 1);
  }

}
