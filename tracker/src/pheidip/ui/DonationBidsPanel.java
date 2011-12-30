package pheidip.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import pheidip.logic.DonationControl;
import pheidip.objects.BidType;
import pheidip.objects.ChallengeBid;
import pheidip.objects.ChoiceBid;
import pheidip.objects.DonationBid;
import pheidip.util.FormatUtils;
import pheidip.util.StringUtils;

import java.awt.GridBagLayout;
import javax.swing.JButton;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class DonationBidsPanel extends JPanel
{
  private ActionHandler actionHandler;
  private DonationControl control;
  private JTable bidTable;
  private JScrollPane bidScrollPane;
  private MainWindow owner;
  private JButton attachBidButton;
  private JButton changeAmountButton;
  private JButton deleteBidButton;
  private List<DonationBid> cachedDonationBids;
  private FocusTraversalManager tabOrder;
  private JButton openBidButton;
  
  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{107, 100, 57, 97, 0, 88, 0};
    gridBagLayout.rowHeights = new int[]{0, 39, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    attachBidButton = new JButton("Attach Bid");
    GridBagConstraints gbc_attachBidButton = new GridBagConstraints();
    gbc_attachBidButton.insets = new Insets(0, 0, 5, 5);
    gbc_attachBidButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_attachBidButton.gridx = 0;
    gbc_attachBidButton.gridy = 0;
    add(attachBidButton, gbc_attachBidButton);
    
    changeAmountButton = new JButton("Change Amount");
    GridBagConstraints gbc_changeAmountButton = new GridBagConstraints();
    gbc_changeAmountButton.insets = new Insets(0, 0, 5, 5);
    gbc_changeAmountButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_changeAmountButton.gridx = 1;
    gbc_changeAmountButton.gridy = 0;
    add(changeAmountButton, gbc_changeAmountButton);
    
    openBidButton = new JButton("Open Bid");
    GridBagConstraints gbc_openBidButton = new GridBagConstraints();
    gbc_openBidButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_openBidButton.insets = new Insets(0, 0, 5, 5);
    gbc_openBidButton.gridx = 3;
    gbc_openBidButton.gridy = 0;
    add(openBidButton, gbc_openBidButton);
    
    deleteBidButton = new JButton("Delete Bid");
    GridBagConstraints gbc_deleteBidButton = new GridBagConstraints();
    gbc_deleteBidButton.insets = new Insets(0, 0, 5, 0);
    gbc_deleteBidButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_deleteBidButton.gridx = 5;
    gbc_deleteBidButton.gridy = 0;
    add(deleteBidButton, gbc_deleteBidButton);
    
    bidScrollPane = new JScrollPane();
    GridBagConstraints gbc_bidScrollPane = new GridBagConstraints();
    gbc_bidScrollPane.gridwidth = 6;
    gbc_bidScrollPane.fill = GridBagConstraints.BOTH;
    gbc_bidScrollPane.gridx = 0;
    gbc_bidScrollPane.gridy = 1;
    add(bidScrollPane, gbc_bidScrollPane);
    
    bidTable = new JTable();
    bidTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    bidScrollPane.setViewportView(bidTable);
  }
  
  private class ActionHandler extends MouseAdapter implements ActionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      try
      {
        if (event.getSource() == attachBidButton)
        {
          attachNewBid();
        }
        else if (event.getSource() == changeAmountButton)
        {
          changeCurrentBidAmount();
        }
        else if (event.getSource() == deleteBidButton)
        {
          removeCurrentBid();
        }
        else if (event.getSource() == openBidButton)
        {
          openSelectedBid();
        }
      }
      catch (Exception e)
      {
        owner.report(e);
      }
    }
    
    @Override
    public void mouseClicked(MouseEvent event)
    {
      try
      {
        if (event.getSource() == DonationBidsPanel.this.bidTable)
        {
          if (event.getClickCount() == 2)
          {
            DonationBidsPanel.this.openSelectedBid();
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
    
    this.attachBidButton.addActionListener(this.actionHandler);
    this.changeAmountButton.addActionListener(this.actionHandler);
    this.deleteBidButton.addActionListener(this.actionHandler);
    this.bidTable.addMouseListener(this.actionHandler);
    this.openBidButton.addActionListener(this.actionHandler);
    this.bidTable.addKeyListener(new TabTraversalKeyListener(this.bidTable)); 
    
    Component[] tabArray = new Component[]
    {
      this.attachBidButton,
      this.changeAmountButton,
      this.openBidButton,
      this.deleteBidButton,
      this.bidTable,
    };
    
    this.tabOrder = new FocusTraversalManager(tabArray);
    this.setFocusTraversalPolicy(this.tabOrder);
    this.setFocusTraversalPolicyProvider(true);
    this.setFocusCycleRoot(true);
  }
  
  public DonationBidsPanel(MainWindow owner, DonationControl control)
  {
    this.owner = owner;
    this.control = control;
    
    this.initializeGUI();
    this.initializeGUIEvents();
  }
  
  private DonationBid getSelectedDonationBid()
  {
    int selectedRow = this.bidTable.getSelectedRow();
    if (selectedRow != -1)
    {
      return this.cachedDonationBids.get(selectedRow);
    }
    else
    {
      return null;
    }
  }
  
  private void removeCurrentBid()
  {
    DonationBid selected = this.getSelectedDonationBid();
    
    if (selected != null)
    {
      int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this bid?", "Confirm delete", JOptionPane.YES_NO_OPTION);
      
      if (result == JOptionPane.YES_OPTION)
      {
        this.control.removeBid(selected);
        
        this.refreshContent();
      }
    }
  }

  public void openSelectedBid()
  {
    int selectedRow = this.bidTable.getSelectedRow();
      
    if (selectedRow != -1)
    {
      DonationBid b = this.cachedDonationBids.get(selectedRow);
      
      if (b.getType() == BidType.CHALLENGE)
      {
        this.owner.openChallengeTab(((ChallengeBid)b).getChallenge().getId());
      }
      else if (b.getType() == BidType.CHOICE)
      {
        this.owner.openChoiceTab(((ChoiceBid)b).getOption().getChoice().getId());
      }
    }
  }
  
  public void setControl(DonationControl control)
  {
    this.control = control;
    this.refreshContent();
  }
  
  void refreshContent()
  {
    if (this.control != null)
    {
      this.attachBidButton.setEnabled(true);
      this.changeAmountButton.setEnabled(true);
      this.deleteBidButton.setEnabled(true);
      this.openBidButton.setEnabled(true);
      CustomTableModel tableData = new CustomTableModel(
          new String[]{"Bid", "Amount"}, 0);
      
      this.cachedDonationBids = this.control.getAttachedBids();
      
      for (DonationBid b : this.cachedDonationBids)
      {
        tableData.addRow(
            new Object[]
            {
                this.control.getDonationBidDisplayName(b),
                b.getAmount()
            });
      }
      
      this.bidTable.setModel(tableData);
    }
    else
    {
      this.attachBidButton.setEnabled(false);
      this.openBidButton.setEnabled(false);
      this.changeAmountButton.setEnabled(false);
      this.deleteBidButton.setEnabled(false);
      this.bidTable.setModel(new CustomTableModel(
          new String[]{"Bid", "Amount"}, 0));
    }

  }

  private void changeCurrentBidAmount()
  {
    DonationBid selected = this.getSelectedDonationBid();
    
    if (selected != null)
    {
      String amount = FormattedInputDialog.showDialog(this.owner, "Please enter a new amount to bid.", "Enter amount", FormatUtils.getMoneyFormat(), selected.getAmount().toString());
      
      if (!StringUtils.isEmptyOrNull(amount))
      {
        this.control.updateDonationBidAmount(selected, new BigDecimal(amount));
        this.refreshContent();
      }
    }
  }

  private void attachNewBid()
  {
    DonationBidSearchDialog dialog = this.owner.openDonationBidSearch();
    
    dialog.setVisible(true);
    
    if (dialog.getSelectionType() != null)
    {
      String amount = FormattedInputDialog.showDialog(this.owner, "Please enter a new amount to bid.", "Enter amount", FormatUtils.getMoneyFormat(), this.control.getTotalAvailiable().toString());
      
      if (!StringUtils.isEmptyOrNull(amount))
      {
        if (dialog.getSelectionType() == BidType.CHOICE)
        {
          this.control.attachNewChoiceBid(dialog.getSelectedOption(), new BigDecimal(amount));
        }
        else
        {
          this.control.attachNewChallengeBid(dialog.getSelectedChallenge(), new BigDecimal(amount));
        }
        this.refreshContent();
      }
    }
  }
}
