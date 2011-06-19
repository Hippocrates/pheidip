package pheidip.db;

@SuppressWarnings("serial")
public class DonationDataConstraintException extends RuntimeException
{
  private DonationDataConstraint constraint;

  public DonationDataConstraintException(DonationDataConstraint constraint)
  {
    this.constraint = constraint;
  }
  
  public DonationDataConstraint getConstraintViolation()
  {
    return this.constraint;
  }
  
  public String getMessage()
  {
    return this.constraint.getErrorMessage();
  }
}
