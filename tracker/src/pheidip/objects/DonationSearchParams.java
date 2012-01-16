package pheidip.objects;

import java.math.BigDecimal;
import java.util.Date;

import pheidip.model.ComparisonOperator;
import pheidip.model.EntitySpecification;
import pheidip.model.SearchProperty;
import pheidip.model.SearchSpecification;

public class DonationSearchParams implements SearchEntity<Donation>
{
  private static SearchSpecification specification;
  
  private Donor donor;
  private DonationDomain domain;
  private Date loTime;
  private Date hiTime;
  private BigDecimal loAmount;
  private BigDecimal hiAmount;
  private DonationBidState targetBidState;
  private DonationReadState targetReadState;
  private DonationCommentState targetCommentState;
  
  @Override
  public SearchSpecification getSearchSpecification()
  {
    if (specification == null)
    {
      EntitySpecification selfSpec = EntityMethods.getSpecification(this.getClass());
      EntitySpecification targetSpec = EntityMethods.getSpecification(Donation.class);

      specification = new SearchSpecification(
          new SearchProperty(selfSpec.getProperty("donor"), targetSpec.getProperty("donor"), ComparisonOperator.EQUALS),
          new SearchProperty(selfSpec.getProperty("domain"), targetSpec.getProperty("domain"), ComparisonOperator.EQUALS),
          new SearchProperty(selfSpec.getProperty("loTime"), targetSpec.getProperty("timeReceived"), ComparisonOperator.GEQUALS),
          new SearchProperty(selfSpec.getProperty("hiTime"), targetSpec.getProperty("timeReceived"), ComparisonOperator.LEQUALS),
          new SearchProperty(selfSpec.getProperty("loAmount"), targetSpec.getProperty("amount"), ComparisonOperator.GEQUALS),
          new SearchProperty(selfSpec.getProperty("hiAmount"), targetSpec.getProperty("amount"), ComparisonOperator.LEQUALS),
          new SearchProperty(selfSpec.getProperty("targetBidState"), targetSpec.getProperty("bidState"), ComparisonOperator.EQUALS),
          new SearchProperty(selfSpec.getProperty("targetReadState"), targetSpec.getProperty("readState"), ComparisonOperator.EQUALS),
          new SearchProperty(selfSpec.getProperty("targetCommentState"), targetSpec.getProperty("commentState"), ComparisonOperator.EQUALS));
    }
    
    return specification;
  }

  public void setDonor(Donor donor)
  {
    this.donor = donor;
  }

  public Donor getDonor()
  {
    return donor;
  }

  public void setDomain(DonationDomain domain)
  {
    this.domain = domain;
  }

  public DonationDomain getDomain()
  {
    return domain;
  }

  public void setLoTime(Date loTime)
  {
    this.loTime = loTime;
  }

  public Date getLoTime()
  {
    return loTime;
  }

  public void setHiTime(Date hiTime)
  {
    this.hiTime = hiTime;
  }

  public Date getHiTime()
  {
    return hiTime;
  }

  public void setLoAmount(BigDecimal loAmount)
  {
    this.loAmount = loAmount;
  }

  public BigDecimal getLoAmount()
  {
    return loAmount;
  }

  public void setHiAmount(BigDecimal hiAmount)
  {
    this.hiAmount = hiAmount;
  }

  public BigDecimal getHiAmount()
  {
    return hiAmount;
  }

  public void setTargetBidState(DonationBidState targetBidState)
  {
    this.targetBidState = targetBidState;
  }

  public DonationBidState getTargetBidState()
  {
    return targetBidState;
  }

  public void setTargetReadState(DonationReadState targetReadState)
  {
    this.targetReadState = targetReadState;
  }

  public DonationReadState getTargetReadState()
  {
    return targetReadState;
  }

  public void setTargetCommentState(DonationCommentState targetCommentState)
  {
    this.targetCommentState = targetCommentState;
  }

  public DonationCommentState getTargetCommentState()
  {
    return targetCommentState;
  }
}
