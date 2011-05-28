package pheidip.ui;

import javax.swing.table.DefaultTableModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

class RowComparator implements Comparator<Vector<Comparable<Object>>>
{
  private int comparisonColumn;
  private boolean nonIncreasing;

  public RowComparator(int comparisonColumn, boolean nonIncreasing)
  {
    this.comparisonColumn = comparisonColumn;
    this.nonIncreasing = nonIncreasing;
  }

  @Override
  public int compare(Vector<Comparable<Object>> rowX, Vector<Comparable<Object>> rowY) 
  {
    if (rowX.size() != rowY.size())
    {
      throw new RuntimeException("Compared rows must be the same size.");
    }
    
    if (rowX.size() < this.comparisonColumn)
    {
      throw new RuntimeException("Rows do not have a column #" + this.comparisonColumn + " to compare.");
    }
    
    if (rowX.elementAt(this.comparisonColumn).getClass() == String.class)
    {
      return (this.nonIncreasing ? -1 : 1) * ((String)(Object)rowX.elementAt(this.comparisonColumn)).compareToIgnoreCase((String)(Object)rowY.elementAt(this.comparisonColumn));
    }
    
    return (this.nonIncreasing ? -1 : 1) * rowX.elementAt(this.comparisonColumn).compareTo(rowY.elementAt(this.comparisonColumn));
  }
}

@SuppressWarnings("serial")
public class CustomTableModel extends DefaultTableModel 
{
  public CustomTableModel(String[] headings, int size)
  {
    super(headings, size);
  }
  
  @Override
  public boolean isCellEditable(int x, int y)
  {
    return false;
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Class getColumnClass(int columnIndex)
  {
    return this.getRowCount() > 0 ? this.getValueAt(0, columnIndex).getClass() : Object.class;
  }
  
  @SuppressWarnings("unchecked")
  public void sortByColumn(int column, boolean nonIncreasing)
  {
    Collections.sort(this.getDataVector(), new RowComparator(column, nonIncreasing));
  }
}
