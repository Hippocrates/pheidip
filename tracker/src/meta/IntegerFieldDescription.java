package meta;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

@SuppressWarnings("serial") 
class IntToLongDecimalFormat extends Format
{
  private static final DecimalFormat decimalFormat = new DecimalFormat("#0");
  
  static
  {
    decimalFormat.setParseIntegerOnly(true);
  }
  
  @Override
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
  {
    return decimalFormat.format(obj, toAppendTo, pos);
  }

  @Override
  public Object parseObject(String source, ParsePosition pos)
  {
    Long result = (Long) decimalFormat.parseObject(source, pos);
    
    if (result != null)
    {
      return (int) ((long)result);
    }
    else
    {
      pos.setErrorIndex(0);
      return null;
    }
  }
}

public class IntegerFieldDescription extends AbstractBoundedField<Integer>
{
  private static final IntToLongDecimalFormat helper = new IntToLongDecimalFormat();
  
  public IntegerFieldDescription()
  {
    this(true);
  }
  
  public IntegerFieldDescription(boolean nullable)
  {
    this(nullable, null, null);
  }
  
  public IntegerFieldDescription(boolean nullable, Integer minValue, Integer maxValue)
  {
    super(Integer.class, nullable, minValue, maxValue, helper);
  }
}
