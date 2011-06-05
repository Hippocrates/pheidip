package pheidip.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

@SuppressWarnings("serial")
public class ChipinTextMergeDialog extends JDialog
{

  private JButton mergeButton;
  private JPanel buttonPane;
  private JButton cancelButton;
  private JScrollPane textScrollPane;
  private JTextArea htmlTextArea;
  private String resultText;
  private JPanel outerPane;

  /**
   * Launch the application.
   */
  public static void main(String[] args)
  {
    try
    {
      ChipinTextMergeDialog dialog = new ChipinTextMergeDialog(null);
      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      dialog.setVisible(true);
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public void initializeGUI()
  {
    setTitle("Enter chipin HTML to merge");
    setBounds(100, 100, 450, 300);
    getContentPane().setLayout(new BorderLayout());
    {
      buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);
      {
        mergeButton = new JButton("Merge");
        mergeButton.setActionCommand("OK");
        buttonPane.add(mergeButton);
        getRootPane().setDefaultButton(mergeButton);
      }
      {
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
      }
    }
    {
      outerPane = new JPanel();
      outerPane.setBorder(new EmptyBorder(5,5,5,5));
      outerPane.setLayout(new BorderLayout());
      getContentPane().add(outerPane, BorderLayout.CENTER);
      textScrollPane = new JScrollPane();
      textScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
      textScrollPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
      outerPane.add(textScrollPane, BorderLayout.CENTER);
      {
        htmlTextArea = new JTextArea();
        textScrollPane.setViewportView(htmlTextArea);
      }
    }
  }
  
  private void initializeGUIEvents()
  {
    mergeButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e) 
      {
        ChipinTextMergeDialog.this.mergeClicked();
      }
    });
    
    cancelButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e) 
      {
        ChipinTextMergeDialog.this.cancelClicked();
      }
    });
  }
  
  public String getResultText()
  {
    return this.resultText;
  }
  
  private void mergeClicked()
  {
    this.resultText = this.htmlTextArea.getText();
    this.closeDialog();
  }
  
  private void cancelClicked()
  {
    this.closeDialog();
  }

  /**
   * Create the dialog.
   */
  public ChipinTextMergeDialog(JFrame parent)
  {
    super(parent, true);
    
    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.resultText = null;
  }
  
  private void closeDialog()
  {
    this.setVisible(false);
    this.dispose();
  }

}
