package test.db;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Session;

import pheidip.db.DonationDataAccess;
import pheidip.db.hibernate.HibernateDonationDataAccess;
import pheidip.objects.BidState;
import pheidip.objects.Challenge;
import pheidip.objects.ChallengeBid;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceBid;
import pheidip.objects.ChoiceOption;
import pheidip.objects.Donation;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationCommentState;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationReadState;
import pheidip.objects.Donor;
import pheidip.objects.Prize;
import pheidip.objects.PrizeDrawMethod;
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

    session.save(new Donor(1, "test1@test.com", "smk", "Stephen", "Kiazyk"));
    session.save(new Donor(2, "test2@test.com", "analias", "Stefan", "Ksiazyk"));
    session.save(new Donor(3, "test3@test.com", "demonrushfan6969", "Brooks", "Cracktackle"));
    session.save(new Donor(4, "test4@test.com", "anotheralias", "", ""));
    session.save(new Donor(5, null, null, "", ""));
    session.save(new Donor(6, null, null, "", ""));
    
    session.save(new SpeedRun(1, "run 1", "", 1, new Date(), new Date(), null));
    session.save(new SpeedRun(2, "run 2", "", 2, new Date(), new Date(), null));
    session.save(new SpeedRun(3, "yet another run", "", 3, new Date(), new Date(), null));

    session.save(new Prize(1, "a prize", null, null, 1, PrizeDrawMethod.RANDOM_UNIFORM_DRAW, new BigDecimal("5.00"), (Donor)session.load(Donor.class, 2), null, null));
    session.save(new Prize(2, "another prize", null, null, 2, PrizeDrawMethod.RANDOM_UNIFORM_DRAW, new BigDecimal("5.00"), (Donor)null, null, null));
    session.save(new Prize(3, "one more prize", null, null, 3, PrizeDrawMethod.RANDOM_UNIFORM_DRAW, new BigDecimal("5.00"), (Donor)session.load(Donor.class, 1), null, null));

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
    
    Calendar c = Calendar.getInstance();
    
    c.set(2004,10,18,23,32,34);
    session.save(new Donation(1, DonationDomain.LOCAL, null, DonationBidState.PENDING, DonationReadState.PENDING, DonationCommentState.PENDING, new BigDecimal("50.40"), c.getTime(), (Donor)session.load(Donor.class, 1), null));
    c.set(2006,10,18,00,32,34);
    session.save(new Donation(2, DonationDomain.LOCAL, null, DonationBidState.PENDING, DonationReadState.PENDING, DonationCommentState.PENDING, new BigDecimal("25.00"), c.getTime(), (Donor)session.load(Donor.class, 2), null));
    c.set(2006,10,18,00,32,34);
    session.save(new Donation(3, DonationDomain.LOCAL, null, DonationBidState.PENDING, DonationReadState.PENDING, DonationCommentState.PENDING, new BigDecimal("25.00"), c.getTime(), (Donor)session.load(Donor.class, 4), null));
    c.set(2006,10,18,12,44,55);
    session.save(new Donation(4, DonationDomain.LOCAL, null, DonationBidState.PENDING, DonationReadState.PENDING, DonationCommentState.PENDING, new BigDecimal("5.00"), c.getTime(), (Donor)session.load(Donor.class, 4), null));
    c.set(2006,10,18,00,32,34);
    session.save(new Donation(5, DonationDomain.LOCAL, null, DonationBidState.PENDING, DonationReadState.PENDING, DonationCommentState.PENDING, new BigDecimal("25.00"), c.getTime(), (Donor)session.load(Donor.class, 6), null));
    c.set(2006,11,13,10,00,00);
    session.save(new Donation(7, DonationDomain.CHIPIN, "1234567890", DonationBidState.PENDING, DonationReadState.PENDING, DonationCommentState.PENDING, new BigDecimal("15.00"), c.getTime(), (Donor)session.load(Donor.class, 3), "Some comment text."));
    
    session.save(new ChoiceBid(1, new BigDecimal("10.00"), (ChoiceOption)session.load(ChoiceOption.class, 1), (Donation)session.load(Donation.class, 1)));
    session.save(new ChoiceBid(2, new BigDecimal("5.00"), (ChoiceOption)session.load(ChoiceOption.class, 1), (Donation)session.load(Donation.class, 1)));
    
    session.save(new ChallengeBid(3, new BigDecimal("10.00"), (Challenge) session.load(Challenge.class, 5), (Donation)session.load(Donation.class, 1)));
    session.save(new ChallengeBid(4, new BigDecimal("10.00"), (Challenge) session.load(Challenge.class, 5), (Donation)session.load(Donation.class, 1)));
    session.save(new ChallengeBid(5, new BigDecimal("10.00"), (Challenge) session.load(Challenge.class, 6), (Donation)session.load(Donation.class, 1)));

    session.getTransaction().commit();
    //session.close();
  }

  public void tearDown()
  {
    if (this.dataAccess.isConnected())
    {
      this.dataAccess.closeConnection();
    }
  }

}