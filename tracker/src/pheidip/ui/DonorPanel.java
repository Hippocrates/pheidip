package pheidip.ui;

import pheidip.logic.DonorControl;

import pheidip.objects.Donation;
import pheidip.objects.Donor;
import pheidip.util.StringUtils;

import java.awt.FocusTraversalPolicy;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

import java.awt.Component;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


@SuppressWarnings("serial")
public class DonorPanel extends TabPanel
{
  private DonorControl donorControl;
  int[] donationTableIds;
  
  private JTextField firstNameField;
  private JTextField lastNameField;
  private JTextField emailField;
  private JTextField aliasField;
  private JTable donationTable;
  private JTextField totalDonatedField;
  private JTextField prizeField;

  private JButton openDonationButton;

  private JButton refreshButton;

  private JButton saveButton;

  private JLabel firstNameLabel;

  private JLabel totalDonatedLabel;

  private JLabel lastNameLabel;

  private JLabel prizeWonLabel;

  private JLabel aliasLabel;

  private JLabel emailLabel;
  private JScrollPane scrollPane;
  
  private FocusTraversalManager tabOrder;
  private JButton deleteDonorButton;

  private MainWindow owner;
  private JButton addDonationButton;

  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{67, 111, 102, 99, 56, 94, 46, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 54, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    firstNameLabel = new JLabel("First Name:");
    GridBagConstraints gbc_lblName = new GridBagConstraints();
    gbc_lblName.insets = new Insets(0, 0, 5, 5);
    gbc_lblName.anchor = GridBagConstraints.EAST;
    gbc_lblName.gridx = 0;
    gbc_lblName.gridy = 0;
    add(firstNameLabel, gbc_lblName);
    
    firstNameField = new JTextField();
    GridBagConstraints gbc_firstNameField = new GridBagConstraints();
    gbc_firstNameField.insets = new Insets(0, 0, 5, 5);
    gbc_firstNameField.fill = GridBagConstraints.HORIZONTAL;
    gbc_firstNameField.gridx = 1;
    gbc_firstNameField.gridy = 0;
    add(firstNameField, gbc_firstNameField);
    firstNameField.setColumns(10);
    
    totalDonatedLabel = new JLabel("Total Donated:");
    GridBagConstraints gbc_lblTotalDonated = new GridBagConstraints();
    gbc_lblTotalDonated.anchor = GridBagConstraints.EAST;
    gbc_lblTotalDonated.insets = new Insets(0, 0, 5, 5);
    gbc_lblTotalDonated.gridx = 2;
    gbc_lblTotalDonated.gridy = 0;
    add(totalDonatedLabel, gbc_lblTotalDonated);
    
    totalDonatedField = new JTextField();
    totalDonatedField.setEditable(false);
    GridBagConstraints gbc_totalDonatedField = new GridBagConstraints();
    gbc_totalDonatedField.gridwidth = 2;
    gbc_totalDonatedField.insets = new Insets(0, 0, 5, 5);
    gbc_totalDonatedField.fill = GridBagConstraints.HORIZONTAL;
    gbc_totalDonatedField.gridx = 3;
    gbc_totalDonatedField.gridy = 0;
    add(totalDonatedField, gbc_totalDonatedField);
    totalDonatedField.setColumns(10);
    
    deleteDonorButton = new JButton("Delete Donor");
    GridBagConstraints gbc_btnDeleteDonor = new GridBagConstraints();
    gbc_btnDeleteDonor.insets = new Insets(0, 0, 5, 5);
    gbc_btnDeleteDonor.gridx = 5;
    gbc_btnDeleteDonor.gridy = 0;
    add(deleteDonorButton, gbc_btnDeleteDonor);
    
    lastNameLabel = new JLabel("Last Name:");
    GridBagConstraints gbc_lblLastName = new GridBagConstraints();
    gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
    gbc_lblLastName.anchor = GridBagConstraints.EAST;
    gbc_lblLastName.gridx = 0;
    gbc_lblLastName.gridy = 1;
    add(lastNameLabel, gbc_lblLastName);
    
    lastNameField = new JTextField();
    GridBagConstraints gbc_lastNameField = new GridBagConstraints();
    gbc_lastNameField.insets = new Insets(0, 0, 5, 5);
    gbc_lastNameField.fill = GridBagConstraints.HORIZONTAL;
    gbc_lastNameField.gridx = 1;
    gbc_lastNameField.gridy = 1;
    add(lastNameField, gbc_lastNameField);
    lastNameField.setColumns(10);
    
    prizeWonLabel = new JLabel("Prize Won:");
    GridBagConstraints gbc_lblPrizeWon = new GridBagConstraints();
    gbc_lblPrizeWon.anchor = GridBagConstraints.EAST;
    gbc_lblPrizeWon.insets = new Insets(0, 0, 5, 5);
    gbc_lblPrizeWon.gridx = 2;
    gbc_lblPrizeWon.gridy = 1;
    add(prizeWonLabel, gbc_lblPrizeWon);
    
    prizeField = new JTextField();
    prizeField.setEditable(false);
    GridBagConstraints gbc_prizeField = new GridBagConstraints();
    gbc_prizeField.gridwidth = 2;
    gbc_prizeField.insets = new Insets(0, 0, 5, 5);
    gbc_prizeField.fill = GridBagConstraints.HORIZONTAL;
    gbc_prizeField.gridx = 3;
    gbc_prizeField.gridy = 1;
    add(prizeField, gbc_prizeField);
    prizeField.setColumns(10);
    
    aliasLabel = new JLabel("Alias:");
    GridBagConstraints gbc_lblAlias = new GridBagConstraints();
    gbc_lblAlias.anchor = GridBagConstraints.EAST;
    gbc_lblAlias.insets = new Insets(0, 0, 5, 5);
    gbc_lblAlias.gridx = 0;
    gbc_lblAlias.gridy = 2;
    add(aliasLabel, gbc_lblAlias);
    
    aliasField = new JTextField();
    GridBagConstraints gbc_aliasField = new GridBagConstraints();
    gbc_aliasField.insets = new Insets(0, 0, 5, 5);
    gbc_aliasField.fill = GridBagConstraints.HORIZONTAL;
    gbc_aliasField.gridx = 1;
    gbc_aliasField.gridy = 2;
    add(aliasField, gbc_aliasField);
    aliasField.setColumns(10);
    
    saveButton = new JButton("Save");
    GridBagConstraints gbc_saveButton = new GridBagConstraints();
    gbc_saveButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_saveButton.insets = new Insets(0, 0, 5, 5);
    gbc_saveButton.gridx = 2;
    gbc_saveButton.gridy = 2;
    add(saveButton, gbc_saveButton);
    
    emailLabel = new JLabel("E-mail:");
    GridBagConstraints gbc_lblEmail = new GridBagConstraints();
    gbc_lblEmail.anchor = GridBagConstraints.EAST;
    gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
    gbc_lblEmail.gridx = 0;
    gbc_lblEmail.gridy = 3;
    add(emailLabel, gbc_lblEmail);
    
    emailField = new JTextField();
    GridBagConstraints gbc_emailField = new GridBagConstraints();
    gbc_emailField.insets = new Insets(0, 0, 5, 5);
    gbc_emailField.fill = GridBagConstraints.HORIZONTAL;
    gbc_emailField.gridx = 1;
    gbc_emailField.gridy = 3;
    add(emailField, gbc_emailField);
    emailField.setColumns(10);
    
    refreshButton = new JButton("Refresh");
    
        GridBagConstraints gbc_refreshButton = new GridBagConstraints();
        gbc_refreshButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_refreshButton.insets = new Insets(0, 0, 5, 5);
        gbc_refreshButton.gridx = 2;
        gbc_refreshButton.gridy = 3;
        add(refreshButton, gbc_refreshButton);
    
    openDonationButton = new JButton("Open Donation");
    GridBagConstraints gbc_btnOpen = new GridBagConstraints();
    gbc_btnOpen.insets = new Insets(0, 0, 5, 5);
    gbc_btnOpen.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnOpen.gridx = 3;
    gbc_btnOpen.gridy = 5;
    add(openDonationButton, gbc_btnOpen);
    
    addDonationButton = new JButton("Add Donation");
    GridBagConstraints gbc_addDonationButton = new GridBagConstraints();
    gbc_addDonationButton.insets = new Insets(0, 0, 5, 5);
    gbc_addDonationButton.gridx = 4;
    gbc_addDonationButton.gridy = 5;
    add(addDonationButton, gbc_addDonationButton);
    
    
    scrollPane = new JScrollPane();
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridwidth = 7;
    gbc_scrollPane.gridx = 0;
    gbc_scrollPane.gridy = 6;
    add(scrollPane, gbc_scrollPane);
    
    donationTable = new JTable();
    scrollPane.setViewportView(donationTable);
    donationTable.setFillsViewportHeight(true);
  }
  
