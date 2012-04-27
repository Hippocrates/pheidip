package meta.search;

import java.util.Collections;
import java.util.List;

import meta.MetaEntityDescription;
import meta.MetaField;

public class MetaSearchEntityDescription extends MetaEntityDescription
{
  public MetaSearchEntityDescription(List<MetaSearchField> fields)
  {
    super(Collections.<MetaField>unmodifiableList(fields));
  }
  
  // these are kinda hacky, this is mostly just to remove the redundant code
  public MetaSearchField getSearchField(String name)
  {
    return (MetaSearchField) super.getField(name);
  }
  
  @SuppressWarnings("unchecked")
  public List<MetaSearchField> getSearchFields()
  {
    return (List<MetaSearchField>) (List<?>) super.getFields();
  }
}
