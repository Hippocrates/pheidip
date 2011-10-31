package test.db;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


import pheidip.db.DonationData;
import pheidip.objects.Donation;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationCommentState;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationReadState;

public class TestDonationData extends DonationDatabaseTest
{
  DonationData donations;
  
  public void setUp()
  {
    super.setUp();
    this.donations = this.getDataAccess().getDonationData();
  }

  public void testGetDonationById()
  {
    final int id = 1;
    Donation d = this.donations.getDonationById(id);

    assertEquals(id, d.getId());
    //assertEquals(1, d.getDonorId());
    assertEquals(new BigDecimal("50.40"), d.getAmount());
    assertEquals(DonationDomain.LOCAL, d.getDomain());
    // domain Id should be null for local donations (they don't have 
    // any specific Id)
    assertEquals(null, d.getDomainId());
    assertEquals(DonationBidState.PENDING, d.getBidState());
  }
  
  public void testGetDonationByDomainId()
  {
    final String domainId = "1234567890";
    Donation d = this.donations.getDonationByDomainId(DonationDomain.CHIPIN, "1234567890");
    
    assertNotNull(d);
    assertEquals(domainId, d.getDomainId());
    //assertEquals(3, d.getDonorId());
  }

  public void testGetDonorDonations()
  {
    List<Donation> dlist = this.donations.getDonorDonations(4);

    assertEquals(2, dlist.size());

    for (int i = 0; i < dlist.size(); ++i)
    {
      assertEquals(4, dlist.get(i).getDonor().getId());
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
  
  public void testSetDonationBidState()
  {
    final int id = 1;
    
    Donation d = this.donations.getDonationById(id);
    
    assertEquals(DonationBidState.PENDING, d.getBidState());
    
    this.donations.setDonationBidState(id, DonationBidState.PROCESSED);
    
    d = this.donations.getDonationById(id);
    
    assertEquals(DonationBidState.PROCESSED, d.getBidState());
  }
  
  public void testSetDonationAmount()
  {
    final int id = 1;
    
    Donation d = this.donations.getDonationById(id);
    
    assertEquals(new BigDecimal("50.40"), d.getAmount());
    
    BigDecimal newAmount = new BigDecimal("5.55");
    
    this.donations.setDonationAmount(id, newAmount);
    
    d = this.donations.getDonationById(id);
    
    assertEquals(newAmount, d.getAmount());
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
        DonationBidState.PENDING, 
        DonationReadState.PENDING,
        DonationCommentState.PENDING,
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
    final DonationDomain domain = DonationDomain.CHIPIN;
    final String domainId = "ffffffffffee";
    final DonationBidState bidState = DonationBidState.PENDING;
    final DonationReadState readState = DonationReadState.PENDING;
    final DonationCommentState commentState = DonationCommentState.PENDING;
    final BigDecimal amount = new BigDecimal("13.37");
    final Date timeReceived = new Date();
    final String comment = "asdlkjhasdkjhasdsda";
    
    Donation updated = new Donation(id, domain, domainId, bidState, readState, commentState, amount, timeReceived, donorId, comment);
    
    this.donations.updateDonation(updated);
    
    this.compareDonations(updated, this.donations.getDonationById(id));
  }
  
  private void compareDonations(Donation a, Donation b)
  {
    assertEquals(a.getId(), b.getId());
    assertEquals(a.getDonorId(), b.getDonorId());
    assertEquals(a.getDomain(), b.getDomain());
    assertEquals(a.getDomainId(), b.getDomainId());
    assertEquals(a.getBidState(), b.getBidState());
    assertEquals(a.getAmount(), b.getAmount());
    assertEquals(a.getTimeReceived(), b.getTimeReceived());
    assertEquals(a.getComment(), b.getComment());
  }
}
