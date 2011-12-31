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
import java.util.List;

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
import pheidip.objects.BidType;
import pheidip.objects.Challenge;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceOption;
import pheidip.objects.SpeedRun;
import pheidip.util.StringUtils;
import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class DonationBidSearchDialog extends JDialog
{
  private final JPanel contentPanel = new JPanel();
  private BidSearch searcher;
  private JTextField speedRunField;
  private JTextField bidField;
  private ActionHandler actionHandler;
  private FocusTraversalManager tabOrder;
  private JList bidList;
  private JCheckBox bidCheckBox;
  private JButton newOptionButton;
  private JButton newChallengeButton;
  private JButton searchButton;
  private JButton okButton;
  private JButton cancelButton;
  private JButton browseSpeedRunButton;
  private JCheckBox speedRunCheckBox;
  private SpeedRun currentRun;
  private Challenge challengeResult;
  private ChoiceOption choiceOptionResult;
  private JTextField optionField;
  private JList optionList;
  private JScrollPane optionScrollPane;
  private JLabel lblOptionName;
  private JLabel lblOptions;
  private JLabel lblBidstate;
  private JCheckBox bidStateCheckBox;
  private JComboBox bidStateComboBox;
  private JButton nextButton;
  private JButton prevButton;

  private void initializeGUI()
  {
    setBounds(100, 100, 594, 361);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    GridBagLayout gbl_contentPanel = new GridBagLayout();
    gbl_contentPanel.columnWidths = new int[]{76, 0, 136, 72, 96, 96, 0};
    gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
    speedRunField.setEnabled(false);
    speedRunField.setEditable(false);
    GridBagConstraints gbc_speedRunField = new GridBagConstraints();
    gbc_speedRunField.insets = new Insets(0, 0, 5, 5);
    gbc_speedRunField.fill = GridBagConstraints.HORIZONTAL;
    gbc_speedRunField.gridx = 2;
    gbc_speedRunField.gridy = 0;
    contentPanel.add(speedRunField, gbc_speedRunField);
    speedRunField.setColumns(10);
    
    browseSpeedRunButton = new JButton("Browse...");
    GridBagConstraints gbc_browseSpeedRunButton = new GridBagConstraints();
    gbc_browseSpeedRunButton.insets = new Insets(0, 0, 5, 5);
    gbc_browseSpeedRunButton.gridx = 3;
    gbc_browseSpeedRunButton.gridy = 0;
    contentPanel.add(browseSpeedRunButton, gbc_browseSpeedRunButton);
    
    JScrollPane bidScrollPane = new JScrollPane();
    GridBagConstraints gbc_bidScrollPane = new GridBagConstraints();
    gbc_bidScrollPane.gridwidth = 2;
    gbc_bidScrollPane.gridheight = 9;
    gbc_bidScrollPane.insets = new Insets(0, 0, 5, 0);
    gbc_bidScrollPane.fill = GridBagConstraints.BOTH;
    gbc_bidScrollPane.gridx = 4;
    gbc_bidScrollPane.gridy = 0;
    contentPanel.add(bidScrollPane, gbc_bidScrollPane);
    
    bidList = new JList();
    bidList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    bidScrollPane.setViewportView(bidList);
    
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
    gbc_bidField.insets = new Insets(0, 0, 5, 5);
    gbc_bidField.fill = GridBagConstraints.HORIZONTAL;
    gbc_bidField.gridx = 2;
    gbc_bidField.gridy = 1;
    contentPanel.add(bidField, gbc_bidField);
    bidField.setColumns(10);
    
    lblBidstate = new JLabel("BidState:");
    GridBagConstraints gbc_lblBidstate = new GridBagConstraints();
    gbc_lblBidstate.anchor = GridBagConstraints.EAST;
    gbc_lblBidstate.insets = new Insets(0, 0, 5, 5);
    gbc_lblBidstate.gridx = 0;
    gbc_lblBidstate.gridy = 2;
    contentPanel.add(lblBidstate, gbc_lblBidstate);
    
    bidStateCheckBox = new JCheckBox("");
    GridBagConstraints gbc_bidStateCheckBox = new GridBagConstraints();
    gbc_bidStateCheckBox.anchor = GridBagConstraints.SOUTH;
    gbc_bidStateCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_bidStateCheckBox.gridx = 1;
    gbc_bidStateCheckBox.gridy = 2;
    contentPanel.add(bidStateCheckBox, gbc_bidStateCheckBox);
    
    bidStateComboBox = new JComboBox(BidState.values());
    GridBagConstraints gbc_bidStateComboBox = new GridBagConstraints();
    gbc_bidStateComboBox.insets = new Insets(0, 0, 5, 5);
    gbc_bidStateComboBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_bidStateComboBox.gridx = 2;
    gbc_bidStateComboBox.gridy = 2;
    contentPanel.add(bidStateComboBox, gbc_bidStateComboBox);
    
    lblOptionName = new JLabel("Option Name:");
    GridBagConstraints gbc_lblOptionName = new GridBagConstraints();
    gbc_lblOptionName.anchor = GridBagConstraints.EAST;
    gbc_lblOptionName.insets = new Insets(0, 0, 5, 5);
    gbc_lblOptionName.gridx = 0;
    gbc_lblOptionName.gridy = 3;
    contentPanel.add(lblOptionName, gbc_lblOptionName);
    
    optionField = new JTextField();
    optionField.setEnabled(false);
    GridBagConstraints gbc_optionField = new GridBagConstraints();
    gbc_optionField.gridwidth = 2;
    gbc_optionField.insets = new Insets(0, 0, 5, 5);
    gbc_optionField.fill = GridBagConstraints.HORIZONTAL;
    gbc_optionField.gridx = 1;
    gbc_optionField.gridy = 3;
    contentPanel.add(optionField, gbc_optionField);
    optionField.setColumns(10);
    
    lblOptions = new JLabel("Options:");
    GridBagConstraints gbc_lblOptions = new GridBagConstraints();
    gbc_lblOptions.anchor = GridBagConstraints.EAST;
    gbc_lblOptions.insets = new Insets(0, 0, 5, 5);
    gbc_lblOptions.gridx = 0;
    gbc_lblOptions.gridy = 4;
    contentPanel.add(lblOptions, gbc_lblOptions);
    
    optionScrollPane = new JScrollPane();
    GridBagConstraints gbc_optionScrollPane = new GridBagConstraints();
    gbc_optionScrollPane.gridwidth = 2;
    gbc_optionScrollPane.gridheight = 2;
    gbc_optionScrollPane.fill = GridBagConstraints.BOTH;
    gbc_optionScrollPane.insets = new Insets(0, 0, 5, 5);
    gbc_optionScrollPane.gridx = 1;
    gbc_optionScrollPane.gridy = 4;
    contentPanel.add(optionScrollPane, gbc_optionScrollPane);
    
    optionList = new JList();
    optionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    optionList.setEnabled(false);
    optionScrollPane.setViewportView(optionList);
    
    newOptionButton = new JButton("New Option");
    GridBagConstraints gbc_newOptionButton = new GridBagConstraints();
    gbc_newOptionButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_newOptionButton.insets = new Insets(0, 0, 5, 5);
    gbc_newOptionButton.gridx = 2;
    gbc_newOptionButton.gridy = 6;
    contentPanel.add(newOptionButton, gbc_newOptionButton);
    
    newChallengeButton = new JButton("New Challenge");
    GridBagConstraints gbc_newChallengeButton = new GridBagConstraints();
    gbc_newChallengeButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_newChallengeButton.insets = new Insets(0, 0, 5, 5);
    gbc_newChallengeButton.gridx = 2;
    gbc_newChallengeButton.gridy = 7;
    contentPanel.add(newChallengeButton, gbc_newChallengeButton);
    
    searchButton = new JButton("Search");
    GridBagConstraints gbc_searchButton = new GridBagConstraints();
    gbc_searchButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_searchButton.insets = new Insets(0, 0, 5, 5);
    gbc_searchButton.gridx = 3;
    gbc_searchButton.gridy = 8;
    contentPanel.add(searchButton, gbc_searchButton);
    
    prevButton = new JButton("Previous");
    prevButton.setEnabled(false);
    GridBagConstraints gbc_prevButton = new GridBagConstraints();
    gbc_prevButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_prevButton.insets = new Insets(0, 0, 5, 5);
    gbc_prevButton.gridx = 4;
    gbc_prevButton.gridy = 9;
    contentPanel.add(prevButton, gbc_prevButton);
    
    nextButton = new JButton("Next");
    nextButton.setEnabled(false);
    GridBagConstraints gbc_nextButton = new GridBagConstraints();
    gbc_nextButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_nextButton.insets = new Insets(0, 0, 5, 0);
    gbc_nextButton.gridx = 5;
    gbc_nextButton.gridy = 9;
    contentPanel.add(nextButton, gbc_nextButton);
    
    okButton = new JButton("OK");
    GridBagConstraints gbc_okButton = new GridBagConstraints();
    gbc_okButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_okButton.insets = new Insets(0, 0, 0, 5);
    gbc_okButton.gridx = 4;
    gbc_okButton.gridy = 10;
    contentPanel.add(okButton, gbc_okButton);
    
    cancelButton = new JButton("Cancel");
    GridBagConstraints gbc_cancelButton = new GridBagConstraints();
    gbc_cancelButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_cancelButton.gridx = 5;
    gbc_cancelButton.gridy = 10;
    contentPanel.add(cancelButton, gbc_cancelButton);
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
        else if (ev.getSource() == newOptionButton)
        {
          createNewOption();
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
        JOptionPane.showMessageDialog(DonationBidSearchDialog.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }

    @Override
    public void valueChanged(ListSelectionEvent event)
    {
      if (!event.getValueIsAdjusting() && event.getSource() == bidList)
      {
        DefaultListModel model = new DefaultListModel();
        
        Choice c = getCurrentChoice();
        if (c != null)
        {
          for (ChoiceOption o : c.getOptions())
          {
            model.addElement(o);
          }
        }
        
        optionList.setModel(model);
        
        updateUIState();
      }
      else if (!event.getValueIsAdjusting() && event.getSource() == optionList)
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
    this.newOptionButton.addActionListener(this.actionHandler);
    this.searchButton.addActionListener(this.actionHandler);
    this.okButton.addActionListener(this.actionHandler);
    this.cancelButton.addActionListener(this.actionHandler);
    this.bidList.addListSelectionListener(this.actionHandler);
    this.optionList.addListSelectionListener(this.actionHandler);
    this.bidField.getDocument().addDocumentListener(this.actionHandler);
    this.optionField.getDocument().addDocumentListener(this.actionHandler);
    this.nextButton.addActionListener(this.actionHandler);
    this.prevButton.addActionListener(this.actionHandler);
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
      this.optionField,
      this.optionList,
      this.newOptionButton,
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
    }
    else
    {
      this.newChallengeButton.setEnabled(false);
    }
    
    if (this.getCurrentChoice() != null && !StringUtils.isEmptyOrNull(this.optionField.getText()))
    {
      this.newOptionButton.setEnabled(true);
    }
    else
    {
      this.newOptionButton.setEnabled(false);
    }
    
    if (this.bidList.isSelectionEmpty())
    {
      this.okButton.setEnabled(false);
      this.optionField.setEnabled(false);
      this.optionList.setEnabled(false);
      this.optionList.setModel(new DefaultListModel());
    }
    else
    {
      Choice currentChoice = this.getCurrentChoice();
      
      if (currentChoice != null)
      {
        this.optionField.setEnabled(true);
        this.optionList.setEnabled(true);
        
        if (this.optionList.isSelectionEmpty())
        {
          this.okButton.setEnabled(false);
        }
        else
        {
          this.okButton.setEnabled(true);
        }
      }
      else
      {
        this.optionField.setEnabled(false);
        this.optionList.setEnabled(false);
        this.okButton.setEnabled(true);
      }
    }
    
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
  public DonationBidSearchDialog(JFrame parent, BidSearch searcher)
  {
    super(parent, true);
    
    this.searcher = searcher;
    this.challengeResult = null;
    this.choiceOptionResult = null;
    
    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.updateUIState();
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
  
  private void createNewOption()
  {
    if (!StringUtils.isEmptyOrNull(this.optionField.getText()))
    {
      Choice currentChoice = this.getCurrentChoice();
      
      if (currentChoice == null)
      {
        throw new RuntimeException("Error, no choice is selected.");
      }
      
      this.choiceOptionResult = this.searcher.createOptionIfAble(currentChoice, this.optionField.getText());
    }
    else
    {
      throw new RuntimeException("Error, option name field is empty.");
    }

    closeDialog();
  }
  
  private void createNewChallenge()
  {
    this.challengeResult = this.searcher.createChallengeIfAble(this.currentRun, this.bidField.getText());

    closeDialog();
  }
  
  private Bid getCurrentBid()
  {
    return (Bid) this.bidList.getSelectedValue();
  }
  
  private Choice getCurrentChoice()
  {
    Bid c = this.getCurrentBid();
    
    if (c != null && c.getType() == BidType.CHOICE)
    {
      return (Choice) c;
    }
    else
    {
      return null;
    }
  }
  
  public BidType getSelectionType()
  {
    if (this.challengeResult != null)
    {
      return BidType.CHALLENGE;
    }
    else if (this.choiceOptionResult != null)
    {
      return BidType.CHOICE;
    }
    else
    {
      return null;
    }
  }
  
  public Challenge getSelectedChallenge()
  {
    return this.challengeResult;
  }
  
  public ChoiceOption getSelectedOption()
  {
    return this.choiceOptionResult;
  }
  
  private void returnValue()
  {
    Bid result = this.getCurrentBid();
    
    if (result == null)
    {
      throw new RuntimeException("Error, no bid selected.");
    }
    else if (result.getType() == BidType.CHALLENGE)
    {
      this.challengeResult = (Challenge) result;
    }
    else if (!this.optionList.isSelectionEmpty())
    {
      this.choiceOptionResult = (ChoiceOption) this.optionList.getSelectedValue();
    }
    else
    {
      throw new RuntimeException("Error, no option selected.");
    }
    
    closeDialog();
  }
  
  private void cancelDialog()
  {
    this.challengeResult = null;
    this.choiceOptionResult = null;
    
    closeDialog();
  }
  
  
  private void moveNextResults()
  {
    this.fillList(this.searcher.moveNext());
  }
  
  private void movePrevResults()
  {
    this.fillList(this.searcher.movePrevious());
  }
  
  private void fillList(List<Bid> filtered)
  {
    DefaultListModel listData = new DefaultListModel();
    
    for (Bid b : filtered)
    {
      listData.addElement(b);
    }
    
    this.bidList.setModel(listData);
    updateUIState();
  }
  
  private void runSearch()
  {
    SpeedRun target = this.speedRunCheckBox.isSelected() ? this.currentRun : null;
    String name = this.bidCheckBox.isSelected() ? StringUtils.nullIfEmpty(this.bidField.getText()) : null;
    BidState state = this.bidStateCheckBox.isSelected() ? (BidState)this.bidStateComboBox.getSelectedItem() : null;
    
    BidSearchParams params = new BidSearchParams(name, target, state);
    
    this.fillList(this.searcher.runSearch(params));
  }
  
  private void closeDialog()
  {
    this.setVisible(false);
    this.dispose();
  }
}
