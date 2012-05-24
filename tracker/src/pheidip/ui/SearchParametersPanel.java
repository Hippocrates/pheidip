package pheidip.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import meta.search.MetaSearchEntity;
import meta.search.MetaSearchField;
import pheidip.logic.ProgramInstance;
import pheidip.model.EntityPropertiesSync;
import pheidip.model.ObjectProperty;
import pheidip.objects.Entity;
import pheidip.util.Pair;
import pheidip.util.StringUtils;
import pheidip.logic.EntitySearchInstance;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class SearchParametersPanel<T extends Entity> extends JPanel
{
  private MetaSearchEntity searchSpec;
  private EntitySearchInstance<T> searcher;
  private EntityPropertiesSync sync;
  private ProgramInstance instance;

  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = null;
    gridBagLayout.rowHeights = null;
    gridBagLayout.columnWeights = new double[]{0.0, 1.0};
    gridBagLayout.rowWeights = null;
    setLayout(gridBagLayout);
    
    int rowIndex = 0;
   
    List<Component> tabOrder = new ArrayList<Component>();
    
    for (MetaSearchField field : this.searchSpec.getSearchEntityDescription().getSearchFields())
    {
      GridBagConstraints labelConstraints = new GridBagConstraints();
      labelConstraints.gridx = 0;
      labelConstraints.gridy = rowIndex;
      labelConstraints.anchor = GridBagConstraints.EAST;
      labelConstraints.insets = new Insets(5, 5, 5, 5);

      JLabel label = new JLabel(StringUtils.javaToNatural(field.getName().replace('_', ' ')));
      
      this.add(label, labelConstraints);
      
      GridBagConstraints componentConstraints = new GridBagConstraints();
      componentConstraints.gridx = 1;
      componentConstraints.gridy = rowIndex;
      componentConstraints.anchor = GridBagConstraints.CENTER;
      componentConstraints.fill = GridBagConstraints.HORIZONTAL;
      componentConstraints.insets = new Insets(5, 5, 5, 5);
      
      Pair<Component, String> component = UIGenerator.generateSearcherComponent(field, this.instance);

      this.sync.synchronizeProperties(new ObjectProperty(component.getFirst(), component.getSecond()), new ObjectProperty(this.searcher.getSearchParams(), field.getName()));
      
      this.add(component.getFirst(), componentConstraints);
      
      tabOrder.add(component.getFirst());
      
      this.setFocusTraversalPolicy(new FocusTraversalManager(tabOrder));
      this.setFocusTraversalPolicyProvider(true);
      
      ++rowIndex;
    }
  }
  
  public SearchParametersPanel(EntitySearchInstance<T> searcher, ProgramInstance instance)
  {
    this.searcher = searcher;
    this.searchSpec = searcher.getSearchDescription();
    this.sync = new EntityPropertiesSync();
    this.instance = instance;
    
    this.initializeGUI();
  }
}
