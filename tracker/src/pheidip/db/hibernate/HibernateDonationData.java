package pheidip.db.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import pheidip.db.DonationData;
import pheidip.objects.BidType;
import pheidip.objects.Challenge;
import pheidip.objects.ChallengeBid;
import pheidip.objects.ChoiceBid;
import pheidip.objects.ChoiceOption;
import pheidip.objects.Donation;
import pheidip.objects.DonationBid;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationSearchParams;
import pheidip.objects.SearchParameters;

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

    Donation dummy = new Donation();
    dummy.setId(id);
    session.evict(dummy);
    
    Donation d = (Donation) session.load(Donation.class, id);
 
    this.endTransaction();
    
    return d;
  }

  @Override
  public void deleteDonation(Donation d)
  {
    Session session = this.beginTransaction();
    
    d = (Donation) session.merge(d);

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
    DonationSearchParams params = new DonationSearchParams();
    
    params.setLoTime(lo);
    params.setHiTime(hi);
    
    return this.searchDonations(params);
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
  public List<Donation> searchDonationsRange(SearchParameters<Donation> params, int offset, int size)
  {
    String queryString = SQLMethods.makeHQLSearchQueryString(params, "Donation", "timeReceived");
    
    StatelessSession dedicatedSession = this.beginBulkTransaction();

    Query q = dedicatedSession.createQuery(queryString);

    SQLMethods.applyParametersToQuery(q, params);

    q.setFirstResult(offset);
    q.setMaxResults(size);
    
    @SuppressWarnings("unchecked")
    List<Donation> listing = q.list();
    
    this.endBulkTransaction(dedicatedSession);
    
    return listing;
  }
  
  @Override
  public List<Donation> searchDonations(SearchParameters<Donation> params)
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
    // Super hacky, I'll need to figure out a way around this...
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
    
    remove = (DonationBid) session.merge(remove);
    
    if (remove.getType() == BidType.CHOICE)
    {
      ChoiceOption bid = (ChoiceOption) session.merge(((ChoiceBid)remove).getOption());
      bid.getBids().remove((ChoiceBid)remove);
    }
    else if (remove.getType() == BidType.CHALLENGE)
    {
      Challenge bid = (Challenge) session.merge(((ChallengeBid)remove).getChallenge());
      bid.getBids().remove((ChallengeBid)remove);
    }
    
    Donation d = (Donation) session.merge(remove.getDonation());
    
    d.getBids().remove(remove);
    
    session.delete(remove);
    this.endTransaction();
  }

}
