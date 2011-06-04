package pheidip.objects;

import java.math.BigDecimal;

import pheidip.util.StringUtils;

public class ChipinDonation
{
  private String name;
  private String email;
  private String comment;
  private String chipinTimeStamp;
  private BigDecimal amount;

  public ChipinDonation(String name, String email, String comment, String chipinId, BigDecimal amount)
  {
    this.name = name;
    this.email = email;
    this.comment = StringUtils.nullIfEmpty(comment);
    this.chipinTimeStamp = chipinId;
    this.amount = amount;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getEmail()
  {
    return this.email;
  }
  
  public String getComment()
  {
    return this.comment;
  }
  
  public String getChipinTimeString()
  {
    return this.chipinTimeStamp;
  }
  
  public BigDecimal getAmount()
  {
    return this.amount;
  }
  
  public String getChipinId()
  {
    return this.chipinTimeStamp + this.email;
  }
  
  public static int maxCommentLength()
  {
    return 4096;
  }
}
