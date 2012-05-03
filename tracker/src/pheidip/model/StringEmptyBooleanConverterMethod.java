package pheidip.model;

import pheidip.util.StringUtils;

public class StringEmptyBooleanConverterMethod implements ConverterMethod
{
  private static final StringEmptyBooleanConverterMethod instance = new StringEmptyBooleanConverterMethod();
  
  public static final StringEmptyBooleanConverterMethod getInstance()
  {
    return instance;
  }
  
  @Override
  public Object convert(Object source)
  {
    return StringUtils.isEmptyOrNull(source.toString());
  }
}
