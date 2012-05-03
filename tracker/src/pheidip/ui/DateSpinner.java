package pheidip.ui;

import java.text.Format;
import java.text.SimpleDateFormat;

import javax.swing.JSpinner;

import meta.format.NullableFormat;

@SuppressWarnings("serial")
public class DateSpinner extends JSpinner
{
  private Object cachedValue = null;
  
  public DateSpinner(String simpleDateFormat)
  {
    Format formatter = new NullableFormat(new SimpleDateFormat(simpleDateFormat));
    pheidip.ui.DateEditor editor = new pheidip.ui.DateEditor(formatter);
    
    this.setModel(editor);
    this.setEditor(editor);
  }
  
  public Object getValue()
  {
    return super.getValue();
  }
  
  public void setValue(Object value)
  {
    Object oldValue = cachedValue;
    super.setValue(value);
    this.firePropertyChange("value", oldValue, value);
    this.cachedValue = value;
  }
}
