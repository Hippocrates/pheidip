package pheidip.db.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;

import pheidip.model.ComparisonOperator;
import pheidip.model.SearchProperty;
import pheidip.objects.Entity;
import pheidip.objects.SearchParameters;
import pheidip.util.StringUtils;

public final class SQLMethods
{
  public static <T extends Entity> String makeHQLSearchQueryString(SearchParameters<T> searchParams, String tableName, String... orderingProperties)
  {
    return makeHQLSearchQueryString(new HQLAliasProvider(), searchParams, tableName, orderingProperties);
  }
  
  public static <T extends Entity> String makeHQLSearchQueryString(HQLAliasProvider aliasProvider, SearchParameters<T> searchParams, String tableName, String... orderingProperties)
  {
    String entityAlias = aliasProvider.generateNewAlias();
    
    String whereClause = "";
    List<String> wherePredicates = buildWherePredicates(entityAlias, searchParams);
    
    if (wherePredicates.size() > 0)
    {
      whereClause = " where " + StringUtils.joinSeperated(wherePredicates, " AND ");
    }
    
    String orderClause = "";
    
    if (orderingProperties.length > 0)
    {
      orderClause = buildOrderClause(entityAlias, orderingProperties);
    }
    
    return "FROM " + tableName + " " + entityAlias + whereClause + orderClause;
  }
  
  public static String buildOrderClause(String entityAlias, String... orderingProperties)
  {
    List<String> orderings = new ArrayList<String>();
    
    for (String base : orderingProperties)
    {
      orderings.add(entityAlias + "." + base);
    }

    return " order by " + StringUtils.joinSeperated(orderings, ", ");
  }
  
  @SuppressWarnings("rawtypes")
  public static <T extends Entity> List<String> buildWherePredicates(String entityAlias, SearchParameters<T> searchParams)
  {
    List<String> predicates = new ArrayList<String>();
    
    for (SearchProperty searchParam : searchParams.getSearchSpecification().getProperties())
    {
      Object searchObject = searchParam.getSearchProperty().getProperty(searchParams);
      
      if (searchObject != null && !(searchObject instanceof Collection && ((Collection)searchObject).isEmpty()))
      {
        predicates.add(SQLMethods.makeSearchPredicate(entityAlias, searchParam));
      }
    }
    
    return predicates;
  }

  public static String makeSearchPredicate(String targetEntityAlias, SearchProperty searchParam)
  {
    final String targetName = searchParam.getTargetProperty().getName();
    final String searchName = searchParam.getSearchProperty().getName();
    
    switch (searchParam.getOperator())
    {
    case INNERMATCH:
      return "lower(" + targetEntityAlias  + "." + targetName + ") LIKE :" + searchName;
    case LIKE:
      return "lower(" + targetEntityAlias  + "." + targetName + ") LIKE :" + searchName;
    case IN:
      return targetEntityAlias + "." + targetName + " IN (:" + searchName + ")";
    case EQUALS:
      return targetEntityAlias + "." + targetName + " = :" + searchName;
    case GEQUALS:
      return targetEntityAlias + "." + targetName + " >= :" + searchName;
    case LEQUALS:
      return targetEntityAlias + "." + targetName + " <= :" + searchName;
    case NOTEQUALS:
      return targetEntityAlias + "." + targetName + " != :" + searchName;
    default:
      throw new RuntimeException("Error, unhandled comparison type.");
    }
  }
  
  public static <T extends Entity> void applyParametersToQuery(Query q, SearchParameters<T> searchParams)
  {
    for (SearchProperty searchParam : searchParams.getSearchSpecification().getProperties())
    {
      applyParameterToQuery(q, searchParam, searchParams);
    }
  }
  
  @SuppressWarnings("rawtypes")
  public static <T extends Entity> void applyParameterToQuery(Query q, SearchProperty searchParam, SearchParameters<T> searchParams)
  {
    final String searchName = searchParam.getSearchProperty().getName();
    final Object searchObject = searchParam.getSearchProperty().getProperty(searchParams);
    
    if (searchObject != null && !(searchObject instanceof Collection && ((Collection)searchObject).isEmpty()))
    {
      if (searchParam.getOperator() == ComparisonOperator.INNERMATCH)
      {
        q.setParameter(searchName, StringUtils.sqlInnerStringMatch(searchObject.toString()));
      }
      else if (searchParam.getOperator() == ComparisonOperator.IN && searchObject instanceof Collection)
      {
        q.setParameterList(searchName, (Collection) searchObject);
      }
      else
      {
        q.setParameter(searchName, searchObject);
      }
    }
  }
}
