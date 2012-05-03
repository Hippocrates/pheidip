package pheidip.db.hibernate;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;

import pheidip.db.hibernate.HibernateManager;

import pheidip.db.DBConnectionParams;
import pheidip.db.DBType;

import lombok.AccessLevel;
import lombok.BoundPropertySupport;
import lombok.BoundSetter;
import lombok.Getter;
import meta.MetaEntity;
import meta.MetaField;
import meta.EntitySetFieldDescription;
import meta.search.MetaSearchEntity;
import pheidip.db.DataAccess;
import pheidip.objects.Entity;
import pheidip.util.Pair;

@BoundPropertySupport
public class HibernateDataAccess implements DataAccess
{
  private SessionFactory sessionFactory;
  
  @Getter
  private DBType databaseConnectionType;
  
  @Getter
  private boolean localConnection;
  
  // allows other elements to bind on whether the connection is open or not
  @Getter @BoundSetter(AccessLevel.PRIVATE)
  private boolean connected;
  
  public HibernateDataAccess()
  {
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public <E extends Entity> E loadInstance(MetaEntity spec, int id)
  {
    Session session = this.sessionFactory.openSession();
    E result = null;
    
    try
    {
      session.beginTransaction();
      
      // this causes it to automagically load all 2nd-order collections, which is generally what you would want
      // when loading a single entity

      Criteria c = session.createCriteria(spec.getStorageClass());
      
      for (MetaField field : spec.getFields())
      {
        if (field.getFieldDescription() instanceof EntitySetFieldDescription)
        {
          c.setFetchMode(field.getName(), FetchMode.JOIN);
        }
      }
      
      c.add(Restrictions.eq("id", id));

      result = (E) c.uniqueResult();
      
      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      if (session != null)
      {
        session.getTransaction().rollback();
      }
      throw new RuntimeException(e);
    }
    finally
    {
      if (session != null)
      {
        session.close();
      }
    }
    
    return result;
  }

  @Override
  public <E extends Entity> void saveInstance(MetaEntity spec, E instance)
  {
    Session session = this.sessionFactory.openSession();

    try
    {
      session.beginTransaction();

      session.save(instance);

      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
      
      if (session != null)
      {
        session.getTransaction().rollback();
      }
      
      throw new RuntimeException(e);
    }
    finally
    {
      if (session != null)
      {
        session.close();
      }
    }
  }
  
  @Override
  public <E extends Entity> void saveMultiple(MetaEntity spec, List<E> instances)
  {
    Session session = this.sessionFactory.openSession();

    try
    {
      session.beginTransaction();
      
      for (E instance : instances)
      {
        session.save(instance);
      }

      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
      
      if (session != null)
      {
        session.getTransaction().rollback();
      }
      
      throw new RuntimeException(e);
    }
    finally
    {
      if (session != null)
      {
        session.close();
      }
    }
    
  }
  
  @Override
  public <E extends Entity> void updateInstance(MetaEntity spec, E instance)
  {
    Session session = this.sessionFactory.openSession();

    try
    {
      session.beginTransaction();
      
      session.update(instance);
      
      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      if (session != null)
      {
        session.getTransaction().rollback();
      }
      throw new RuntimeException(e);
    }
    finally
    {
      if (session != null)
      {
        session.close();
      }
    }
  }
  
  @Override
  public <E extends Entity> List<E> updateMultiple(MetaEntity spec, List<E> instances)
  {
    Session session = this.sessionFactory.openSession();

    try
    {
      session.beginTransaction();
      
      for (E instance : instances)
      {
        session.update(instance);
      }

      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      if (session != null)
      {
        session.getTransaction().rollback();
      }
      throw new RuntimeException(e);
    }
    finally
    {
      if (session != null)
      {
        session.close();
      }
    }

    return instances;
  }

  @Override
  public <E extends Entity> void deleteInstance(MetaEntity spec, E instance)
  {
    Session session = this.sessionFactory.openSession();

    try
    {
      session.beginTransaction();

      session.delete(instance);
      
      session.getTransaction().commit();
    }
    catch(Exception e)
    {
      if (session != null)
      {
        session.getTransaction().rollback();
      }
      throw new RuntimeException(e);
    }
    finally
    {
      if (session != null)
      {
        session.close();
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <E extends Entity, S> Pair<Long, List<E>> searchEntityRange(
      MetaEntity instanceSpec, MetaSearchEntity searchSpec, S searchParams,
      int offset, int count)
  {
    String hqlQuery = HQLMethods.makeHQLSearchQueryString(instanceSpec.getName(), searchSpec, searchParams).trim();
    
    Session session = this.sessionFactory.openSession();
    
    List<E> results = null;
    Long total = null;
    
    try
    {
      session.beginTransaction();
      
      Query countQuery = session.createQuery("SELECT COUNT(*) " + hqlQuery);
      
      HQLMethods.applyParametersToQuery(countQuery, searchSpec, searchParams);
      
      List<Long> totalCount = countQuery.list();
      total = totalCount.get(0);
      
      Query q = session.createQuery(hqlQuery);
    
      HQLMethods.applyParametersToQuery(q, searchSpec, searchParams);
      
      q.setFirstResult(offset);
      q.setMaxResults(count);

      results = q.list();

      session.getTransaction().rollback();
    }
    catch(Exception e)
    {
      throw new RuntimeException(e);
    }
    finally
    {
      if (session != null)
      {
        session.close();
      }
    }
    
    return new Pair<Long, List<E>>(total, results);
  }

  @Override
  public <E extends Entity, S> List<E> searchEntity(MetaEntity instanceSpec, MetaSearchEntity searchSpec, S searchParams)
  {
    Pair<Long, List<E>> result = this.searchEntityRange(instanceSpec, searchSpec, searchParams, 0, Integer.MAX_VALUE);
    return result.getSecond();
  }
  
  @Override
  public void createMemoryConnection()
  {
    if (this.isConnected())
    {
      throw new RuntimeException("Error, connection is open.");
    }
    
    this.sessionFactory = HibernateManager.createMemorySessionFactory();
    this.databaseConnectionType = DBType.HSQLDB;
    this.localConnection = true;
    this.setConnected(true);
  }

  @Override
  public void connectToServer(DBConnectionParams params)
  {
    if (this.isConnected())
    {
      throw new RuntimeException("Error, connection is open.");
    }
    
    this.sessionFactory = HibernateManager.createServerSessionFactory(params.getDatabaseType(), params.getDatabaseServer(), params.getDatabaseName(), params.getUserName(), params.getPassword());
    this.databaseConnectionType = params.getDatabaseType();
    this.localConnection = false;
    this.setConnected(true);
  }

  @Override
  public void closeConnection() 
  {
    if (!this.isConnected())
    {
      throw new RuntimeException("Error, connection is already closed.");
    }
    
    if (this.localConnection)
    {
      Session session = this.sessionFactory.openSession();
      
      session.doWork(new Work()
      {
        public void execute(Connection c) throws SQLException
        {
          c.createStatement().execute("SHUTDOWN COMPACT");
        }
      });
      
      session.close();
    }

    this.sessionFactory.close();
    this.sessionFactory = null;
    this.setConnected(false);
  }
  @Override
  public void openFileDatabase(File file)
  {
    throw new RuntimeException("Not implemented yet.");
  }

}
