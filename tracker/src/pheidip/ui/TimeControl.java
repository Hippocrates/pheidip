package pheidip.ui;

import java.util.Date;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

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
}
