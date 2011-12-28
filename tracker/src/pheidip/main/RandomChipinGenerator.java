package pheidip.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import pheidip.logic.chipin.ChipinDonation;
import pheidip.objects.Donor;
import pheidip.util.IdUtils;
import pheidip.util.StringUtils;
import test.logic.ChipinTestUtils;

public class RandomChipinGenerator 
{
  public static void main(String[] args)
  {
		List<String> strings = GetAllStdIn();
		
		List<Donor> donors = new ArrayList<Donor>();
		
		for (String s : strings)
		{
		  String[] toks = s.trim().split("\\s+");
		  if (toks.length == 3)
		  {
		    donors.add(new Donor(IdUtils.generateId(), toks[1] + "." + toks[2] + "@" + toks[2] + ".com", null, toks[1], toks[2]));
		  }
		}
		
		int numDonations = Integer.parseInt(args[0]);
		Random rand = new SecureRandom();
		
		List<ChipinDonation> donations = new ArrayList<ChipinDonation>();
		
		Date currenttime = new Date();
		
		for (int i = 0; i < numDonations; ++i)
		{
		  String amount = StringUtils.randomDollarAmountString(1, 5, rand);
      
      String comment = StringUtils.randomStringOverAlphabet(0, 1000, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789()@:.,! ;'\"-?_", rand);
    
      int donorId = rand.nextInt(donors.size());
      
      donations.add(
          new ChipinDonation(
              donors.get(donorId).getFirstName() + " " + donors.get(donorId).getLastName(),
              donors.get(donorId).getEmail(),
              comment,
              ""+(currenttime.getTime()+rand.nextInt(1000000000)),
              new BigDecimal(amount)));
		}
		
		System.out.print(ChipinTestUtils.generateChipinHTMLTable(donations));
  }
	
  public static List<String> GetAllStdIn()
  {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	
    List<String> results = new ArrayList<String>();

    try
    {
      while (input.ready())
      {
        results.add(input.readLine());
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    
    return results;
  }
}
