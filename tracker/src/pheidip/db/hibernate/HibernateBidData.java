package pheidip.db.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import pheidip.db.BidData;
import pheidip.objects.Bid;
import pheidip.objects.BidSearchParams;
import pheidip.objects.Challenge;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceOption;
import pheidip.objects.SpeedRun;
import pheidip.util.StringUtils;

public class HibernateBidData extends HibernateDataInterface implements BidData
{
  public HibernateBidData(HibernateDonationDataAccess manager)
  {
    super(manager);
  }

  @Override
  public List<Bid> getAllBids()
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Query q = session.createQuery("from Bid");

    @SuppressWarnings("unchecked")
    List<Bid> listing = q.list();
    
    session.getTransaction().commit();
    session.close();
    
    return listing;
  }

  @Override
  public Choice getChoiceById(int choiceId)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Choice c = (Choice) session.get(Choice.class, choiceId);
    
    session.getTransaction().commit();
    session.close();
    
    return c;
  }

  @Override
  public void insertChoice(Choice choice)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    session.save(choice);
    
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void updateChoice(Choice choice)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    session.update(choice);
    
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public List<Choice> getChoicesBySpeedrun(int speedRunId)
  {
    List<Choice> listing = new ArrayList<Choice>();
    
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    SpeedRun s = (SpeedRun) session.load(SpeedRun.class, speedRunId);
    
    for (Bid c : s.getBids())
    {
      if (c instanceof Choice)
      {
        listing.add((Choice)c);
      }
    }
    
    session.getTransaction().commit();
    session.close();
    
    return listing;
  }

  @Override
  public void deleteChoice(int choiceId)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Choice c = (Choice) session.load(Choice.class, choiceId);
    c.getSpeedRun().getBids().remove(c);
    
    session.delete(c);
    
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public ChoiceOption getChoiceOptionById(int optionId)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    ChoiceOption o = (ChoiceOption) session.get(ChoiceOption.class, optionId);

    session.getTransaction().commit();
    session.close();
    
    return o;
  }

  @Override
  public void insertChoiceOption(ChoiceOption choiceOption)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    session.save(choiceOption);
    
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void updateChoiceOption(ChoiceOption option)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    session.update(option);
    
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public List<ChoiceOption> getChoiceOptionsByChoiceId(int choiceId)
  {
    List<ChoiceOption> listing = new ArrayList<ChoiceOption>();
    
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Choice c = (Choice) session.load(Choice.class, choiceId);
    
    for (ChoiceOption o : c.getOptions())
    {
      listing.add(o);
    }
    
    session.getTransaction().commit();
    session.close();
    
    return listing;
  }

  @Override
  public void deleteChoiceOption(int optionId)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    ChoiceOption o = (ChoiceOption) session.load(ChoiceOption.class, optionId);
    o.getChoice().getOptions().remove(o);
    
    session.delete(o);
    
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public Challenge getChallengeById(int challengeId)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Challenge c = (Challenge) session.get(Challenge.class, challengeId);
    
    session.getTransaction().commit();
    session.close();
    
    return c;
  }

  @Override
  public void insertChallenge(Challenge challenge)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    session.save(challenge);
    
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void updateChallenge(Challenge challenge)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    session.update(challenge);
    
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public List<Challenge> getChallengesBySpeedrun(int speedRunId)
  {
    List<Challenge> listing = new ArrayList<Challenge>();
    
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    SpeedRun s = (SpeedRun) session.load(SpeedRun.class, speedRunId);
    
    for (Bid c : s.getBids())
    {
      if (c instanceof Challenge)
      {
        listing.add((Challenge)c);
      }
    }
    
    session.getTransaction().commit();
    session.close();
    
    return listing;
  }

  @Override
  public BigDecimal getChoiceOptionTotal(int optionId)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Query q = session.createQuery("select sum(b.amount) from ChoiceBid b left join b.option where b.option.id = :id");
    
    q.setInteger("id", optionId);
    
    //@SuppressWarnings("unchecked")
    @SuppressWarnings("rawtypes")
    List listing = q.list();
    
    session.getTransaction().commit();
    session.close();

    return listing.get(0) == null ? BigDecimal.ZERO : (BigDecimal) listing.get(0);
  }

  @Override
  public BigDecimal getChallengeTotal(int challengeId)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Query q = session.createQuery("select sum(b.amount) from ChallengeBid b left join b.challenge where b.challenge.id = :id");
    
    q.setInteger("id", challengeId);
    
    //@SuppressWarnings("unchecked")
    @SuppressWarnings("rawtypes")
    List listing = q.list();
    
    session.getTransaction().commit();
    session.close();
    
    return listing.get(0) == null ? BigDecimal.ZERO : (BigDecimal) listing.get(0);
  }

  @Override
  public void deleteChallenge(int challengeId)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Challenge c = (Challenge) session.load(Challenge.class, challengeId);
    c.getSpeedRun().getBids().remove(c);
    
    session.delete(c);
    
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public List<ChoiceOption> getAllChoiceOptions()
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();

    Query q = session.createQuery("from ChoiceOption");

    @SuppressWarnings("unchecked")
    List<ChoiceOption> listing = q.list();
    
    session.getTransaction().commit();
    session.close();
    
    return listing;
  }

  @Override
  public List<Bid> searchBids(BidSearchParams params)
  {
    String queryString = "from Bid b";
    List<String> whereClause = new ArrayList<String>();
    
    if (params.name != null)
      whereClause.add("b.name like :name");
    
    if (params.owner != null)
      whereClause.add("b.speedRun = :owner");
    
    if (whereClause.size() > 0)
    {
      queryString += " where " + StringUtils.joinSeperated(whereClause, " AND ");
    }
    
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Query q = session.createQuery(queryString + " order by b.name");

    if (params.name != null)
      q.setString("name", StringUtils.sqlInnerStringMatch(params.name));
    
    if (params.owner != null)
      q.setParameter("owner", params.owner);

    @SuppressWarnings("unchecked")
    List<Bid> listing = q.list();
    
    session.getTransaction().commit();
    session.close();
    
    return listing;
  }

}
