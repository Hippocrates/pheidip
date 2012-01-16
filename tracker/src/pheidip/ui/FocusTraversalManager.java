package pheidip.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.text.JTextComponent;

import com.google.common.collect.Lists;

public class FocusTraversalManager extends FocusTraversalPolicy
{
  List<Component> components;
  
  public FocusTraversalManager(Component... components)
  {
    this(Arrays.asList(components));
  }
  
  public FocusTraversalManager(List<Component> components)
  {
    this.components = Collections.unmodifiableList(components);
  }
  
  private Component getComponentAfter(Container container, Component target, boolean findFirst)
  {
    boolean found = false;
    
    for (Component current : this.components)
    {
      if (found || findFirst)
      {
        Component result = getUnderlyingFirst(current);
        
        if (isComponentSelectable(result))
        {
          return result;
        }
      }
      else
      {
        if (current == target)
        {
          found = true;
        }
        else if (isFocusTraversalPolicyProvider(current))
        {
          Container currentContainer = ((Container)current);
          FocusTraversalPolicy policy = currentContainer.getFocusTraversalPolicy();
          
          Component next = policy.getComponentAfter(currentContainer, target);
          
          if (next != null)
          {
            return next;
          }
          else if (target == policy.getLastComponent(currentContainer))
          {
            found = true;
          }
        }
      }
    }
    
    if (!findFirst && found && container.isFocusCycleRoot())
    {
      return this.getComponentAfter(container, target, true);
    }
    else
    {
      return null;
    }
  }

  @Override
  public Component getComponentAfter(Container container, Component target)
  {
    return this.getComponentAfter(container, target, false);
  }

  private Component getComponentBefore(Container container, Component target, boolean findFirst)
  {
    boolean found = false;
    
    for (Component current : Lists.reverse(this.components))
    {
      if (found || findFirst)
      {
        Component result = getUnderlyingLast(current);
        
        if (isComponentSelectable(result))
        {
          return result;
        }
      }
      else
      {
        if (current == target)
        {
          found = true;
        }
        else if (isFocusTraversalPolicyProvider(current))
        {
          Container currentContainer = ((Container)current);
          FocusTraversalPolicy policy = currentContainer.getFocusTraversalPolicy();
          
          Component next = policy.getComponentBefore(currentContainer, target);
          
          if (next != null)
          {
            return next;
          }
          else if (target == policy.getFirstComponent(currentContainer))
          {
            found = true;
          }
        }
      }
    }
    
    if (!findFirst && found && container.isFocusCycleRoot())
    {
      return this.getComponentBefore(container, target, true);
    }
    else
    {
      return null;
    }
  }

  @Override
  public Component getComponentBefore(Container container, Component target)
  {
    return this.getComponentBefore(container, target, false);
  }

  @Override
  public Component getDefaultComponent(Container container)
  {
    return this.getFirstComponent(container);
  }

  @Override
  public Component getFirstComponent(Container container)
  {
    return this.getComponentAfter(container, null, true);
  }
  
  @Override
  public Component getLastComponent(Container container)
  {
    return this.getComponentBefore(container, null, true);
  }
  
  private static boolean isFocusTraversalPolicyProvider(Component current)
  {
    return current instanceof Container && ((Container)current).getFocusTraversalPolicy() != null;
  }
  
  private static Component getUnderlyingFirst(Component current)
  {
    if (isFocusTraversalPolicyProvider(current))
    {
      return ((Container)current).getFocusTraversalPolicy().getFirstComponent(((Container)current));
    }
    else
    {
      return current;
    }
  }
  
  private static Component getUnderlyingLast(Component current)
  {
    if (current instanceof Container && ((Container)current).getFocusTraversalPolicy() != null)
    {
      return ((Container)current).getFocusTraversalPolicy().getLastComponent(((Container)current));
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
