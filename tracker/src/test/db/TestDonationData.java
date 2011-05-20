package test.db;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import pheidip.db.DonationDataAccess;
import pheidip.db.DonationData;
import pheidip.db.ScriptRunner;
import pheidip.objects.Donation;
import pheidip.objects.DonationAnnounceState;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationPaymentState;

public class TestDonationData extends TestCase
{
  private DonationDataAccess dataAccess;
  private DonationData donations;

  public void setUp()
  {
    this.dataAccess = new DonationDataAccess();
    this.dataAccess.createMemoryDatabase();

    ScriptRunner runner = new ScriptRunner(this.dataAccess.getConnection(), true,
        true);

    try
    {
      runner.runScript(new FileReader(DBTestConfiguration
          .getTestDataDirectory() + "donation_bid_test_data_1.sql"));
    } 
    catch (IOException e)
    {
      fail(e.getMessage());
    } 
    catch (SQLException e)
    {
      this.dataAccess.handleSQLException(e);
    }

    this.donations = new DonationData(this.dataAccess);
  }

  public void tearDown()
  {
    if (this.dataAccess.isConnected())
    {
      this.dataAccess.closeConnection();
    }
  }

  public void testGetDonationById()
  {
    final int id = 1;
    Donation d = this.donations.getDonationById(id);

    assertEquals(id, d.getId());
    assertEquals(1, d.getDonorId());
    assertEquals(new BigDecimal("12.40"), d.getAmount());
    assertEquals(DonationDomain.LOCAL, d.getDomain());
    // domain Id should be null for local donations (they don't have 
    // any specific Id)
    assertEquals(null, d.getDomainId());
    assertEquals(DonationPaymentState.RECEIVED, d.getPaymentState());
    assertEquals(DonationAnnounceState.UNREAD, d.getAnnounceState());
    assertEquals(DonationBidState.PENDING, d.getBidState());
  }
  
  public void testGetDonationByDomainId()
  {
    final String domainId = "1234567890";
    Donation d = this.donations.getDonationByDomainId(DonationDomain.CHIPIN, "1234567890");
    
    assertNotNull(d);
    assertEquals(domainId, d.getDomainId());
    assertEquals(3, d.getDonorId());
  }

  public void testGetDonorDonations()
  {
    List<Donation> dlist = this.donations.getDonorDonations(4);

    assertEquals(2, dlist.size());

    for (int i = 0; i < dlist.size(); ++i)
    {
      assertEquals(4, dlist.get(i).getDonorId());
    }
  }

  public void testGetDonorDonationTotal()
  {
    assertEquals(new BigDecimal("30.00"),
        this.donations.getDonorDonationTotal(4));
  }

  public void testSetDonationComment()
  {
    final int id = 1;
    Donation d = this.donations.getDonationById(id);
    
    assertNull(d.getComment());
    
    final String comment = "Some text, :LASDFJFLSDAKHASDGLIHASOIHJASDOUIDFSANLASDFJKHFDSALKJDFAS";
    
    this.donations.setDonationComment(id, comment);
    
    Donation result = this.donations.getDonationById(id);
    
    assertEquals(comment, result.getComment());
  }
  
  public void testSetDonationPaymentState()
  {
    final int id = 1;
    
    Donation d = this.donations.getDonationById(id);
    
    assertEquals(DonationPaymentState.RECEIVED, d.getPaymentState());
    
    this.donations.setDonationPaymentState(id, DonationPaymentState.CANCELLED);
    
    d = this.donations.getDonationById(id);
    
    assertEquals(DonationPaymentState.CANCELLED, d.getPaymentState());
  }
  
  public void testSetDonationAnnounceState()
  {
    final int id = 1;
    
    Donation d = this.donations.getDonationById(id);
    
    assertEquals(DonationAnnounceState.UNREAD, d.getAnnounceState());
    
    this.donations.setDonationAnnounceState(id, DonationAnnounceState.AMOUNT_READ);
    
    d = this.donations.getDonationById(id);
    
    assertEquals(DonationAnnounceState.AMOUNT_READ, d.getAnnounceState());
  }
  
  public void testSetDonationBidState()
  {
    final int id = 1;
    
    Donation d = this.donations.getDonationById(id);
    
    assertEquals(DonationBidState.PENDING, d.getBidState());
    
    this.donations.setDonationBidState(id, DonationBidState.PROCESSED);
    
    d = this.donations.getDonationById(id);
    
    assertEquals(DonationBidState.PROCESSED, d.getBidState());
  }
  
  public void testDeleteDonation()
  {
    final int id = 4;
    Donation d = this.donations.getDonationById(id);
    
    assertNotNull(d);
    
    this.donations.deleteDonation(id);
    
    assertNull(this.donations.getDonationById(id));
  }
  
  public void testCreateDonation()
  {
    final Date timeStamp = new Date();
    final int id = (int) timeStamp.getTime();
    
    final Donation template = new Donation(
        id, 
        DonationDomain.LOCAL, 
        null, 
        DonationPaymentState.RECEIVED, 
        DonationAnnounceState.UNREAD,
        DonationBidState.PENDING, 
        new BigDecimal("3.50"),
        timeStamp, 
        1,
        "A comment of some sort.");
    
    this.donations.insertDonation(template);
    
    final Donation result = this.donations.getDonationById(template.getId());
    
    this.compareDonations(template, result);
  }
  
  public void testUpdateDonation()
  {
    final int id = 4;
    final int donorId = 1;
    final DonationDomain domain = DonationDomain.PAYPAL;
    final String domainId = "ffffffffffee";
    final DonationPaymentState paymentState = DonationPaymentState.PENDING;
    final DonationAnnounceState announceState = DonationAnnounceState.UNREAD;
    final DonationBidState bidState = DonationBidState.PENDING;
    final BigDecimal amount = new BigDecimal("13.37");
    final Date timeReceived = new Date();
    final String comment = "asdlkjhasdkjhasdsda";
    
    Donation updated = new Donation(id, domain, domainId, paymentState, announceState, bidState, amount, timeReceived, donorId, comment);
    
    this.donations.updateDonation(updated);
    
    this.compareDonations(updated, this.donations.getDonationById(id));
  }
  
  private void compareDonations(Donation a, Donation b)
  {
    assertEquals(a.getId(), b.getId());
    assertEquals(a.getDonorId(), b.getDonorId());
    assertEquals(a.getDomain(), b.getDomain());
    assertEquals(a.getDomainId(), b.getDomainId());
    assertEquals(a.getPaymentState(), b.getPaymentState());
    assertEquals(a.getAnnounceState(), b.getAnnounceState());
    assertEquals(a.getBidState(), b.getBidState());
    assertEquals(a.getAmount(), b.getAmount());
    assertEquals(a.getTimeReceived(), b.getTimeReceived());
    assertEquals(a.getComment(), b.getComment());
  }
}
