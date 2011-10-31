package pheidip.db.hibernate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import pheidip.db.DonationData;
import pheidip.objects.BidType;
import pheidip.objects.ChallengeBid;
import pheidip.objects.ChoiceBid;
import pheidip.objects.Donation;
import pheidip.objects.DonationBid;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationReadState;
import pheidip.objects.Donor;

public class HibernateDonationData extends HibernateDataInterface implements DonationData
{
  public HibernateDonationData(HibernateDonationDataAccess manager)
  {
    super(manager);
  }

  @Override
  public Donation getDonationById(int id)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Donation d = (Donation) session.get(Donation.class, id);

    session.getTransaction().commit();
    session.close();
    
    return d;
  }

  @Override
  public List<Donation> getDonorDonations(int donorId)
  {
    List<Donation> list = new ArrayList<Donation>();
    
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Donor d = (Donor) session.load(Donor.class, donorId);
    
    session.update(d);
    
    System.out.println("" + d.getDonations().size());

    for (Donation donation : d.getDonations())
    {
      list.add(donation);
    }
    
    session.getTransaction().commit();
    session.close();
    
    return list;
  }

  @Override
  public BigDecimal getDonorDonationTotal(int id)
  {
    BigDecimal sum = BigDecimal.ZERO;
    
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Donor d = (Donor) session.load(Donor.class, id);

    for (Donation donation : d.getDonations())
    {
      sum = sum.add(donation.getAmount());
    }
    
    session.getTransaction().commit();
    session.close();
    
    return sum.setScale(2, RoundingMode.FLOOR);
  }

  @Override
  public void setDonationComment(int id, String comment)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Donation d = (Donation) session.get(Donation.class, id);
    d.setComment(comment);
    session.flush();

    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void deleteDonation(int id)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Donation d = (Donation) session.get(Donation.class, id);
    d.getDonor().getDonations().remove(d);
    session.delete(d);

    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void setDonationBidState(int id, DonationBidState bidState)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Donation d = (Donation) session.get(Donation.class, id);
    d.setBidState(bidState);
    session.flush();

    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void setDonationAmount(int donationId, BigDecimal amount)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Donation d = (Donation) session.get(Donation.class, donationId);
    d.setAmount(amount);
    session.flush();

    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void insertDonation(Donation d)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
/*
    session.update(d.getDonor());
    
    d.getDonor().getDonations().add(d);
    session.update(d.getDonor());
    */
    session.save(d);

    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void updateDonation(Donation updated)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    session.update(updated);

    session.getTransaction().commit();
    session.close();
  }

  @Override
  public Donation getDonationByDomainId(DonationDomain domain, String domainId)
  {
    Donation result = null;
    
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Query q = session.createQuery("from Donation d where d.domain = :domain and d.domainId = :domainid");
    q.setString("domain", domain.toString());
    q.setString("domainid", domainId);
    
    @SuppressWarnings("unchecked")
    List<Donation> listing = q.list();
    
    if (listing.size() == 1)
    {
      result = listing.get(0);
    }
    
    session.getTransaction().commit();
    session.close();
    
    return result;
  }

  @Override
  public List<ChallengeBid> getChallengeBidsByDonationId(int donationId)
  {
    List<ChallengeBid> list = new ArrayList<ChallengeBid>();
    
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Donation d = (Donation) session.get(Donation.class, donationId);

    for (DonationBid b : d.getBids())
    {
      if (b.getType() == BidType.CHALLENGE)
      {
        list.add((ChallengeBid)b);
      }
    }
    
    session.getTransaction().commit();
    session.close();
    
    return list;
  }

  @Override
  public List<ChoiceBid> getChoiceBidsByDonationId(int donationId)
  {
    List<ChoiceBid> list = new ArrayList<ChoiceBid>();
    
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Donation d = (Donation) session.get(Donation.class, donationId);

    for (DonationBid b : d.getBids())
    {
      if (b.getType() == BidType.CHOICE)
      {
        list.add((ChoiceBid)b);
      }
    }
    
    session.getTransaction().commit();
    session.close();
    
    return list;
  }

  @Override
  public void attachChallengeBid(ChallengeBid created)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    session.save(created);
    
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void attachChoiceBid(ChoiceBid created)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    session.save(created);
    
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void updateChallengeBidAmount(int challengeBidId, BigDecimal newAmount)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    DonationBid b = (DonationBid) session.load(DonationBid.class, challengeBidId);
    b.setAmount(newAmount);
    session.flush();
    
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void updateChoiceBidAmount(int choiceBidId, BigDecimal newAmount)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    DonationBid b = (DonationBid) session.load(DonationBid.class, choiceBidId);
    b.setAmount(newAmount);
    session.flush();
    
    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void removeChallengeBid(int challengeBidId)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    DonationBid b = (DonationBid) session.load(DonationBid.class, challengeBidId);
    session.delete(b);

    session.getTransaction().commit();
    session.close();
  }

  @Override
  public void removeChoiceBid(int choiceBidId)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    DonationBid b = (DonationBid) session.load(DonationBid.class, choiceBidId);
    session.delete(b);

    session.getTransaction().commit();
    session.close();
  }

  @Override
  public List<Donation> getDonationsWithPendingBids()
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Query q = session.createQuery("from Donation d where d.bidState = :bidstate");
    q.setString("bidstate", DonationBidState.PENDING.toString());
    
    @SuppressWarnings("unchecked")
    List<Donation> listing = q.list();
    
    session.getTransaction().commit();
    session.close();
    
    return listing;
  }

  @Override
  public List<Donation> getDonationsToBeRead()
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Query q = session.createQuery("from Donation d where d.readState = :readstate");
    q.setString("readstate", DonationReadState.PENDING.toString());
    
    @SuppressWarnings("unchecked")
    List<Donation> listing = q.list();
    
    session.getTransaction().commit();
    session.close();
    
    return listing;
  }

  @Override
  public void setDonationReadState(int donationId, DonationReadState readState)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Donation d = (Donation) session.get(Donation.class, donationId);
    d.setReadState(readState);
    session.flush();

    session.getTransaction().commit();
    session.close();
  }

  @Override
  public List<Donation> getDonationsInTimeRange(Date lo, Date hi)
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Query q = session.createQuery("from Donation d where d.timeReceived between :lotime and :hitime");
    q.setDate("lotime", lo);
    q.setDate("hitime", hi);

    @SuppressWarnings("unchecked")
    List<Donation> listing = q.list();
    
    session.getTransaction().commit();
    session.close();
    
    return listing;
  }

  @Override
  public List<Donation> getAllDonations()
  {
    Session session = this.getSessionFactory().openSession();
    session.beginTransaction();
    
    Query q = session.createQuery("from Donation");
    
    @SuppressWarnings("unchecked")
    List<Donation> listing = q.list();
    
    session.getTransaction().commit();
    session.close();
    
    return listing;
  }

}
