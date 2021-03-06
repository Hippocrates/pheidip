package pheidip.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import pheidip.logic.EntityControlInstance;
import pheidip.objects.BidType;
import pheidip.objects.Challenge;
import pheidip.objects.ChallengeBid;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceBid;
import pheidip.objects.ChoiceOption;
import pheidip.objects.Donation;
import pheidip.objects.DonationBid;
import pheidip.objects.SpeedRun;
import pheidip.util.FormatUtils;
import pheidip.util.StringUtils;

import java.awt.GridBagLayout;
import javax.swing.JButton;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import meta.reflect.MetaEntityReflector;

@SuppressWarnings("serial")
public class DonationBidsPanel extends JPanel
{
  private ActionHandler actionHandler;
  private EntityControlInstance<Donation> donationControl;
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
        e.printStackTrace();
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
  
  public DonationBidsPanel(MainWindow owner, EntityControlInstance<Donation> control)
  {
    this.owner = owner;

    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.setDonationControl(control);
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
        this.donationControl.getInstance().getBids().remove(selected);
        this.redrawContent();
      }
    }
  }

  public void openSelectedBid()
  {
    int selectedRow = this.bidTable.getSelectedRow();
      
    if (selectedRow != -1)
    {
      DonationBid b = this.cachedDonationBids.get(selectedRow);
      
      if (b.getBidType() == BidType.CHALLENGE)
      {
        this.owner.openChallengeTab(((ChallengeBid)b).getChallenge());
      }
      else if (b.getBidType() == BidType.CHOICE)
      {
        this.owner.openChoiceTab(((ChoiceBid)b).getChoiceOption().getChoice());
      }
    }
  }
  
  public void setDonationControl(EntityControlInstance<Donation> donationControl)
  {
    this.donationControl = donationControl;
    this.redrawContent();
  }
  
  private String getDonationBidDisplayName(DonationBid b)
  {
    if (b.getBidType() == BidType.CHOICE)
    {
      ChoiceBid c = (ChoiceBid) b;
      ChoiceOption option = c.getChoiceOption();
      Choice choice = option.getChoice();
      SpeedRun run = choice.getSpeedRun();
      return (run == null ? "" : run.getName() + " : ") + choice.getName() + " : " + option.getName();
    }
    else
    {
      ChallengeBid c = (ChallengeBid) b;
      Challenge challenge = c.getChallenge();
      SpeedRun run = challenge.getSpeedRun();
      return run.getName() + " : " + challenge.getName();
    }
  }
  
  void redrawContent()
  {
    if (this.donationControl != null)
    {
      this.attachBidButton.setEnabled(true);
      this.changeAmountButton.setEnabled(true);
      this.deleteBidButton.setEnabled(true);
      this.openBidButton.setEnabled(true);
      CustomTableModel tableData = new CustomTableModel(
          new String[]{"Bid", "Amount"}, 0);
      
      this.cachedDonationBids = new ArrayList<DonationBid>(this.donationControl.getInstance().getBids());
      
      for (DonationBid b : this.cachedDonationBids)
      {
        tableData.addRow(
            new Object[]
            {
                this.getDonationBidDisplayName(b),
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
        selected.setAmount(new BigDecimal(amount));
        this.redrawContent();
      }
    }
  }
  
  private static BigDecimal amountBid(Donation target)
  {
    BigDecimal sum = BigDecimal.ZERO;
    
    for (DonationBid bid : target.getBids())
    {
      sum = sum.add(bid.getAmount());
    }
    
    return sum;
  }
  
  private static BigDecimal getRemainingToBid(Donation target)
  {
    return target.getAmount().subtract(amountBid(target));
  }

  private void attachNewBid()
  {
    DonationBidSearchDialog dialog = this.owner.openDonationBidSearch();
    
    dialog.setVisible(true);
    
    if (dialog.getSelectionType() != null)
    {
      String amount = FormattedInputDialog.showDialog(this.owner, "Please enter a new amount to bid.", "Enter amount", FormatUtils.getMoneyFormat(), getRemainingToBid(this.donationControl.getInstance()).toString());
      
      if (!StringUtils.isEmptyOrNull(amount))
      {
        if (dialog.getSelectionType() == BidType.CHOICE)
        {
          ChoiceBid bid = new ChoiceBid();
          bid.setDonation(this.donationControl.getInstance());
          bid.setChoiceOption(dialog.getSelectedOption());
          bid.setAmount(new BigDecimal(amount));
          this.donationControl.getInstance().getBids().add(bid);
          this.donationControl.getControl().getDataAccess().saveInstance(MetaEntityReflector.getMetaEntity(ChoiceBid.class), bid);
        }
        else
        {
          ChallengeBid bid = new ChallengeBid();
          bid.setDonation(this.donationControl.getInstance());
          bid.setChallenge(dialog.getSelectedChallenge());
          bid.setAmount(new BigDecimal(amount));
          this.donationControl.getInstance().getBids().add(bid);
          this.donationControl.getControl().getDataAccess().saveInstance(MetaEntityReflector.getMetaEntity(ChallengeBid.class), bid);
        }
        this.redrawContent();
      }
    }
  }
}
