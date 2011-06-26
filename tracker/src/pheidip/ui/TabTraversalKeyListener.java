package pheidip.ui;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TabTraversalKeyListener extends KeyAdapter
{
  private Component target;

  TabTraversalKeyListener(Component target)
  {
    this.target = target;
  }
  
  public void keyPressed(KeyEvent e) 
  {  
    if (e.getKeyCode() == KeyEvent.VK_TAB) 
    {  
      e.consume();  
      if (e.isShiftDown())
      {
        this.target.transferFocusBackward();
      }
      else
      {
        this.target.transferFocus();
      }
    }
  }  
}
