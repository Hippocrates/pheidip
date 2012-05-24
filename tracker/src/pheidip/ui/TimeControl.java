package pheidip.ui;

import java.util.Date;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

// TODO: make it possible to set this editor as nullable, since that would be useful
@SuppressWarnings("serial")
public class TimeControl extends JSpinner
{
  private JSpinner.DateEditor timeEditor;

  public TimeControl()
  {
    this(new Date());
  }
  
  public TimeControl(Date defaultDate)
  {
    this.setModel(new SpinnerDateModel());
    timeEditor = new JSpinner.DateEditor(this);
    this.setEditor(timeEditor);
    this.setValue(defaultDate);
  }
  
  public Date getTimeValue()
  {
    return (Date) this.getValue();
  }
  
  public void setTimeValue(Date value)
  {
    if (value == null)
    {
      this.setValue(new Date());
    }
    else
    {
      this.setValue(value);
    }
  }
}
