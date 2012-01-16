package pheidip.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// this crams a bunch of checkboxes into a panel
// an altenative would be to use a multi-select list to specify which elements you want to include/exclude

@SuppressWarnings("serial")
public class SetSelector<E> extends JPanel
{
  private Set<E> selections;
  private List<E> options;
  private JCheckBox[] boxes;
  private ActionHandler actionHandler;
  
  private void initializeGUI()
  {
    setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    
    this.boxes = new JCheckBox[options.size()];

    for (int i = 0; i < this.options.size(); ++i)
    {
      this.boxes[i] = new JCheckBox(this.options.get(i).toString());
      this.add(this.boxes[i]);
      this.boxes[i].setSelected(true);
    }
  }
  
  private class ActionHandler implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        for (int i = 0; i < boxes.length; ++i)
        {
          if (ev.getSource() == boxes[i])
          {
            Set<E> oldSet = Collections.unmodifiableSet(new HashSet<E>(getSelections()));
            
            if (boxes[i].isSelected())
            {
              selections.add(options.get(i));
            }
            else
            {
              selections.remove(options.get(i));
            }
            SetSelector.this.firePropertyChange("selections", oldSet, getSelections());
          }
        }
      }
      catch(Exception e)
      {
        JOptionPane.showMessageDialog(SetSelector.this, e.getMessage(), "Error", JOptionPane.OK_OPTION);
      }
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    List<Component> tabOrder = new ArrayList<Component>();
    
    for (JCheckBox box : this.boxes)
    {
      tabOrder.add(box);
      box.addActionListener(this.actionHandler);
    }
    
    this.setFocusTraversalPolicy(new FocusTraversalManager(tabOrder));
  }
  
  public SetSelector(List<E> options)
  {
    this.options = Collections.unmodifiableList(options);
    
    // default all options to selected
    this.selections = new HashSet<E>(this.options);
    
    this.initializeGUI();
    this.initializeGUIEvents();
  }
  
  public SetSelector(E[] options)
  {
    this(Arrays.asList(options));
  }
  
  public Set<E> getSelections()
  {
    return Collections.unmodifiableSet(this.selections);
  }
}
