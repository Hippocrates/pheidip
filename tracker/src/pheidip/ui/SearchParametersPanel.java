package pheidip.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import pheidip.logic.ProgramInstance;
import pheidip.model.EntityPropertiesSync;
import pheidip.model.ObjectProperty;
import pheidip.model.SearchProperty;
import pheidip.model.SearchSpecification;
import pheidip.objects.Entity;
import pheidip.objects.SearchParameters;
import pheidip.util.Pair;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SearchParametersPanel<T extends Entity> extends JPanel
{
  private SearchParameters<T> searchParams;
  private List<Pair<Component, String>> components;
  private EntityPropertiesSync sync;
  private ProgramInstance instance;

  private void initializeGUI()
  {
    SearchSpecification spec = this.searchParams.getSearchSpecification();
    
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = null;
    gridBagLayout.rowHeights = null;
    gridBagLayout.columnWeights = new double[]{0.0, 1.0};
    gridBagLayout.rowWeights = null;
    setLayout(gridBagLayout);
    
    int rowIndex = 0;
    
    for (SearchProperty property : spec.getProperties())
    {
      GridBagConstraints labelConstraints = new GridBagConstraints();
      labelConstraints.gridx = 0;
      labelConstraints.gridy = rowIndex;
      labelConstraints.anchor = GridBagConstraints.EAST;
      labelConstraints.insets = new Insets(5, 5, 5, 5);
      
      JLabel label = new JLabel(property.getSearchProperty().getName());
      
      this.add(label, labelConstraints);
      
      GridBagConstraints componentConstraints = new GridBagConstraints();
      componentConstraints.gridx = 1;
      componentConstraints.gridy = rowIndex;
      componentConstraints.anchor = GridBagConstraints.CENTER;
      componentConstraints.fill = GridBagConstraints.HORIZONTAL;
      componentConstraints.insets = new Insets(5, 5, 5, 5);
      
      Pair<Component, String> component = UIGenerator.createPropertyComponent(property.getSearchProperty(), this.instance);
      components.add(component);
      
      this.add(component.getFirst(), componentConstraints);
      
      ++rowIndex;
    }
  }
  
  private void initializeGUIEvents()
  {
    SearchSpecification spec = this.searchParams.getSearchSpecification();
    List<SearchProperty> searchProperties = spec.getProperties();
    
    for (int i = 0; i < this.components.size(); ++i)
    {
      this.sync.synchronizeProperties(new ObjectProperty(this.components.get(i).getFirst(), this.components.get(i).getSecond()), new ObjectProperty(this.searchParams, searchProperties.get(i).getSearchProperty().getName()));
    }
    
    List<Component> tabOrder = new ArrayList<Component>();
    
    for (int i = 0; i < this.components.size(); ++i)
    {
      tabOrder.add(this.components.get(i).getFirst());
    }
    
    this.setFocusTraversalPolicy(new FocusTraversalManager(tabOrder));
    this.setFocusTraversalPolicyProvider(true);
  }
  
  public SearchParametersPanel(SearchParameters<T> searchParams, ProgramInstance instance)
  {
    this.searchParams = searchParams;
    this.instance = instance;
    this.sync = new EntityPropertiesSync();
    this.components = new ArrayList<Pair<Component, String>>();
    
    this.initializeGUI();
    this.initializeGUIEvents();
  }

  public SearchParameters<T> getParameters()
  {
    return this.searchParams;
  }
}
