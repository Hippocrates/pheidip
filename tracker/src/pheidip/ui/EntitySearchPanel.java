package pheidip.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import pheidip.objects.Entity;
import pheidip.objects.SearchParameters;
import java.awt.GridBagLayout;

@SuppressWarnings("serial")
public abstract class EntitySearchPanel<T extends Entity> extends JPanel
{
  private List<T> results;
  private boolean multiSelectAllowed;
  
  public EntitySearchPanel(SearchParameters<T> parameters, Class<? extends T>... creationClasses)
  {
    this.results = Collections.unmodifiableList(new ArrayList<T>());
    this.multiSelectAllowed = false;
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{0};
    gridBagLayout.rowHeights = new int[]{0};
    gridBagLayout.columnWeights = new double[]{Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{Double.MIN_VALUE};
    setLayout(gridBagLayout);
  }

  public boolean isMultiSelectAllowed()
  {
    return this.multiSelectAllowed;
  }
  
  public void setMultiSelectAllowed(boolean enabled)
  {
    boolean oldValue = this.multiSelectAllowed;
    this.multiSelectAllowed = enabled;
    this.firePropertyChange("multiSelectAllowed", oldValue, this.multiSelectAllowed);
  }
  
  public T getResult()
  {
    return this.results.size() > 0 ? this.results.get(0) : null;
  }
  
  public List<T> getResults()
  {
    return this.results;
  }
  
  @SuppressWarnings("unchecked")
  public void setResult(T result)
  {
    this.setResults(Arrays.<T>asList(result));
  }
  
  // should probably not name this something that java beans will pick up on
  // since this should only be set by the search panel itself?
  public void setResults(List<T> results)
  {
    T oldResult = this.getResult();
    List<T> oldValue = this.results;
    this.results = Collections.unmodifiableList(results);
    this.firePropertyChange("results", oldValue, this.results);
    this.firePropertyChange("result", oldResult, this.getResult());
  }
}
