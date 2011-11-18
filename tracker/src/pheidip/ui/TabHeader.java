package pheidip.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * This is the component that lives in the 'header' part of any tab, and 
 * automagically allows the tab to be closed by pressing the 'x'
 * 
 * Copied almost verbatem from : 
 *   http://download.oracle.com/javase/tutorial/uiswing/examples/components/TabComponentsDemoProject/src/components/ButtonTabComponent.java
 * 
 * And hence I must include the following disclaimer:
 * 
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

@SuppressWarnings("serial")
public class TabHeader extends JPanel
{
  private final JTabbedPane parent;

  public TabHeader(final JTabbedPane parent) 
  {
      super(new FlowLayout(FlowLayout.LEFT, 0, 0));
      
      if (parent == null) 
      {
        throw new NullPointerException("TabbedPane is null");
      }
      
      this.parent = parent;
      this.setOpaque(false);
      
      titleLabel = new JLabel("New Tab");
      this.add(titleLabel);
      
      //add more space between the label and the button
      titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
      
      //tab button
      JButton button = new TabButton();
      this.add(button);
      
      //add more space to the top of the component
      this.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
  }
  
  public void setTabTitle(String title)
  {
    this.titleLabel.setText(title);
  }
  
  private void closeTab()
  {
    int i = this.parent.indexOfTabComponent(this);
    
    if (i != -1) 
    {
      Component tabContent = this.parent.getComponentAt(i);
      
      if (!(tabContent instanceof TabPanel) || ((TabPanel)tabContent).confirmClose())
      {
        this.parent.remove(i);
      }
    }
  }
  
  private class TabButton extends JButton implements ActionListener
  {
    public TabButton()
    {
      int size = 17;
      this.setPreferredSize(new Dimension(size, size));
      this.setToolTipText("Close tab");
      //Make the button looks the same for all Laf's
      this.setUI(new BasicButtonUI());
      //Make it transparent
      this.setContentAreaFilled(false);
      //No need to be focusable
      this.setFocusable(false);
      this.setBorder(BorderFactory.createEtchedBorder());
      this.setBorderPainted(false);
      //Making nice rollover effect
      //we use the same listener for all buttons
      this.addMouseListener(buttonMouseListener);
      this.setRolloverEnabled(true);
      //Close the proper tab by clicking the button
      this.addActionListener(this);
    }
  
    public void actionPerformed(ActionEvent e) 
    {
      TabHeader.this.closeTab();
    }

    //paint the cross
    protected void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g.create();
      //shift the image for pressed buttons
      if (getModel().isPressed()) 
      {
          g2.translate(1, 1);
      }
      g2.setStroke(new BasicStroke(2));
      g2.setColor(Color.BLACK);
      if (getModel().isRollover()) 
      {
          g2.setColor(Color.RED);
      }
      int delta = 6;
      g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
      g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
      g2.dispose();
    }
  }

  private final static MouseListener buttonMouseListener = new MouseAdapter() 
  {
    public void mouseEntered(MouseEvent e) 
    {
      Component component = e.getComponent();
      if (component instanceof AbstractButton)
      {
          AbstractButton button = (AbstractButton) component;
          button.setBorderPainted(true);
      }
    }

    public void mouseExited(MouseEvent e) 
    {
      Component component = e.getComponent();
      if (component instanceof AbstractButton) 
      {
          AbstractButton button = (AbstractButton) component;
          button.setBorderPainted(false);
      }
    }
  };
  private JLabel titleLabel;

}
