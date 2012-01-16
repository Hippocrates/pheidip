package pheidip.ui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JList;

import pheidip.logic.BidSearch;
import pheidip.objects.Bid;
import pheidip.objects.BidSearchParams;
import pheidip.objects.BidState;
import pheidip.objects.SpeedRun;
import pheidip.util.StringUtils;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class BidSearchDialog extends JDialog
{
  private final JPanel contentPanel = new JPanel();
  private BidSearch searcher;
  private JTextField speedRunField;
  private JTextField bidField;
  private ActionHandler actionHandler;
  private FocusTraversalManager tabOrder;
  private JList bidList;
  private JCheckBox bidCheckBox;
  private JButton newChoiceButton;
  private JButton newChallengeButton;
  private JButton searchButton;
  private JButton okButton;
  private JButton cancelButton;
  private JButton browseSpeedRunButton;
  private JCheckBox speedRunCheckBox;
  private SpeedRun currentRun;
  private List<Bid> results;
  private JLabel lblBidState;
  private JCheckBox bidStateCheckBox;
  private JComboBox bidStateComboBox;
  private JButton prevButton;
  private JButton nextButton;

  private void initializeGUI()
  {
    setBounds(100, 100, 594, 361);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    GridBagLayout gbl_contentPanel = new GridBagLayout();
    gbl_contentPanel.columnWidths = new int[]{76, 0, 127, 136, 72, 96, 103, 0};
    gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    contentPanel.setLayout(gbl_contentPanel);
    
    JLabel lblSpeedRun = new JLabel("Speed Run:");
    GridBagConstraints gbc_lblSpeedRun = new GridBagConstraints();
    gbc_lblSpeedRun.anchor = GridBagConstraints.EAST;
    gbc_lblSpeedRun.insets = new Insets(0, 0, 5, 5);
    gbc_lblSpeedRun.gridx = 0;
    gbc_lblSpeedRun.gridy = 0;
    contentPanel.add(lblSpeedRun, gbc_lblSpeedRun);
    
    speedRunCheckBox = new JCheckBox("");
    GridBagConstraints gbc_speedRunCheckBox = new GridBagConstraints();
    gbc_speedRunCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_speedRunCheckBox.gridx = 1;
    gbc_speedRunCheckBox.gridy = 0;
    contentPanel.add(speedRunCheckBox, gbc_speedRunCheckBox);
    
    speedRunField = new JTextField();
    speedRunField.setEditable(false);
    GridBagConstraints gbc_speedRunField = new GridBagConstraints();
    gbc_speedRunField.gridwidth = 2;
    gbc_speedRunField.insets = new Insets(0, 0, 5, 5);
    gbc_speedRunField.fill = GridBagConstraints.HORIZONTAL;
    gbc_speedRunField.gridx = 2;
    gbc_speedRunField.gridy = 0;
    contentPanel.add(speedRunField, gbc_speedRunField);
    speedRunField.setColumns(10);
    
    browseSpeedRunButton = new JButton("Browse...");
    GridBagConstraints gbc_browseSpeedRunButton = new GridBagConstraints();
    gbc_browseSpeedRunButton.insets = new Insets(0, 0, 5, 5);
    gbc_browseSpeedRunButton.gridx = 4;
    gbc_browseSpeedRunButton.gridy = 0;
    contentPanel.add(browseSpeedRunButton, gbc_browseSpeedRunButton);
    
    JScrollPane scrollPane = new JScrollPane();
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.gridwidth = 2;
    gbc_scrollPane.gridheight = 7;
    gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridx = 5;
    gbc_scrollPane.gridy = 0;
    contentPanel.add(scrollPane, gbc_scrollPane);
    
    bidList = new JList();
    scrollPane.setViewportView(bidList);
    
    JLabel lblBidName = new JLabel("Bid Name:");
    GridBagConstraints gbc_lblBidName = new GridBagConstraints();
    gbc_lblBidName.anchor = GridBagConstraints.EAST;
    gbc_lblBidName.insets = new Insets(0, 0, 5, 5);
    gbc_lblBidName.gridx = 0;
    gbc_lblBidName.gridy = 1;
    contentPanel.add(lblBidName, gbc_lblBidName);
    
    bidCheckBox = new JCheckBox("");
    bidCheckBox.setSelected(true);
    GridBagConstraints gbc_bidCheckBox = new GridBagConstraints();
    gbc_bidCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_bidCheckBox.gridx = 1;
    gbc_bidCheckBox.gridy = 1;
    contentPanel.add(bidCheckBox, gbc_bidCheckBox);
    
    bidField = new JTextField();
    GridBagConstraints gbc_bidField = new GridBagConstraints();
    gbc_bidField.gridwidth = 2;
    gbc_bidField.insets = new Insets(0, 0, 5, 5);
    gbc_bidField.fill = GridBagConstraints.HORIZONTAL;
    gbc_bidField.gridx = 2;
    gbc_bidField.gridy = 1;
    contentPanel.add(bidField, gbc_bidField);
    bidField.setColumns(10);
    
    lblBidState = new JLabel("Bid State:");
    GridBagConstraints gbc_lblBidState = new GridBagConstraints();
    gbc_lblBidState.anchor = GridBagConstraints.EAST;
    gbc_lblBidState.insets = new Insets(0, 0, 5, 5);
    gbc_lblBidState.gridx = 0;
    gbc_lblBidState.gridy = 2;
    contentPanel.add(lblBidState, gbc_lblBidState);
    
    bidStateCheckBox = new JCheckBox("");
    GridBagConstraints gbc_bidStateCheckBox = new GridBagConstraints();
    gbc_bidStateCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_bidStateCheckBox.gridx = 1;
    gbc_bidStateCheckBox.gridy = 2;
    contentPanel.add(bidStateCheckBox, gbc_bidStateCheckBox);
    
    bidStateComboBox = new JComboBox(BidState.values());
    GridBagConstraints gbc_bidStateComboBox = new GridBagConstraints();
    gbc_bidStateComboBox.gridwidth = 2;
    gbc_bidStateComboBox.insets = new Insets(0, 0, 5, 5);
    gbc_bidStateComboBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_bidStateComboBox.gridx = 2;
    gbc_bidStateComboBox.gridy = 2;
    contentPanel.add(bidStateComboBox, gbc_bidStateComboBox);
    
    newChoiceButton = new JButton("New Choice");
    GridBagConstraints gbc_newChoiceButton = new GridBagConstraints();
    gbc_newChoiceButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_newChoiceButton.insets = new Insets(0, 0, 5, 5);
    gbc_newChoiceButton.gridx = 2;
    gbc_newChoiceButton.gridy = 3;
    contentPanel.add(newChoiceButton, gbc_newChoiceButton);
    
    newChallengeButton = new JButton("New Challenge");
    GridBagConstraints gbc_newChallengeButton = new GridBagConstraints();
    gbc_newChallengeButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_newChallengeButton.insets = new Insets(0, 0, 5, 5);
    gbc_newChallengeButton.gridx = 2;
    gbc_newChallengeButton.gridy = 4;
    contentPanel.add(newChallengeButton, gbc_newChallengeButton);
    
    searchButton = new JButton("Search");
    GridBagConstraints gbc_searchButton = new GridBagConstraints();
    gbc_searchButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_searchButton.insets = new Insets(0, 0, 5, 5);
    gbc_searchButton.gridx = 4;
    gbc_searchButton.gridy = 6;
    contentPanel.add(searchButton, gbc_searchButton);
    
    prevButton = new JButton("Previous");
    prevButton.setEnabled(false);
    GridBagConstraints gbc_prevButton = new GridBagConstraints();
    gbc_prevButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_prevButton.insets = new Insets(0, 0, 5, 5);
    gbc_prevButton.gridx = 5;
    gbc_prevButton.gridy = 7;
    contentPanel.add(prevButton, gbc_prevButton);
    
    nextButton = new JButton("Next");
    nextButton.setEnabled(false);
    GridBagConstraints gbc_nextButton = new GridBagConstraints();
    gbc_nextButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_nextButton.insets = new Insets(0, 0, 5, 0);
    gbc_nextButton.gridx = 6;
    gbc_nextButton.gridy = 7;
    contentPanel.add(nextButton, gbc_nextButton);
    
    okButton = new JButton("OK");
    GridBagConstraints gbc_okButton = new GridBagConstraints();
    gbc_okButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_okButton.insets = new Insets(0, 0, 0, 5);
    gbc_okButton.gridx = 5;
    gbc_okButton.gridy = 8;
    contentPanel.add(okButton, gbc_okButton);
    
    cancelButton = new JButton("Cancel");
    GridBagConstraints gbc_cancelButton = new GridBagConstraints();
    gbc_cancelButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_cancelButton.gridx = 6;
    gbc_cancelButton.gridy = 8;
    contentPanel.add(cancelButton, gbc_cancelButton);
    
    this.pack();
  }
  
  private class ActionHandler implements ActionListener, ListSelectionListener, DocumentListener
  {
    @Override
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == browseSpeedRunButton)
        {
          openBrowseDialog();
        }
        else if (ev.getSource() == newChoiceButton)
        {
          createNewChoice();
        }
        else if (ev.getSource() == newChallengeButton)
        {
          createNewChallenge();
        }
        else if (ev.getSource() == searchButton)
        {
          runSearch();
        }
        else if (ev.getSource() == okButton)
        {
          returnValue();
        }
        else if (ev.getSource() == cancelButton)
        {
          cancelDialog();
        }
        else if (ev.getSource() == prevButton)
        {
          movePrevResults();
        }
        else if (ev.getSource() == nextButton)
        {
          moveNextResults();
        }
        else if (ev.getSource() == speedRunCheckBox || ev.getSource() == bidCheckBox || ev.getSource() == bidStateCheckBox)
        {
          updateUIState();
        }
      }
      catch(Exception e)
      {
        JOptionPane.showMessageDialog(BidSearchDialog.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }

    @Override
    public void valueChanged(ListSelectionEvent event)
    {
      if (!event.getValueIsAdjusting() && event.getSource() == bidList)
      {
        updateUIState();
      }
    }
    
    public void documentUpdate(DocumentEvent ev)
    {
      updateUIState();
    }

    @Override
    public void changedUpdate(DocumentEvent arg0)
    {
      documentUpdate(arg0);
    }

    @Override
    public void insertUpdate(DocumentEvent arg0)
    {
      documentUpdate(arg0);
    }

    @Override
    public void removeUpdate(DocumentEvent arg0)
    {
      documentUpdate(arg0);
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    this.browseSpeedRunButton.addActionListener(this.actionHandler);
    this.newChallengeButton.addActionListener(this.actionHandler);
    this.newChoiceButton.addActionListener(this.actionHandler);
    this.searchButton.addActionListener(this.actionHandler);
    this.okButton.addActionListener(this.actionHandler);
    this.cancelButton.addActionListener(this.actionHandler);
    this.bidList.addListSelectionListener(this.actionHandler);
    this.bidField.getDocument().addDocumentListener(this.actionHandler);
    this.prevButton.addActionListener(this.actionHandler);
    this.nextButton.addActionListener(this.actionHandler);
    this.speedRunCheckBox.addActionListener(this.actionHandler);
    this.bidCheckBox.addActionListener(this.actionHandler);
    this.bidStateCheckBox.addActionListener(this.actionHandler);

    this.getRootPane().setDefaultButton(this.searchButton);
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {
      this.speedRunCheckBox,
      this.browseSpeedRunButton,
      this.bidCheckBox,
      this.bidField,
      this.bidStateCheckBox,
      this.bidStateComboBox,
      this.newChoiceButton,
      this.newChallengeButton,
      this.bidList,
      this.searchButton,
      this.prevButton,
      this.nextButton,
      this.okButton,
      this.cancelButton,
    });
    
    this.setFocusTraversalPolicy(this.tabOrder);
  }

  private void updateUIState()
  {
    if (this.currentRun != null && !StringUtils.isEmptyOrNull(this.bidField.getText()))
    {
      this.newChallengeButton.setEnabled(true);
      this.newChoiceButton.setEnabled(true);
    }
    else
    {
      this.newChallengeButton.setEnabled(false);
      this.newChoiceButton.setEnabled(false);
    }
    
    this.okButton.setEnabled(!this.bidList.isSelectionEmpty());
    
    this.nextButton.setEnabled(this.searcher.getHasNext());
    this.prevButton.setEnabled(this.searcher.getHasPrevious());
    
    this.speedRunField.setEnabled(this.speedRunCheckBox.isSelected());
    this.browseSpeedRunButton.setEnabled(this.speedRunCheckBox.isSelected());
    
    this.bidField.setEnabled(this.bidCheckBox.isSelected());
    this.bidStateComboBox.setEnabled(this.bidStateCheckBox.isSelected());
  }
  
  /**
   * Create the dialog.
   */
  public BidSearchDialog(JFrame parent, BidSearch searcher)
  {
    super(parent, true);
    
    this.searcher = searcher;
    this.results = new ArrayList<Bid>();
    
    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.updateUIState();
  }
  
  public Bid getResult()
  {
    return this.results == null ? null : this.results.size() > 0 ? this.results.iterator().next() : null;
  }
  
  public List<Bid> getResults()
  {
    return Collections.unmodifiableList(this.results);
  }

  private void openBrowseDialog()
  {
    SpeedRunSearchDialog dialog = new SpeedRunSearchDialog(this, this.searcher.createSpeedRunSearch());
    
    dialog.setVisible(true);
    
    if (dialog.getResult() != null)
    {
      this.currentRun = dialog.getResult();
      this.speedRunField.setText(this.currentRun.toString());
    }
    else
    {
      this.currentRun = null;
      this.speedRunField.setText("");
    }
    
    this.updateUIState();
  }
  
  private void moveNextResults()
  {
    this.fillList(this.searcher.moveNext());
  }
  
  private void movePrevResults()
  {
    this.fillList(this.searcher.movePrevious());
  }
  
  private void createNewChoice()
  {
    this.results = new ArrayList<Bid>();
    this.results.add(this.searcher.createChoiceIfAble(this.currentRun, this.bidField.getText()));

    closeDialog();
  }
  
  private void createNewChallenge()
  {
    this.results = new ArrayList<Bid>();
    this.results.add(this.searcher.createChallengeIfAble(this.currentRun, this.bidField.getText()));

    closeDialog();
  }

  private void returnValue()
  {
    this.results = new ArrayList<Bid>();
    
    for (int i : this.bidList.getSelectedIndices())
    {
      this.results.add((Bid)this.bidList.getModel().getElementAt(i));
    }

    closeDialog();
  }
  
  private void cancelDialog()
  {
    this.results = new ArrayList<Bid>();
    
    closeDialog();
  }
  
  private void fillList(List<Bid> filtered)
  {
    DefaultListModel listData = new DefaultListModel();
    
    for (Bid b : filtered)
    {
      listData.addElement(b);
    }
    
    this.bidList.setModel(listData);
    this.updateUIState();
  }
  
  private void runSearch()
  {
    SpeedRun target = this.speedRunCheckBox.isSelected() ? this.currentRun : null;
    String name = this.bidCheckBox.isSelected() ? StringUtils.nullIfEmpty(this.bidField.getText()) : null;
    Set<BidState> states = new HashSet<BidState>();
    
    if (this.bidStateCheckBox.isSelected())
    {
      states.add((BidState)this.bidStateComboBox.getSelectedItem());
    }
    
    BidSearchParams params = new BidSearchParams(name, null, target, states);
    
    this.fillList(this.searcher.runSearch(params));
  }
  
  private void closeDialog()
  {
    this.setVisible(false);
    this.dispose();
  }
}
