package pheidip.ui;

import pheidip.logic.DonationControl;
import pheidip.objects.Donation;
import pheidip.objects.Donor;
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
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;

@SuppressWarnings("serial")
public class DonationPanel extends EntityPanel
{
  private DonationControl donationControl;
  private JFormattedTextField amountField;
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
  private JCheckBox markAsReadCheckBox;
  private DonationBidsPanel donationBidsPanel;
  
  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{66, 113, 105, 90, 104, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 49, 0, 0};
    gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
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
    
    deleteButton = new JButton("Delete Donation");
    GridBagConstraints gbc_deleteButton = new GridBagConstraints();
    gbc_deleteButton.insets = new Insets(0, 0, 5, 0);
    gbc_deleteButton.gridx = 5;
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
    gbc_domainIdField.gridwidth = 2;
    gbc_domainIdField.insets = new Insets(0, 0, 5, 5);
    gbc_domainIdField.fill = GridBagConstraints.HORIZONTAL;
    gbc_domainIdField.gridx = 1;
    gbc_domainIdField.gridy = 1;
    add(domainIdField, gbc_domainIdField);
    domainIdField.setColumns(10);
    
    commentScrollPane = new JScrollPane();
    commentScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
    gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
    gbc_scrollPane_1.gridheight = 7;
    gbc_scrollPane_1.gridwidth = 3;
    gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
    gbc_scrollPane_1.gridx = 3;
    gbc_scrollPane_1.gridy = 1;
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
        gbc_openDonorButton.gridx = 1;
        gbc_openDonorButton.gridy = 4;
        add(openDonorButton, gbc_openDonorButton);
        
        markAsReadCheckBox = new JCheckBox("Mark As Read");
        markAsReadCheckBox.setHorizontalAlignment(SwingConstants.TRAILING);
        GridBagConstraints gbc_markAsReadCheckBox = new GridBagConstraints();
        gbc_markAsReadCheckBox.insets = new Insets(0, 0, 5, 5);
        gbc_markAsReadCheckBox.gridx = 2;
        gbc_markAsReadCheckBox.gridy = 4;
        add(markAsReadCheckBox, gbc_markAsReadCheckBox);
    
        refreshButton = new JButton("Refresh");
        GridBagConstraints gbc_refreshButton = new GridBagConstraints();
        gbc_refreshButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_refreshButton.insets = new Insets(0, 0, 5, 5);
        gbc_refreshButton.gridx = 1;
        gbc_refreshButton.gridy = 6;
        add(refreshButton, gbc_refreshButton);
    
    saveButton = new JButton("Save");
    GridBagConstraints gbc_saveButton = new GridBagConstraints();
    gbc_saveButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_saveButton.insets = new Insets(0, 0, 5, 5);
    gbc_saveButton.gridx = 2;
    gbc_saveButton.gridy = 6;
    add(saveButton, gbc_saveButton);
    
    donationBidsPanel = new DonationBidsPanel(this.owner, this.donationControl);
    GridBagConstraints gbc_donationBidsPanel = new GridBagConstraints();
    gbc_donationBidsPanel.gridwidth = 6;
    gbc_donationBidsPanel.fill = GridBagConstraints.BOTH;
    gbc_donationBidsPanel.gridx = 0;
    gbc_donationBidsPanel.gridy = 8;
    add(donationBidsPanel, gbc_donationBidsPanel);
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
    
    List<Component> tabArray = new ArrayList<Component>();
    tabArray.add(this.amountField);
    tabArray.add(this.openDonorButton);
    tabArray.add(this.refreshButton);
    tabArray.add(this.saveButton);
    tabArray.add(this.commentTextArea);
    
    for (Component c : this.donationBidsPanel.getTabOrder())
    {
      tabArray.add(c);
    }

    this.tabOrder = new FocusTraversalManager(tabArray.toArray(new Component[tabArray.size()]));
    this.setFocusTraversalPolicy(this.tabOrder);
    this.setFocusTraversalPolicyProvider(true);
    this.setFocusCycleRoot(true);
  }

  @Override
  public void deleteContent()
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
    this.owner = owner;
    this.donationControl = control;
    
    this.initializeGUI();
    this.initializeGUIEvents();
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
    this.donationControl.refreshData();
    this.redrawContent();
  }
  
  public void redrawContent()
  {
    Donation result = this.donationControl.getData();
    
    if (result != null)
    {
      Donor donor = result.getDonor();
      
      this.amountField.setText(result.getAmount().toString());
      this.timeField.setText(result.getTimeReceived().toString());
      
      if (this.donationControl.allowUpdateData())
      {
        this.amountField.setEditable(true);
        this.commentTextArea.setEditable(true);
      }
      else
      {
        this.amountField.setEditable(false);
        this.commentTextArea.setEditable(false);
      }
      
      this.domainIdField.setText(result.getDomainString());
      this.donorField.setText(donor.toString());
      this.commentTextArea.setText(result.getComment());
      this.markAsReadCheckBox.setSelected(result.isMarkedAsRead());

      this.donationBidsPanel.refreshContent();

      this.setHeaderText(result.getDomainString());
    }
  }
  
  public void saveContent()
  {
    if (this.donationControl.allowUpdateData())
    {
      Donation d = this.donationControl.getData();
      
      d.setAmount(new BigDecimal(this.amountField.getText()));
      d.setComment(this.commentTextArea.getText());
      d.markAsRead(this.markAsReadCheckBox.isSelected());
      
      this.donationControl.updateData(d);
    }
    
    this.refreshContent();
  }

  private void openDonor()
  {
    Donor donor = this.donationControl.getDonationDonor();
    this.owner.openDonorTab(donor.getId());
  }
}
