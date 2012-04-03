package meta.format;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Locale;

@SuppressWarnings("serial")
public class MoneyFormat extends Format
{
  private static final DecimalFormat moneyFormat = new DecimalFormat("#0.00", new DecimalFormatSymbols(Locale.CANADA));

  static
  {
    moneyFormat.setParseBigDecimal(true);
  }
  
  @Override
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
  {
    return moneyFormat.format(obj, toAppendTo, pos);
  }
  
  @Override
  public Object parseObject(String source, ParsePosition pos)
  {
    BigDecimal result = (BigDecimal) moneyFormat.parseObject(source, pos);
    
    if (result != null)
    {
      return result.setScale(2);
    }
    else
    {
      pos.setErrorIndex(0);
      return null;
    }
  }
}
