package pheidip.ui;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class TabPanel extends JPanel
{
  private TabHeader header;
  
  public TabPanel()
  {
  }
  
  protected void setTabHeader(TabHeader header)
  {
    this.header = header;
  }
  
  protected void setHeaderText(String text)
  {
    if (this.header != null)
    {
      this.header.setTabTitle(text);
    }
  }
  
  public abstract boolean confirmClose();
  public abstract void refreshContent();
}
