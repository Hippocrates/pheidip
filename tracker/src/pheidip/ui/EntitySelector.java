package pheidip.ui;

import javax.swing.JPanel;

import pheidip.logic.ProgramInstance;
import pheidip.model.EntityPropertiesSync;
import pheidip.model.NullToBooleanConverterMethod;
import pheidip.model.ObjectProperty;
import pheidip.objects.Entity;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class EntitySelector<T extends Entity> extends JPanel implements ActionListener
{
  private boolean navigationAllowed;
  private boolean nullSelectionAllowed;
  private boolean readOnly;
  private T entity;
  private EntityPropertiesSync sync;
  private JButton openButton;
  private JButton clearSelectionButton;
  private JButton setSelectionButton;
  private MainWindow owner;
  private JTextField entityField;
  private Class<T> entityClass;
  private FocusTraversalManager tabOrder;

  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{135, 0, -1, 0};
    gridBagLayout.rowHeights = new int[]{20, 0};
    gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0};
    gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    entityField = new JTextField();
    entityField.setEditable(false);
    GridBagConstraints gbc_textField = new GridBagConstraints();
    gbc_textField.insets = new Insets(0, 0, 0, 5);
    gbc_textField.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField.gridx = 0;
    gbc_textField.gridy = 0;
    add(entityField, gbc_textField);
    entityField.setColumns(10);
    
    if (this.navigationAllowed)
    {
      openButton = new JButton("Open");
      GridBagConstraints gbc_openButton = new GridBagConstraints();
      gbc_openButton.insets = new Insets(0, 0, 0, 5);
      gbc_openButton.gridx = 1;
      gbc_openButton.gridy = 0;
      gbc_openButton.fill = GridBagConstraints.HORIZONTAL;
      add(openButton, gbc_openButton);
    }
    
    if (this.nullSelectionAllowed && !this.readOnly)
    {
      clearSelectionButton = new JButton("Clear");
      GridBagConstraints gbc_clearSelectionButton = new GridBagConstraints();
      gbc_clearSelectionButton.fill = GridBagConstraints.HORIZONTAL;
      gbc_clearSelectionButton.insets = new Insets(0, 0, 0, 5);
      gbc_clearSelectionButton.gridx = 2;
      gbc_clearSelectionButton.gridy = 0;
      add(clearSelectionButton, gbc_clearSelectionButton);
    }
    
    if (!this.readOnly)
    {
      setSelectionButton = new JButton("Set...");
      GridBagConstraints gbc_setSelectionButton = new GridBagConstraints();
      gbc_setSelectionButton.fill = GridBagConstraints.HORIZONTAL;
      gbc_setSelectionButton.insets = new Insets(0, 0, 0, 5);
      gbc_setSelectionButton.gridx = 3;
      gbc_setSelectionButton.gridy = 0;
      add(setSelectionButton, gbc_setSelectionButton);
    }
  }

  @Override
  public void actionPerformed(ActionEvent ev)
  {
    try
    {
      if (ev.getSource() == this.openButton)
      {
        this.owner.openEntityTab(this.getEntity());
      }
      else if (ev.getSource() == this.setSelectionButton)
      {
        this.owner.createSearchDialog(entityClass);
      }
      else if (ev.getSource() == this.clearSelectionButton)
      {
        this.setEntity(null);
      }
    }
    catch(Exception e)
    {
      this.owner.report(e);
    }
  }
  
  private void initializeGUIEvents()
  {
    List<Component> tabArray = new ArrayList<Component>();
    
    if (this.navigationAllowed)
    {
      this.openButton.addActionListener(this);
      tabArray.add(this.openButton);
      sync.synchronizeProperties(new ObjectProperty(this, "entity"), new ObjectProperty(this.openButton, "enabled"), NullToBooleanConverterMethod.getInstance());
    }
    
    if (!this.readOnly && this.nullSelectionAllowed)
    {
      this.clearSelectionButton.addActionListener(this);
      tabArray.add(this.clearSelectionButton);
      sync.synchronizeProperties(new ObjectProperty(this, "entity"), new ObjectProperty(this.clearSelectionButton, "enabled"), NullToBooleanConverterMethod.getInstance());
    }
    
    if (!this.readOnly)
    {
      this.setSelectionButton.addActionListener(this);
      tabArray.add(this.setSelectionButton);
      sync.synchronizeProperties(new ObjectProperty(this, "entity"), new ObjectProperty(this.setSelectionButton, "enabled"), NullToBooleanConverterMethod.getInstance());
    }

    this.tabOrder = new FocusTraversalManager(tabArray.toArray(new Component[tabArray.size()]));
    this.setFocusTraversalPolicy(this.tabOrder);
    this.setFocusTraversalPolicyProvider(true);
    this.setFocusCycleRoot(false);
  }
  
  /**
   * @wbp.parser.constructor
   */
  public EntitySelector(MainWindow owner, ProgramInstance instance, boolean navigationAllowed, boolean nullSelectionAllowed, boolean readOnly, Class<T> entityClass)
  {
    this(owner, instance, navigationAllowed, nullSelectionAllowed, readOnly, entityClass, null);
  }
  
  public EntitySelector(MainWindow owner, ProgramInstance instance, boolean navigationAllowed, boolean nullSelectionAllowed, boolean readOnly, Class<T> entityClass, T defaultValue)
  {
    this.sync = new EntityPropertiesSync();
    this.owner = owner;
    this.entity = defaultValue;
    this.entityClass = entityClass;
    this.navigationAllowed = navigationAllowed;
    this.nullSelectionAllowed = nullSelectionAllowed;
    this.readOnly = readOnly;
    
    this.initializeGUI();
    this.initializeGUIEvents();
  }

  public T getEntity()
  {
    return this.entity;
  }
  
  public void setEntity(T entity)
  {
    T oldValue = this.entity;
    this.entityField.setText(entity == null ? "" : entity.toString());
    this.entity = entity;
    this.firePropertyChange("entity", oldValue, this.entity);
  }
}
