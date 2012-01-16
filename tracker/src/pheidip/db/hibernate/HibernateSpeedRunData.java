package pheidip.db.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import pheidip.db.SpeedRunData;
import pheidip.objects.Prize;
import pheidip.objects.SearchEntity;
import pheidip.objects.SpeedRun;

public class HibernateSpeedRunData extends HibernateDataInterface implements SpeedRunData
{
  public HibernateSpeedRunData(HibernateDonationDataAccess manager)
  {
    super(manager);
  }

  @Override
  public SpeedRun getSpeedRunById(int runId)
  {
    Session session = this.beginTransaction();
    
    SpeedRun s = (SpeedRun) session.get(SpeedRun.class, runId);

    this.endTransaction();

    return s;
  }

  @Override
  public void insertSpeedRun(SpeedRun speedRun)
  {
    Session session = this.beginTransaction();
    
    session.save(speedRun);
    
    this.endTransaction();
  }

  @Override
  public void deleteSpeedRun(SpeedRun speedRun)
  {
    Session session = this.beginTransaction();
    
    speedRun = (SpeedRun) session.merge(speedRun);
    
    for (Prize p : speedRun.getPrizeStartGame())
    {
      p.setStartGame(null);
    }
    
    speedRun.getPrizeStartGame().clear();
    
    for (Prize p : speedRun.getPrizeEndGame())
    {
      p.setEndGame(null);
    }
    
    speedRun.getPrizeEndGame().clear();
    
    session.delete(speedRun);
    
    this.endTransaction();
  }

  @Override
  public void updateSpeedRun(SpeedRun run)
  {
    Session session = this.beginTransaction();

    session.merge(run);
    
    this.endTransaction();
  }
  
  @Override
  public void multiUpdateSpeedRuns(List<SpeedRun> toUpdate)
  {
	  StatelessSession session = this.beginBulkTransaction();
	  
	  for (SpeedRun s : toUpdate)
	  {
		  session.update(s);
	  }
	  
	  this.endBulkTransaction(session);
  }

  @Override
  public List<SpeedRun> getAllSpeedRuns()
  {
    Session session = this.beginTransaction();

    Query q = session.createQuery("From SpeedRun order by id");
    
    @SuppressWarnings("unchecked")
    List<SpeedRun> listing = q.list();
    
    this.endTransaction();
    
    return listing;
  }

  @Override
  public List<SpeedRun> searchSpeedRuns(SearchEntity<SpeedRun> params)
  {
    return this.searchSpeedRunsRange(params, 0, Integer.MAX_VALUE);
  }
  
  @Override
  public List<SpeedRun> searchSpeedRunsRange(SearchEntity<SpeedRun> params, int offset, int size)
  {
    String queryString = SQLMethods.makeHQLSearchQueryString(params, "SpeedRun", "name");
    
    StatelessSession dedicatedSession = this.beginBulkTransaction();

    Query q = dedicatedSession.createQuery(queryString);

    SQLMethods.applyParametersToQuery(q, params);

    q.setFirstResult(offset);
    q.setMaxResults(size);
    
    @SuppressWarnings("unchecked")
    List<SpeedRun> listing = q.list();
    
    this.endTransaction();

    return listing;
  }

}
