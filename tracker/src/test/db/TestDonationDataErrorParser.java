package test.db;

import pheidip.db.DonationDataConstraint;
import pheidip.db.DonationDataErrorParser;
import junit.framework.TestCase;

public class TestDonationDataErrorParser extends TestCase
{
  public void testParseMySQLUniqueError()
  {
    DonationDataConstraint c = DonationDataErrorParser.parseError("Error Code = 1062 : Duplicate entry 'yy' for key 'DonorAliasUnique'");
    
    assertEquals(DonationDataConstraint.DonorAliasUnique, c);
  }
  
  public void testParseHSQLDBUniqueError()
  {
    DonationDataConstraint c = DonationDataErrorParser.parseError("Error Code = -104 : integrity constraint violation: unique constraint or index violation; SPEEDRUNNAMEUNIQUE table: SPEEDRUN");
    
    assertEquals(DonationDataConstraint.SpeedRunNameUnique, c);
  }
  
  public void testParseHSQLDBFKError()
  {
    DonationDataConstraint c = DonationDataErrorParser.parseError("Error Code = -8 : integrity constraint violation: foreign key no action; CHOICEFKSPEEDRUN table: CHOICE");
    
    assertEquals(DonationDataConstraint.ChoiceFKSpeedRun, c);
  }
}
