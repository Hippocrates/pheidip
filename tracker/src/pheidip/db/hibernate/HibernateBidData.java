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
    Session session = this.beginTransaction();
    Query q = session.createQuery("from Bid");

    @SuppressWarnings("unchecked")
    List<Bid> listing = q.list();
    
    this.endTransaction();
    ////session.close();
    
    return listing;
  }

  @Override
  public Choice getChoiceById(int choiceId)
  {
    Session session = this.beginTransaction();
    Choice c = (Choice) session.get(Choice.class, choiceId);
    
    this.endTransaction();
    //session.close();
    
    return c;
  }

  @Override
  public void insertChoice(Choice choice)
  {
    Session session = this.beginTransaction();
    session.save(choice);
    this.endTransaction();
  }

  @Override
  public void updateChoice(Choice choice)
  {
    Session session = this.beginTransaction();
    
    
    session.update(choice);
    
    this.endTransaction();
    //session.close();
  }

  @Override
  public void deleteChoice(int choiceId)
  {
    Session session = this.beginTransaction();
    Choice c = (Choice) session.load(Choice.class, choiceId);
    c.getSpeedRun().getBids().remove(c);
    
    session.delete(c);
    
    this.endTransaction();
    //session.close();
  }

  @Override
  public ChoiceOption getChoiceOptionById(int optionId)
  {
    Session session = this.beginTransaction();
    ChoiceOption o = (ChoiceOption) session.get(ChoiceOption.class, optionId);

    this.endTransaction();
    //session.close();
    
    return o;
  }

  @Override
  public void insertChoiceOption(ChoiceOption choiceOption)
  {
    Session session = this.beginTransaction();
    session.save(choiceOption);
    
    this.endTransaction();
    //session.close();
  }

  @Override
  public void updateChoiceOption(ChoiceOption option)
  {
    Session session = this.beginTransaction();
    session.update(option);
    
    this.endTransaction();
    //session.close();
  }

  @Override
  public void deleteChoiceOption(int optionId)
  {
    Session session = this.beginTransaction();
    
    
    ChoiceOption o = (ChoiceOption) session.load(ChoiceOption.class, optionId);
    o.getChoice().getOptions().remove(o);
    
    session.delete(o);
    
    this.endTransaction();
    //session.close();
  }

  @Override
  public Challenge getChallengeById(int challengeId)
  {
    Session session = this.beginTransaction();
    
    
    Challenge c = (Challenge) session.get(Challenge.class, challengeId);
    
    this.endTransaction();
    //session.close();
    
    return c;
  }

  @Override
  public void insertChallenge(Challenge challenge)
  {
    Session session = this.beginTransaction();
    
    
    session.save(challenge);
    
    this.endTransaction();
    //session.close();
  }

  @Override
  public void updateChallenge(Challenge challenge)
  {
    Session session = this.beginTransaction();
    
    
    session.update(challenge);
    
    this.endTransaction();
    //session.close();
  }

  @Override
  public BigDecimal getChoiceOptionTotal(int optionId)
  {
    Session session = this.beginTransaction();
    
    
    Query q = session.createQuery("select sum(b.amount) from ChoiceBid b left join b.option where b.option.id = :id");
    
    q.setInteger("id", optionId);
    
    //@SuppressWarnings("unchecked")
    @SuppressWarnings("rawtypes")
    List listing = q.list();
    
    this.endTransaction();
    //session.close();

    return listing.get(0) == null ? BigDecimal.ZERO : (BigDecimal) listing.get(0);
  }

  @Override
  public BigDecimal getChallengeTotal(int challengeId)
  {
    Session session = this.beginTransaction();
    
    
    Query q = session.createQuery("select sum(b.amount) from ChallengeBid b left join b.challenge where b.challenge.id = :id");
    
    q.setInteger("id", challengeId);
    
    //@SuppressWarnings("unchecked")
    @SuppressWarnings("rawtypes")
    List listing = q.list();
    
    this.endTransaction();
    //session.close();
    
    return listing.get(0) == null ? BigDecimal.ZERO : (BigDecimal) listing.get(0);
  }

  @Override
  public void deleteChallenge(int challengeId)
  {
    Session session = this.beginTransaction();
    
    
    Challenge c = (Challenge) session.load(Challenge.class, challengeId);
    c.getSpeedRun().getBids().remove(c);
    
    session.delete(c);
    
    this.endTransaction();
    //session.close();
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
    
    Session session = this.beginTransaction();
    
    
    Query q = session.createQuery(queryString + " order by b.name");

    if (params.name != null)
      q.setString("name", StringUtils.sqlInnerStringMatch(params.name));
    
    if (params.owner != null)
      q.setParameter("owner", params.owner);

    @SuppressWarnings("unchecked")
    List<Bid> listing = q.list();
    
    this.endTransaction();
    //session.close();
    
    return listing;
  }

}
