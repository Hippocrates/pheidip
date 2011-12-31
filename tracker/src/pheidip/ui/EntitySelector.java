package pheidip.ui;

import javax.swing.JPanel;

import pheidip.logic.ProgramInstance;
import pheidip.model.AggregateBooleanProperty;
import pheidip.model.BooleanInverseConverterMethod;
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
  private AggregateBooleanProperty openable;
  private AggregateBooleanProperty clearable;
  private AggregateBooleanProperty nullable;
  private AggregateBooleanProperty settable;

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
    
    openButton = new JButton("Open");
    GridBagConstraints gbc_openButton = new GridBagConstraints();
    gbc_openButton.insets = new Insets(0, 0, 0, 5);
    gbc_openButton.gridx = 1;
    gbc_openButton.gridy = 0;
    gbc_openButton.fill = GridBagConstraints.HORIZONTAL;
    add(openButton, gbc_openButton);

    clearSelectionButton = new JButton("Clear");
    GridBagConstraints gbc_clearSelectionButton = new GridBagConstraints();
    gbc_clearSelectionButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_clearSelectionButton.insets = new Insets(0, 0, 0, 5);
    gbc_clearSelectionButton.gridx = 2;
    gbc_clearSelectionButton.gridy = 0;
    add(clearSelectionButton, gbc_clearSelectionButton);

    setSelectionButton = new JButton("Set...");
    GridBagConstraints gbc_setSelectionButton = new GridBagConstraints();
    gbc_setSelectionButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_setSelectionButton.insets = new Insets(0, 0, 0, 5);
    gbc_setSelectionButton.gridx = 3;
    gbc_setSelectionButton.gridy = 0;
    add(setSelectionButton, gbc_setSelectionButton);
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
    
    this.openable = new AggregateBooleanProperty();
    this.openable.addSource(new ObjectProperty(this, "entity"), NullToBooleanConverterMethod.getInstance());
    this.openable.addSource(new ObjectProperty(this, "enabled"));
    this.openable.addSource(new ObjectProperty(this, "navigationAllowed"));
    
    this.openButton.addActionListener(this);
    tabArray.add(this.openButton);
    
    sync.synchronizeProperties(new ObjectProperty(this.openable, "value"), new ObjectProperty(this.openButton, "enabled"));
    sync.synchronizeProperties(new ObjectProperty(this, "navigationAllowed"), new ObjectProperty(this.openButton, "visible"));

    this.clearable = new AggregateBooleanProperty();
    this.clearable.addSource(new ObjectProperty(this, "entity"), NullToBooleanConverterMethod.getInstance());
    this.clearable.addSource(new ObjectProperty(this, "enabled"));
    this.clearable.addSource(new ObjectProperty(this, "readOnly"), BooleanInverseConverterMethod.getInstance());
    
    this.nullable = new AggregateBooleanProperty();
    this.nullable.addSource(new ObjectProperty(this, "readOnly"), BooleanInverseConverterMethod.getInstance());
    this.nullable.addSource(new ObjectProperty(this, "nullSelectionAllowed"));
    
    this.clearSelectionButton.addActionListener(this);
    tabArray.add(this.clearSelectionButton);
    
    sync.synchronizeProperties(new ObjectProperty(this.clearable, "value"), new ObjectProperty(this.clearSelectionButton, "enabled"));
    sync.synchronizeProperties(new ObjectProperty(this.nullable, "value"), new ObjectProperty(this.clearSelectionButton, "visible"));
    
    this.settable = new AggregateBooleanProperty();
    this.settable.addSource(new ObjectProperty(this, "enabled"));
    this.settable.addSource(new ObjectProperty(this, "readOnly"), BooleanInverseConverterMethod.getInstance());
    
    this.setSelectionButton.addActionListener(this);
    tabArray.add(this.setSelectionButton);
    sync.synchronizeProperties(new ObjectProperty(this.settable, "value"), new ObjectProperty(this.setSelectionButton, "enabled"));
    sync.synchronizeProperties(new ObjectProperty(this, "readOnly"), new ObjectProperty(this.setSelectionButton, "visible"), BooleanInverseConverterMethod.getInstance());
    
    this.tabOrder = new FocusTraversalManager(tabArray.toArray(new Component[tabArray.size()]));
    this.setFocusTraversalPolicy(this.tabOrder);
    this.setFocusTraversalPolicyProvider(true);
    this.setFocusCycleRoot(false);
  }

  public EntitySelector(MainWindow owner, ProgramInstance instance, Class<T> entityClass)
  {
    this.sync = new EntityPropertiesSync();
    this.owner = owner;
    this.entity = null;
    this.entityClass = entityClass;
    this.navigationAllowed = false;
    this.nullSelectionAllowed = false;
    this.readOnly = false;
    
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
  
  public boolean isNavigationAllowed()
  {
    return this.navigationAllowed;
  }
  
  public void setNavigationAllowed(boolean value)
  {
    boolean oldValue = this.navigationAllowed;
    this.navigationAllowed = value;
    this.firePropertyChange("navigationAllowed", oldValue, this.navigationAllowed);
  }
  
  public boolean isNullSelectionAllowed()
  {
    return this.nullSelectionAllowed;
  }
  
  public void setNullSelectionAllowed(boolean value)
  {
    boolean oldValue = this.nullSelectionAllowed;
    this.nullSelectionAllowed = value;
    this.firePropertyChange("nullSelectionAllowed", oldValue, this.nullSelectionAllowed);
  }
  
  public boolean isReadOnly()
  {
    return this.readOnly;
  }
  
  public void setReadOnly(boolean value)
  {
    boolean oldValue = this.readOnly;
    this.readOnly = value;
    this.firePropertyChange("readOnly", oldValue, this.readOnly);
  }
}
