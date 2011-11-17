package pheidip.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pheidip.logic.DonationSearch;
import pheidip.objects.Donation;
import pheidip.objects.DonationSearchParams;
import pheidip.objects.Donor;
import pheidip.util.FormatUtils;

import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class DonationSearchDialog extends JDialog
{
  private DonationSearch searcher;
  private ActionHandler actionHandler;
  private JTextField donorField;
  private TimeControl donatedAfterField;
  private TimeControl donatedBeforeField;
  private JFormattedTextField amountAboveField;
  private JFormattedTextField amountBelowField;
  private JCheckBox onlyIfUnreadCheckBox;
  private JCheckBox onlyIfBidPendingCheckBox;
  private JList donationList;
  private JScrollPane scrollPane;
  private JButton browseDonorButton;
  
  private Donor currentDonor = null;
  private Donation result;
  private JButton okButton;
  private JButton cancelButton;
  private JCheckBox donorCheckBox;
  private JCheckBox donatedAfterCheckBox;
  private JCheckBox donatedBeforeCheckBox;
  private JCheckBox amountAboveCheckBox;
  private JCheckBox amountBelowCheckBox;
  private JLabel lblDonor;
  private JLabel lblDonatedAfter;
  private JLabel lblDonatedBefore;
  private JLabel lblAmountAbove;
  private JLabel lblAmountBelow;
  private JLabel lblOnlyIfUnread;
  private JLabel lblOnlyIfPending;
  private JButton searchButton;
  private FocusTraversalManager tabOrder;
  
  private void initializeGUI()
  {
    setBounds(100, 100, 583, 300);
    getContentPane().setLayout(new BorderLayout(0, 0));
    JPanel panel = new JPanel();
    panel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(panel, BorderLayout.CENTER);
    GridBagLayout gbl_panel = new GridBagLayout();
    gbl_panel.columnWidths = new int[]{107, 23, 113, 76, 85, 80, 0};
    gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
    gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
    panel.setLayout(gbl_panel);
    
    lblDonor = new JLabel("Donor:");
    GridBagConstraints gbc_lblDonor = new GridBagConstraints();
    gbc_lblDonor.anchor = GridBagConstraints.EAST;
    gbc_lblDonor.insets = new Insets(0, 0, 5, 5);
    gbc_lblDonor.gridx = 0;
    gbc_lblDonor.gridy = 0;
    panel.add(lblDonor, gbc_lblDonor);
    
    donorCheckBox = new JCheckBox("");
    donorCheckBox.setHorizontalAlignment(SwingConstants.TRAILING);
    GridBagConstraints gbc_donorCheckBox = new GridBagConstraints();
    gbc_donorCheckBox.anchor = GridBagConstraints.EAST;
    gbc_donorCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_donorCheckBox.gridx = 1;
    gbc_donorCheckBox.gridy = 0;
    panel.add(donorCheckBox, gbc_donorCheckBox);
    
    donorField = new JTextField();
    donorField.setEditable(false);
    GridBagConstraints gbc_donorField = new GridBagConstraints();
    gbc_donorField.insets = new Insets(0, 0, 5, 5);
    gbc_donorField.fill = GridBagConstraints.HORIZONTAL;
    gbc_donorField.gridx = 2;
    gbc_donorField.gridy = 0;
    panel.add(donorField, gbc_donorField);
    donorField.setColumns(10);
    
    browseDonorButton = new JButton("Browse...");
    GridBagConstraints gbc_browseDonorButton = new GridBagConstraints();
    gbc_browseDonorButton.insets = new Insets(0, 0, 5, 5);
    gbc_browseDonorButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_browseDonorButton.gridx = 3;
    gbc_browseDonorButton.gridy = 0;
    panel.add(browseDonorButton, gbc_browseDonorButton);
    
    scrollPane = new JScrollPane();
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.gridwidth = 2;
    gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
    gbc_scrollPane.gridheight = 9;
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridx = 4;
    gbc_scrollPane.gridy = 0;
    panel.add(scrollPane, gbc_scrollPane);
    
    donationList = new JList();
    scrollPane.setViewportView(donationList);
    
    lblDonatedAfter = new JLabel("Donated After:");
    GridBagConstraints gbc_lblDonatedAfter = new GridBagConstraints();
    gbc_lblDonatedAfter.anchor = GridBagConstraints.EAST;
    gbc_lblDonatedAfter.insets = new Insets(0, 0, 5, 5);
    gbc_lblDonatedAfter.gridx = 0;
    gbc_lblDonatedAfter.gridy = 1;
    panel.add(lblDonatedAfter, gbc_lblDonatedAfter);
    
    donatedAfterCheckBox = new JCheckBox("");
    donatedAfterCheckBox.setSelected(false);
    GridBagConstraints gbc_donatedAfterCheckBox = new GridBagConstraints();
    gbc_donatedAfterCheckBox.anchor = GridBagConstraints.EAST;
    gbc_donatedAfterCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_donatedAfterCheckBox.gridx = 1;
    gbc_donatedAfterCheckBox.gridy = 1;
    panel.add(donatedAfterCheckBox, gbc_donatedAfterCheckBox);
    
    donatedAfterField = new TimeControl();
    GridBagConstraints gbc_donatedAfterField = new GridBagConstraints();
    gbc_donatedAfterField.insets = new Insets(0, 0, 5, 5);
    gbc_donatedAfterField.fill = GridBagConstraints.HORIZONTAL;
    gbc_donatedAfterField.gridx = 2;
    gbc_donatedAfterField.gridy = 1;
    panel.add(donatedAfterField, gbc_donatedAfterField);
    
    lblDonatedBefore = new JLabel("Donated Before:");
    GridBagConstraints gbc_lblDonatedBefore = new GridBagConstraints();
    gbc_lblDonatedBefore.anchor = GridBagConstraints.EAST;
    gbc_lblDonatedBefore.insets = new Insets(0, 0, 5, 5);
    gbc_lblDonatedBefore.gridx = 0;
    gbc_lblDonatedBefore.gridy = 2;
    panel.add(lblDonatedBefore, gbc_lblDonatedBefore);
    
    donatedBeforeCheckBox = new JCheckBox("");
    GridBagConstraints gbc_donatedBeforeCheckBox = new GridBagConstraints();
    gbc_donatedBeforeCheckBox.anchor = GridBagConstraints.EAST;
    gbc_donatedBeforeCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_donatedBeforeCheckBox.gridx = 1;
    gbc_donatedBeforeCheckBox.gridy = 2;
    panel.add(donatedBeforeCheckBox, gbc_donatedBeforeCheckBox);
    
    donatedBeforeField = new TimeControl();
    GridBagConstraints gbc_donatedBeforeField = new GridBagConstraints();
    gbc_donatedBeforeField.insets = new Insets(0, 0, 5, 5);
    gbc_donatedBeforeField.fill = GridBagConstraints.HORIZONTAL;
    gbc_donatedBeforeField.gridx = 2;
    gbc_donatedBeforeField.gridy = 2;
    panel.add(donatedBeforeField, gbc_donatedBeforeField);
    
    lblAmountAbove = new JLabel("Amount Above:");
    GridBagConstraints gbc_lblAmountAbove = new GridBagConstraints();
    gbc_lblAmountAbove.anchor = GridBagConstraints.EAST;
    gbc_lblAmountAbove.insets = new Insets(0, 0, 5, 5);
    gbc_lblAmountAbove.gridx = 0;
    gbc_lblAmountAbove.gridy = 3;
    panel.add(lblAmountAbove, gbc_lblAmountAbove);
    
    amountAboveCheckBox = new JCheckBox("");
    GridBagConstraints gbc_amountAboveCheckBox = new GridBagConstraints();
    gbc_amountAboveCheckBox.anchor = GridBagConstraints.EAST;
    gbc_amountAboveCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_amountAboveCheckBox.gridx = 1;
    gbc_amountAboveCheckBox.gridy = 3;
    panel.add(amountAboveCheckBox, gbc_amountAboveCheckBox);
    
    amountAboveField = new JFormattedTextField(FormatUtils.getMoneyFormat());
    amountAboveField.setHorizontalAlignment(SwingConstants.TRAILING);
    GridBagConstraints gbc_amountAboveField = new GridBagConstraints();
    gbc_amountAboveField.insets = new Insets(0, 0, 5, 5);
    gbc_amountAboveField.fill = GridBagConstraints.HORIZONTAL;
    gbc_amountAboveField.gridx = 2;
    gbc_amountAboveField.gridy = 3;
    panel.add(amountAboveField, gbc_amountAboveField);
    amountAboveField.setColumns(10);
    
    lblAmountBelow = new JLabel("Amount Below:");
    GridBagConstraints gbc_lblAmountBelow = new GridBagConstraints();
    gbc_lblAmountBelow.anchor = GridBagConstraints.EAST;
    gbc_lblAmountBelow.insets = new Insets(0, 0, 5, 5);
    gbc_lblAmountBelow.gridx = 0;
    gbc_lblAmountBelow.gridy = 4;
    panel.add(lblAmountBelow, gbc_lblAmountBelow);
    
    amountBelowCheckBox = new JCheckBox("");
    GridBagConstraints gbc_amountBelowCheckBox = new GridBagConstraints();
    gbc_amountBelowCheckBox.anchor = GridBagConstraints.EAST;
    gbc_amountBelowCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_amountBelowCheckBox.gridx = 1;
    gbc_amountBelowCheckBox.gridy = 4;
    panel.add(amountBelowCheckBox, gbc_amountBelowCheckBox);
    
    amountBelowField = new JFormattedTextField(FormatUtils.getMoneyFormat());
    amountBelowField.setHorizontalAlignment(SwingConstants.TRAILING);
    GridBagConstraints gbc_amountBelowField = new GridBagConstraints();
    gbc_amountBelowField.insets = new Insets(0, 0, 5, 5);
    gbc_amountBelowField.fill = GridBagConstraints.HORIZONTAL;
    gbc_amountBelowField.gridx = 2;
    gbc_amountBelowField.gridy = 4;
    panel.add(amountBelowField, gbc_amountBelowField);
    amountBelowField.setColumns(10);
    
    lblOnlyIfUnread = new JLabel("Only if Unread:");
    GridBagConstraints gbc_lblOnlyIfUnread = new GridBagConstraints();
    gbc_lblOnlyIfUnread.anchor = GridBagConstraints.EAST;
    gbc_lblOnlyIfUnread.insets = new Insets(0, 0, 5, 5);
    gbc_lblOnlyIfUnread.gridx = 0;
    gbc_lblOnlyIfUnread.gridy = 5;
    panel.add(lblOnlyIfUnread, gbc_lblOnlyIfUnread);
    
    onlyIfUnreadCheckBox = new JCheckBox("");
    GridBagConstraints gbc_onlyIfUnreadCheckBox = new GridBagConstraints();
    gbc_onlyIfUnreadCheckBox.anchor = GridBagConstraints.EAST;
    gbc_onlyIfUnreadCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_onlyIfUnreadCheckBox.gridx = 1;
    gbc_onlyIfUnreadCheckBox.gridy = 5;
    panel.add(onlyIfUnreadCheckBox, gbc_onlyIfUnreadCheckBox);
    
    lblOnlyIfPending = new JLabel("Only if Pending Bid:");
    GridBagConstraints gbc_lblOnlyIfPending = new GridBagConstraints();
    gbc_lblOnlyIfPending.anchor = GridBagConstraints.EAST;
    gbc_lblOnlyIfPending.insets = new Insets(0, 0, 5, 5);
    gbc_lblOnlyIfPending.gridx = 0;
    gbc_lblOnlyIfPending.gridy = 6;
    panel.add(lblOnlyIfPending, gbc_lblOnlyIfPending);
    
    onlyIfBidPendingCheckBox = new JCheckBox("");
    GridBagConstraints gbc_onlyIfBidPendingCheckBox = new GridBagConstraints();
    gbc_onlyIfBidPendingCheckBox.anchor = GridBagConstraints.EAST;
    gbc_onlyIfBidPendingCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_onlyIfBidPendingCheckBox.gridx = 1;
    gbc_onlyIfBidPendingCheckBox.gridy = 6;
    panel.add(onlyIfBidPendingCheckBox, gbc_onlyIfBidPendingCheckBox);
    
    searchButton = new JButton("Search");
    GridBagConstraints gbc_searchButton = new GridBagConstraints();
    gbc_searchButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_searchButton.insets = new Insets(0, 0, 5, 5);
    gbc_searchButton.gridx = 3;
    gbc_searchButton.gridy = 8;
    panel.add(searchButton, gbc_searchButton);
    
    okButton = new JButton("OK");
    GridBagConstraints gbc_okButton = new GridBagConstraints();
    gbc_okButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_okButton.insets = new Insets(0, 0, 0, 5);
    gbc_okButton.gridx = 3;
    gbc_okButton.gridy = 9;
    panel.add(okButton, gbc_okButton);
    
    cancelButton = new JButton("Cancel");
    GridBagConstraints gbc_cancelButton = new GridBagConstraints();
    gbc_cancelButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_cancelButton.insets = new Insets(0, 0, 0, 5);
    gbc_cancelButton.gridx = 4;
    gbc_cancelButton.gridy = 9;
    panel.add(cancelButton, gbc_cancelButton);
    
  }
  
  private class ActionHandler implements ActionListener, ChangeListener
  {
    private void runEvent(Object source)
    {
      try
      {
        if (source == browseDonorButton)
        {
          openDonorSearch();
        }
        else if (source == okButton)
        {
          returnValue();
        }
        else if (source == cancelButton)
        {
          cancelDialog();
        }
        else if (source == searchButton)
        {
          runSearch();
        }
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(DonationSearchDialog.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    
    public void actionPerformed(ActionEvent ev)
    {
      runEvent(ev.getSource());
    }

    @Override
    public void stateChanged(ChangeEvent ev)
    {
      runEvent(ev.getSource());
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    this.browseDonorButton.addActionListener(this.actionHandler);
    this.donatedAfterField.addChangeListener(this.actionHandler);
    this.donatedAfterField.addChangeListener(this.actionHandler);
    this.onlyIfBidPendingCheckBox.addActionListener(this.actionHandler);
    this.onlyIfUnreadCheckBox.addActionListener(this.actionHandler);
    this.okButton.addActionListener(this.actionHandler);
    this.cancelButton.addActionListener(this.actionHandler);
    this.donorCheckBox.addActionListener(this.actionHandler);
    this.amountAboveCheckBox.addActionListener(this.actionHandler);
    this.amountBelowCheckBox.addActionListener(this.actionHandler);
    this.donatedAfterCheckBox.addActionListener(this.actionHandler);
    this.donatedBeforeCheckBox.addActionListener(this.actionHandler);
    this.searchButton.addActionListener(this.actionHandler);
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {
      this.donorCheckBox,
      this.browseDonorButton,
      this.donatedAfterCheckBox,
      this.donatedAfterField,
      this.donatedBeforeCheckBox,
      this.amountAboveCheckBox,
      this.amountAboveField,
      this.amountBelowCheckBox,
      this.amountBelowField,
      this.searchButton,
      this.okButton,
      this.cancelButton,
    });
    
    this.setFocusTraversalPolicy(this.tabOrder);
  }

  public DonationSearchDialog(JFrame owner, DonationSearch searcher)
  {
    super(owner, true);
    this.searcher = searcher;

    this.initializeGUI();
    this.initializeGUIEvents();
  }
  
  public Donation getResult()
  {
    return this.result;
  }
  
  private void returnValue()
  {
    this.result = (Donation) this.donationList.getSelectedValue();
    
    if (this.result == null)
    {
      JOptionPane.showMessageDialog(this, "No donation is selected.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    else
    {
      this.closeDialog();
    }
  }
  
  private void cancelDialog()
  {
    this.result = null;
    this.closeDialog();
  }
  
  private void closeDialog()
  {
    this.setVisible(false);
    this.dispose();
  }
  
  private void runSearch()
  {
    DonationSearchParams params = new DonationSearchParams();
    
    params.donor = this.donorCheckBox.isSelected() ? this.currentDonor : null;
    params.loTime = this.donatedAfterCheckBox.isSelected() ? (Date)this.donatedAfterField.getValue() : null;
    params.hiTime = this.donatedBeforeCheckBox.isSelected() ? (Date)this.donatedBeforeField.getValue() : null;
    params.loAmount = this.amountAboveCheckBox.isSelected() ? FormatUtils.getNumberOrNull(this.amountAboveField.getText()) : null;
    params.hiAmount = this.amountBelowCheckBox.isSelected() ? FormatUtils.getNumberOrNull(this.amountBelowField.getText()) : null;
    params.onlyIfUnread = this.onlyIfUnreadCheckBox.isSelected();
    params.onlyIfUnbid = this.onlyIfBidPendingCheckBox.isSelected();
    
    List<Donation> filtered = this.searcher.searchDonations(params);
    
    DefaultListModel listData = new DefaultListModel();
    
    for (Donation d : filtered)
    {
      listData.addElement(d);
    }
    
    this.donationList.setModel(listData);
  }
  
  private void openDonorSearch()
  {
    DonorSearchDialog dialog = new DonorSearchDialog(this, this.searcher.createDonorSearch());
    
    dialog.setVisible(true);
    
    if (dialog.getResult() != null)
    {
      this.currentDonor = dialog.getResult();
      this.donorField.setText(this.currentDonor.toString());
    }
  }
}
