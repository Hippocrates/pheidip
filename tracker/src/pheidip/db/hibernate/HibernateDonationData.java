package pheidip.db.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import pheidip.db.DonationData;
import pheidip.objects.BidType;
import pheidip.objects.ChallengeBid;
import pheidip.objects.ChoiceBid;
import pheidip.objects.Donation;
import pheidip.objects.DonationBid;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationSearchParams;
import pheidip.util.StringUtils;

public class HibernateDonationData extends HibernateDataInterface implements DonationData
{
  public HibernateDonationData(HibernateDonationDataAccess manager)
  {
    super(manager);
  }

  @Override
  public Donation getDonationById(int id)
  {
    Session session = this.beginTransaction();

    Donation d = (Donation) session.get(Donation.class, id);

    this.endTransaction();
    
    return d;
  }

  @Override
  public void deleteDonation(Donation d)
  {
    Session session = this.beginTransaction();
    
    d.getDonor().getDonations().remove(d);
    session.delete(d);

    this.endTransaction();
  }

  @Override
  public void insertDonation(Donation d)
  {
    Session session = this.beginTransaction();
    session.save(d);
    this.endTransaction();
  }

  @Override
  public void updateDonation(Donation updated)
  {
    Session session = this.beginTransaction();    
    session.merge(updated);
    this.endTransaction();
  }

  @Override
  public Donation getDonationByDomainId(DonationDomain domain, String domainId)
  {
    Donation result = null;
    
    Session session = this.beginTransaction();

    Query q = session.createQuery("from Donation d inner join fetch d.donor where d.domain = :domain and d.domainId = :domainid");
    q.setString("domain", domain.toString());
    q.setString("domainid", domainId);
    
    @SuppressWarnings("unchecked")
    List<Donation> listing = q.list();
    
    if (listing.size() == 1)
    {
      result = listing.get(0);
    }
    
    this.endTransaction();
    
    return result;
  }

  @Override
  public List<Donation> getDonationsInTimeRange(Date lo, Date hi)
  {
    StatelessSession dedicatedSession = this.beginBulkTransaction();
    
    String queryString = "from Donation d";
    List<String> whereClause = new ArrayList<String>();

    if (lo != null)
      whereClause.add("d.timeReceived >= :lotime");
    
    if (hi != null)
      whereClause.add("d.timeReceived <= :hitime");
    
    if (whereClause.size() > 0)
    {
      queryString += " where " + StringUtils.joinSeperated(whereClause, " AND ");
    }
    
    Query q = dedicatedSession.createQuery(queryString);
    
    if (lo != null)
      q.setTimestamp("lotime", lo);
    
    if (hi != null)
      q.setTimestamp("hitime", hi);

    @SuppressWarnings("unchecked")
    List<Donation> listing = q.list();
    
    this.endBulkTransaction(dedicatedSession);
    
    return listing;
  }

  @Override
  public List<Donation> getAllDonations()
  {
    StatelessSession dedicatedSession = this.beginBulkTransaction();
    
    Query q = dedicatedSession.createQuery("from Donation");
    
    @SuppressWarnings("unchecked")
    List<Donation> listing = q.list();
    
    this.endBulkTransaction(dedicatedSession);
    
    return listing;
  }

  @Override
  public List<Donation> searchDonationsRange(DonationSearchParams params, int offset, int size)
  {
    String queryString = "from Donation d inner join fetch d.donor";
    List<String> whereClause = new ArrayList<String>();
    
    if (params.donor != null)
      whereClause.add("d.donor = :donor");
    
    if (params.domain != null)
      whereClause.add("d.domain = :domain");
    
    if (params.domainId != null)
      whereClause.add("d.domainId like :domainId");
    
    if (params.loTime != null)
      whereClause.add("d.timeReceived >= :loTime");
    
    if (params.hiTime != null)
      whereClause.add("d.timeReceived <= :hiTime");
    
    if (params.loAmount != null)
      whereClause.add("d.amount >= :loAmount");
    
    if (params.hiAmount != null)
      whereClause.add("d.amount <= :hiAmount");
    
    if (params.targetBidState != null)
      whereClause.add("d.bidState = :bidState");
    
    if (params.targetReadState != null)
      whereClause.add("d.readState = :readState");
    
    if (params.targetCommentState != null)
      whereClause.add("d.commentState = :commentState");
    
    if (whereClause.size() > 0)
    {
      queryString += " where " + StringUtils.joinSeperated(whereClause, " AND ");
    }

    StatelessSession dedicatedSession = this.beginBulkTransaction();
    
    Query q = dedicatedSession.createQuery(queryString + " order by d.timeReceived");
    
    if (params.donor != null)
      q.setParameter("donor", params.donor);
    
    if (params.domain != null)
      q.setParameter("domain", params.domain);
    
    if (params.domainId != null)
      q.setString("domainId", params.domainId);
    
    if (params.loTime != null)
      q.setTimestamp("loTime", params.loTime);
    
    if (params.hiTime != null)
      q.setTimestamp("hiTime", params.hiTime);

    if (params.loAmount != null)
      q.setBigDecimal("loAmount", params.loAmount);
    
    if (params.hiAmount != null)
      q.setBigDecimal("hiAmount", params.hiAmount);
    
    if (params.targetBidState != null)
      q.setParameter("bidState", params.targetBidState);
    
    if (params.targetReadState != null)
      q.setParameter("readState", params.targetReadState);
    
    if (params.targetCommentState != null)
      q.setParameter("commentState", params.targetCommentState);
    
    q.setFirstResult(offset);
    q.setMaxResults(size);
    
    @SuppressWarnings("unchecked")
    List<Donation> listing = q.list();
    
    this.endBulkTransaction(dedicatedSession);
    
    return listing;
  }
  
  @Override
  public List<Donation> searchDonations(DonationSearchParams params)
  {
    return this.searchDonationsRange(params, 0, Integer.MAX_VALUE);
  }

  @Override
  public void insertMultipleDonations(List<Donation> donationsToInsert)
  {
    StatelessSession dedicatedSession = this.beginBulkTransaction();
    
    for (Donation donation : donationsToInsert)
    {
      dedicatedSession.insert(donation);
    }
    
    this.endBulkTransaction(dedicatedSession);
  }

  @Override
  public void updateMultipleDonations(List<Donation> donationsToUpdate)
  {
    StatelessSession dedicatedSession = this.beginBulkTransaction();
    
    for (Donation donation : donationsToUpdate)
    {
      dedicatedSession.update(donation);
    }
    
    this.endBulkTransaction(dedicatedSession);
  }

  @Override
  public void addDonationBid(DonationBid added)
  {
    Session session = this.beginTransaction();
    session.save(added);
    this.endTransaction();
  }

  @Override
  public void updateDonationBid(DonationBid update)
  {
    Session session = this.beginTransaction();
    session.merge(update);
    this.endTransaction();
  }

  @Override
  public void deleteDonationBid(DonationBid remove)
  {
    Session session = this.beginTransaction();
    
    if (remove.getType() == BidType.CHOICE)
    {
      ((ChoiceBid)remove).getOption().getBids().remove((ChoiceBid)remove);
    }
    else if (remove.getType() == BidType.CHALLENGE)
    {
      ((ChallengeBid)remove).getChallenge().getBids().remove((ChallengeBid)remove);
    }
    
    remove.getDonation().getBids().remove(remove);
    
    session.delete(remove);
    this.endTransaction();
  }

}
