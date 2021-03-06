package pheidip.ui;

import pheidip.logic.EntityControlInstance;
import pheidip.objects.BidState;
import pheidip.objects.Challenge;
import pheidip.objects.ChallengeBid;
import pheidip.objects.SpeedRun;
import pheidip.util.FormatUtils;

import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class ChallengePanel extends EntityPanel
{
  private MainWindow owner;
  private EntityControlInstance<Challenge> challengeControl;
  private JTextField nameField;
  private JFormattedTextField amountField;
  private JButton saveButton;
  private JButton refreshButton;
  private JLabel totalCollectedLabel;
  private JTextField totalAmountField;
  private JButton deleteChallengeButton;
  private JLabel nameLabel;
  private JLabel goalAmountLabel;
  private ActionHandler actionHandler;
  private FocusTraversalManager tabOrder;
  private JScrollPane descriptionScrollPane;
  private JTextArea descriptionTextArea;
  private JLabel lblDescription;
  private JLabel stateLabel;
  private JComboBox bidStateComboBox;
  private JLabel lblRun;
  private JScrollPane bidScrollPane;
  private JTable bidsTable;
  private JButton openDonationButton;
  
  private ListTableModel<ChallengeBid> bidsTableData;
  private EntitySelector<SpeedRun> runSelector;
  
  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{0, 107, 103, 95, 93, 26, 93, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    nameLabel = new JLabel("Name:");
    GridBagConstraints gbc_nameLabel = new GridBagConstraints();
    gbc_nameLabel.insets = new Insets(0, 0, 5, 5);
    gbc_nameLabel.anchor = GridBagConstraints.EAST;
    gbc_nameLabel.gridx = 0;
    gbc_nameLabel.gridy = 0;
    add(nameLabel, gbc_nameLabel);
    
    nameField = new JTextField();
    GridBagConstraints gbc_nameField = new GridBagConstraints();
    gbc_nameField.gridwidth = 2;
    gbc_nameField.insets = new Insets(0, 0, 5, 5);
    gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
    gbc_nameField.gridx = 1;
    gbc_nameField.gridy = 0;
    add(nameField, gbc_nameField);
    nameField.setColumns(10);
    
    deleteChallengeButton = new JButton("Delete Challenge");
    GridBagConstraints gbc_deleteChallengeButton = new GridBagConstraints();
    gbc_deleteChallengeButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_deleteChallengeButton.insets = new Insets(0, 0, 5, 0);
    gbc_deleteChallengeButton.gridx = 6;
    gbc_deleteChallengeButton.gridy = 0;
    add(deleteChallengeButton, gbc_deleteChallengeButton);
    
    lblRun = new JLabel("Run:");
    GridBagConstraints gbc_lblRun = new GridBagConstraints();
    gbc_lblRun.anchor = GridBagConstraints.EAST;
    gbc_lblRun.insets = new Insets(0, 0, 5, 5);
    gbc_lblRun.gridx = 0;
    gbc_lblRun.gridy = 1;
    add(lblRun, gbc_lblRun);
    
    runSelector = new EntitySelector<SpeedRun>(this.owner, SpeedRun.class);
    runSelector.setNullSelectionAllowed(true);
    runSelector.setNavigationAllowed(true);
    GridBagConstraints gbc_runSelector = new GridBagConstraints();
    gbc_runSelector.gridwidth = 4;
    gbc_runSelector.insets = new Insets(0, 0, 5, 5);
    gbc_runSelector.fill = GridBagConstraints.BOTH;
    gbc_runSelector.gridx = 1;
    gbc_runSelector.gridy = 1;
    add(runSelector, gbc_runSelector);
    
    goalAmountLabel = new JLabel("Goal Amount:");
    GridBagConstraints gbc_goalAmountLabel = new GridBagConstraints();
    gbc_goalAmountLabel.anchor = GridBagConstraints.EAST;
    gbc_goalAmountLabel.insets = new Insets(0, 0, 5, 5);
    gbc_goalAmountLabel.gridx = 0;
    gbc_goalAmountLabel.gridy = 2;
    add(goalAmountLabel, gbc_goalAmountLabel);
    
    amountField = new JFormattedTextField(FormatUtils.getMoneyFormat());
    GridBagConstraints gbc_amountField = new GridBagConstraints();
    gbc_amountField.gridwidth = 2;
    gbc_amountField.insets = new Insets(0, 0, 5, 5);
    gbc_amountField.fill = GridBagConstraints.HORIZONTAL;
    gbc_amountField.gridx = 1;
    gbc_amountField.gridy = 2;
    add(amountField, gbc_amountField);
    amountField.setColumns(10);
    
    stateLabel = new JLabel("State:");
    GridBagConstraints gbc_stateLabel = new GridBagConstraints();
    gbc_stateLabel.anchor = GridBagConstraints.NORTHEAST;
    gbc_stateLabel.insets = new Insets(0, 0, 5, 5);
    gbc_stateLabel.gridx = 0;
    gbc_stateLabel.gridy = 3;
    add(stateLabel, gbc_stateLabel);

    bidStateComboBox = new JComboBox(BidState.values());
    GridBagConstraints gbc_bidStateComboBox = new GridBagConstraints();
    gbc_bidStateComboBox.gridwidth = 2;
    gbc_bidStateComboBox.insets = new Insets(0, 0, 5, 5);
    gbc_bidStateComboBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_bidStateComboBox.gridx = 1;
    gbc_bidStateComboBox.gridy = 3;
    add(bidStateComboBox, gbc_bidStateComboBox);
    
    openDonationButton = new JButton("Open Donation");
    GridBagConstraints gbc_openBidButton = new GridBagConstraints();
    gbc_openBidButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_openBidButton.insets = new Insets(0, 0, 5, 5);
    gbc_openBidButton.gridx = 4;
    gbc_openBidButton.gridy = 3;
    add(openDonationButton, gbc_openBidButton);
    
    bidScrollPane = new JScrollPane();
    GridBagConstraints gbc_bidScrollPane = new GridBagConstraints();
    gbc_bidScrollPane.gridwidth = 3;
    gbc_bidScrollPane.insets = new Insets(0, 0, 5, 0);
    gbc_bidScrollPane.fill = GridBagConstraints.BOTH;
    gbc_bidScrollPane.gridx = 4;
    gbc_bidScrollPane.gridy = 4;
    add(bidScrollPane, gbc_bidScrollPane);
    
    bidsTable = new JTable();
    bidsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    bidScrollPane.setViewportView(bidsTable);
    
    lblDescription = new JLabel("Description:");
    GridBagConstraints gbc_lblDescription = new GridBagConstraints();
    gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
    gbc_lblDescription.gridx = 0;
    gbc_lblDescription.gridy = 4;
    add(lblDescription, gbc_lblDescription);
    
    descriptionScrollPane = new JScrollPane();
    descriptionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    GridBagConstraints gbc_descriptionScrollPane = new GridBagConstraints();
    gbc_descriptionScrollPane.gridwidth = 3;
    gbc_descriptionScrollPane.insets = new Insets(0, 0, 5, 5);
    gbc_descriptionScrollPane.fill = GridBagConstraints.BOTH;
    gbc_descriptionScrollPane.gridx = 1;
    gbc_descriptionScrollPane.gridy = 4;
    add(descriptionScrollPane, gbc_descriptionScrollPane);
    
    descriptionTextArea = new JTextArea();
    descriptionTextArea.setWrapStyleWord(true);
    descriptionTextArea.setLineWrap(true);
    descriptionScrollPane.setViewportView(descriptionTextArea);
    
    saveButton = new JButton("Save");
    GridBagConstraints gbc_saveButton = new GridBagConstraints();
    gbc_saveButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_saveButton.insets = new Insets(0, 0, 5, 5);
    gbc_saveButton.gridx = 1;
    gbc_saveButton.gridy = 5;
    add(saveButton, gbc_saveButton);
    
    refreshButton = new JButton("Refresh");
    GridBagConstraints gbc_refreshButton = new GridBagConstraints();
    gbc_refreshButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_refreshButton.insets = new Insets(0, 0, 5, 5);
    gbc_refreshButton.gridx = 2;
    gbc_refreshButton.gridy = 5;
    add(refreshButton, gbc_refreshButton);
    
    totalCollectedLabel = new JLabel("Total Collected:");
    GridBagConstraints gbc_totalCollectedLabel = new GridBagConstraints();
    gbc_totalCollectedLabel.anchor = GridBagConstraints.EAST;
    gbc_totalCollectedLabel.insets = new Insets(0, 0, 0, 5);
    gbc_totalCollectedLabel.gridx = 0;
    gbc_totalCollectedLabel.gridy = 7;
    add(totalCollectedLabel, gbc_totalCollectedLabel);
    
    totalAmountField = new JTextField();
    totalAmountField.setEditable(false);
    GridBagConstraints gbc_totalAmountField = new GridBagConstraints();
    gbc_totalAmountField.insets = new Insets(0, 0, 0, 5);
    gbc_totalAmountField.gridwidth = 3;
    gbc_totalAmountField.fill = GridBagConstraints.HORIZONTAL;
    gbc_totalAmountField.gridx = 1;
    gbc_totalAmountField.gridy = 7;
    add(totalAmountField, gbc_totalAmountField);
    totalAmountField.setColumns(10);
  }
  
  private class ActionHandler extends MouseAdapter implements ActionListener
  {
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == saveButton)
        {
          ChallengePanel.this.saveContent();
        }
        else if (ev.getSource() == refreshButton)
        {
          ChallengePanel.this.refreshContent();
        }
        else if (ev.getSource() == deleteChallengeButton)
        {
          ChallengePanel.this.deleteContent();
        }
        else if (ev.getSource() == openDonationButton)
        {
          ChallengePanel.this.openSelectedBid();
        }
      }
      catch(Exception e)
      {
        e.printStackTrace();
        owner.report(e);
      }
    }
    
    @Override
    public void mouseClicked(MouseEvent event)
    {
      try
      {
        if (event.getSource() == bidsTable)
        {
          if (event.getClickCount() == 2)
          {
            ChallengePanel.this.openSelectedBid();
          }
        }
      }
      catch(Exception e)
      {
        owner.report(e);
      }
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    this.saveButton.addActionListener(this.actionHandler);
    this.refreshButton.addActionListener(this.actionHandler);
    this.deleteChallengeButton.addActionListener(this.actionHandler);
    this.openDonationButton.addActionListener(this.actionHandler);
    this.bidsTable.addMouseListener(this.actionHandler);
    
    this.descriptionTextArea.addKeyListener(new TabTraversalKeyListener(this.descriptionTextArea));
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {
      this.nameField,
      this.runSelector,
      this.amountField,
      this.bidStateComboBox,
      this.descriptionTextArea,
      this.saveButton,
      this.refreshButton,
    });
    this.setFocusTraversalPolicy(this.tabOrder);
  }
  
  public void openSelectedBid()
  {
    int selectedRow = this.bidsTable.getSelectedRow();
      
    if (selectedRow != -1)
    {
      this.owner.openDonationTab(this.bidsTableData.getRow(selectedRow).getDonation());
    }
  }

  public boolean isFocusCycleRoot()
  {
    return true;
  }
  
  public FocusTraversalPolicy getFocusTraversalPolicy() 
  {
    return this.tabOrder;
  }

  public ChallengePanel(MainWindow owner, Challenge challenge)
  {
    this.owner = owner;
    this.challengeControl = new EntityControlInstance<Challenge>(owner.getInstance().getEntityControl(Challenge.class), challenge);
    
    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.refreshContent();
  }
  
  @Override
  public boolean confirmClose()
  {
    return true;
  }


  @Override
  public void refreshContent()
  {
    this.challengeControl.refreshInstance();

    if (!this.challengeControl.isValid())
    {
      JOptionPane.showMessageDialog(this, "Error, this challenge no longer exists", "Not Found", JOptionPane.ERROR_MESSAGE);
      this.owner.removeTab(this);
      return;
    }
    
    Challenge data = this.challengeControl.getInstance();
    
    this.nameField.setText(data.getName());
    this.amountField.setText(data.getGoalAmount().toString());
    this.descriptionTextArea.setText(data.getDescription());
    this.totalAmountField.setText(data.getTotalCollected().toString());
    this.bidStateComboBox.setSelectedItem(data.getBidState());
    this.runSelector.setEntity(data.getSpeedRun());
    
    this.bidsTableData = new ListTableModel<ChallengeBid>(ChallengeBid.class,
      "amount",
      "donorName");
    
    this.bidsTableData.setCollection(data.getBids());

    this.bidsTable.setModel(this.bidsTableData);
    
    this.setHeaderText(data.toString());
  }
  
  @Override
  public void redrawContent()
  {
    if (!this.challengeControl.isValid())
    {
      JOptionPane.showMessageDialog(this, "Error, this challenge no longer exists", "Not Found", JOptionPane.ERROR_MESSAGE);
      this.owner.removeTab(this);
      return;
    }
    
    Challenge data = this.challengeControl.getInstance();

    this.bidsTableData = new ListTableModel<ChallengeBid>(ChallengeBid.class,
        "amount",
        "donorName");
      
    this.bidsTableData.setCollection(data.getBids());

    this.bidsTable.setModel(this.bidsTableData);
  }

  public int getChallengeId()
  {
    return this.challengeControl.getId();
  }

  public void deleteContent()
  {
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this challenge?", "Confirm delete", JOptionPane.YES_NO_OPTION);
    
    if (result == JOptionPane.YES_OPTION)
    {
      this.challengeControl.deleteInstance();
      this.owner.removeTab(this);
    }
  }

  public void saveContent()
  {
    Challenge data = this.challengeControl.getInstance();
    data.setName(this.nameField.getText());
    data.setGoalAmount(new BigDecimal(this.amountField.getText()));
    data.setDescription(this.descriptionTextArea.getText());
    data.setBidState((BidState)this.bidStateComboBox.getSelectedItem());
    data.setSpeedRun(this.runSelector.getEntity());
    this.challengeControl.saveInstance();
    this.refreshContent();
  }
}
