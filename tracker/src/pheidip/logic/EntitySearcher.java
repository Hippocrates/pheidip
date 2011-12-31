package pheidip.logic;

import java.util.List;

import pheidip.objects.Entity;

public abstract class EntitySearcher<T extends Entity, P>
{
  public static final int DEFAULT_SEARCH_SIZE = 20;
  
  private int searchOffset;
  private int searchSize;
  private P searchParams;

  private boolean hasMore;
  
  final public List<T> runSearch(P params)
  {
    this.searchOffset = 0;
    this.searchSize = DEFAULT_SEARCH_SIZE;
    this.searchParams = params;
    hasMore = false;
    
    return this.internalRunSearch();
  }
  
  private List<T> internalRunSearch()
  {
    // WOW this is hacky.  Okay, to see if there are more pages, I request exactly 1 more record than
    // I need.  If this extra record is present, then there is another page
    List<T> result = this.implRunSearch(this.searchParams, this.searchOffset, this.searchSize + 1);

    // strip off the extra record as neccessary
    if (result.size() > this.searchSize)
    {
      this.hasMore = true;
      result.remove(this.searchSize);
    }
    else
    {
      this.hasMore = false;
    }
    
    return result;
  }
  
  abstract protected List<T> implRunSearch(P params, int searchOffset, int searchSize);
  
  final public boolean getHasPrevious()
  {
    return this.searchParams != null && this.searchOffset > 0;
  }
  
  final public boolean getHasNext()
  {
    return this.searchParams != null && this.hasMore;
  }

  final public List<T> movePrevious()
  {
    this.searchOffset = Math.max(0, this.searchOffset - this.searchSize);
    
    return this.internalRunSearch();
  }
  
  final public List<T> moveNext()
  {
    this.searchOffset += this.searchSize;
    
    return this.internalRunSearch();
  }
}
