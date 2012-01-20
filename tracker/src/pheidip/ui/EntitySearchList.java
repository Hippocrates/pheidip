package pheidip.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pheidip.logic.EntitySearcher;
import pheidip.objects.Entity;
import pheidip.objects.SearchParameters;

import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class EntitySearchList<T extends Entity> extends JPanel
{
  private SearchParameters<T> searchParams;
  private EntitySearcher<T> searcher;
  private List<T> cachedList;
  private JList resultsList;
  private JButton previousButton;
  private JButton nextButton;
  private boolean multiSelectAllowed = false;
  private ActionHandler actionHandler;

  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{0, 0, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0};
    gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    JScrollPane scrollPane = new JScrollPane();
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.gridwidth = 2;
    gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridx = 0;
    gbc_scrollPane.gridy = 0;
    add(scrollPane, gbc_scrollPane);
    
    resultsList = new JList();
    resultsList.setEnabled(false);
    resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    scrollPane.setViewportView(resultsList);
    
    previousButton = new JButton("Previous");
    GridBagConstraints gbc_previousButton = new GridBagConstraints();
    gbc_previousButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_previousButton.insets = new Insets(0, 0, 0, 5);
    gbc_previousButton.gridx = 0;
    gbc_previousButton.gridy = 1;
    add(previousButton, gbc_previousButton);
    
    nextButton = new JButton("Next");
    GridBagConstraints gbc_nextButton = new GridBagConstraints();
    gbc_nextButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_nextButton.gridx = 1;
    gbc_nextButton.gridy = 1;
    add(nextButton, gbc_nextButton);
  }
  
  private class ActionHandler implements ListSelectionListener, ActionListener
  {
    @SuppressWarnings("unchecked")
    @Override
    public void valueChanged(ListSelectionEvent ev)
    {
      try
      {
        if (ev.getSource() == resultsList && !ev.getValueIsAdjusting())
        {
          List<T> oldValue = getResults();
          T oldResult = getResult();
          
          List<T> results = new ArrayList<T>();
          
          for (Object v : resultsList.getSelectedValues())
          {
            results.add((T) v);
          }

          cachedList = Collections.unmodifiableList(results);
          firePropertyChange("results", oldValue, getResults());
          firePropertyChange("result", oldResult, getResult());
        }
      }
      catch(Exception e)
      {
        JOptionPane.showMessageDialog(EntitySearchList.this, "Error: " + e.getMessage(), "Error", JOptionPane.YES_OPTION);
      }
    }

    @Override
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == nextButton)
        {
          navigateToNext();
        }
        else if (ev.getSource() == previousButton)
        {
          navigateToPrevious();
        }
      }
      catch(Exception e)
      {
        UIConfiguration.reportError(e);
      }
    }
    
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    this.nextButton.addActionListener(this.actionHandler);
    this.previousButton.addActionListener(this.actionHandler);
    
    this.resultsList.addListSelectionListener(this.actionHandler);
    
    List<Component> tabOrder = new ArrayList<Component>();
    
    tabOrder.add(this.resultsList);
    tabOrder.add(this.previousButton);
    tabOrder.add(this.nextButton);
    
    this.setFocusTraversalPolicy(new FocusTraversalManager(tabOrder));
    this.setFocusTraversalPolicyProvider(true);
  }
  
  public EntitySearchList(EntitySearcher<T> searcher)
  {
    this.searcher = searcher;
    this.cachedList = new ArrayList<T>();

    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.setSearchListContents(new ArrayList<T>());
  }

  public T getResult()
  {
    return this.cachedList.size() > 0 ? this.cachedList.get(0) : null;
  }

  public List<T> getResults()
  {
    return this.cachedList;
  }
  
  public SearchParameters<T> getSearchParams()
  {
    return this.searchParams;
  }
  
  public void setSearchParams(SearchParameters<T> searchParams)
  {
    SearchParameters<T> oldValue = this.searchParams;
    this.searchParams = searchParams;
    this.runSearch();
    this.firePropertyChange("searchParams", oldValue, this.searchParams);
  }
  
  public boolean isMultiSelectAllowed()
  {
    return this.multiSelectAllowed;
  }
  
  public void setMultiSelectAllowed(boolean enabled)
  {
    boolean oldValue = this.multiSelectAllowed;
    this.multiSelectAllowed = enabled;
    this.resultsList.setSelectionMode(this.multiSelectAllowed ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION);
    this.firePropertyChange("multiSelectAllowed", oldValue, this.multiSelectAllowed);
  }
  
  private void runSearch()
  {
    List<T> results = this.searcher.runSearch(this.searchParams);
    
    this.setSearchListContents(results);
  }
  
  private void setSearchListContents(List<T> results)
  {
    DefaultListModel listData = new DefaultListModel();
    
    for (T result : results)
    {
      listData.addElement(result);
    }
    
    this.resultsList.setModel(listData);
    if (this.resultsList.getModel().getSize() > 0)
    {
      this.resultsList.setEnabled(true);
      this.resultsList.setSelectedIndex(0);
    }
    else
    {
      this.resultsList.setEnabled(false);
    }
    
    this.nextButton.setEnabled(this.isEnabled() && this.searcher.getHasNext());
    this.previousButton.setEnabled(this.isEnabled() && this.searcher.getHasPrevious());
  }
  
  private void navigateToNext()
  {
    this.setSearchListContents(this.searcher.moveNext());
  }
  
  private void navigateToPrevious()
  {
    this.setSearchListContents(this.searcher.movePrevious());
  }
}