  private void initializeGUIEvents()
  {
    saveButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent arg0) 
      {
        DonorPanel.this.saveEnteredContent();
      }
    });
    
    refreshButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent arg0) 
      {
        DonorPanel.this.refreshContent();
      }
    });
    
    openDonationButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) 
      {
        DonorPanel.this.openDonation();
      }
    });
    
    donationTable.addMouseListener(new MouseAdapter() 
    {
      @Override
      public void mouseClicked(MouseEvent e) 
      {
        if (e.getClickCount() == 2)
        {
          DonorPanel.this.openDonation();
        }
      }
    });
    
    deleteDonorButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0) 
      {
        DonorPanel.this.deleteDonor();
      }
    });
    
    this.addDonationButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent arg0) 
      {
        DonorPanel.this.createNewDonation();
      }
    });
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {
      this.firstNameField,
      this.lastNameField,
      this.aliasField,
      this.emailField,
      this.saveButton,
      this.refreshButton,
      this.openDonationButton,
      this.addDonationButton,
    });
    this.setFocusTraversalPolicy(this.tabOrder);
  }
  
  public boolean isFocusCycleRoot()
  {
    return true;
  }
  
  public FocusTraversalPolicy getFocusTraversalPolicy() 
  {
    return this.tabOrder;
  }
  
  /**
   * @wbp.parser.constructor
   */
  public DonorPanel(MainWindow owner, DonorControl donorControl)
  {
    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.owner = owner;
    this.donorControl = donorControl;
    this.refreshContent();
  }
  
  public int getDonorId()
  {
    return this.donorControl.getDonorId();
  }

  public void refreshContent()
  {
    Donor data = this.donorControl.getData();
    
    this.firstNameField.setText(data.getFirstName());
    this.lastNameField.setText(data.getLastName());
    this.aliasField.setText(data.getAlias());
    this.emailField.setText(data.getEmail());
    
    this.emailField.setEditable(this.donorControl.allowEmailUpdate());
    
    this.totalDonatedField.setText(this.donorControl.getTotalDonated().toString());
    this.prizeField.setText("NOT IMPLEMENTED YET.");
    
    List<Donation> donations = this.donorControl.getDonorDonations();
    CustomTableModel tableData = new CustomTableModel(new String[]
    {
        "Time Received",
        "Domain",
        "Amount",
        "Comment",
    },0);

    donationTableIds = new int[donations.size()];
    
    for(int i = 0; i < donations.size(); ++i)
    {
      Donation d = donations.get(i);
      donationTableIds[i] = d.getId();
      tableData.addRow(new Object[]
      {
        d.getTimeReceived().toString(), 
        StringUtils.symbolToNatural(d.getDomain().toString()),
        d.getAmount().toString(),
        StringUtils.emptyIfNull(d.getComment())
      });
    }
    
    this.donationTable.setModel(tableData);
    
    this.setHeaderText(data.toString());
  }

  private void deleteDonor()
  {
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this donor?", "Confirm delete", JOptionPane.YES_NO_OPTION);
    
    if (result == JOptionPane.OK_OPTION)
    {
      this.donorControl.deleteDonor();
      this.owner.removeTab(this);
    }
  }
  
  private void createNewDonation()
  {
    int id = this.donorControl.createNewLocalDonation();
    this.owner.openDonationTab(id);
  }
  
  private void saveEnteredContent()
  {
    this.donorControl.updateData(
        this.emailField.getText(),
        this.aliasField.getText(),
        this.firstNameField.getText(),
        this.lastNameField.getText());
    
    this.refreshContent();
  }
  
  private void openDonation()
  {
    int selectedRow = this.donationTable.getSelectedRow();
    
    if (selectedRow != -1)
    {
      this.owner.openDonationTab(this.donationTableIds[selectedRow]);
    }
  }

  @Override
  public boolean confirmClose()
  {
    // this could open a JOptionPane to prevent the user from closing a tab with 
    // pending modifications ...
    return true;
  }
}
