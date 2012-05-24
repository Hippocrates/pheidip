package pheidip.ui;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;

import meta.format.NullableFormat;

@SuppressWarnings("serial")
public class DateSpinner extends JSpinner
{
  public DateSpinner(String simpleDateFormat)
  {
    Format formatter = new NullableFormat(new SimpleDateFormat(simpleDateFormat));
    pheidip.ui.DateEditor editor = new pheidip.ui.DateEditor(formatter, this);
    
    this.setModel(editor);
    this.setEditor(editor);
  }
  
  protected void fire(Date prev, Date curr)
  {
    this.firePropertyChange("value", prev, curr);
    
  }
}
