package meta;

import java.text.Format;

import lombok.Getter;

public class AbstractBoundedField<T extends Comparable<T>> extends MetaFieldDescription
{
  @Getter
  private final T minValue;
  
  @Getter
  private final T maxValue;
  
  public AbstractBoundedField(Class<T> storageClass, boolean nullable, T minValue, T maxValue, Format rootFormat)
  {
    super(storageClass, nullable, rootFormat);
    this.minValue = minValue;
    this.maxValue = maxValue;
  }
  
  public boolean hasMinimumValue()
  {
    return this.minValue != null;
  }
  
  public boolean hasMaximumValue()
  {
    return this.maxValue != null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean validate(Object toValidate)
  {
    return super.validate(toValidate) && (toValidate == null || ((!this.hasMinimumValue() || this.minValue.compareTo((T)toValidate) <= 0) && (!this.hasMaximumValue() || this.maxValue.compareTo((T)toValidate) >= 0)));
  }
}
