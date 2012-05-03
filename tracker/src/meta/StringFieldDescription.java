package meta;

import lombok.Getter;
import meta.format.PassthroughFormat;


public class StringFieldDescription extends MetaFieldDescription
{
  private static final PassthroughFormat nilFormatter = new PassthroughFormat();
  
  @Getter
  private final Integer maxLength;
  
  @Getter
  private final Integer minLength;
  
  @Getter
  private final boolean caseSentitive;
  
  public StringFieldDescription(boolean nullable)
  {
    this(nullable, true);
  }
  
  public StringFieldDescription(boolean nullable, boolean caseSentitive)
  {
    this(nullable, true, null, null);
  }
  
  public StringFieldDescription(boolean nullable, boolean caseSentitive, Integer minLength, Integer maxLength)
  {
    super(String.class, nullable, nilFormatter);
    this.minLength = minLength;
    this.maxLength = maxLength;
    this.caseSentitive = caseSentitive;
  }
  
  public boolean hasMaxLength()
  {
    return this.maxLength != null;
  }
  
  public boolean hasMinLength()
  {
    return this.minLength != null;
  }
  
  public boolean validate(Object toValidate)
  {
    return super.validate(toValidate) && (toValidate == null || (toValidate.toString().length() >= this.getMinLength() && toValidate.toString().length() <= getMaxLength()));
  }
}
