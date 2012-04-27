package pheidip.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import meta.TimeFieldDescription;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DateEditor extends JFormattedTextField implements SpinnerModel
{
  public static void main(String[] args)
  {
    TimeFieldDescription desc = new TimeFieldDescription(true);
    
    JDialog dialog = new JDialog((JFrame)null, true);
    JPanel panel = new JPanel();
    dialog.add(panel);
    JSpinner spinner = new JSpinner();
    DateEditor editor = new DateEditor(desc.getFormatter(), new Date());
    editor.setColumns(20);
    spinner.setModel(editor);
    spinner.setEditor(editor);
    panel.add(spinner);
    panel.add(new JButton("X"));
    dialog.pack();
    dialog.setVisible(true);
    
    System.out.println(editor.getDate());
    
    System.exit(0);
  }
  
  public static final long ONE_SECOND = 1000;

  public static final long ONE_MINUTE = 60 * ONE_SECOND;

  public static final long ONE_HOUR = 60 * ONE_MINUTE;

  public static final long ONE_DAY = 24 * ONE_HOUR;

  public static final long ONE_WEEK = 7 * ONE_DAY;
  
  private static int[] fieldTypes = new int[]
  { 
    DateFormat.ERA_FIELD, DateFormat.YEAR_FIELD,
    DateFormat.MONTH_FIELD, DateFormat.DATE_FIELD,
    DateFormat.HOUR_OF_DAY1_FIELD, DateFormat.HOUR_OF_DAY0_FIELD,
    DateFormat.MINUTE_FIELD, DateFormat.SECOND_FIELD,
    DateFormat.MILLISECOND_FIELD, DateFormat.DAY_OF_WEEK_FIELD,
    DateFormat.DAY_OF_YEAR_FIELD,
    DateFormat.DAY_OF_WEEK_IN_MONTH_FIELD,
    DateFormat.WEEK_OF_YEAR_FIELD, DateFormat.WEEK_OF_MONTH_FIELD,
    DateFormat.AM_PM_FIELD, DateFormat.HOUR1_FIELD,
    DateFormat.HOUR0_FIELD 
  };
  
  private List<ChangeListener> listeners;
  private Format formatter;
  private Calendar calendar;
  private Date cachedValue;

  public DateEditor(Format formatter)
  {
    this(formatter, null);
  }
  
  public DateEditor(Format formatter, Date value)
  {
    super(formatter);
    this.formatter = formatter;
    this.listeners = new ArrayList<ChangeListener>();
    this.calendar = Calendar.getInstance();
    this.cachedValue = null;
    this.setValue(value);

    this.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        try
        {
          commitEdit();
        }
        catch (ParseException e)
        {
        }
      }
    });
  }
  
  @Override
  public void addChangeListener(ChangeListener listen)
  {
    this.listeners.add(listen);
  }
  
  @Override
  public void removeChangeListener(ChangeListener listen)
  {
    this.listeners.remove(listen);
  }

  @Override
  public Object getNextValue()
  {
    if (this.getValue() == null)
    {
      this.setValue(new Date());
    }
    else
    {
      this.setValue(getValueDirection(1));
    }
    
    return this.getValue();
  }

  @Override
  public Object getPreviousValue()
  {
    if (this.getValue() == null)
    {
      this.setValue(new Date());
    }
    else
    {
      this.setValue(getValueDirection(-1));
    }
    
    return this.getValue();
  }
  
  private Object getValueDirection(int direction)
  {
    int currentField = this.getCurrentDateField();
    Date currentDate = this.getDate();
    
    switch (currentField) 
    {
    case DateFormat.AM_PM_FIELD:
      currentDate.setTime(currentDate.getTime()
          + (direction * 12 * ONE_HOUR));
      break;
    case DateFormat.DATE_FIELD:
    case DateFormat.DAY_OF_WEEK_FIELD:
    case DateFormat.DAY_OF_WEEK_IN_MONTH_FIELD:
    case DateFormat.DAY_OF_YEAR_FIELD:
      currentDate.setTime(currentDate.getTime()
          + (direction * ONE_DAY));
      break;
    case DateFormat.HOUR0_FIELD:
    case DateFormat.HOUR1_FIELD:
    case DateFormat.HOUR_OF_DAY0_FIELD:
    case DateFormat.HOUR_OF_DAY1_FIELD:
      currentDate.setTime(currentDate.getTime()
          + (direction * ONE_HOUR));
      break;
    case DateFormat.MILLISECOND_FIELD:
      currentDate.setTime(currentDate.getTime() + (direction * 1));
      break;
    case DateFormat.MINUTE_FIELD:
      currentDate.setTime(currentDate.getTime() + (direction * ONE_MINUTE));
      break;
    case DateFormat.MONTH_FIELD:
      this.calendar.set(Calendar.MONTH, this.calendar.get(Calendar.MONTH) + direction);
      currentDate = this.calendar.getTime();
      break;
    case DateFormat.SECOND_FIELD:
      currentDate.setTime(currentDate.getTime()
          + (direction * ONE_SECOND));
      break;
    case DateFormat.WEEK_OF_MONTH_FIELD:
      this.calendar.set(Calendar.WEEK_OF_MONTH, this.calendar
          .get(Calendar.WEEK_OF_MONTH)
          + direction);
      currentDate = this.calendar.getTime();
      break;
    case DateFormat.WEEK_OF_YEAR_FIELD:
      this.calendar.set(Calendar.WEEK_OF_MONTH, this.calendar
          .get(Calendar.WEEK_OF_MONTH)
          + direction);
      currentDate = this.calendar.getTime();
      break;
    case DateFormat.YEAR_FIELD:
      this.calendar.set(Calendar.YEAR, this.calendar.get(Calendar.YEAR)
          + direction);
      currentDate = this.calendar.getTime();
      break;
    }
    
    return currentDate;
  }
  
  @Override
  public void setValue(Object value)
  {
    int currentField = this.getCurrentDateField();
    FieldPosition pos = this.getFieldLocation(currentField);
    
    //if (value != this.cachedValue && this.cachedValue != null && !this.cachedValue.equals(value))
    {
      for (ChangeListener listen : this.listeners)
      {
        listen.stateChanged(new ChangeEvent(this));
      }
    }
    
    super.setValue(value);
    try
    {
      this.commitEdit();
    }
    catch (ParseException e)
    {
      super.setValue(this.cachedValue);
    }
    
    if (currentField != -1)
    {
      this.select(pos.getBeginIndex(), pos.getEndIndex());
    }
    
    this.cachedValue = this.getDate();
  }
  
  public Date getDate()
  {
    return (Date) this.getValue();
  }
  
  private FieldPosition getFieldLocation(int field)
  {
    FieldPosition position = new FieldPosition(field);
    StringBuffer buffer = new StringBuffer();
    this.formatter.format(this.getDate(), buffer, position);
    
    return position;
  }
  
  private int getCurrentDateField()
  {
    int caretPosition = this.getCaretPosition();
    
    if (caretPosition != -1)
    {
      for (int field : fieldTypes)
      {
        FieldPosition position = this.getFieldLocation(field);
        
        if (position.getBeginIndex() <= caretPosition && position.getEndIndex() >= caretPosition)
        {
          return field;
        }
      }
    }
    
    return DateFormat.SECOND_FIELD;
  }
}
