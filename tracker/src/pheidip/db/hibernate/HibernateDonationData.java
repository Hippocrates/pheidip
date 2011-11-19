package pheidip.db.hibernate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import pheidip.db.DonationData;
import pheidip.objects.Donation;
import pheidip.objects.DonationBid;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationReadState;
import pheidip.objects.DonationSearchParams;
import pheidip.objects.Donor;
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
    //session.close();
    
    return d;
  }

  @Override
  public List<Donation> getDonorDonations(int donorId)
  {
    List<Donation> list = new ArrayList<Donation>();
    
    Session session = this.beginTransaction();
    
    
    Donor d = (Donor) session.load(Donor.class, donorId);
    
    session.update(d);
    
    System.out.println("" + d.getDonations().size());

    for (Donation donation : d.getDonations())
    {
      list.add(donation);
    }
    
    this.endTransaction();
    //session.close();
    
    return list;
  }

  @Override
  public BigDecimal getDonorDonationTotal(int id)
  {
    BigDecimal sum = BigDecimal.ZERO;
    
    Session session = this.beginTransaction();
    
    
    Donor d = (Donor) session.load(Donor.class, id);

    for (Donation donation : d.getDonations())
    {
      sum = sum.add(donation.getAmount());
    }
    
    this.endTransaction();
    //session.close();
    
    return sum.setScale(2, RoundingMode.FLOOR);
  }

  @Override
  public void setDonationComment(int id, String comment)
  {
    Session session = this.beginTransaction();
    
    
    Donation d = (Donation) session.get(Donation.class, id);
    d.setComment(comment);
    session.flush();

    this.endTransaction();
    //session.close();
  }

  @Override
  public void deleteDonation(int id)
  {
    Session session = this.beginTransaction();
    
    
    Donation d = (Donation) session.get(Donation.class, id);
    d.getDonor().getDonations().remove(d);
    session.delete(d);

    this.endTransaction();
    //session.close();
  }

  @Override
  public void setDonationBidState(int id, DonationBidState bidState)
  {
    Session session = this.beginTransaction();
    
    
    Donation d = (Donation) session.get(Donation.class, id);
    d.setBidState(bidState);
    session.flush();

    this.endTransaction();
    //session.close();
  }

  @Override
  public void setDonationAmount(int donationId, BigDecimal amount)
  {
    Session session = this.beginTransaction();
    
    
    Donation d = (Donation) session.get(Donation.class, donationId);
    d.setAmount(amount);
    session.flush();

    this.endTransaction();
    //session.close();
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
    //session.close();
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
    //session.close();
    
    return result;
  }

  @Override
  public void updateChallengeBidAmount(int challengeBidId, BigDecimal newAmount)
  {
    Session session = this.beginTransaction();

    DonationBid b = (DonationBid) session.load(DonationBid.class, challengeBidId);
    b.setAmount(newAmount);
    session.flush();
    
    this.endTransaction();
  }

  @Override
  public void updateChoiceBidAmount(int choiceBidId, BigDecimal newAmount)
  {
    Session session = this.beginTransaction();
    
    
    DonationBid b = (DonationBid) session.load(DonationBid.class, choiceBidId);
    b.setAmount(newAmount);
    session.flush();
    
    this.endTransaction();
    //session.close();
  }

  @Override
  public List<Donation> getDonationsInTimeRange(Date lo, Date hi)
  {
    Session session = this.beginTransaction();
    
    
    Query q = session.createQuery("from Donation d where d.timeReceived between :lotime and :hitime");
    q.setDate("lotime", lo);
    q.setDate("hitime", hi);

    @SuppressWarnings("unchecked")
    List<Donation> listing = q.list();
    
    this.endTransaction();
    //session.close();
    
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
  public List<Donation> searchDonations(DonationSearchParams params)
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
    
    // TODO: change this to actually check for the comment and stuff for these to mirror the client behavior
    if (params.onlyIfUnbid)
      whereClause.add("d.bidState = :bidState");
    
    if (params.onlyIfUnread)
      whereClause.add("d.readState = :readState");
    
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
      q.setDate("loTime", params.loTime);
    
    if (params.hiTime != null)
      q.setDate("hiTime", params.hiTime);

    if (params.loAmount != null)
      q.setBigDecimal("loAmount", params.loAmount);
    
    if (params.hiAmount != null)
      q.setBigDecimal("hiAmount", params.hiAmount);
    
    if (params.onlyIfUnbid)
      q.setParameter("bidState", DonationBidState.PENDING);
    
    if (params.onlyIfUnread)
      q.setParameter("readState", DonationReadState.PENDING);
    
    @SuppressWarnings("unchecked")
    List<Donation> listing = q.list();
    
    this.endBulkTransaction(dedicatedSession);
    
    return listing;
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
  public void updateMultiplDonations(List<Donation> donationsToUpdate)
  {
    StatelessSession dedicatedSession = this.beginBulkTransaction();
    
    for (Donation donation : donationsToUpdate)
    {
      dedicatedSession.update(donation);
    }
    
    this.endBulkTransaction(dedicatedSession);
  }

}
