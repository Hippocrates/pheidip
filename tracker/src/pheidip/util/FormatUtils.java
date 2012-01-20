package pheidip.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;

public final class FormatUtils
{
  public static String getMoneyFormatString()
  {
    return "#0.00";
  }
  
  public static Format getNullableMoneyFormat()
  {
    return new NullableFormat(getMoneyFormat());
  }
  
  public static Format getMoneyFormat()
  {
    return new DecimalFormat(getMoneyFormatString());
  }
  
  public static Format getNullableIntegerFormat()
  {
    return new NullableFormat(getIntegerFormat());
  }
  
  public static Format getIntegerFormat()
  {
    return new DecimalFormat("#0");
  }
  
  public static BigDecimal getNumberOrNull(String formatString)
  {
    return StringUtils.isEmptyOrNull(formatString) ? null : new BigDecimal(formatString);
  }
}
