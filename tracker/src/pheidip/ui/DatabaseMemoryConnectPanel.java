package pheidip.ui;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class DatabaseMemoryConnectPanel extends JPanel
{
  private JTextField fileTextField;
  private JButton browseButton;
  private JLabel initializationFileLabel;

  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
    gridBagLayout.rowHeights = new int[]{0, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    initializationFileLabel = new JLabel("Initialization File:");
    GridBagConstraints gbc_lblInitializationFile = new GridBagConstraints();
    gbc_lblInitializationFile.insets = new Insets(0, 0, 0, 5);
    gbc_lblInitializationFile.anchor = GridBagConstraints.EAST;
    gbc_lblInitializationFile.gridx = 0;
    gbc_lblInitializationFile.gridy = 0;
    add(initializationFileLabel, gbc_lblInitializationFile);
    
    fileTextField = new JTextField();
    GridBagConstraints gbc_textField = new GridBagConstraints();
    gbc_textField.insets = new Insets(0, 0, 0, 5);
    gbc_textField.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField.gridx = 1;
    gbc_textField.gridy = 0;
    add(fileTextField, gbc_textField);
    fileTextField.setColumns(10);
    
    browseButton = new JButton("Browse...");
    GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
    gbc_btnBrowse.anchor = GridBagConstraints.WEST;
    gbc_btnBrowse.gridx = 2;
    gbc_btnBrowse.gridy = 0;
    add(browseButton, gbc_btnBrowse);
  }
  
  private void initializeGUIEvents()
  {
    browseButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) 
      {
        DatabaseMemoryConnectPanel.this.browseButtonClicked();
      }
    });
  }
  
  /**
   * Create the panel.
   */
  public DatabaseMemoryConnectPanel()
  {
    this.initializeGUI();
    this.initializeGUIEvents();
  }
  
  public String getInitializeScriptFilename()
  {
    String result = this.fileTextField.getText();
    return result == null ? "" : result;
  }
  
  private void browseButtonClicked()
  {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.addChoosableFileFilter(new ListFileFilter(new String[]{"sql"}));
    int result = fileChooser.showOpenDialog(this);
    
    if (result == JFileChooser.APPROVE_OPTION)
    {
      this.fileTextField.setText(fileChooser.getSelectedFile().toString());
    }
  }

}
