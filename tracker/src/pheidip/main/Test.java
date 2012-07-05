package pheidip.main;

import java.util.Date;

public class Test
{
  public static void main(String[] args)
  {
    Date d = new Date(Long.parseLong("1337904708000"));
    
    System.out.println(d.toString());
    
    System.out.println(d.toGMTString());
  }
}
