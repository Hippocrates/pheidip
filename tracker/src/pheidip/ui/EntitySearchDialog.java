package pheidip.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pheidip.model.EntityPropertiesSync;
import pheidip.model.NullToBooleanConverterMethod;
import pheidip.model.ObjectProperty;
import pheidip.objects.Entity;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import pheidip.logic.EntitySearchInstance;
import pheidip.logic.ProgramInstance;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class EntitySearchDialog<T extends Entity> extends JDialog
{
  private JPanel contentPanel;
  private EntityPropertiesSync sync;
  private JButton okButton;
  private JButton cancelButton;
  private EntitySearchList<T> listPanel;
  private SearchParametersPanel<T> parametersPanel;
  private JButton searchButton;
  private EntitySearchInstance<T> searcher;
  private boolean allowMultiSelect;
  private List<T> results;
  private ActionHandler actionHandler;
  private ProgramInstance instance;
  private List<JButton> creationButtons;
  private JPanel panel;
  
  private void intitializeGUI()
  {
    this.contentPanel = new JPanel();
    setBounds(100, 100, 542, 499);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    GridBagLayout gbl_contentPanel = new GridBagLayout();
    gbl_contentPanel.columnWidths = new int[]{258, 133, 0};
    gbl_contentPanel.rowHeights = new int[]{47, 0, 0, 0, 0, 0};
    gbl_contentPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
    gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
    contentPanel.setLayout(gbl_contentPanel);
    {
      parametersPanel = new SearchParametersPanel<T>(this.searcher, this.instance);
      GridBagConstraints gbc_panel_1 = new GridBagConstraints();
      gbc_panel_1.insets = new Insets(0, 0, 5, 5);
      gbc_panel_1.fill = GridBagConstraints.BOTH;
      gbc_panel_1.gridx = 0;
      gbc_panel_1.gridy = 0;
      contentPanel.add(parametersPanel, gbc_panel_1);
    }
    {
      listPanel = new EntitySearchList<T>(this.searcher);
      listPanel.setMultiSelectAllowed(this.allowMultiSelect);
      GridBagConstraints gbc_panel = new GridBagConstraints();
      gbc_panel.insets = new Insets(0, 0, 5, 0);
      gbc_panel.gridheight = 5;
      gbc_panel.fill = GridBagConstraints.BOTH;
      gbc_panel.gridx = 1;
      gbc_panel.gridy = 0;
      contentPanel.add(listPanel, gbc_panel);
    }
    {
      searchButton = new JButton("Search");
      GridBagConstraints gbc_searchButton = new GridBagConstraints();
      gbc_searchButton.anchor = GridBagConstraints.EAST;
      gbc_searchButton.insets = new Insets(0, 0, 5, 5);
      gbc_searchButton.gridx = 0;
      gbc_searchButton.gridy = 1;
      contentPanel.add(searchButton, gbc_searchButton);
    }
    {
      panel = new JPanel();
      GridBagConstraints gbc_panel = new GridBagConstraints();
      gbc_panel.anchor = GridBagConstraints.EAST;
      gbc_panel.insets = new Insets(0, 0, 0, 5);
      gbc_panel.fill = GridBagConstraints.VERTICAL;
      gbc_panel.gridx = 0;
      gbc_panel.gridy = 4;
      contentPanel.add(panel, gbc_panel);
      GridBagLayout gbl_panel = new GridBagLayout();
      gbl_panel.columnWidths = new int[]{0, 0};
      gbl_panel.rowHeights = new int[]{0, 0};
      gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
      gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
      panel.setLayout(gbl_panel);
      {
        this.creationButtons = new ArrayList<JButton>();
        
        int i = 0;
        /*
        for (Class<?> clazz : this.parameters.getSearchSpecification().getCreationClasses())
        {
          JButton creationButton = new JButton("Create new " + clazz.getSimpleName());
          GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
          gbc_btnNewButton.anchor = GridBagConstraints.EAST;
          gbc_btnNewButton.gridx = 0;
          gbc_btnNewButton.gridy = i;
          panel.add(creationButton, gbc_btnNewButton);
          this.creationButtons.add(creationButton);
          ++i;
        }*/
      }
    }
    {
      JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);
      {
        okButton = new JButton("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
      }
      {
        cancelButton = new JButton("Cancel");
        buttonPane.add(cancelButton);
      }
    }
  }
  
  private class ActionHandler implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == okButton)
        {
          okSelected();
        }
        else if (ev.getSource() == cancelButton)
        {
          cancelSelected();
        }
        else if (ev.getSource() == searchButton)
        {
          runSearch();
        }
        else if (creationButtons.contains(ev.getSource()))
        {
          int i = creationButtons.indexOf(ev.getSource());
          //createFromFields(parameters.getSearchSpecification().getCreationClasses().get(i));
        }
      }
      catch(Exception e)
      {
        e.printStackTrace();
        UIConfiguration.reportError(e);
      }
    }
    
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    this.okButton.addActionListener(this.actionHandler);
    this.cancelButton.addActionListener(this.actionHandler);
    this.searchButton.addActionListener(this.actionHandler);
  
    for (JButton b : this.creationButtons)
    {
      b.addActionListener(this.actionHandler);
    }
    
    this.sync.synchronizeProperties(new ObjectProperty(this.listPanel, "result"), new ObjectProperty(this.okButton, "enabled"), NullToBooleanConverterMethod.getInstance());
  
    this.getRootPane().setDefaultButton(this.searchButton);
    
    List<Component> tabOrder = new ArrayList<Component>();
    
    tabOrder.add(this.parametersPanel);
    tabOrder.add(this.searchButton);
    for (JButton b : this.creationButtons)
    {
      tabOrder.add(b);
    }
    tabOrder.add(this.listPanel);
    tabOrder.add(this.okButton);
    tabOrder.add(this.cancelButton);
    
    this.setFocusTraversalPolicy(new FocusTraversalManager(tabOrder));
    this.setFocusTraversalPolicyProvider(true);
  }
  
  public EntitySearchDialog(ProgramInstance instance, Class<T> clazz, boolean allowMultiSelect)
  {
    super((JFrame)null, true);
    
    this.instance = instance;
    this.sync = new EntityPropertiesSync();
    this.searcher = this.instance.getEntitySearch(clazz).createSearchInstance();
    this.allowMultiSelect = allowMultiSelect;
    this.results = Collections.unmodifiableList(new ArrayList<T>());
    
    this.intitializeGUI();
    this.initializeGUIEvents();
  }

  public T getResult()
  {
    return this.getResults().size() > 0 ? this.getResults().get(0) : null;
  }
  
  public List<T> getResults()
  {
    return this.results;
  }
  
  private void cancelSelected()
  {
    this.results = Collections.unmodifiableList(new ArrayList<T>());
    this.setVisible(false);
  }
  
  private void okSelected()
  {
    this.results = this.listPanel.getResults();
    this.setVisible(false);
  }
  
  private void runSearch()
  {
    this.listPanel.updateSearchList();
  }
  
  @SuppressWarnings("unchecked")
  private void createFromFields(Class<?> clazz) throws Exception
  {
    //T instance = this.parameters.createInstanceFromParameters(clazz);
    
    //this.instance.saveObject(instance);
    
    //this.results = Collections.unmodifiableList(Arrays.<T>asList(instance));
    //this.setVisible(false);
  }
}
