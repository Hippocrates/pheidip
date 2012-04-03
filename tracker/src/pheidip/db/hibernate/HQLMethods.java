package pheidip.db.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;

import meta.search.ComparisonOperator;
import meta.search.MetaSearchEntity;
import meta.search.MetaSearchField;

import pheidip.util.StringUtils;

public final class HQLMethods
{
  public static <S> String makeHQLSearchQueryString(String tableName, MetaSearchEntity searchSpec, S searchParams, String... orderingProperties)
  {
    String entityAlias = "a";

    String orderClause = "";
    
    if (orderingProperties.length > 0)
    {
      orderClause = buildOrderClause(entityAlias, orderingProperties);
    }
    
    String fromClause = buildFromClause(entityAlias, tableName);
    
    String whereClause = "";
    List<String> wherePredicates = buildWherePredicates(entityAlias, searchSpec, searchParams);
    if (wherePredicates.size() > 0)
    {
      whereClause = "WHERE " + StringUtils.joinSeperated(wherePredicates, " AND ");
    }
    
    return fromClause +
        " " + whereClause + " " + orderClause;    
  }
  
  public static String buildFromClause(String entityAlias, String tableName)
  {
    return "FROM " + tableName + " " + entityAlias;
  }
  
  public static String buildOrderClause(String entityAlias, String... orderingProperties)
  {
    List<String> orderings = new ArrayList<String>();
    
    for (String base : orderingProperties)
    {
      orderings.add(entityAlias + "." + base);
    }

    return "ORDER BY " + StringUtils.joinSeperated(orderings, ", ");
  }

  public static <S> List<String> buildWherePredicates(String entityAlias, MetaSearchEntity searchSpec, S searchParams)
  {
    List<String> predicates = new ArrayList<String>();
    
    for (MetaSearchField searchField : searchSpec.getSearchEntityDescription().getSearchFields())
    {
      Object searchObject = searchField.getValue(searchParams);
      
      if (isSearchObjectApplicable(searchObject))
      {
        predicates.add(makeSearchPredicate(entityAlias, searchField, searchObject));
      }
    }
    
    return predicates;
  }
  
  public static String makeSearchPredicate(String entityAlias, MetaSearchField searchField, Object field)
  {
    String searchName = searchField.getName();
    String targetName = searchField.getTargetField().getName();
    
    switch (searchField.getOperator())
    {
    case INNERMATCH:
      return "lower(" + entityAlias  + "." + targetName + ") LIKE :" + searchName;
    case LIKE:
      return "lower(" + entityAlias  + "." + targetName + ") LIKE :" + searchName;
    case IN:
      return entityAlias + "." + targetName + " IN (:" + searchName + ")";
    case EQUALS:
      return entityAlias + "." + targetName + " = :" + searchName;
    case GEQUALS:
      return entityAlias + "." + targetName + " >= :" + searchName;
    case LEQUALS:
      return entityAlias + "." + targetName + " <= :" + searchName;
    case NOTEQUALS:
      return entityAlias + "." + targetName + " != :" + searchName;
    default:
      throw new RuntimeException("Error, unhandled comparison type.");
    }
  }
  
  @SuppressWarnings("rawtypes")
  public static boolean isSearchObjectApplicable(Object searchObject)
  {
    return searchObject != null && !(searchObject instanceof String && searchObject.toString().length() == 0) && !(searchObject instanceof Collection && ((Collection)searchObject).isEmpty());
  }
  
  public static <S> void applyParametersToQuery(Query q, MetaSearchEntity searchSpec, S searchParams)
  {
    for (MetaSearchField searchField : searchSpec.getSearchEntityDescription().getSearchFields())
    {
      Object searchObject = searchField.getValue(searchParams);
      applyParameterToQuery(q, searchField, searchObject);
    }
  }
  
  @SuppressWarnings("rawtypes")
  public static void applyParameterToQuery(Query q, MetaSearchField searchField, Object searchObject)
  {
    final String searchName = searchField.getName();

    if (isSearchObjectApplicable(searchObject))
    {
      if (searchField.getOperator() == ComparisonOperator.INNERMATCH)
      {
        q.setParameter(searchName, StringUtils.sqlInnerStringMatch(searchObject.toString()));
      }
      else if (searchField.getOperator() == ComparisonOperator.IN && searchObject instanceof Collection)
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
