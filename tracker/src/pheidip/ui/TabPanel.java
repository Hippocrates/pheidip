package pheidip.ui;

import java.awt.LayoutManager;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class TabPanel extends JPanel
{
  private TabHeader header;
  
  private String expectedTabTitle;

  public TabPanel()
  {
    super();
  }

  public TabPanel(LayoutManager layout)
  {
    super(layout);
  }

  public TabPanel(boolean isDoubleBuffered)
  {
    super(isDoubleBuffered);
  }

  public TabPanel(LayoutManager layout, boolean isDoubleBuffered)
  {
    super(layout, isDoubleBuffered);
  }

  protected final void setTabHeader(TabHeader header)
  {
    this.header = header;
    
    if (this.expectedTabTitle != null)
    {
      this.header.setTabTitle(this.expectedTabTitle);
      this.expectedTabTitle = null;
    }
  }

  protected final void setHeaderText(String text)
  {
    // this is so ugly, I don't really want to explain it...
    if (this.header != null)
    {
      this.header.setTabTitle(text);
    }
    else
    {
      this.expectedTabTitle = text;
    }
  }

  public abstract boolean confirmClose();
}