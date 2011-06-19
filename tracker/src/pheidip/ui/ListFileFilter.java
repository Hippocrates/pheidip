package pheidip.ui;

import java.io.File;
import java.util.Arrays;

import javax.swing.filechooser.FileFilter;

import pheidip.util.StringUtils;

public class ListFileFilter extends FileFilter
{
  String[] extensions;
  
  public ListFileFilter(String[] extensions)
  {
    this.extensions = Arrays.copyOf(extensions, extensions.length);
  }
  
  @Override
  public boolean accept(File f)
  {
    for (String extension : this.extensions)
    {
      if (f == null)
      {
        return true;
      }
      else if (f.isDirectory())
      {
        return true;
      }
      if (StringUtils.getFileExtension(f.getName()).equalsIgnoreCase(extension))
      {
        return true;
      }
    }
    
    return false;
  }

  @Override
  public String getDescription()
  {
    String result = "";
    
    for (int i = 0; i < this.extensions.length; ++i)
    {
      if (extensions[i].length() == 0)
      {
        result += "*";
      }
      else
      {
        result += "*." + extensions[i];
      }
      
      if (i < this.extensions.length - 1)
      {
        result += ", ";
      }
    }
    
    return result;
  }

}
