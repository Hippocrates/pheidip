package meta;

import java.text.Format;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import lombok.Getter;
import meta.format.EnumFormat;
import meta.format.NullableFormat;

public class EnumFieldDescription<T extends Enum<T>> extends MetaFieldDescription
{
  @Getter
  private final List<T> enumValues;
  
  @Getter
  private int requiredStorageLength;
  
  public EnumFieldDescription(String name, Class<T> enumClass)
  {
    this(enumClass, false);
  }

  public EnumFieldDescription(Class<T> enumClass, boolean nullable)
  {
    super(enumClass, nullable, buildFormatter(enumClass, nullable));

    this.requiredStorageLength = 0;
    
    for (T e : EnumSet.allOf(enumClass))
    {
      this.requiredStorageLength = Math.max(this.requiredStorageLength, this.getFormatter().format(e).length());
    }
    
    this.enumValues = Collections.unmodifiableList(new ArrayList<T>(EnumSet.allOf(enumClass)));
  }
  
  private static <E extends Enum<E>> Format buildFormatter(Class<E> enumClass, boolean nullable)
  {
    Format rootFormat = new EnumFormat<E>(enumClass);
    
    return nullable ? new NullableFormat(rootFormat) : rootFormat;
  }
}
