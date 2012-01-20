package pheidip.ui;

import javax.swing.JFormattedTextField;

import pheidip.util.FormatUtils;

@SuppressWarnings("serial")
public class IntegerField extends JFormattedTextField
{
  public IntegerField()
  {
    this(true);
  }
  
  public IntegerField(boolean nullable)
  {
    super(nullable ? FormatUtils.getNullableIntegerFormat() : FormatUtils.getIntegerFormat());
  }
  
  public Integer getValue()
  {
    return new Integer(super.getText());
  }
  
  public void setValue(Integer value)
  {
    Integer oldValue = this.getValue();
    super.setText(value.toString());
    this.firePropertyChange("value", oldValue, this.getValue());
  }
}
