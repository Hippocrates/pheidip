package pheidip.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pheidip.model.EntityPropertiesSync;
import pheidip.model.NullToBooleanConverterMethod;
import pheidip.model.ObjectProperty;
import pheidip.objects.Entity;

@SuppressWarnings("serial")
public class EntitySearchDialog<T extends Entity> extends JDialog
{
  private JPanel contentPanel;
  private EntityPropertiesSync sync;
  private EntitySearchPanel<T> internalPanel;
  private JButton okButton;
  private JButton cancelButton;

  private void intitializeGUI()
  {
    this.contentPanel = new JPanel();
    setBounds(100, 100, 450, 300);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setLayout(new FlowLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    this.contentPanel.add(this.internalPanel);
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    {
      JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);
      {
        okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
      }
      {
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
      }
    }
  }
  
  private void initializeGUIEvents()
  {
    this.sync.synchronizeProperties(new ObjectProperty(this.internalPanel, "result"), new ObjectProperty(this.okButton, "enabled"), NullToBooleanConverterMethod.getInstance());
  }
  
  public EntitySearchDialog(EntitySearchPanel<T> internalPanel)
  {
    this.sync = new EntityPropertiesSync();
    this.internalPanel = internalPanel;
    
    this.intitializeGUI();
    this.initializeGUIEvents();
  }

  public T getResult()
  {
    return this.internalPanel.getResult();
  }
  
  public List<T> getResults()
  {
    return this.internalPanel.getResults();
  }
}
