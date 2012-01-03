package pheidip.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pheidip.logic.DonationSearch;
import pheidip.objects.Donation;
import pheidip.objects.DonationSearchParams;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationReadState;
import pheidip.objects.DonationCommentState;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JFormattedTextField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;

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
  private JCheckBox bidStateCheckBox;
  private JCheckBox readStateCheckBox;
  private JList donationList;
  private JScrollPane scrollPane;
  private JButton browseDonorButton;
  
  private Donor currentDonor = null;
  private List<Donation> results;
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
  private JLabel lblCommentState;
  private JCheckBox commentStateCheckBox;
  private JComboBox bidStateComboBox;
  private JComboBox readStateComboBox;
  private JComboBox commentStateComboBox;
  private JButton prevButton;
  private JButton nextButton;
  
  private void initializeGUI()
  {
    //setBounds(100, 100, 583, 327);
    getContentPane().setLayout(new BorderLayout(0, 0));
    JPanel panel = new JPanel();
    panel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(panel, BorderLayout.CENTER);
    GridBagLayout gbl_panel = new GridBagLayout();
    gbl_panel.columnWidths = new int[]{107, 23, 113, 84, 101, 87, 0};
    gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
    gbc_scrollPane.gridheight = 10;
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
    
    lblOnlyIfUnread = new JLabel("Bid State:");
    GridBagConstraints gbc_lblOnlyIfUnread = new GridBagConstraints();
    gbc_lblOnlyIfUnread.anchor = GridBagConstraints.EAST;
    gbc_lblOnlyIfUnread.insets = new Insets(0, 0, 5, 5);
    gbc_lblOnlyIfUnread.gridx = 0;
    gbc_lblOnlyIfUnread.gridy = 5;
    panel.add(lblOnlyIfUnread, gbc_lblOnlyIfUnread);
    
    bidStateCheckBox = new JCheckBox("");
    GridBagConstraints gbc_bidStateCheckBox = new GridBagConstraints();
    gbc_bidStateCheckBox.anchor = GridBagConstraints.EAST;
    gbc_bidStateCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_bidStateCheckBox.gridx = 1;
    gbc_bidStateCheckBox.gridy = 5;
    panel.add(bidStateCheckBox, gbc_bidStateCheckBox);
    
    bidStateComboBox = new JComboBox(DonationBidState.values());
    bidStateComboBox.setSelectedItem(DonationBidState.PENDING);
    GridBagConstraints gbc_bidStateComboBox = new GridBagConstraints();
    gbc_bidStateComboBox.insets = new Insets(0, 0, 5, 5);
    gbc_bidStateComboBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_bidStateComboBox.gridx = 2;
    gbc_bidStateComboBox.gridy = 5;
    panel.add(bidStateComboBox, gbc_bidStateComboBox);
    
    lblOnlyIfPending = new JLabel("Read State:");
    GridBagConstraints gbc_lblOnlyIfPending = new GridBagConstraints();
    gbc_lblOnlyIfPending.anchor = GridBagConstraints.EAST;
    gbc_lblOnlyIfPending.insets = new Insets(0, 0, 5, 5);
    gbc_lblOnlyIfPending.gridx = 0;
    gbc_lblOnlyIfPending.gridy = 6;
    panel.add(lblOnlyIfPending, gbc_lblOnlyIfPending);
    
    readStateCheckBox = new JCheckBox("");
    GridBagConstraints gbc_readStateCheckBox = new GridBagConstraints();
    gbc_readStateCheckBox.anchor = GridBagConstraints.EAST;
    gbc_readStateCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_readStateCheckBox.gridx = 1;
    gbc_readStateCheckBox.gridy = 6;
    panel.add(readStateCheckBox, gbc_readStateCheckBox);
    
    readStateComboBox = new JComboBox(DonationReadState.values());
    bidStateComboBox.setSelectedItem(DonationReadState.PENDING);
    GridBagConstraints gbc_readStateComboBox = new GridBagConstraints();
    gbc_readStateComboBox.insets = new Insets(0, 0, 5, 5);
    gbc_readStateComboBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_readStateComboBox.gridx = 2;
    gbc_readStateComboBox.gridy = 6;
    panel.add(readStateComboBox, gbc_readStateComboBox);
    
    lblCommentState = new JLabel("Comment State:");
    GridBagConstraints gbc_lblCommentState = new GridBagConstraints();
    gbc_lblCommentState.insets = new Insets(0, 0, 5, 5);
    gbc_lblCommentState.gridx = 0;
    gbc_lblCommentState.gridy = 7;
    panel.add(lblCommentState, gbc_lblCommentState);
    
    commentStateCheckBox = new JCheckBox("");
    GridBagConstraints gbc_commentStateCheckBox = new GridBagConstraints();
    gbc_commentStateCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_commentStateCheckBox.gridx = 1;
    gbc_commentStateCheckBox.gridy = 7;
    panel.add(commentStateCheckBox, gbc_commentStateCheckBox);
    
    commentStateComboBox = new JComboBox(DonationCommentState.values());
    bidStateComboBox.setSelectedItem(DonationCommentState.PENDING);
    GridBagConstraints gbc_commentStateComboBox = new GridBagConstraints();
    gbc_commentStateComboBox.insets = new Insets(0, 0, 5, 5);
    gbc_commentStateComboBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_commentStateComboBox.gridx = 2;
    gbc_commentStateComboBox.gridy = 7;
    panel.add(commentStateComboBox, gbc_commentStateComboBox);
    
    searchButton = new JButton("Search");
    GridBagConstraints gbc_searchButton = new GridBagConstraints();
    gbc_searchButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_searchButton.insets = new Insets(0, 0, 5, 5);
    gbc_searchButton.gridx = 3;
    gbc_searchButton.gridy = 9;
    panel.add(searchButton, gbc_searchButton);
    
    prevButton = new JButton("Previous");
    prevButton.setEnabled(false);
    GridBagConstraints gbc_prevButton = new GridBagConstraints();
    gbc_prevButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_prevButton.insets = new Insets(0, 0, 5, 5);
    gbc_prevButton.gridx = 4;
    gbc_prevButton.gridy = 10;
    panel.add(prevButton, gbc_prevButton);
    
    nextButton = new JButton("Next");
    nextButton.setEnabled(false);
    GridBagConstraints gbc_nextButton = new GridBagConstraints();
    gbc_nextButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_nextButton.insets = new Insets(0, 0, 5, 0);
    gbc_nextButton.gridx = 5;
    gbc_nextButton.gridy = 10;
    panel.add(nextButton, gbc_nextButton);
    
    okButton = new JButton("OK");
    GridBagConstraints gbc_okButton = new GridBagConstraints();
    gbc_okButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_okButton.insets = new Insets(0, 0, 0, 5);
    gbc_okButton.gridx = 4;
    gbc_okButton.gridy = 11;
    panel.add(okButton, gbc_okButton);
    
    cancelButton = new JButton("Cancel");
    GridBagConstraints gbc_cancelButton = new GridBagConstraints();
    gbc_cancelButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_cancelButton.gridx = 5;
    gbc_cancelButton.gridy = 11;
    panel.add(cancelButton, gbc_cancelButton);
    
    this.pack();
  }
  
  private class ActionHandler implements ActionListener, ChangeListener, ListSelectionListener
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
        else if (source == nextButton)
        {
          moveNextResults();
        }
        else if (source == prevButton)
        {
          movePrevResults();
        }
        else if (source == donorCheckBox || 
            source == donatedAfterCheckBox ||
            source == donatedBeforeCheckBox ||
            source == amountAboveCheckBox ||
            source == amountBelowCheckBox ||
            source == bidStateCheckBox ||
            source == readStateCheckBox ||
            source == commentStateCheckBox)
        {
          updateUIState();
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

    @Override
    public void valueChanged(ListSelectionEvent ev)
    {
      if (ev.getSource() == donationList)
      {
        updateUIState();
      }
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    this.browseDonorButton.addActionListener(this.actionHandler);
    this.donatedAfterField.addChangeListener(this.actionHandler);
    this.donatedAfterField.addChangeListener(this.actionHandler);
    this.readStateCheckBox.addActionListener(this.actionHandler);
    this.bidStateCheckBox.addActionListener(this.actionHandler);
    this.okButton.addActionListener(this.actionHandler);
    this.cancelButton.addActionListener(this.actionHandler);
    this.donorCheckBox.addActionListener(this.actionHandler);
    this.amountAboveCheckBox.addActionListener(this.actionHandler);
    this.amountBelowCheckBox.addActionListener(this.actionHandler);
    this.donatedAfterCheckBox.addActionListener(this.actionHandler);
    this.donatedBeforeCheckBox.addActionListener(this.actionHandler);
    this.searchButton.addActionListener(this.actionHandler);
    this.nextButton.addActionListener(this.actionHandler);
    this.prevButton.addActionListener(this.actionHandler);
    this.donationList.addListSelectionListener(this.actionHandler);
    this.donorCheckBox.addActionListener(this.actionHandler);
    this.donatedAfterCheckBox.addActionListener(this.actionHandler);
    this.donatedBeforeCheckBox.addActionListener(this.actionHandler);
    this.amountAboveCheckBox.addActionListener(this.actionHandler);
    this.amountBelowCheckBox.addActionListener(this.actionHandler);
    this.bidStateCheckBox.addActionListener(this.actionHandler);
    this.readStateCheckBox.addActionListener(this.actionHandler);
    this.commentStateCheckBox.addActionListener(this.actionHandler);
    
    this.getRootPane().setDefaultButton(this.searchButton);
    
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
      this.bidStateCheckBox,
      this.bidStateComboBox,
      this.readStateCheckBox,
      this.readStateComboBox,
      this.commentStateCheckBox,
      this.commentStateComboBox,
      this.searchButton,
      this.donationList,
      this.prevButton,
      this.nextButton,
      this.okButton,
      this.cancelButton,
    });
    
    this.setFocusTraversalPolicy(this.tabOrder);
  }

  public DonationSearchDialog(JFrame owner, DonationSearch searcher)
  {
    super(owner, true);
    this.searcher = searcher;
    this.results = new ArrayList<Donation>();

    this.initializeGUI();
    this.initializeGUIEvents();
    this.updateUIState();
  }
  
  public Donation getResult()
  {
    return this.results == null ? null : this.results.size() > 0 ? this.results.iterator().next() : null;
  }
  
  public List<Donation> getResults()
  {
    return Collections.unmodifiableList(this.results);
  }
  
  private void returnValue()
  {
    if (this.donationList.getSelectedValue() == null)
    {
      JOptionPane.showMessageDialog(this, "No donation is selected.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    else
    {
      for (int i : this.donationList.getSelectedIndices())
      {
        this.results.add((Donation) this.donationList.getModel().getElementAt(i));
      }
      
      this.closeDialog();
    }
  }
  
  private void cancelDialog()
  {
    this.results = new ArrayList<Donation>();
    this.closeDialog();
  }
  
  private void closeDialog()
  {
    this.setVisible(false);
    this.dispose();
  }

  private void updateUIState()
  {
    this.okButton.setEnabled(!this.donationList.isSelectionEmpty());
    this.nextButton.setEnabled(this.searcher.hasNext());
    this.prevButton.setEnabled(this.searcher.hasPrev());
    
    this.browseDonorButton.setEnabled(this.donorCheckBox.isSelected());
    this.donatedBeforeField.setEnabled(this.donatedBeforeCheckBox.isSelected());
    this.donatedAfterField.setEnabled(this.donatedAfterCheckBox.isSelected());
    this.amountAboveField.setEnabled(this.amountAboveCheckBox.isSelected());
    this.amountBelowField.setEnabled(this.amountBelowCheckBox.isSelected());
    this.readStateComboBox.setEnabled(this.readStateCheckBox.isSelected());
    this.commentStateComboBox.setEnabled(this.commentStateCheckBox.isSelected());
    this.bidStateComboBox.setEnabled(this.bidStateCheckBox.isSelected());
  }
  
  private void moveNextResults()
  {
    this.fillList(this.searcher.getNext());
  }
  
  private void movePrevResults()
  {
    this.fillList(this.searcher.getPrev());
  }

  private void fillList(List<Donation> filtered)
  {
    DefaultListModel listData = new DefaultListModel();
    
    for (Donation d : filtered)
    {
      listData.addElement(d);
    }
    
    this.donationList.setModel(listData);
    updateUIState();
  }

  private void runSearch()
  {
    DonationSearchParams params = new DonationSearchParams();
    
    params.donor = this.donorCheckBox.isSelected() ? this.currentDonor : null;
    params.loTime = this.donatedAfterCheckBox.isSelected() ? (Date)this.donatedAfterField.getValue() : null;
    params.hiTime = this.donatedBeforeCheckBox.isSelected() ? (Date)this.donatedBeforeField.getValue() : null;
    params.loAmount = this.amountAboveCheckBox.isSelected() ? FormatUtils.getNumberOrNull(this.amountAboveField.getText()) : null;
    params.hiAmount = this.amountBelowCheckBox.isSelected() ? FormatUtils.getNumberOrNull(this.amountBelowField.getText()) : null;
    params.targetBidState = this.bidStateCheckBox.isSelected() ? (DonationBidState)this.bidStateComboBox.getSelectedItem() : null;
    params.targetReadState = this.readStateCheckBox.isSelected() ? (DonationReadState)this.readStateComboBox.getSelectedItem() : null;
    params.targetCommentState = this.commentStateCheckBox.isSelected() ? (DonationCommentState)this.commentStateComboBox.getSelectedItem() : null;
    
    this.fillList(this.searcher.runSearch(params));
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
