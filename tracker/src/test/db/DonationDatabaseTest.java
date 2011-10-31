package test.db;

import java.math.BigDecimal;

import org.hibernate.Session;

import pheidip.db.DonationDataAccess;
import pheidip.db.hibernate.HibernateDonationDataAccess;
import pheidip.objects.BidState;
import pheidip.objects.Challenge;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceOption;
import pheidip.objects.Donor;
import pheidip.objects.Prize;
import pheidip.objects.SpeedRun;
import junit.framework.TestCase;

public abstract class DonationDatabaseTest extends TestCase
{
  private HibernateDonationDataAccess dataAccess;
  
  public DonationDataAccess getDataAccess()
  {
    return this.dataAccess;
  }

  public DonationDatabaseTest()
  {
  }

  public void setUp()
  {
    this.dataAccess = new HibernateDonationDataAccess();
    
    this.dataAccess.createMemoryDatabase();
    
    this.populate();
  }
  
  public void populate()
  {
    Session session = this.dataAccess.getSessionFactory().openSession();
    session.beginTransaction();
    
    session.save(new Donor(1, "test1@test.com", "smk", "Stephen", "Kiazyk"));
    session.save(new Donor(2, "test2@test.com", "analias", "Stefan", "Ksiazyk"));
    session.save(new Donor(3, "test3@test.com", "demonrushfan6969", "Brooks", "Cracktackle"));
    session.save(new Donor(4, "test4@test.com", "anotheralias", "", ""));
    session.save(new Donor(5, null, null, "", ""));
    session.save(new Donor(6, null, null, "", ""));
    
    session.save(new SpeedRun(1, "run 1", null));
    session.save(new SpeedRun(2, "run 2", null));
    session.save(new SpeedRun(3, "yet another run", null));

    session.save(new Prize(1, "a prize", null, null, (Donor)session.load(Donor.class, 2)));
    session.save(new Prize(2, "another prize", null, null, (Donor)null));
    session.save(new Prize(3, "one more prize", null, null, (Donor)session.load(Donor.class, 1)));

    session.save(new Choice(1, "naming something", null, BidState.OPENED, (SpeedRun)session.load(SpeedRun.class, 1)));
    session.save(new Choice(2, "naming something something", null, BidState.OPENED, (SpeedRun)session.load(SpeedRun.class, 1)));
    session.save(new Choice(3, "a path of some sort", null, BidState.OPENED, (SpeedRun)session.load(SpeedRun.class, 1)));
    session.save(new Choice(4, "a path of some sort", null, BidState.OPENED, (SpeedRun)session.load(SpeedRun.class, 2)));
    
    session.save(new ChoiceOption(1, "name 1", (Choice)session.load(Choice.class, 1)));
    session.save(new ChoiceOption(2, "name 2", (Choice)session.load(Choice.class, 1)));
    session.save(new ChoiceOption(3, "name 1", (Choice)session.load(Choice.class, 2)));
    
    session.save(new Challenge(5, "challenge 1", new BigDecimal("20.00"), null, BidState.OPENED, (SpeedRun)session.load(SpeedRun.class, 2)));
    session.save(new Challenge(6, "challenge 2", new BigDecimal("50.00"), null, BidState.OPENED, (SpeedRun)session.load(SpeedRun.class, 1)));
    session.save(new Challenge(7, "challenge 2", new BigDecimal("100.00"), null, BidState.OPENED, (SpeedRun)session.load(SpeedRun.class, 2)));
    session.save(new Challenge(8, "challenge whatever", new BigDecimal("150.00"), null, BidState.OPENED, (SpeedRun)session.load(SpeedRun.class, 2)));
    
    //session.save(new Donation(1, DonationDomain.LOCAL, null, DonationBidState.PENDING, DonationReadState.PENDING, DonationCommentState.PENDING, new BigDecimal("50.40"), new Date("yikes"
    /*
    INSERT INTO Donation VALUES(1, 1, 'LOCAL', NULL, 'PENDING', 'PENDING', 'PENDING', 50.40, '2004-10-18 23:32:34', NULL);
    INSERT INTO Donation VALUES(2, 2, 'LOCAL', NULL, 'PENDING', 'PENDING', 'PENDING', 25.00, '2006-10-18 00:32:34', NULL);
    INSERT INTO Donation VALUES(3, 4, 'LOCAL', NULL, 'PENDING', 'PENDING', 'PENDING', 25.00, '2006-10-18 00:32:34', NULL);
    INSERT INTO Donation VALUES(4, 4, 'LOCAL', NULL, 'PENDING', 'PENDING', 'PENDING',  5.00, '2006-10-18 12:44:55', NULL);
    INSERT INTO Donation VALUES(5, 6, 'LOCAL', NULL, 'PENDING', 'PENDING', 'PENDING', 25.00, '2006-10-18 00:32:34', NULL);
    INSERT INTO Donation VALUES(7, 3, 'CHIPIN', '1234567890', 'PENDING', 'PENDING', 'PENDING', 15.00, '2006-11-13 10:00:00', 'Some comment text.');

    INSERT INTO ChoiceBid VALUES(1, 1, 1, 10.00);
    INSERT INTO ChoiceBid VALUES(2, 1, 1, 5.00);

    INSERT INTO ChallengeBid VALUES(1, 1, 1, 10.00);
    INSERT INTO ChallengeBid VALUES(2, 1, 1, 10.00);
    INSERT INTO ChallengeBid VALUES(3, 2, 1, 10.00);

    
    */
    session.getTransaction().commit();
    session.close();
  }

  public void tearDown()
  {
    if (this.dataAccess.isConnected())
    {
      this.dataAccess.closeConnection();
    }
  }

}