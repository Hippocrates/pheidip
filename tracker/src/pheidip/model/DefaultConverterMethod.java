package pheidip.model;

public class DefaultConverterMethod implements ConverterMethod
{
  private static final DefaultConverterMethod instance = new DefaultConverterMethod();
  
  public static final DefaultConverterMethod getInstance()
  {
    return instance;
  }
  
  @Override
  public Object convert(Object source)
  {
    return source;
  }

}
