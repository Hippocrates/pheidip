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
  
  private List<Container> getSubContainers()
  {
    List<Container> result = new ArrayList<Container>();
    
    for (Component c : components)
    {
      if (c instanceof Container)
      {
        result.add((Container)c);
      }
    }
    
    return result;
  }
  
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

  @Override
  public Component getComponentAfter(Container container, Component component)
  {
    for (Container c : getSubContainers())
    {
      if (c.getFocusTraversalPolicy() != null)
      {
        Component result = c.getFocusTraversalPolicy().getComponentAfter(c, component);
        
        if (result != null)
        {
          return result;
        }
      }
    }
    
    int currentIndex = this.components.indexOf(component);
    
    if (currentIndex != -1)
    {
      for (int i = currentIndex + 1; i < this.components.size(); ++i)
      {
        Component current = this.components.get(i);
        if (isComponentSelectable(current))
        {
          if (current instanceof Container && ((Container)current).getFocusTraversalPolicy() != null)
          {
            return ((Container)current).getFocusTraversalPolicy().getFirstComponent(((Container)current));
          }
          else
          {
            return current;
          }
        }
      }
    }
    
    return null;
  }

  @Override
  public Component getComponentBefore(Container container, Component component)
  {
    for (Container c : getSubContainers())
    {
      if (c.getFocusTraversalPolicy() != null)
      {
        Component result = c.getFocusTraversalPolicy().getComponentBefore(c, component);
        
        if (result != null)
        {
          return result;
        }
      }
    }
    
    int currentIndex = this.components.indexOf(component);
    
    if (currentIndex != -1)
    {
      for (int i = currentIndex - 1; i >= 0; --i)
      {
        Component current = this.components.get(i);
        if (isComponentSelectable(current))
        {
          if (current instanceof Container && ((Container)current).getFocusTraversalPolicy() != null)
          {
            return ((Container)current).getFocusTraversalPolicy().getFirstComponent(((Container)current));
          }
          else
          {
            return current;
          }
        }
      }
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
      return getUnderlyingComponent(first);
    }
    else
    {
      return this.getComponentAfter(container, first);
    }
  }
  
  @Override
  public Component getLastComponent(Container container)
  {
    Component last = this.components.get(this.components.size() - 1);
    if (isComponentSelectable(last))
    {
      return getUnderlyingComponent(last);
    }
    else
    {
      return this.getComponentBefore(container, last);
    }
  }
  
  private Component getUnderlyingComponent(Component current)
  {
    if (current instanceof Container && ((Container)current).getFocusTraversalPolicy() != null)
    {
      return ((Container)current).getFocusTraversalPolicy().getFirstComponent(((Container)current));
    }
    else
    {
      return current;
    }
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
