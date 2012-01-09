package pheidip.ui;

import java.text.Format;

import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class FormattedInputDialog extends JDialog
{
  private JFormattedTextField textField;
  private JButton okButton;
  private JButton cancelButton;
  private String result;

  private void initializeGUI(String message, String title, Format formatter)
  {
    this.setTitle(title);
    this.setSize(224,95);
    this.setResizable(true);
    getContentPane().setLayout(new BorderLayout(0, 0));
    
    JLabel messageLabel = new JLabel(message);
    getContentPane().add(messageLabel, BorderLayout.NORTH);
    
    textField = new JFormattedTextField(formatter);
    getContentPane().add(textField, BorderLayout.CENTER);
    textField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) 
        {
          okSelected();
        }
      });
    
    JPanel panel = new JPanel();
    getContentPane().add(panel, BorderLayout.SOUTH);
    panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    
    okButton = new JButton("OK");
    okButton.setActionCommand("OK");
    okButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) 
      {
        okSelected();
      }
    });
    panel.add(okButton);
    
    cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) 
      {
        cancelSelected();
      }
    });
    panel.add(cancelButton);
    
    this.getRootPane().setDefaultButton(this.okButton);
    
    this.pack();
  }
  
  /**
   * @wbp.parser.constructor
   */
  public FormattedInputDialog(JFrame parent, String message, String title, Format formatter)
  {
    super(parent, true);
    setResizable(false);
    this.initializeGUI(message, title, formatter);
  }
  
  public FormattedInputDialog(JDialog parent, String message, String title, Format formatter)
  {
    super(parent, true);
    this.initializeGUI(message, title, formatter);
  }
  
  private void okSelected()
  {
    this.result = this.textField.getText();
    closeDialog();
  }
  
  private void cancelSelected()
  {
    this.result = null;
    closeDialog();
  }
  
  private void closeDialog()
  {
    this.setVisible(false);
    this.dispose();
  }
  
  public String getResult()
  {
    return this.result;
  }
  
  public void setDefaultText(String text)
  {
    this.textField.setText(text);
  }
  
  public static String showDialog(JFrame parent, String message, String title, Format formatter)
  {
    return runDialog(new FormattedInputDialog(parent, message, title, formatter), "");
  }
  
  public static String showDialog(JDialog parent, String message, String title, Format formatter)
  {
    return runDialog(new FormattedInputDialog(parent, message, title, formatter), "");
  }
  
  public static String showDialog(JFrame parent, String message, String title, Format formatter, String defaultText)
  {
    return runDialog(new FormattedInputDialog(parent, message, title, formatter), defaultText);
  }
  
  public static String showDialog(JDialog parent, String message, String title, Format formatter, String defaultText)
  {
    return runDialog(new FormattedInputDialog(parent, message, title, formatter), defaultText);
  }
  
  private static String runDialog(FormattedInputDialog d, String defaultText)
  {
    d.setDefaultText(defaultText);
    d.setVisible(true);
    return d.getResult();
  }
}
