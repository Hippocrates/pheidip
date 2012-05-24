package pheidip.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SearchSpecification<T>
{
  private List<SearchProperty> properties;
  private List<Class<?>> creationClasses;

  public SearchSpecification(List<SearchProperty> properties, List<Class<?>> creationClasses)
  {
    this.properties = Collections.unmodifiableList(new ArrayList<SearchProperty>(properties));
    this.creationClasses = Collections.unmodifiableList(creationClasses);
  }

  public SearchSpecification(SearchProperty[] properties, Class<?>[] creationClasses)
  {
    this(Arrays.asList(properties), Arrays.asList(creationClasses));
  }
  
  public List<SearchProperty> getProperties()
  {
    return this.properties;
  }
  
  public List<Class<?>> getCreationClasses()
  {
    return this.creationClasses;
  }
}
