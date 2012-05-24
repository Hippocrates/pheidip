package pheidip.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.BoundPropertySupport;
import lombok.BoundSetter;
import lombok.Getter;
import lombok.Setter;
import meta.MetaEntity;
import meta.search.MetaSearchEntity;
import pheidip.db.DataAccess;
import pheidip.objects.Donation;
import pheidip.objects.Donor;
import pheidip.objects.Entity;
import pheidip.util.Pair;

@BoundPropertySupport
public class EntitySearchInstance<E extends Entity>
{
  @Getter @NotNull
  private DataAccess dataAccess;
  
  @Getter @NotNull
  private MetaEntity entityDescription;
  
  @Getter @NotNull
  private MetaSearchEntity searchDescription;
  
  @Getter @Setter @NotNull
  private Object searchParams;
  
  @Getter @BoundSetter(AccessLevel.PRIVATE)
  private boolean nextPageAvailable;
  
  @Getter @BoundSetter(AccessLevel.PRIVATE)
  private boolean previousPageAvailable;
  
  @BoundSetter(AccessLevel.PRIVATE)
  private List<E> cachedResults;
  
  @Getter @Setter
  private int pageSize = 20;
  private int pageNumber = 0;
  private Long cachedNumResults;
  
  public EntitySearchInstance(DataAccess dataAccess, MetaEntity entityDescription, MetaSearchEntity searchDescription, Object searchParams)
  {
    this.dataAccess = dataAccess;
    this.entityDescription = entityDescription;
    this.searchDescription = searchDescription;
    this.searchParams = searchParams;
    this.cachedResults = Collections.unmodifiableList(new ArrayList<E>());
  }
  
  public List<E> getResults()
  {
    return this.cachedResults;
  }
  
  private static String[] getDefaultEntityOrder(MetaEntity desc)
  {
    final Class<?> cls = desc.getStorageClass();
    if (cls == Donor.class)
    {
      return new String[]{ "alias", "firstName", "lastName", "email" };
    }
    else if (cls == Donation.class)
    {
      return new String[]{ "timeReceived" };
    }
    else if (desc.getEntityDescription().getField("name") != null)
    {
      return new String[]{ "name" };
    }
    else
    {
      return new String[]{ "id" };
    }
  }
  
  public void runSearch(String... paramOrder)
  {
    if (paramOrder.length == 0)
    {
      paramOrder = getDefaultEntityOrder(this.entityDescription);
    }
    
    Pair<Long, List<E>> results = this.dataAccess.searchEntityRange(
        this.entityDescription, this.searchDescription, 
        this.searchParams, pageNumber * pageSize, pageSize, paramOrder);
    
    List<E> oldResults = this.getResults();

    this.cachedNumResults = results.getFirst();
    this.cachedResults = Collections.unmodifiableList(results.getSecond());
    
    this.propertySupport.firePropertyChange("results", oldResults, this.getResults());

    if (cachedNumResults < ((pageNumber) * pageSize))
    {
      pageNumber = (int) (cachedNumResults / pageSize);
    }
    
    this.setPreviousPageAvailable(pageNumber > 0);
    this.setNextPageAvailable(cachedNumResults > ((pageNumber + 1) * pageSize));
  }
  
  public void nextPage()
  {
    if (this.isNextPageAvailable())
    {
      ++pageNumber;
      this.runSearch();
    }
  }
  
  public void previousPage()
  {
    if (this.isPreviousPageAvailable())
    {
      --pageNumber;
      this.runSearch();
    }
  }
}
