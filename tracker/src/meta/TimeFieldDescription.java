package meta;

import java.util.Date;
import java.text.SimpleDateFormat;

import lombok.Getter;

public class TimeFieldDescription extends AbstractBoundedField<Date>
{
  public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy kk:mm:ss zzz";
  
  @Getter
  private final String dateFormat;
  
  public TimeFieldDescription(boolean nullable)
  {
    this(nullable, DEFAULT_DATE_FORMAT);
  }
  
  public TimeFieldDescription(boolean nullable, String dateFormat)
  {
    this(nullable, dateFormat, null, null);
  }
  
  public TimeFieldDescription(boolean nullable, String dateFormat, Date minValue, Date maxValue)
  {
    super(Date.class, nullable, minValue, maxValue, new SimpleDateFormat(dateFormat));
    this.dateFormat = dateFormat;
  }
}
