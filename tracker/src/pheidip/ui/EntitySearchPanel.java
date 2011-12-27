package pheidip.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pheidip.objects.Entity;

@SuppressWarnings("serial")
public abstract class EntitySearchPanel<T extends Entity> extends TabPanel
{
  private List<T> results;
  private boolean multiSelectionAllowed;
  
  public EntitySearchPanel(boolean multiSelectionAllowed)
  {
    this.multiSelectionAllowed = multiSelectionAllowed;
    this.results = Collections.unmodifiableList(new ArrayList<T>());
  }
  
  public T getResult()
  {
    return results.size() > 0 ? results.iterator().next() : null;
  }
  
  public List<T> getResults()
  {
    return this.results;
  }
  
  public boolean getIsMultiSelectionAllowed()
  {
    return this.multiSelectionAllowed;
  }
  
  protected void setResult(T result)
  {
    T oldResult = this.getResult();
    List<T> temp = new ArrayList<T>();
    temp.add(result);
    this.setResults(temp);
    this.firePropertyChange("result", oldResult, this.getResult());
  }
  
  protected void setResults(List<T> results)
  {
    List<T> oldResults = this.results;
    this.results = Collections.unmodifiableList(results);
    this.firePropertyChange("results", oldResults, this.results);
  }
}
