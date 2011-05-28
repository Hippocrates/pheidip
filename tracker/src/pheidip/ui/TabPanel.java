package pheidip.ui;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class TabPanel extends JPanel
{
  public abstract boolean confirmClose();
  public abstract void refreshContent();
}
