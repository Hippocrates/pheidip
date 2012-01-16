package pheidip.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SearchSpecification
{
  private List<SearchProperty> properties;

  public SearchSpecification(List<SearchProperty> properties)
  {
    this.properties = Collections.unmodifiableList(new ArrayList<SearchProperty>(properties));
  }

  public SearchSpecification(SearchProperty... properties)
  {
    this(Arrays.asList(properties));
  }
  
  public List<SearchProperty> getProperties()
  {
    return this.properties;
  }
}
