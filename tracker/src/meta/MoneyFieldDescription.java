package meta;

import java.math.BigDecimal;
import java.text.Format;

import meta.format.MoneyFormat;

public class MoneyFieldDescription extends AbstractBoundedField<BigDecimal>
{
  private static final Format moneyFormatter = new MoneyFormat();
  
  public MoneyFieldDescription(boolean nullable)
  {
    super(BigDecimal.class, nullable, new BigDecimal("0.00"), null, moneyFormatter);
  }
  
  @Override
  public boolean validate(Object toValidate)
  {
    return super.validate(toValidate) && (toValidate == null || ((BigDecimal)toValidate).scale() == 2);
  }
}
