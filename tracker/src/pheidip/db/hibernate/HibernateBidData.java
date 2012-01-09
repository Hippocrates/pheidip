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

    return listing;
  }

  @Override
  public Choice getChoiceById(int choiceId)
  {
    Session session = this.beginTransaction();
    Choice c = (Choice) session.get(Choice.class, choiceId);
    
    this.endTransaction();

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

    session.merge(choice);

    this.endTransaction();
  }

  @Override
  public void deleteChoice(Choice choice)
  {
    Session session = this.beginTransaction();
    
    choice = (Choice) session.merge(choice);
    
    if (choice.getSpeedRun() != null)
    {
      choice.getSpeedRun().getBids().remove(choice);
    }
    
    session.delete(choice);
    
    this.endTransaction();
  }

  @Override
  public ChoiceOption getChoiceOptionById(int optionId)
  {
    Session session = this.beginTransaction();
    ChoiceOption o = (ChoiceOption) session.get(ChoiceOption.class, optionId);

    this.endTransaction();
    
    return o;
  }

  @Override
  public void insertChoiceOption(ChoiceOption choiceOption)
  {
    Session session = this.beginTransaction();
    session.save(choiceOption);
    
    this.endTransaction();
  }

  @Override
  public void updateChoiceOption(ChoiceOption option)
  {
    Session session = this.beginTransaction();
    session.update(option);
    
    this.endTransaction();
  }

  @Override
  public void deleteChoiceOption(ChoiceOption option)
  {
    Session session = this.beginTransaction();

    if (option.getChoice() != null)
    {
      option.getChoice().getOptions().remove(option);
    }

    session.delete(option);
    
    this.endTransaction();
  }

  @Override
  public Challenge getChallengeById(int challengeId)
  {
    Session session = this.beginTransaction();

    Challenge c = (Challenge) session.get(Challenge.class, challengeId);

    this.endTransaction();

    return c;
  }

  @Override
  public void insertChallenge(Challenge challenge)
  {
    Session session = this.beginTransaction();
    
    session.save(challenge);
    
    this.endTransaction();
  }

  @Override
  public void updateChallenge(Challenge challenge)
  {
    Session session = this.beginTransaction();

    session.update(challenge);
    
    this.endTransaction();
  }

  @Override
  public BigDecimal getChoiceOptionTotal(int optionId)
  {
    Session session = this.beginTransaction();
    
    Query q = session.createQuery("select sum(b.amount) from ChoiceBid b left join b.option where b.option.id = :id");
    
    q.setInteger("id", optionId);

    @SuppressWarnings("rawtypes")
    List listing = q.list();
    
    this.endTransaction();

    return listing.get(0) == null ? BigDecimal.ZERO.setScale(2) : (BigDecimal) listing.get(0);
  }

  @Override
  public BigDecimal getChallengeTotal(int challengeId)
  {
    Session session = this.beginTransaction();
    
    Query q = session.createQuery("select sum(b.amount) from ChallengeBid b left join b.challenge where b.challenge.id = :id");
    
    q.setInteger("id", challengeId);
    
    @SuppressWarnings("rawtypes")
    List listing = q.list();
    
    this.endTransaction();

    return listing.get(0) == null ? BigDecimal.ZERO.setScale(2) : (BigDecimal) listing.get(0);
  }

  @Override
  public void deleteChallenge(Challenge challenge)
  {
    Session session = this.beginTransaction();

    challenge = (Challenge) session.merge(challenge);
    
    if (challenge.getSpeedRun() != null)
    {
      challenge.getSpeedRun().getBids().remove(challenge);
    }
  
    session.delete(challenge);
      
    this.endTransaction();
  }

  @Override
  public List<Bid> searchBids(BidSearchParams params)
  {
    return this.searchBidsRange(params, 0, Integer.MAX_VALUE);
  }
    
  @Override
  public List<Bid> searchBidsRange(BidSearchParams params, int offset, int size)
  {
    String queryString = "from Bid b";
    List<String> whereClause = new ArrayList<String>();
    
    if (params.name != null)
      whereClause.add("lower(b.name) like :name");
    
    if (params.owner != null)
      whereClause.add("b.speedRun = :owner");
    
    if (params.state != null)
      whereClause.add("lower(b.bidState) = :state");
    
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
    
    if (params.state != null)
      q.setParameter("state", params.state);

    q.setFirstResult(offset);
    q.setMaxResults(size);
    
    @SuppressWarnings("unchecked")
    List<Bid> listing = q.list();
    
    this.endTransaction();

    return listing;
  }

}
