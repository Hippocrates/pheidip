package pheidip.util;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

@SuppressWarnings("serial")
public class NullableFormat extends Format
{
  private Format format;
  
  public NullableFormat(Format format)
  {
    this.format = format;
  }

  @Override
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
  {
    if (obj == null)
    {
      return toAppendTo;
    }
    else
    {
      return this.format.format(obj, toAppendTo, pos);
    }
  }

  @Override
  public Object parseObject(String source, ParsePosition pos)
  {
    if (source == null || source.equals(""))
    {
      return null;
    }
    else
    {
      return this.format.parseObject(source, pos);
    }
  }
}
