package meta;

public class IdFieldDescription extends IntegerFieldDescription
{
  public IdFieldDescription()
  {
    super(false, Integer.MIN_VALUE, Integer.MAX_VALUE);
  }
}
