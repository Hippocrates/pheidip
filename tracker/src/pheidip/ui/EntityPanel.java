package pheidip.ui;


@SuppressWarnings("serial")
public abstract class EntityPanel extends TabPanel
{
  public abstract void refreshContent();
  public abstract void saveContent();
  public abstract void deleteContent();
}
