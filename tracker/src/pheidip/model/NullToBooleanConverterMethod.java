package pheidip.model;

public class NullToBooleanConverterMethod implements ConverterMethod
{
  private static final NullToBooleanConverterMethod instance = new NullToBooleanConverterMethod();
  
  public static final NullToBooleanConverterMethod getInstance()
  {
    return instance;
  }
  
  @Override
  public Object convert(Object source)
  {
    return source != null;
  }
}
