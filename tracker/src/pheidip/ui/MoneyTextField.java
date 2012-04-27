package pheidip.ui;

import java.math.BigDecimal;

import javax.swing.JFormattedTextField;

import pheidip.util.FormatUtils;

@SuppressWarnings("serial")
public class MoneyTextField extends JFormattedTextField
{
  public MoneyTextField()
  {
    this(true);
  }
  
  public MoneyTextField(boolean nullable)
  {
    super(nullable ? FormatUtils.getNullableMoneyFormat() : FormatUtils.getMoneyFormat());
  }
  
  public BigDecimal getValue()
  {
    if (super.getText() == null || super.getText().equals(""))
    {
      return null;
    }
    else
    {
      return new BigDecimal(super.getText());
    }
  }
  
  public void setValue(BigDecimal value)
  {
    BigDecimal oldValue = this.getValue();
    super.setText(value.toString());
    this.firePropertyChange("value", oldValue, this.getValue());
  }
}
