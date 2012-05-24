package meta.format;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
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
  public Object parseObject(String source) throws ParseException
  {
    ParsePosition p = new ParsePosition(0);
    
    Object result = this.parseObject(source, p);
    
    if (p.getErrorIndex() == -1)
    {
      return result;
    }
    else
    {
      throw new ParseException(String.format("Parse of %s failed.", source), p.getErrorIndex());
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
