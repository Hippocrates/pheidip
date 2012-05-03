package meta.format;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.EnumSet;


import pheidip.util.StringUtils;

@SuppressWarnings("serial")
public class EnumSetFormat<T extends Enum<T>> extends Format
{
  public static final String DELIMITER = ";";
  
  private Class<T> enumClass;
  private Format enumFormatter;
  
  public EnumSetFormat(Class<T> enumClass)
  {
    this.enumClass = enumClass;
    this.enumFormatter = new EnumFormat<T>(this.enumClass);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
  {
    if (obj == null)
    {
      throw new NullPointerException();
    }
    else if (!(obj instanceof EnumSet))
    {
      throw new IllegalArgumentException(String.format("Error, expected: %s, got: %s", EnumSet.class.getName(), obj.getClass().getName()));
    }
    else
    {
      EnumSet<T> casted = (EnumSet<T>) obj;
      
      StringBuilder builder = new StringBuilder();
      
      boolean first = true;
      
      for (T value : casted)
      {
        if (value.getClass() != this.enumClass)
        {
          throw new RuntimeException("Don't be an idiot");
        }
        
        if (first)
        {
          first = false;
        }
        else
        {
          builder.append(DELIMITER);
        }
        
        builder.append(this.enumFormatter.format(value));
      }
      
      toAppendTo.append(builder.toString());
      
      return toAppendTo;
    }
  }


  @SuppressWarnings("unchecked")
  @Override
  public Object parseObject(String source, ParsePosition pos)
  {
    String[] toks = StringUtils.emptyIfNull(source).substring(pos.getIndex()).split(";");
    
    EnumSet<T> result = EnumSet.noneOf(this.enumClass);
    
    int position = 0;
    ParsePosition subPosition = new ParsePosition(0);
    
    if (!(toks.length == 1 && toks[0].length() == 0))
    {
      for (String t : toks)
      {
        T parsed = (T)this.enumFormatter.parseObject(t, subPosition);
        
        if (subPosition.getErrorIndex() != -1)
        {
          pos.setErrorIndex(position + subPosition.getErrorIndex());
          return null;
        }
        
        result.add(parsed);
        position += t.length() + 1;
      }
    }
    
    position = position > 0 ? position - 1 : 1;
    
    pos.setIndex(position);
    return result;
  }

}
