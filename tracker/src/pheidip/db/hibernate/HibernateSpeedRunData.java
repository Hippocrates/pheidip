package pheidip.db.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import pheidip.db.SpeedRunData;
import pheidip.objects.SpeedRun;
import pheidip.objects.SpeedRunSearchParams;
import pheidip.util.StringUtils;

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
    //session.close();
    
    return s;
  }

  @Override
  public void insertSpeedRun(SpeedRun speedRun)
  {
    Session session = this.beginTransaction();
    
    
    session.save(speedRun);
    
    this.endTransaction();
    //session.close();
  }

  @Override
  public void deleteSpeedRun(int runId)
  {
    SpeedRun s = this.getSpeedRunById(runId);
    
    Session session = this.beginTransaction();
    
    
    session.delete(s);
    
    this.endTransaction();
    //session.close();
  }

  @Override
  public void updateSpeedRun(SpeedRun run)
  {
    Session session = this.beginTransaction();
    
    
    session.merge(run);
    
    this.endTransaction();
    //session.close();
  }

  @Override
  public List<SpeedRun> getAllSpeedRuns()
  {
    Session session = this.beginTransaction();
    
    
    Query q = session.createQuery("From SpeedRun order by id");
    
    @SuppressWarnings("unchecked")
    List<SpeedRun> listing = q.list();
    
    this.endTransaction();
    //session.close();
    
    return listing;
  }

  @Override
  public List<SpeedRun> searchSpeedRuns(SpeedRunSearchParams params)
  {
    String queryString = "from SpeedRun s";
    List<String> whereClause = new ArrayList<String>();
    
    if (params.name != null)
      whereClause.add("s.name like :name");

    if (whereClause.size() > 0)
    {
      queryString += " where " + StringUtils.joinSeperated(whereClause, " AND ");
    }
    
    Session session = this.beginTransaction();
    
    
    Query q = session.createQuery(queryString + " order by s.name");

    if (params.name != null)
      q.setString("name", StringUtils.sqlInnerStringMatch(params.name));
    
    @SuppressWarnings("unchecked")
    List<SpeedRun> listing = q.list();
    
    this.endTransaction();
    //session.close();
    
    return listing;
  }

}
