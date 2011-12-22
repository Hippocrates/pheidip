package pheidip.ui;

import java.awt.event.KeyEvent;
import java.awt.Toolkit;

import javax.swing.KeyStroke;

public enum HotkeyAction
{
  DELETE(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0)),
  CLOSE(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0)),
  SAVE(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())),
  REFRESH(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())),
  CHIPIN_MERGE(KeyStroke.getKeyStroke(KeyEvent.VK_M, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
  
  HotkeyAction(KeyStroke key)
  {
    this.key = key;
  }
  
  private static HotkeyAction[] _list = HotkeyAction.values();
  
  public static HotkeyAction get(int i)
  {
    return _list[i];
  }
  
  private KeyStroke key;
  
  public KeyStroke getKeyStroke()
  {
    return this.key;
  }
}
