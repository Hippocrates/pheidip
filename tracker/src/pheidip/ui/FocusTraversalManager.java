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
    for (int currentIndex = this.clampToSize(this.components.indexOf(component) + 1); currentIndex != this.components.indexOf(component); currentIndex = this.clampToSize(currentIndex + 1))
    {
      Component current = this.components.get(currentIndex);
      if (current.isEnabled())
      {
        return current;
      }
    }
    
    return component;
  }

  @Override
  public Component getComponentBefore(Container container, Component component)
  {
    for (int currentIndex = this.clampToSize(this.components.indexOf(component) - 1); currentIndex != this.components.indexOf(component); currentIndex = this.clampToSize(currentIndex - 1))
    {
      Component current = this.components.get(currentIndex);
      if (current.isEnabled())
      {
        return current;
      }
    }
    
    return component;
  }

  @Override
  public Component getDefaultComponent(Container arg0)
  {
    return getFirstComponent(arg0);
  }

  @Override
  public Component getFirstComponent(Container arg0)
  {
    Component first = this.components.get(0);
    if (first.isEnabled())
    {
      return first;
    }
    else
    {
      return this.getComponentAfter(arg0, first);
    }
  }

  @Override
  public Component getLastComponent(Container arg0)
  {
    return this.getComponentBefore(arg0, this.getFirstComponent(arg0));
  }

}
