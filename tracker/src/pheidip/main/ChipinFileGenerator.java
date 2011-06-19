package pheidip.main;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

import pheidip.objects.ChipinDonation;
import test.logic.ChipinTestUtils;

public class ChipinFileGenerator
{
  public static void main(String[] args)
  {
    Random rand;
    
    if (args.length > 0)
    {
      rand = new SecureRandom(args[0].getBytes());
    }
    else
    {
      rand = new SecureRandom();
    }
      
    List<ChipinDonation> donations = ChipinTestUtils.generateRandomDonations(375, 2000, rand);

    System.out.println(ChipinTestUtils.generateChipinHTMLTable(donations));
  }
}
