package meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import meta.format.EnumSetFormat;

public class EnumSetFieldDescription<T extends Enum<T>> extends MetaFieldDescription
{
  @Getter
  private final List<T> enumValues;
  
  @Getter
  private Class<T> enumClass;
  
  @Getter
  private int requiredStorageLength;

  public EnumSetFieldDescription(Class<T> enumClass)
  {
    super(Set.class, false, new EnumSetFormat<T>(enumClass));
    this.enumClass = enumClass;
    this.requiredStorageLength = this.getFormatter().format(EnumSet.allOf(this.enumClass)).length();
    
    this.enumValues = Collections.unmodifiableList(new ArrayList<T>(EnumSet.allOf(enumClass)));
  }
}
