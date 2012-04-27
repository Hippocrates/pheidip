package meta;

import java.util.EnumSet;

import lombok.Getter;
import meta.format.EnumSetFormat;

public class EnumSetFieldDescription<T extends Enum<T>> extends MetaFieldDescription
{
  @Getter
  private Class<T> enumClass;
  
  @Getter
  private int requiredStorageLength;

  public EnumSetFieldDescription(Class<T> enumClass)
  {
    super(EnumSet.class, false, new EnumSetFormat<T>(enumClass));
    this.enumClass = enumClass;
    this.requiredStorageLength = this.getFormatter().format(EnumSet.allOf(this.enumClass)).length();
  }
}
