package pheidip.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.JTextComponent;

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
    int currentIndex = this.components.indexOf(component);
    
    if (currentIndex != -1)
    {
      for (int i = 1; i < this.components.size(); ++i)
      {
        Component current = this.components.get(this.clampToSize(currentIndex + i));
        if (isComponentSelectable(current))
        {
          return current;
        }
      }
      return component;
    }
    
    return null;
  }

  @Override
  public Component getComponentBefore(Container container, Component component)
  {
    int currentIndex = this.components.indexOf(component);
    
    if (currentIndex != -1)
    {
      for (int i = 1; i < this.components.size(); ++i)
      {
        Component current = this.components.get(this.clampToSize(currentIndex - i));
        if (isComponentSelectable(current))
        {
          return current;
        }
      }
      return component;
    }
    
    return null;
  }

  @Override
  public Component getDefaultComponent(Container arg0)
  {
    return getFirstComponent(arg0);
  }

  @Override
  public Component getFirstComponent(Container container)
  {
    Component first = this.components.get(0);
    if (isComponentSelectable(first))
    {
      return first;
    }
    else
    {
      return this.getComponentAfter(container, first);
    }
  }
  
  @Override
  public Component getLastComponent(Container container)
  {
    return this.getComponentBefore(container, this.getFirstComponent(container));
  }
  
  private static boolean isComponentSelectable(Component c)
  {
    if (c == null)
    {
      return false;
    }
    else if (c instanceof JTextComponent && !((JTextComponent)c).isEditable())
    {
      return false;
    }
    else if (c.isEnabled())
    {
      return true;
    }
    else
    {
      return false;
    }
  }

}
