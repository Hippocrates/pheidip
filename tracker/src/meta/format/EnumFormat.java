package meta.format;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

import pheidip.util.StringUtils;

@SuppressWarnings("serial")
public class EnumFormat<T extends Enum<T>> extends Format
{
  private Class<T> enumClass;
  
  public EnumFormat(Class<T> enumClass)
  {
    this.enumClass = enumClass;
  }
  
  @Override
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
  {
    if (toAppendTo == null)
    {
      throw new NullPointerException();
    }
    else if (obj == null)
    {
      throw new IllegalArgumentException("Null string not allowed");
    }
    else if (!(obj instanceof Enum<?>))
    {
      throw new IllegalArgumentException(String.format("Expected an enum class, Got: %s", obj.getClass().getName()));
    }
    else
    {
      toAppendTo.append(obj.toString());
      return toAppendTo;
    }
  }

  @Override
  public Object parseObject(String source, ParsePosition pos)
  {
    try
    {
      T result = (T) Enum.valueOf(enumClass, StringUtils.naturalToSymbol(source));
      pos.setIndex(source.length());
      return result;
    }
    catch (Exception e)
    {
      pos.setErrorIndex(0);
      return null;
    }
  }
}
