package pheidip.objects;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import pheidip.model.ComparisonOperator;
import pheidip.model.EntitySpecification;
import pheidip.model.SearchProperty;
import pheidip.model.SearchSpecification;

public class DonationSearchParams extends SearchParameters<Donation>
{
  private static SearchSpecification<Donation> specification;
  
  private Donor donor;
  private Set<DonationDomain> domain;
  private Date loTime;
  private Date hiTime;
  private BigDecimal loAmount;
  private BigDecimal hiAmount;
  private Set<DonationBidState> targetBidState;
  private Set<DonationReadState> targetReadState;
  private Set<DonationCommentState> targetCommentState;
  
  @Override
  public SearchSpecification<Donation> getSearchSpecification()
  {
    if (specification == null)
    {
      EntitySpecification selfSpec = EntityMethods.getSpecification(this.getClass());
      EntitySpecification targetSpec = EntityMethods.getSpecification(Donation.class);

      specification = new SearchSpecification<Donation>(
          new SearchProperty[]{ new SearchProperty(selfSpec.getProperty("donor"), targetSpec.getProperty("donor"), ComparisonOperator.EQUALS),
          new SearchProperty(selfSpec.getProperty("domain"), targetSpec.getProperty("domain"), ComparisonOperator.IN),
          new SearchProperty(selfSpec.getProperty("loTime"), targetSpec.getProperty("timeReceived"), ComparisonOperator.GEQUALS),
          new SearchProperty(selfSpec.getProperty("hiTime"), targetSpec.getProperty("timeReceived"), ComparisonOperator.LEQUALS),
          new SearchProperty(selfSpec.getProperty("loAmount"), targetSpec.getProperty("amount"), ComparisonOperator.GEQUALS),
          new SearchProperty(selfSpec.getProperty("hiAmount"), targetSpec.getProperty("amount"), ComparisonOperator.LEQUALS),
          new SearchProperty(selfSpec.getProperty("targetBidState"), targetSpec.getProperty("bidState"), ComparisonOperator.IN),
          new SearchProperty(selfSpec.getProperty("targetReadState"), targetSpec.getProperty("readState"), ComparisonOperator.IN),
          new SearchProperty(selfSpec.getProperty("targetCommentState"), targetSpec.getProperty("commentState"), ComparisonOperator.IN) },
          new Class<?>[]{});
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

  public void setDomain(Set<DonationDomain> domain)
  {
    this.domain = Collections.unmodifiableSet(new HashSet<DonationDomain>(domain));
  }

  public Set<DonationDomain> getDomain()
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

  public void setTargetBidState(Set<DonationBidState> targetBidState)
  {
    this.targetBidState = Collections.unmodifiableSet(new HashSet<DonationBidState>(targetBidState));
  }

  public Set<DonationBidState> getTargetBidState()
  {
    return targetBidState;
  }

  public void setTargetReadState(Set<DonationReadState> targetReadState)
  {
    this.targetReadState = Collections.unmodifiableSet(new HashSet<DonationReadState>(targetReadState));
  }

  public Set<DonationReadState> getTargetReadState()
  {
    return targetReadState;
  }

  public void setTargetCommentState(Set<DonationCommentState> targetCommentState)
  {
    this.targetCommentState = Collections.unmodifiableSet(new HashSet<DonationCommentState>(targetCommentState));
  }

  public Set<DonationCommentState> getTargetCommentState()
  {
    return targetCommentState;
  }
}
