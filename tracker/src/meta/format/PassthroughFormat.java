package meta.format;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

@SuppressWarnings("serial")
public class PassthroughFormat extends Format
{
  @Override
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
  {
    toAppendTo.append(obj.toString());
    return toAppendTo;
  }

  @Override
  public Object parseObject(String source, ParsePosition pos)
  {
    pos.setIndex(source.length() - pos.getIndex());
    return source;
  }
}
