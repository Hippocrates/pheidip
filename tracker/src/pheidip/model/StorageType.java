package pheidip.model;

import java.text.Format;

public interface StorageType
{
  Class<?> getStorageClass();
  Format getTextFormat();
  Object[] getOptions();
  SearchParams getSearchParamType(); 
}
