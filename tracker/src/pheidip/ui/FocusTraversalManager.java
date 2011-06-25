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
    if (container instanceof DonationBidsPanel)
    {
      int y = 75;
      System.out.println("" + y);
    }
    
    for (int currentIndex = this.clampToSize(this.components.indexOf(component) + 1); currentIndex != this.components.indexOf(component); currentIndex = this.clampToSize(currentIndex + 1))
    {
      Component current = this.components.get(currentIndex);
      if (isComponentSelectable(current))
      {
        return getUnderlyingComponent(current);
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
      if (isComponentSelectable(current))
      {
        return getUnderlyingComponent(current);
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
    if (isComponentSelectable(first))
    {
      return getUnderlyingComponent(first);
    }
    else
    {
      return this.getComponentAfter(arg0, first);
    }
  }
  
  private Component getUnderlyingComponent(Component c)
  {
    if (c instanceof Container)
    {
      Container container = (Container) c;

      FocusTraversalPolicy ftp = container.getFocusTraversalPolicy();
      
      if (ftp != null)
      {
        return ftp.getFirstComponent(container);
      }
    }
      
    return c;
  }

  @Override
  public Component getLastComponent(Container arg0)
  {
    return this.getComponentBefore(arg0, this.getFirstComponent(arg0));
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
