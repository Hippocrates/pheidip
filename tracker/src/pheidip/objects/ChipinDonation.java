package pheidip.objects;

import java.math.BigDecimal;
import java.util.Date;

import pheidip.util.StringUtils;

public class ChipinDonation
{
  public static final int MAX_COMMENT_LENGTH = 4096;
  
  private String name;
  private String email;
  private String comment;
  private String chipinTimeStamp;
  private BigDecimal amount;
  private Date timeStamp;
  private String chipinId;

  public ChipinDonation(String name, String email, String comment, String chipinTimeStamp, BigDecimal amount)
  {
    this.name = name;
    this.email = email;
    this.comment = StringUtils.nullIfEmpty(comment);
    this.chipinTimeStamp = chipinTimeStamp;
    this.amount = amount;
    this.timeStamp = new Date(Long.parseLong(chipinTimeStamp));
    
    this.chipinId = this.chipinTimeStamp + this.email;
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
    return this.chipinId;
  }
  
  public Date getTimeStamp()
  {
    return this.timeStamp;
  }
}
