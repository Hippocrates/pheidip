package pheidip.ui;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pheidip.logic.SpeedRunSearch;
import pheidip.objects.SpeedRun;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JList;

@SuppressWarnings("serial")
public class SpeedRunSearchDialog extends JDialog
{
  private SpeedRunSearch searcher;
  private SpeedRun resultRun;
  private JTextField nameField;
  private JLabel nameLabel;
  private JList speedRunList;
  private JButton searchButton;
  private JButton okButton;
  private JButton cancelButton;

  private void initializeGUI()
  {
    this.setTitle("Find Run...");
    setBounds(100, 100, 450, 300);
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{0, 235, 103, 102, 99, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
    getContentPane().setLayout(gridBagLayout);
    
    nameLabel = new JLabel("Name:");
    GridBagConstraints gbc_nameLabel = new GridBagConstraints();
    gbc_nameLabel.insets = new Insets(0, 0, 5, 5);
    gbc_nameLabel.anchor = GridBagConstraints.EAST;
    gbc_nameLabel.gridx = 0;
    gbc_nameLabel.gridy = 0;
    getContentPane().add(nameLabel, gbc_nameLabel);
    
    nameField = new JTextField();
    GridBagConstraints gbc_nameField = new GridBagConstraints();
    gbc_nameField.gridwidth = 2;
    gbc_nameField.insets = new Insets(0, 0, 5, 5);
    gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
    gbc_nameField.gridx = 1;
    gbc_nameField.gridy = 0;
    getContentPane().add(nameField, gbc_nameField);
    nameField.setColumns(10);
    
    speedRunList = new JList();
    GridBagConstraints gbc_speedRunList = new GridBagConstraints();
    gbc_speedRunList.gridheight = 3;
    gbc_speedRunList.gridwidth = 2;
    gbc_speedRunList.insets = new Insets(0, 0, 5, 5);
    gbc_speedRunList.fill = GridBagConstraints.BOTH;
    gbc_speedRunList.gridx = 3;
    gbc_speedRunList.gridy = 0;
    getContentPane().add(speedRunList, gbc_speedRunList);
    
    searchButton = new JButton("Search");
    GridBagConstraints gbc_searchButton = new GridBagConstraints();
    gbc_searchButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_searchButton.insets = new Insets(0, 0, 5, 5);
    gbc_searchButton.gridx = 2;
    gbc_searchButton.gridy = 2;
    getContentPane().add(searchButton, gbc_searchButton);
    
    okButton = new JButton("OK");
    GridBagConstraints gbc_okButton = new GridBagConstraints();
    gbc_okButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_okButton.insets = new Insets(0, 0, 0, 5);
    gbc_okButton.gridx = 3;
    gbc_okButton.gridy = 3;
    getContentPane().add(okButton, gbc_okButton);
    
    cancelButton = new JButton("Cancel");
    GridBagConstraints gbc_cancelButton = new GridBagConstraints();
    gbc_cancelButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_cancelButton.gridx = 4;
    gbc_cancelButton.gridy = 3;
    getContentPane().add(cancelButton, gbc_cancelButton);
  }
  
  private void initializeGUIEvents()
  {
    searchButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e) 
      {
        SpeedRunSearchDialog.this.runFilters();
      }
    });
    
    okButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e) 
      {
        SpeedRunSearchDialog.this.returnSelectedDonor();
      }
    });
    
    cancelButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e) 
      {
        SpeedRunSearchDialog.this.cancelSelection();
      }
    });
  }

  /**
   * Create the dialog.
   */
  public SpeedRunSearchDialog(JFrame parent, SpeedRunSearch searcher)
  {
    super(parent, true);
    
    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.searcher = searcher;
    this.resultRun = null;
  }
  
  public SpeedRun getResult()
  {
    return this.resultRun;
  }
  
  private void returnSelectedDonor()
  {
    SpeedRun result = (SpeedRun) this.speedRunList.getSelectedValue();
    if (result == null)
    {
      JOptionPane.showMessageDialog(this, "No run is selected.", "Error", JOptionPane.OK_OPTION);
    }
    else
    {
      this.resultRun = result;
      this.closeDialog();
    }
  }
  
  private void cancelSelection()
  {
    this.closeDialog();
  }
  
  private void closeDialog()
  {
    this.setVisible(false);
    this.dispose();
  }
  
  private void runFilters()
  {
    List<SpeedRun> filtered = this.searcher.searchSpeedRuns(this.nameField.getText());
    
    DefaultListModel listData = new DefaultListModel();
    
    for (SpeedRun s : filtered)
    {
      listData.addElement(s);
    }
    
    this.speedRunList.setModel(listData);
  }
}
