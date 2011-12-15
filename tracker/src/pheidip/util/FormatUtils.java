package pheidip.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public final class FormatUtils
{
  public static String getMoneyFormatString()
  {
    return "#0.00";
  }
  
  public static DecimalFormat getMoneyFormat()
  {
    return new DecimalFormat(getMoneyFormatString());
  }
  
  public static DecimalFormat getIntegerFormat()
  {
    return new DecimalFormat("#0");
  }
  
  public static BigDecimal getNumberOrNull(String formatString)
  {
    return StringUtils.isEmptyOrNull(formatString) ? null : new BigDecimal(formatString);
  }
}
