package pheidip.ui;

import pheidip.logic.DonationDatabaseManager;
import pheidip.logic.DonorControl;
import pheidip.objects.Donor;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class DonorPanel extends TabPanel
{
  private DonorControl donorControl;
  
  private JTextField firstNameField;
  private JTextField lastNameField;
  private JTextField emailField;
  private JTextField aliasField;
  private JTable donationTable;
  private JTextField totalDonatedField;
  private JTextField prizeField;

  /**
   * Create the panel.
   */
  public DonorPanel(DonationDatabaseManager donationDatabase, int donorId)
  {
    this.donorControl = new DonorControl(donationDatabase, donorId);
    
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{67, 111, 12, 85, 116, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 54, 0, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    JLabel lblName = new JLabel("First Name:");
    GridBagConstraints gbc_lblName = new GridBagConstraints();
    gbc_lblName.insets = new Insets(0, 0, 5, 5);
    gbc_lblName.anchor = GridBagConstraints.EAST;
    gbc_lblName.gridx = 0;
    gbc_lblName.gridy = 0;
    add(lblName, gbc_lblName);
    
    firstNameField = new JTextField();
    firstNameField.setEditable(false);
    GridBagConstraints gbc_firstNameField = new GridBagConstraints();
    gbc_firstNameField.insets = new Insets(0, 0, 5, 5);
    gbc_firstNameField.fill = GridBagConstraints.HORIZONTAL;
    gbc_firstNameField.gridx = 1;
    gbc_firstNameField.gridy = 0;
    add(firstNameField, gbc_firstNameField);
    firstNameField.setColumns(10);
    
    JLabel lblTotalDonated = new JLabel("Total Donated:");
    GridBagConstraints gbc_lblTotalDonated = new GridBagConstraints();
    gbc_lblTotalDonated.anchor = GridBagConstraints.EAST;
    gbc_lblTotalDonated.insets = new Insets(0, 0, 5, 5);
    gbc_lblTotalDonated.gridx = 3;
    gbc_lblTotalDonated.gridy = 0;
    add(lblTotalDonated, gbc_lblTotalDonated);
    
    totalDonatedField = new JTextField();
    totalDonatedField.setEditable(false);
    GridBagConstraints gbc_totalDonatedField = new GridBagConstraints();
    gbc_totalDonatedField.insets = new Insets(0, 0, 5, 0);
    gbc_totalDonatedField.fill = GridBagConstraints.HORIZONTAL;
    gbc_totalDonatedField.gridx = 4;
    gbc_totalDonatedField.gridy = 0;
    add(totalDonatedField, gbc_totalDonatedField);
    totalDonatedField.setColumns(10);
    
    JLabel lblLastName = new JLabel("Last Name:");
    GridBagConstraints gbc_lblLastName = new GridBagConstraints();
    gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
    gbc_lblLastName.anchor = GridBagConstraints.EAST;
    gbc_lblLastName.gridx = 0;
    gbc_lblLastName.gridy = 1;
    add(lblLastName, gbc_lblLastName);
    
    lastNameField = new JTextField();
    lastNameField.setEditable(false);
    GridBagConstraints gbc_lastNameField = new GridBagConstraints();
    gbc_lastNameField.insets = new Insets(0, 0, 5, 5);
    gbc_lastNameField.fill = GridBagConstraints.HORIZONTAL;
    gbc_lastNameField.gridx = 1;
    gbc_lastNameField.gridy = 1;
    add(lastNameField, gbc_lastNameField);
    lastNameField.setColumns(10);
    
    JLabel lblPrizeWon = new JLabel("Prize Won:");
    GridBagConstraints gbc_lblPrizeWon = new GridBagConstraints();
    gbc_lblPrizeWon.anchor = GridBagConstraints.EAST;
    gbc_lblPrizeWon.insets = new Insets(0, 0, 5, 5);
    gbc_lblPrizeWon.gridx = 3;
    gbc_lblPrizeWon.gridy = 1;
    add(lblPrizeWon, gbc_lblPrizeWon);
    
    prizeField = new JTextField();
    prizeField.setEditable(false);
    GridBagConstraints gbc_prizeField = new GridBagConstraints();
    gbc_prizeField.insets = new Insets(0, 0, 5, 0);
    gbc_prizeField.fill = GridBagConstraints.HORIZONTAL;
    gbc_prizeField.gridx = 4;
    gbc_prizeField.gridy = 1;
    add(prizeField, gbc_prizeField);
    prizeField.setColumns(10);
    
    JLabel label = new JLabel("");
    GridBagConstraints gbc_label = new GridBagConstraints();
    gbc_label.insets = new Insets(0, 0, 5, 5);
    gbc_label.gridx = 0;
    gbc_label.gridy = 2;
    add(label, gbc_label);
    
    JLabel lblAlias = new JLabel("Alias:");
    GridBagConstraints gbc_lblAlias = new GridBagConstraints();
    gbc_lblAlias.anchor = GridBagConstraints.EAST;
    gbc_lblAlias.insets = new Insets(0, 0, 5, 5);
    gbc_lblAlias.gridx = 0;
    gbc_lblAlias.gridy = 3;
    add(lblAlias, gbc_lblAlias);
    
    aliasField = new JTextField();
    aliasField.setEditable(false);
    GridBagConstraints gbc_aliasField = new GridBagConstraints();
    gbc_aliasField.insets = new Insets(0, 0, 5, 5);
    gbc_aliasField.fill = GridBagConstraints.HORIZONTAL;
    gbc_aliasField.gridx = 1;
    gbc_aliasField.gridy = 3;
    add(aliasField, gbc_aliasField);
    aliasField.setColumns(10);
    
    JButton saveButton = new JButton("Save");
    saveButton.setEnabled(false);
    saveButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent arg0) 
      {
        DonorPanel.this.saveEnteredContent();
      }
    });
    GridBagConstraints gbc_saveButton = new GridBagConstraints();
    gbc_saveButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_saveButton.insets = new Insets(0, 0, 5, 5);
    gbc_saveButton.gridx = 3;
    gbc_saveButton.gridy = 3;
    add(saveButton, gbc_saveButton);
    
    JLabel lblEmail = new JLabel("E-mail:");
    GridBagConstraints gbc_lblEmail = new GridBagConstraints();
    gbc_lblEmail.anchor = GridBagConstraints.EAST;
    gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
    gbc_lblEmail.gridx = 0;
    gbc_lblEmail.gridy = 4;
    add(lblEmail, gbc_lblEmail);
    
    emailField = new JTextField();
    emailField.setEditable(false);
    GridBagConstraints gbc_emailField = new GridBagConstraints();
    gbc_emailField.insets = new Insets(0, 0, 5, 5);
    gbc_emailField.fill = GridBagConstraints.HORIZONTAL;
    gbc_emailField.gridx = 1;
    gbc_emailField.gridy = 4;
    add(emailField, gbc_emailField);
    emailField.setColumns(10);
    
    JButton refreshButton = new JButton("Refresh");
    refreshButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent arg0) 
      {
        DonorPanel.this.refreshContent();
      }
    });
    GridBagConstraints gbc_refreshButton = new GridBagConstraints();
    gbc_refreshButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_refreshButton.insets = new Insets(0, 0, 5, 5);
    gbc_refreshButton.gridx = 3;
    gbc_refreshButton.gridy = 4;
    add(refreshButton, gbc_refreshButton);
    
    donationTable = new JTable();
    GridBagConstraints gbc_donationTable = new GridBagConstraints();
    gbc_donationTable.insets = new Insets(0, 0, 5, 0);
    gbc_donationTable.gridwidth = 5;
    gbc_donationTable.fill = GridBagConstraints.BOTH;
    gbc_donationTable.gridx = 0;
    gbc_donationTable.gridy = 6;
    add(donationTable, gbc_donationTable);
    
    JButton openDonationButton = new JButton("Open Donation");
    openDonationButton.setEnabled(false);
    openDonationButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) 
      {
        DonorPanel.this.openDonation();
      }
    });
    GridBagConstraints gbc_btnOpen = new GridBagConstraints();
    gbc_btnOpen.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnOpen.gridx = 4;
    gbc_btnOpen.gridy = 7;
    add(openDonationButton, gbc_btnOpen);
    
    this.refreshContent();
  }
  
  public void refreshContent()
  {
    Donor data = this.donorControl.getData();
    
    this.firstNameField.setText(data.getFirstName());
    this.lastNameField.setText(data.getLastName());
    this.aliasField.setText(data.getAlias());
    this.emailField.setText(data.getEmail());
    
    this.totalDonatedField.setText(this.donorControl.getTotalDonated().toString());
    this.prizeField.setText("NOT IMPLEMENTED YET.");
  }
  
  private void saveEnteredContent()
  {
    // TODO: this
    
    this.refreshContent();
  }
  
  private void openDonation()
  {
    // TODO: actually open a donation
    // N.B. this will need a ref back to the main window to do so
  }

  @Override
  public boolean confirmClose()
  {
    // this could open a JOptionPane to prevent the user from closing a tab with 
    // pending modifications ...
    return true;
  }
}
