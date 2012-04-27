package pheidip.model;

public class BooleanInverseConverterMethod implements ConverterMethod
{
  private static BooleanInverseConverterMethod instance = new BooleanInverseConverterMethod();
  
  public static BooleanInverseConverterMethod getInstance()
  {
    return instance;
  }
  @Override
  public Object convert(Object source)
  {
    return !((Boolean) source);
  }

}
