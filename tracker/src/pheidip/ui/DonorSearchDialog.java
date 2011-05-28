package pheidip.ui;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pheidip.logic.DonationDatabaseManager;
import pheidip.logic.DonorSearch;
import pheidip.objects.Donor;
import test.db.DBTestConfiguration;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

@SuppressWarnings("serial")
public class DonorSearchDialog extends JDialog
{
  private DonorSearch searcher;
  private JTextField firstNameField;
  private JTextField lastNameField;
  private JTextField aliasField;
  private JTextField emailField;
  private JList donorList;
  private Donor resultDonor;

  /**
   * Launch the application.
   */
  public static void main(String[] args)
  {
    try
    {
      DonationDatabaseManager manager = new DonationDatabaseManager();
      manager.createMemoryDatabase();
      manager.runSQLScript(DBTestConfiguration.getTestDataDirectory() + "donation_bid_test_data_1.sql");
      
      DonorSearchDialog dialog = new DonorSearchDialog(null, manager);
      dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      dialog.setVisible(true);
      
      System.out.println(dialog.getResult());
    } 
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Create the dialog.
   * @param manager 
   */
  public DonorSearchDialog(JFrame parent, DonationDatabaseManager manager)
  {
    super(parent, true);
    
    this.searcher = new DonorSearch(manager);
    this.resultDonor = null;
    
    this.setTitle("Find Donor...");
    setBounds(100, 100, 450, 300);
    
    JPanel contentPanel = new JPanel();
    this.setContentPane(contentPanel);
    
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    
    GridBagLayout gbl_panel = new GridBagLayout();
    gbl_panel.columnWidths = new int[]{0, 190, 90, 76, 0};
    gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
    gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
    gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
    contentPanel.setLayout(gbl_panel);
    {
      JLabel lblFirstName = new JLabel("First Name:");
      GridBagConstraints gbc_lblFirstName = new GridBagConstraints();
      gbc_lblFirstName.anchor = GridBagConstraints.EAST;
      gbc_lblFirstName.insets = new Insets(0, 0, 5, 5);
      gbc_lblFirstName.gridx = 0;
      gbc_lblFirstName.gridy = 0;
      contentPanel.add(lblFirstName, gbc_lblFirstName);
    }
    {
      firstNameField = new JTextField();
      GridBagConstraints gbc_textField = new GridBagConstraints();
      gbc_textField.insets = new Insets(0, 0, 5, 5);
      gbc_textField.fill = GridBagConstraints.HORIZONTAL;
      gbc_textField.gridx = 1;
      gbc_textField.gridy = 0;
      contentPanel.add(firstNameField, gbc_textField);
      firstNameField.setColumns(10);
    }
    {
      donorList = new JList();
      GridBagConstraints gbc_list = new GridBagConstraints();
      gbc_list.gridwidth = 2;
      gbc_list.gridheight = 6;
      gbc_list.insets = new Insets(0, 0, 5, 0);
      gbc_list.fill = GridBagConstraints.BOTH;
      gbc_list.gridx = 2;
      gbc_list.gridy = 0;
      contentPanel.add(donorList, gbc_list);
    }
    {
      JLabel lblLastName = new JLabel("Last Name:");
      GridBagConstraints gbc_lblLastName = new GridBagConstraints();
      gbc_lblLastName.anchor = GridBagConstraints.EAST;
      gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
      gbc_lblLastName.gridx = 0;
      gbc_lblLastName.gridy = 1;
      contentPanel.add(lblLastName, gbc_lblLastName);
    }
    {
      lastNameField = new JTextField();
      GridBagConstraints gbc_textField_1 = new GridBagConstraints();
      gbc_textField_1.insets = new Insets(0, 0, 5, 5);
      gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
      gbc_textField_1.gridx = 1;
      gbc_textField_1.gridy = 1;
      contentPanel.add(lastNameField, gbc_textField_1);
      lastNameField.setColumns(10);
    }
    {
      JLabel lblAlias = new JLabel("Alias:");
      GridBagConstraints gbc_lblAlias = new GridBagConstraints();
      gbc_lblAlias.anchor = GridBagConstraints.EAST;
      gbc_lblAlias.insets = new Insets(0, 0, 5, 5);
      gbc_lblAlias.gridx = 0;
      gbc_lblAlias.gridy = 2;
      contentPanel.add(lblAlias, gbc_lblAlias);
    }
    {
      aliasField = new JTextField();
      GridBagConstraints gbc_textField_2 = new GridBagConstraints();
      gbc_textField_2.insets = new Insets(0, 0, 5, 5);
      gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
      gbc_textField_2.gridx = 1;
      gbc_textField_2.gridy = 2;
      contentPanel.add(aliasField, gbc_textField_2);
      aliasField.setColumns(10);
    }
    {
      JLabel lblEmail = new JLabel("E-mail:");
      GridBagConstraints gbc_lblEmail = new GridBagConstraints();
      gbc_lblEmail.anchor = GridBagConstraints.EAST;
      gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
      gbc_lblEmail.gridx = 0;
      gbc_lblEmail.gridy = 3;
      contentPanel.add(lblEmail, gbc_lblEmail);
    }
    {
      emailField = new JTextField();
      GridBagConstraints gbc_textField_3 = new GridBagConstraints();
      gbc_textField_3.insets = new Insets(0, 0, 5, 5);
      gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
      gbc_textField_3.gridx = 1;
      gbc_textField_3.gridy = 3;
      contentPanel.add(emailField, gbc_textField_3);
      emailField.setColumns(10);
    }
    {
      JButton btnSearch = new JButton("Search");
      btnSearch.addActionListener(new ActionListener() 
      {
        public void actionPerformed(ActionEvent arg0) 
        {
          DonorSearchDialog.this.runFilters();
        }
      });
      GridBagConstraints gbc_btnSearch = new GridBagConstraints();
      gbc_btnSearch.fill = GridBagConstraints.HORIZONTAL;
      gbc_btnSearch.insets = new Insets(0, 0, 5, 5);
      gbc_btnSearch.gridx = 1;
      gbc_btnSearch.gridy = 5;
      contentPanel.add(btnSearch, gbc_btnSearch);
    }
    JButton okButton = new JButton("OK");
    GridBagConstraints gbc_okButton = new GridBagConstraints();
    gbc_okButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_okButton.insets = new Insets(0, 0, 0, 5);
    gbc_okButton.gridx = 2;
    gbc_okButton.gridy = 6;
    contentPanel.add(okButton, gbc_okButton);
    okButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent arg0) 
      {
        DonorSearchDialog.this.returnSelectedDonor();
      }
    });
    getRootPane().setDefaultButton(okButton);
    {
      JButton cancelButton = new JButton("Cancel");
      GridBagConstraints gbc_cancelButton = new GridBagConstraints();
      gbc_cancelButton.fill = GridBagConstraints.HORIZONTAL;
      gbc_cancelButton.gridx = 3;
      gbc_cancelButton.gridy = 6;
      contentPanel.add(cancelButton, gbc_cancelButton);
      cancelButton.addActionListener(new ActionListener() 
      {
        public void actionPerformed(ActionEvent arg0) 
        {
          DonorSearchDialog.this.cancelSelection();
        }
      });
    }
  }
  
  public Donor getResult()
  {
    return this.resultDonor;
  }
  
  private void returnSelectedDonor()
  {
    Donor result = (Donor) this.donorList.getSelectedValue();
    if (result == null)
    {
      JOptionPane.showMessageDialog(this, "No donor is selected.");
    }
    else
    {
      this.resultDonor = result;
      this.setVisible(false);
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
    List<Donor> filtered = this.searcher.searchDonors(
        this.firstNameField.getText(),
        this.lastNameField.getText(),
        this.emailField.getText(),
        this.aliasField.getText());
    
    DefaultListModel listData = new DefaultListModel();
    
    for (Donor d : filtered)
    {
      listData.addElement(d);
    }
    
    this.donorList.setModel(listData);
  }
}
