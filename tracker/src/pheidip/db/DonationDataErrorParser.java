package pheidip.db;

public final class DonationDataErrorParser
{
  public static DonationDataConstraint parseError(String errorString)
  {
    final String errorLowerCase = errorString.toLowerCase();
    
    for (int i = 0; i < DonationDataConstraint.size(); ++i)
    {
      DonationDataConstraint c = DonationDataConstraint.get(i);

      if (errorLowerCase.contains(c.toString().toLowerCase()))
      {
        return c;
      }
    }
    
    return null;
  }
}
