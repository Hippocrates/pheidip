package test.hdb;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import pheidip.db.hibernate.HibernateManager;
import pheidip.objects.Donor;
import pheidip.objects.Prize;

public class TestCreateSession extends TestCase
{
  private SessionFactory sessionFactory;
  
  public void setUp()
  {
    this.sessionFactory = HibernateManager.createMemorySessionFactory();
  }
  
  public void tearDown()
  {
    if (this.sessionFactory != null) 
    {
      this.sessionFactory.close();
    }
  }
  
  public void testCreateSession()
  {
    System.out.println("yay");
    
    Set<Donor> donors = new HashSet<Donor>();
    
    for (int i = 0; i < 5; ++i)
    {
      Donor donor = new Donor();
      donors.add(donor);
      
      donor.setAlias("" + i);
      donor.getPrizes().add(new Prize());
      
      System.out.println("Donor(" + donor.getId() + ", " + donor.getAlias() + ") : " + donors.contains(donor));
      
      for (Prize prize : donor.getPrizes())
      {
        System.out.println("\tPrize(" + prize.getId() + ")");
      }
    }
    
    System.out.println();

    Session session = sessionFactory.openSession();
    session.beginTransaction();
    
    for (Donor d : donors)
    {
      session.save(d);
    }
    
    session.getTransaction().commit();
    session.close();
    
    Session session2 = sessionFactory.openSession();
    session2.beginTransaction();
    @SuppressWarnings("unchecked")
    List<Donor> allDonors = session2.createQuery("from Donor order by id").list();
    
    for (Donor donor : allDonors) 
    {
      System.out.println("Donor(" + donor.getId() + ", " + donor.getAlias() + ") : " + donors.contains(donor));
      
      for (Prize prize : donor.getPrizes())
      {
        System.out.println("\tPrize(" + prize.getId() + ")");
      }
    }
    
    Donor disconnected = allDonors.get(4);
    
    session2.getTransaction().commit();
    session2.close();
    
    System.out.println();
    
    Session session3 = sessionFactory.openSession();
    session3.beginTransaction();
    @SuppressWarnings("unchecked")
    List<Prize> allPrizes = session3.createQuery("from Prize order by id").list();
    for (Prize prize : allPrizes)
    {
      System.out.println("\tPrize(" + prize.getId() + ")");
    }
    session3.getTransaction().commit();
    session3.close();
    
    disconnected.setAlias("Buffalol");
    
    Session session4 = sessionFactory.openSession();
    session4.beginTransaction();
    
    Donor d = (Donor) session4.load(Donor.class, allDonors.get(0).getId());
    session4.delete(d);
    session4.merge(disconnected);
    session4.getTransaction().commit();
    session4.close();
    
    System.out.println();
    
    Session session5 = sessionFactory.openSession();
    session5.beginTransaction();

    @SuppressWarnings("unchecked")
    List<Donor> allDonors2 = session5.createQuery("from Donor order by id").list();
    
    for (Donor donor : allDonors2) 
    {
      System.out.println("Donor(" + donor.getId() + ", " + donor.getAlias() + ") : " + donors.contains(donor));
      
      for (Prize prize : donor.getPrizes())
      {
        System.out.println("\tPrize(" + prize.getId() + ")");
      }
    }
    
    session5.getTransaction().commit();
    session5.close();
  }
}
