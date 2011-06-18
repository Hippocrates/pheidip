package pheidip.ui;

import pheidip.logic.DonationControl;
import pheidip.objects.Donation;
import pheidip.objects.Donor;

import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class DonationPanel extends TabPanel
{
  private DonationControl donationControl;
  private JTextField amountField;
  private JTextField domainIdField;
  private MainWindow owner;
  private JLabel domainIdLabel;
  private JLabel amountLabel;
  private JLabel donorLabel;
  private JTextField donorField;
  private JButton openDonorButton;
  private JLabel timeLabel;
  private JTextField timeField;
  private JLabel commentLabel;
  private JTextArea commentTextArea;
  private JButton refreshButton;
  private JButton saveButton;
  private JScrollPane commentScrollPane;
  private JButton deleteButton;
  private FocusTraversalManager tabOrder;
  private ActionHandler actionHandler;
  private JScrollPane bidScrollPane;
  private JButton attachBidButton;
  private JButton changeAmountButton;
  private JButton removeBidButton;
  private JTable bidTable;
  
  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{66, 113, 105, 90, 104, 0, 95};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 113, 0, 25, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    amountLabel = new JLabel("Amount:");
    GridBagConstraints gbc_lblAmount = new GridBagConstraints();
    gbc_lblAmount.insets = new Insets(0, 0, 5, 5);
    gbc_lblAmount.anchor = GridBagConstraints.EAST;
    gbc_lblAmount.gridx = 0;
    gbc_lblAmount.gridy = 0;
    add(amountLabel, gbc_lblAmount);
    
    amountField = new JTextField();
    GridBagConstraints gbc_amountField = new GridBagConstraints();
    gbc_amountField.gridwidth = 2;
    gbc_amountField.insets = new Insets(0, 0, 5, 5);
    gbc_amountField.fill = GridBagConstraints.HORIZONTAL;
    gbc_amountField.gridx = 1;
    gbc_amountField.gridy = 0;
    add(amountField, gbc_amountField);
    amountField.setColumns(10);
    
    commentLabel = new JLabel("Comment:");
    GridBagConstraints gbc_commentLabel = new GridBagConstraints();
    gbc_commentLabel.insets = new Insets(0, 0, 5, 5);
    gbc_commentLabel.gridx = 3;
    gbc_commentLabel.gridy = 0;
    add(commentLabel, gbc_commentLabel);
    
    commentScrollPane = new JScrollPane();
    commentScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
    gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
    gbc_scrollPane_1.gridheight = 7;
    gbc_scrollPane_1.gridwidth = 3;
    gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
    gbc_scrollPane_1.gridx = 4;
    gbc_scrollPane_1.gridy = 0;
    add(commentScrollPane, gbc_scrollPane_1);
    
    commentTextArea = new JTextArea();
    commentTextArea.setLineWrap(true);
    commentTextArea.setWrapStyleWord(true);
    commentScrollPane.setViewportView(commentTextArea);
    
    domainIdLabel = new JLabel("Domain Id:");
    GridBagConstraints gbc_lblDomainId = new GridBagConstraints();
    gbc_lblDomainId.anchor = GridBagConstraints.EAST;
    gbc_lblDomainId.insets = new Insets(0, 0, 5, 5);
    gbc_lblDomainId.gridx = 0;
    gbc_lblDomainId.gridy = 1;
    add(domainIdLabel, gbc_lblDomainId);
    
    domainIdField = new JTextField();
    domainIdField.setEditable(false);
    GridBagConstraints gbc_domainIdField = new GridBagConstraints();
    gbc_domainIdField.gridwidth = 2;
    gbc_domainIdField.insets = new Insets(0, 0, 5, 5);
    gbc_domainIdField.fill = GridBagConstraints.HORIZONTAL;
    gbc_domainIdField.gridx = 1;
    gbc_domainIdField.gridy = 1;
    add(domainIdField, gbc_domainIdField);
    domainIdField.setColumns(10);
    
    timeLabel = new JLabel("Timestamp:");
    GridBagConstraints gbc_timeLabel = new GridBagConstraints();
    gbc_timeLabel.anchor = GridBagConstraints.EAST;
    gbc_timeLabel.insets = new Insets(0, 0, 5, 5);
    gbc_timeLabel.gridx = 0;
    gbc_timeLabel.gridy = 2;
    add(timeLabel, gbc_timeLabel);
    
    timeField = new JTextField();
    timeField.setEditable(false);
    GridBagConstraints gbc_timeField = new GridBagConstraints();
    gbc_timeField.gridwidth = 2;
    gbc_timeField.insets = new Insets(0, 0, 5, 5);
    gbc_timeField.fill = GridBagConstraints.HORIZONTAL;
    gbc_timeField.gridx = 1;
    gbc_timeField.gridy = 2;
    add(timeField, gbc_timeField);
    timeField.setColumns(10);
    
    donorLabel = new JLabel("Donor:");
    GridBagConstraints gbc_donorLabel = new GridBagConstraints();
    gbc_donorLabel.anchor = GridBagConstraints.EAST;
    gbc_donorLabel.insets = new Insets(0, 0, 5, 5);
    gbc_donorLabel.gridx = 0;
    gbc_donorLabel.gridy = 3;
    add(donorLabel, gbc_donorLabel);
    
    donorField = new JTextField();
    donorField.setEditable(false);
    GridBagConstraints gbc_donorField = new GridBagConstraints();
    gbc_donorField.gridwidth = 2;
    gbc_donorField.insets = new Insets(0, 0, 5, 5);
    gbc_donorField.fill = GridBagConstraints.HORIZONTAL;
    gbc_donorField.gridx = 1;
    gbc_donorField.gridy = 3;
    add(donorField, gbc_donorField);
    donorField.setColumns(10);
    
    openDonorButton = new JButton("Open Donor");
    GridBagConstraints gbc_openDonorButton = new GridBagConstraints();
    gbc_openDonorButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_openDonorButton.insets = new Insets(0, 0, 5, 5);
    gbc_openDonorButton.gridx = 3;
    gbc_openDonorButton.gridy = 3;
    add(openDonorButton, gbc_openDonorButton);

    refreshButton = new JButton("Refresh");
    GridBagConstraints gbc_refreshButton = new GridBagConstraints();
    gbc_refreshButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_refreshButton.insets = new Insets(0, 0, 5, 5);
    gbc_refreshButton.gridx = 1;
    gbc_refreshButton.gridy = 5;
    add(refreshButton, gbc_refreshButton);
    
    saveButton = new JButton("Save");
    GridBagConstraints gbc_saveButton = new GridBagConstraints();
    gbc_saveButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_saveButton.insets = new Insets(0, 0, 5, 5);
    gbc_saveButton.gridx = 2;
    gbc_saveButton.gridy = 5;
    add(saveButton, gbc_saveButton);
    
    attachBidButton = new JButton("Attach Bid");
    GridBagConstraints gbc_attachBidButton = new GridBagConstraints();
    gbc_attachBidButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_attachBidButton.insets = new Insets(0, 0, 5, 5);
    gbc_attachBidButton.gridx = 1;
    gbc_attachBidButton.gridy = 7;
    add(attachBidButton, gbc_attachBidButton);
    
    changeAmountButton = new JButton("Change Amount");
    GridBagConstraints gbc_changeAmountButton = new GridBagConstraints();
    gbc_changeAmountButton.insets = new Insets(0, 0, 5, 5);
    gbc_changeAmountButton.gridx = 2;
    gbc_changeAmountButton.gridy = 7;
    add(changeAmountButton, gbc_changeAmountButton);
    
    removeBidButton = new JButton("Remove Bid");
    GridBagConstraints gbc_removeBidButton = new GridBagConstraints();
    gbc_removeBidButton.insets = new Insets(0, 0, 5, 5);
    gbc_removeBidButton.gridx = 3;
    gbc_removeBidButton.gridy = 7;
    add(removeBidButton, gbc_removeBidButton);
    
    deleteButton = new JButton("Delete Donation");
    GridBagConstraints gbc_deleteButton = new GridBagConstraints();
    gbc_deleteButton.insets = new Insets(0, 0, 5, 5);
    gbc_deleteButton.gridx = 5;
    gbc_deleteButton.gridy = 7;
    add(deleteButton, gbc_deleteButton);
    
    bidScrollPane = new JScrollPane();
    GridBagConstraints gbc_bidScrollPane = new GridBagConstraints();
    gbc_bidScrollPane.gridwidth = 7;
    gbc_bidScrollPane.fill = GridBagConstraints.BOTH;
    gbc_bidScrollPane.gridx = 0;
    gbc_bidScrollPane.gridy = 8;
    add(bidScrollPane, gbc_bidScrollPane);
    
    bidTable = new JTable();
    bidScrollPane.setViewportView(bidTable);
  }
  
  private class ActionHandler extends MouseAdapter implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent event)
    {
      try
      {
        if (event.getSource() == openDonorButton)
        {
          DonationPanel.this.openDonor();
        }
        else if (event.getSource() == refreshButton)
        {
          DonationPanel.this.refreshContent();
        }
        else if (event.getSource() == saveButton)
        {
          DonationPanel.this.saveEnteredContent();
        }
        else if (event.getSource() == deleteButton)
        {
          DonationPanel.this.deleteDonation();
        }
        else if (event.getSource() == attachBidButton)
        {
          DonationPanel.this.attachNewBid();
        }
        else if (event.getSource() == changeAmountButton)
        {
          DonationPanel.this.changeCurrentBidAmount();
        }
        else if (event.getSource() == removeBidButton)
        {
          DonationPanel.this.removeCurrentBid();
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
    
    this.openDonorButton.addActionListener(this.actionHandler);
    this.refreshButton.addActionListener(this.actionHandler);
    this.saveButton.addActionListener(this.actionHandler);
    this.deleteButton.addActionListener(this.actionHandler);
    this.attachBidButton.addActionListener(this.actionHandler);
    this.changeAmountButton.addActionListener(this.actionHandler);
    this.removeBidButton.addActionListener(this.actionHandler);
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {
      this.amountField,
      this.openDonorButton,
      this.refreshButton,
      this.saveButton,
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

  protected void deleteDonation()
  {
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this donation?", "Confirm delete", JOptionPane.YES_NO_OPTION);
    
    if (result == JOptionPane.OK_OPTION)
    {
      this.donationControl.deleteDonation();
      this.owner.removeTab(this);
    }
  }

  public DonationPanel(MainWindow owner, DonationControl control)
  {
    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.owner = owner;
    this.donationControl = control;
  }
  
  public int getDonationId()
  {
    return this.donationControl.getDonationId();
  }

  @Override
  public boolean confirmClose()
  {
    return true;
  }

  @Override
  public void refreshContent()
  {
    Donation result = this.donationControl.getData();
    Donor donor = this.donationControl.getDonationDonor();
    
    this.amountField.setText(result.getAmount().toString());
    this.timeField.setText(result.getTimeReceived().toString());
    
    if (this.donationControl.allowUpdateData())
    {
      this.amountField.setEditable(true);
      this.commentTextArea.setEditable(true);
      this.saveButton.setEnabled(true);
    }
    else
    {
      this.amountField.setEditable(false);
      this.commentTextArea.setEditable(false);
      this.saveButton.setEnabled(false);
    }
    
    this.domainIdField.setText(result.getDomainString());
    
    this.donorField.setText(donor.toString());
    
    this.commentTextArea.setText(result.getComment());

    this.setHeaderText(result.getDomainString());
  }
  
  private void saveEnteredContent()
  {
    if (this.donationControl.allowUpdateData())
    {
      this.donationControl.updateData(new BigDecimal(this.amountField.getText()), this.commentTextArea.getText());
    }
    
    this.refreshContent();
  }

  private void openDonor()
  {
    Donor donor = this.donationControl.getDonationDonor();
    this.owner.openDonorTab(donor.getId());
  }
  
  public void removeCurrentBid()
  {
    // TODO Auto-generated method stub
    
  }

  public void changeCurrentBidAmount()
  {
    // TODO Auto-generated method stub
    
  }

  public void attachNewBid()
  {
    // TODO Auto-generated method stub
    
  }
}
