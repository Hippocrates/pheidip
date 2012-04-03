package meta;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

import lombok.Getter;
import meta.format.NullableFormat;

public abstract class MetaFieldDescription
{
  @Getter
  private Class<?> storageClass;
  
  @Getter
  private boolean nullable;
  
  @Getter
  private Format formatter;
  
  private ValidationFormat validationFormatter;

  public MetaFieldDescription(Class<?> storageClass, boolean nullable, Format formatter)
  {
    this.storageClass = storageClass;
    this.nullable = nullable;
    
    if (formatter != null)
    {
      this.validationFormatter = new ValidationFormat(formatter);
      this.formatter = this.nullable ? new NullableFormat(this.validationFormatter) : this.validationFormatter;
    }
  }

  public boolean validate(Object toValidate)
  {
    return (this.isNullable() && toValidate == null) || this.storageClass.isAssignableFrom(toValidate.getClass());
  }
  
  @SuppressWarnings("serial")
  private class ValidationFormat extends Format
  {
    Format rootFormatter;
    
    public ValidationFormat(Format rootFormatter)
    {
      this.rootFormatter = rootFormatter;
    }
    
    @Override
    public StringBuffer format(Object obj, StringBuffer str, FieldPosition pos)
    {
      if (!validate(obj))
      {
        throw new IllegalArgumentException("Error, object is not valid for this field");
      }
      else
      {
        return this.rootFormatter.format(obj, str, pos);
      }
    }

    @Override
    public Object parseObject(String str, ParsePosition pos)
    {
      int startIndex = pos.getIndex();
      Object result = this.rootFormatter.parseObject(str, pos);
      
      if (pos.getErrorIndex() == -1 && validate(result))
      {
        return result;
      }
      else
      {
        pos.setErrorIndex(startIndex);
        pos.setIndex(startIndex);
        return null;
      }
    }
    
  }
}
