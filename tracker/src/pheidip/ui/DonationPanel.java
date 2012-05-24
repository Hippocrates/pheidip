package pheidip.ui;

import pheidip.logic.EntityControlInstance;
import pheidip.objects.Donation;
import pheidip.objects.Donor;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationCommentState;
import pheidip.objects.DonationReadState;
import pheidip.util.FormatUtils;

import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class DonationPanel extends EntityPanel
{
  private EntityControlInstance<Donation> donationControl;
  private JFormattedTextField amountField;
  private JTextField domainIdField;
  private MainWindow owner;
  private JLabel domainIdLabel;
  private JLabel amountLabel;
  private JLabel donorLabel;
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
  private DonationBidsPanel donationBidsPanel;
  private JComboBox bidStateComboBox;
  private JComboBox readStateComboBox;
  private JComboBox commentStateComboBox;
  private JLabel bidStateLabel;
  private JLabel readStateLabel;
  private JLabel commentStateLabel;
  private EntitySelector<Donor> donorSelector;

  public int getDonationId()
  {
    return this.donationControl.getInstance().getId();
  }
  
  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{99, 93, 105, 105, 58, 48, 63};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 23, 58, 31, 0, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    amountLabel = new JLabel("Amount:");
    GridBagConstraints gbc_lblAmount = new GridBagConstraints();
    gbc_lblAmount.insets = new Insets(0, 0, 5, 5);
    gbc_lblAmount.anchor = GridBagConstraints.EAST;
    gbc_lblAmount.gridx = 0;
    gbc_lblAmount.gridy = 0;
    add(amountLabel, gbc_lblAmount);
    
    amountField = new JFormattedTextField(FormatUtils.getMoneyFormat());
    amountField.setHorizontalAlignment(SwingConstants.TRAILING);
    GridBagConstraints gbc_amountField = new GridBagConstraints();
    gbc_amountField.gridwidth = 3;
    gbc_amountField.insets = new Insets(0, 0, 5, 5);
    gbc_amountField.fill = GridBagConstraints.HORIZONTAL;
    gbc_amountField.gridx = 1;
    gbc_amountField.gridy = 0;
    add(amountField, gbc_amountField);
    amountField.setColumns(10);
    
    deleteButton = new JButton("Delete Donation");
    GridBagConstraints gbc_deleteButton = new GridBagConstraints();
    gbc_deleteButton.insets = new Insets(0, 0, 5, 0);
    gbc_deleteButton.gridx = 6;
    gbc_deleteButton.gridy = 0;
    add(deleteButton, gbc_deleteButton);
    
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
    gbc_domainIdField.gridwidth = 3;
    gbc_domainIdField.insets = new Insets(0, 0, 5, 5);
    gbc_domainIdField.fill = GridBagConstraints.HORIZONTAL;
    gbc_domainIdField.gridx = 1;
    gbc_domainIdField.gridy = 1;
    add(domainIdField, gbc_domainIdField);
    domainIdField.setColumns(10);
    
    commentLabel = new JLabel("Comment:");
    GridBagConstraints gbc_commentLabel = new GridBagConstraints();
    gbc_commentLabel.insets = new Insets(0, 0, 5, 5);
    gbc_commentLabel.gridx = 4;
    gbc_commentLabel.gridy = 1;
    add(commentLabel, gbc_commentLabel);
    
    commentScrollPane = new JScrollPane();
    commentScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
    gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
    gbc_scrollPane_1.gridheight = 7;
    gbc_scrollPane_1.gridwidth = 3;
    gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
    gbc_scrollPane_1.gridx = 4;
    gbc_scrollPane_1.gridy = 2;
    add(commentScrollPane, gbc_scrollPane_1);
    
    commentTextArea = new JTextArea();
    commentTextArea.setLineWrap(true);
    commentTextArea.setWrapStyleWord(true);
    commentScrollPane.setViewportView(commentTextArea);
    
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
    gbc_timeField.gridwidth = 3;
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
        
        donorSelector = new EntitySelector<Donor>(this.owner, Donor.class);
        donorSelector.setNavigationAllowed(true);
        donorSelector.setNullSelectionAllowed(false);
        donorSelector.setReadOnly(false);
        GridBagConstraints gbc_donorSelector = new GridBagConstraints();
        gbc_donorSelector.gridwidth = 3;
        gbc_donorSelector.insets = new Insets(0, 0, 5, 5);
        gbc_donorSelector.fill = GridBagConstraints.BOTH;
        gbc_donorSelector.gridx = 1;
        gbc_donorSelector.gridy = 3;
        add(donorSelector, gbc_donorSelector);
        
        bidStateLabel = new JLabel("Bid State:");
        GridBagConstraints gbc_bidStateLabel = new GridBagConstraints();
        gbc_bidStateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_bidStateLabel.anchor = GridBagConstraints.EAST;
        gbc_bidStateLabel.gridx = 0;
        gbc_bidStateLabel.gridy = 4;
        add(bidStateLabel, gbc_bidStateLabel);
        
        bidStateComboBox = new JComboBox(DonationBidState.values());
        GridBagConstraints gbc_bidStateComboBox = new GridBagConstraints();
        gbc_bidStateComboBox.gridwidth = 3;
        gbc_bidStateComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_bidStateComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_bidStateComboBox.gridx = 1;
        gbc_bidStateComboBox.gridy = 4;
        add(bidStateComboBox, gbc_bidStateComboBox);
        
        readStateLabel = new JLabel("Read State:");
        GridBagConstraints gbc_readStateLabel = new GridBagConstraints();
        gbc_readStateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_readStateLabel.anchor = GridBagConstraints.EAST;
        gbc_readStateLabel.gridx = 0;
        gbc_readStateLabel.gridy = 5;
        add(readStateLabel, gbc_readStateLabel);
        
        readStateComboBox = new JComboBox(DonationReadState.values());
        GridBagConstraints gbc_readStateComboBox = new GridBagConstraints();
        gbc_readStateComboBox.gridwidth = 3;
        gbc_readStateComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_readStateComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_readStateComboBox.gridx = 1;
        gbc_readStateComboBox.gridy = 5;
        add(readStateComboBox, gbc_readStateComboBox);
        
        commentStateLabel = new JLabel("Comment Approval:");
        GridBagConstraints gbc_commentStateLabel = new GridBagConstraints();
        gbc_commentStateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_commentStateLabel.anchor = GridBagConstraints.EAST;
        gbc_commentStateLabel.gridx = 0;
        gbc_commentStateLabel.gridy = 6;
        add(commentStateLabel, gbc_commentStateLabel);
        
        commentStateComboBox = new JComboBox(DonationCommentState.values());
        GridBagConstraints gbc_commentStateComboBox = new GridBagConstraints();
        gbc_commentStateComboBox.gridwidth = 3;
        gbc_commentStateComboBox.insets = new Insets(0, 0, 5, 5);
        gbc_commentStateComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_commentStateComboBox.gridx = 1;
        gbc_commentStateComboBox.gridy = 6;
        add(commentStateComboBox, gbc_commentStateComboBox);
    
        refreshButton = new JButton("Refresh");
        GridBagConstraints gbc_refreshButton = new GridBagConstraints();
        gbc_refreshButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_refreshButton.insets = new Insets(0, 0, 5, 5);
        gbc_refreshButton.gridx = 1;
        gbc_refreshButton.gridy = 7;
        add(refreshButton, gbc_refreshButton);
    
    saveButton = new JButton("Save");
    GridBagConstraints gbc_saveButton = new GridBagConstraints();
    gbc_saveButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_saveButton.insets = new Insets(0, 0, 5, 5);
    gbc_saveButton.gridx = 2;
    gbc_saveButton.gridy = 7;
    add(saveButton, gbc_saveButton);
    
    donationBidsPanel = new DonationBidsPanel(this.owner, null);
    GridBagConstraints gbc_donationBidsPanel = new GridBagConstraints();
    gbc_donationBidsPanel.gridwidth = 7;
    gbc_donationBidsPanel.fill = GridBagConstraints.BOTH;
    gbc_donationBidsPanel.gridx = 0;
    gbc_donationBidsPanel.gridy = 10;
    add(donationBidsPanel, gbc_donationBidsPanel);
  }
  
  private class ActionHandler extends MouseAdapter implements ActionListener
  {
    @Override
    public void actionPerformed(ActionEvent event)
    {
      try
      {
        if (event.getSource() == refreshButton)
        {
          DonationPanel.this.refreshContent();
        }
        else if (event.getSource() == saveButton)
        {
          DonationPanel.this.saveContent();
        }
        else if (event.getSource() == deleteButton)
        {
          DonationPanel.this.deleteContent();
        }
      }
      catch(NumberFormatException nfe)
      {
        owner.report("Invalid amount entered.");
      }
      catch(Exception e)
      {
        e.printStackTrace();
        owner.report(e);
      }
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    this.refreshButton.addActionListener(this.actionHandler);
    this.saveButton.addActionListener(this.actionHandler);
    this.deleteButton.addActionListener(this.actionHandler);
    
    this.commentTextArea.addKeyListener(new TabTraversalKeyListener(this.commentTextArea)); 
    
    List<Component> tabArray = new ArrayList<Component>();
    tabArray.add(this.amountField);
    tabArray.add(this.donorSelector);
    tabArray.add(this.bidStateComboBox);
    tabArray.add(this.readStateComboBox);
    tabArray.add(this.commentStateComboBox);
    tabArray.add(this.refreshButton);
    tabArray.add(this.saveButton);
    tabArray.add(this.commentTextArea);
    tabArray.add(this.donationBidsPanel);
    
    this.donationBidsPanel.setFocusCycleRoot(false);

    this.tabOrder = new FocusTraversalManager(tabArray.toArray(new Component[tabArray.size()]));
    this.setFocusTraversalPolicy(this.tabOrder);
    this.setFocusTraversalPolicyProvider(true);
    this.setFocusCycleRoot(true);
  }
  
  protected void disableDeletion()
  {
    this.deleteButton.setEnabled(false);
  }

  @Override
  public void deleteContent()
  {
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this donation?", "Confirm delete", JOptionPane.YES_NO_OPTION);
    
    if (result == JOptionPane.OK_OPTION)
    {
      this.donationControl.deleteInstance();
      this.owner.removeTab(this);
    }
  }

  public DonationPanel(MainWindow owner, Donation donation)
  {    
    this.owner = owner;
    
    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.setDonation(donation);
  }
  
  public void setDonation(Donation donation)
  {
    if (donation != null)
    {
      this.donationControl = new EntityControlInstance<Donation>(this.owner.getInstance().getEntityControl(Donation.class), donation);
    }
    else
    {
      this.donationControl = null;
    }
    
    this.refreshContent();

    this.donationBidsPanel.setDonationControl(this.donationControl);
  }

  @Override
  public boolean confirmClose()
  {
    return true;
  }
  
  @Override
  public void refreshContent()
  {
    if (this.donationControl != null)
    {
      this.donationControl.refreshInstance();
    }
    
    if (this.donationControl != null)
    {
      if (!this.donationControl.isValid())
      {
        JOptionPane.showMessageDialog(this, "Error, This donation no longer exists", "Not Found", JOptionPane.ERROR_MESSAGE);
        this.owner.removeTab(this);
        return;
      }
      
      Donation result = this.donationControl.getInstance();

      this.amountField.setText(result.getAmount().toString());
      this.timeField.setText(result.getTimeReceived().toString());
      
      this.amountField.setEnabled(true);
      this.commentTextArea.setEnabled(true);
      this.commentTextArea.setEditable(true);
      this.amountField.setEditable(true);

      this.domainIdField.setText(result.getDomain().toString() + ":" + result.getDomainId());
      this.donorSelector.setEntity(result.getDonor());
      this.commentTextArea.setText(result.getComment());
      this.bidStateComboBox.setEnabled(true);
      this.bidStateComboBox.setSelectedItem(result.getBidState());
      this.readStateComboBox.setEnabled(true);
      this.readStateComboBox.setSelectedItem(result.getReadState());
      this.commentStateComboBox.setEnabled(true);
      this.commentStateComboBox.setSelectedItem(result.getCommentState());
      this.refreshButton.setEnabled(true);
      this.saveButton.setEnabled(true);
      
      this.donationBidsPanel.redrawContent();

      this.setHeaderText(result.toString());
    }
    else
    {
      this.amountField.setEnabled(false);
      this.amountField.setText("");
      this.commentTextArea.setEnabled(false);
      this.commentTextArea.setText("");
      this.refreshButton.setEnabled(false);
      this.saveButton.setEnabled(false);
      
      this.domainIdField.setText("");
      this.donorSelector.setEntity(null);
      this.bidStateComboBox.setEnabled(false);
      this.readStateComboBox.setEnabled(false);
      this.commentStateComboBox.setEnabled(false);
      this.donationBidsPanel.redrawContent();
      this.deleteButton.setEnabled(false);
    }
  }
  
  public void redrawContent()
  {
    if (this.donationControl != null)
    {
      if (!this.donationControl.isValid())
      {
        JOptionPane.showMessageDialog(this, "Error, This donation no longer exists", "Not Found", JOptionPane.ERROR_MESSAGE);
        this.owner.removeTab(this);
        return;
      }
    }
    else
    {
      this.amountField.setEnabled(false);
      this.amountField.setText("");
      this.commentTextArea.setEnabled(false);
      this.commentTextArea.setText("");
      this.refreshButton.setEnabled(false);
      this.saveButton.setEnabled(false);
      
      this.domainIdField.setText("");
      this.donorSelector.setEntity(null);
      this.bidStateComboBox.setEnabled(false);
      this.readStateComboBox.setEnabled(false);
      this.commentStateComboBox.setEnabled(false);
      this.donationBidsPanel.redrawContent();
      this.deleteButton.setEnabled(false);
    }
  }
  
  public void saveContent()
  {
    Donation data = this.donationControl.getInstance();
    data.setAmount(new BigDecimal(this.amountField.getText()));
    data.setComment(this.commentTextArea.getText());
    data.setCommentState((DonationCommentState)this.commentStateComboBox.getSelectedItem());
    data.setReadState((DonationReadState)this.readStateComboBox.getSelectedItem());
    data.setBidState((DonationBidState)this.bidStateComboBox.getSelectedItem());
    data.setDonor(this.donorSelector.getEntity());
    
    this.donationControl.saveInstance();
    
    this.redrawContent();
  }
}
