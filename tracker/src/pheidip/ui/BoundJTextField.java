package pheidip.ui;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

@SuppressWarnings("serial")
public class BoundJTextField extends JTextField
{
  String cachedText = "";
  
  public BoundJTextField()
  {
    super();
    createListeners();
  }

  public BoundJTextField(int columns)
  {
    super(columns);
    createListeners();
  }

  public BoundJTextField(Document doc, String text, int columns)
  {
    super(doc, text, columns);
    createListeners();
  }

  public BoundJTextField(String text)
  {
    super(text);
    createListeners();
  }

  public BoundJTextField(String text, int columns)
  {
    super(text, columns);
    createListeners();
  }

  private void createListeners()
  {
    this.cachedText = this.getText();
    getDocument().addDocumentListener(new DocumentListener()
    {

      private void fireEvent(DocumentEvent e)
      {
        firePropertyChange("text", cachedText, getText());
        cachedText = getText();
      }

      public void insertUpdate(DocumentEvent e)
      {
        fireEvent(e);
      }

      public void removeUpdate(DocumentEvent e)
      {
        fireEvent(e);
      }

      public void changedUpdate(DocumentEvent e)
      {
        fireEvent(e);
      }
    });
  }
}
