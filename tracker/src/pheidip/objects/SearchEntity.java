package pheidip.objects;

import pheidip.model.SearchSpecification;

public interface SearchEntity<T extends Entity>
{
  SearchSpecification getSearchSpecification();
}
