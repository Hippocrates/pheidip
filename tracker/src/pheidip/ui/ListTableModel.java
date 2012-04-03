package pheidip.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import pheidip.util.StringUtils;

import meta.MetaEntity;
import meta.MetaField;
import meta.reflect.MetaEntityReflector;

@SuppressWarnings("serial")
public class ListTableModel<T> extends AbstractTableModel
{
  private Class<T> clazz;
  private MetaEntity entity;
  private List<T> internalList;
  private List<MetaField> fields;
  
  public ListTableModel(Class<T> clazz, String... columns)
  {
    this.clazz = clazz;
    this.entity = MetaEntityReflector.getMetaEntity(this.clazz);
    this.fields = new ArrayList<MetaField>();
    
    for (String column : columns)
    {
      this.fields.add(this.entity.getField(column));
    }
    
    this.setCollection(new HashSet<T>());
  }
  
  public Set<T> getCollection()
  {
    return new HashSet<T>(this.internalList);
  }
  
  public void setCollection(Collection<T> collection) 
  {
    if (collection == null) 
    {
      this.internalList = new ArrayList<T>();
    } 
    else
    {
      this.internalList = new ArrayList<T>(collection);
    }

    fireTableDataChanged();
  }
  
  @Override
  public int getColumnCount()
  {
    return this.fields.size();
  }
  
  @Override
  public int getRowCount()
  {
    return this.internalList.size();
  }
  
  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) 
  {
    return false;
  }
  
  @Override
  public String getColumnName(int columnIndex) 
  {
    return StringUtils.javaToNatural(this.fields.get(columnIndex).getName());
  }
  
  @Override
  public Class<?> getColumnClass( int columnIndex ) 
  {
    return this.fields.get(columnIndex).getFieldDescription().getStorageClass();
  }
  
  @Override
  public Object getValueAt(int row, int col)
  {
    T obj = this.internalList.get(row);
    MetaField field = this.fields.get(col);
    
    return field.getValue(obj);
  }
  
  public T getRow(int row)
  {
    return this.internalList.get(row);
  }
  
  public void addRow(T newValue)
  {
    this.internalList.add(newValue);
    this.fireTableDataChanged();
  }
  
  public void removeRow(T target)
  {
    this.internalList.remove(target);
    this.fireTableDataChanged();
  }
}
