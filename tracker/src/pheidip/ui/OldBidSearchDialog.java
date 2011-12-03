package pheidip.ui;

import javax.swing.JDialog;
import javax.swing.JFrame;

import pheidip.logic.BidSearch;
import pheidip.logic.SpeedRunSearch;
import pheidip.objects.Bid;
import pheidip.objects.BidSearchParams;
import pheidip.objects.BidType;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceOption;
import pheidip.objects.ChoiceOptionSearchParams;
import pheidip.objects.SpeedRun;
import pheidip.objects.SpeedRunSearchParams;
import pheidip.util.StringUtils;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.border.LineBorder;
import java.awt.Color;

@SuppressWarnings("serial")
public class OldBidSearchDialog extends JDialog
{
  private BidSearch searcher;
  private Bid selectedBid;
  private JPanel panel;
  private ActionHandler actionHandler;
  private JTextField speedRunField;
  private JTextField bidNameField;
  private SpeedRunSearch speedRunSearcher;
  private JScrollPane bidNameScrollPane;
  private JList speedRunList;
  private JList bidNameList;
  private JButton okButton;
  private JButton cancelButton;
  private JScrollPane speedRunScrollPane;
  private JLabel bidNameLabel;
  private JLabel speedRunLabel;
  private FocusTraversalManager tabOrder;
  private JLabel optionNameLabel;
  private JTextField optionNameField;
  private JScrollPane optionNameScrollPane;
  private JList optionNameList;
  private ChoiceOption selectedOption;
  private boolean showOptions;
  private JButton newOptionButton;
  private JButton newChallengeButton;
  private JButton newChoiceButton;
  private JButton searchButton;

  private void initializeGUI()
  {
    setBounds(100, 100, 450, 325);
    
    panel = new JPanel();
    getContentPane().add(panel, BorderLayout.SOUTH);
    GridBagLayout gbl_panel = new GridBagLayout();
    gbl_panel.columnWidths = new int[]{141, 164, 144, 0};
    gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
    gbl_panel.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
    gbl_panel.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    panel.setLayout(gbl_panel);
    
    speedRunLabel = new JLabel("Speed Run:");
    GridBagConstraints gbc_speedRunLabel = new GridBagConstraints();
    gbc_speedRunLabel.insets = new Insets(0, 0, 5, 5);
    gbc_speedRunLabel.gridx = 0;
    gbc_speedRunLabel.gridy = 0;
    panel.add(speedRunLabel, gbc_speedRunLabel);
    
    bidNameLabel = new JLabel("Bid Name:");
    GridBagConstraints gbc_bidNameLabel = new GridBagConstraints();
    gbc_bidNameLabel.insets = new Insets(0, 0, 5, 5);
    gbc_bidNameLabel.gridx = 1;
    gbc_bidNameLabel.gridy = 0;
    panel.add(bidNameLabel, gbc_bidNameLabel);
    
    speedRunField = new JTextField();
    GridBagConstraints gbc_speedRunField = new GridBagConstraints();
    gbc_speedRunField.insets = new Insets(0, 0, 5, 5);
    gbc_speedRunField.fill = GridBagConstraints.HORIZONTAL;
    gbc_speedRunField.gridx = 0;
    gbc_speedRunField.gridy = 1;
    panel.add(speedRunField, gbc_speedRunField);
    speedRunField.setColumns(10);
   
    speedRunScrollPane = new JScrollPane();
    speedRunScrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
    GridBagConstraints gbc_speedRunScrollPane = new GridBagConstraints();
    gbc_speedRunScrollPane.insets = new Insets(0, 0, 5, 5);
    gbc_speedRunScrollPane.fill = GridBagConstraints.BOTH;
    gbc_speedRunScrollPane.gridx = 0;
    gbc_speedRunScrollPane.gridy = 2;
    panel.add(speedRunScrollPane, gbc_speedRunScrollPane);
    
    speedRunList = new JList();
    speedRunScrollPane.setViewportView(speedRunList);
    
    bidNameField = new JTextField();
    GridBagConstraints gbc_bidNameField = new GridBagConstraints();
    gbc_bidNameField.insets = new Insets(0, 0, 5, 5);
    gbc_bidNameField.fill = GridBagConstraints.HORIZONTAL;
    gbc_bidNameField.gridx = 1;
    gbc_bidNameField.gridy = 1;
    panel.add(bidNameField, gbc_bidNameField);
    bidNameField.setColumns(10);
    
    bidNameScrollPane = new JScrollPane();
    bidNameScrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
    GridBagConstraints gbc_bidNameScrollPane = new GridBagConstraints();
    gbc_bidNameScrollPane.insets = new Insets(0, 0, 5, 5);
    gbc_bidNameScrollPane.fill = GridBagConstraints.BOTH;
    gbc_bidNameScrollPane.gridx = 1;
    gbc_bidNameScrollPane.gridy = 2;
    panel.add(bidNameScrollPane, gbc_bidNameScrollPane);
    
    bidNameList = new JList();
    bidNameScrollPane.setViewportView(bidNameList);
    
    searchButton = new JButton("Search");
    GridBagConstraints gbc_searchButton = new GridBagConstraints();
    gbc_searchButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_searchButton.insets = new Insets(0, 0, 5, 5);
    gbc_searchButton.gridx = 0;
    gbc_searchButton.gridy = 3;
    panel.add(searchButton, gbc_searchButton);
    
    newChallengeButton = new JButton("New Challenge");
    newChallengeButton.setEnabled(false);
    GridBagConstraints gbc_newChallengeButton = new GridBagConstraints();
    gbc_newChallengeButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_newChallengeButton.insets = new Insets(0, 0, 5, 5);
    gbc_newChallengeButton.gridx = 1;
    gbc_newChallengeButton.gridy = 3;
    panel.add(newChallengeButton, gbc_newChallengeButton);
    
    if (this.showOptions)
    {
      optionNameLabel = new JLabel("Option Name:");
      GridBagConstraints gbc_optionNameLabel = new GridBagConstraints();
      gbc_optionNameLabel.insets = new Insets(0, 0, 5, 0);
      gbc_optionNameLabel.gridx = 2;
      gbc_optionNameLabel.gridy = 0;
      panel.add(optionNameLabel, gbc_optionNameLabel);
      
      optionNameField = new JTextField();
      GridBagConstraints gbc_optionNameField = new GridBagConstraints();
      gbc_optionNameField.insets = new Insets(0, 0, 5, 0);
      gbc_optionNameField.fill = GridBagConstraints.HORIZONTAL;
      gbc_optionNameField.gridx = 2;
      gbc_optionNameField.gridy = 1;
      panel.add(optionNameField, gbc_optionNameField);
      optionNameField.setColumns(10);
      
      
      optionNameScrollPane = new JScrollPane();
      optionNameScrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
      GridBagConstraints gbc_optionNameScrollPane = new GridBagConstraints();
      gbc_optionNameScrollPane.insets = new Insets(0, 0, 5, 0);
      gbc_optionNameScrollPane.fill = GridBagConstraints.BOTH;
      gbc_optionNameScrollPane.gridx = 2;
      gbc_optionNameScrollPane.gridy = 2;
      panel.add(optionNameScrollPane, gbc_optionNameScrollPane);
      
      optionNameList = new JList();
      optionNameScrollPane.setViewportView(optionNameList);

      newOptionButton = new JButton("New Option");
      newOptionButton.setEnabled(false);
      GridBagConstraints gbc_newOptionButton = new GridBagConstraints();
      gbc_newOptionButton.fill = GridBagConstraints.HORIZONTAL;
      gbc_newOptionButton.insets = new Insets(0, 0, 5, 0);
      gbc_newOptionButton.gridx = 2;
      gbc_newOptionButton.gridy = 3;
      panel.add(newOptionButton, gbc_newOptionButton);
    }
    else
    {
      newChoiceButton = new JButton("New Choice");
      newChoiceButton.setEnabled(false);
      GridBagConstraints gbc_newChoiceButton = new GridBagConstraints();
      gbc_newChoiceButton.anchor = GridBagConstraints.BELOW_BASELINE;
      gbc_newChoiceButton.fill = GridBagConstraints.HORIZONTAL;
      gbc_newChoiceButton.insets = new Insets(0, 0, 5, 5);
      gbc_newChoiceButton.gridx = 1;
      gbc_newChoiceButton.gridy = 4;
      panel.add(newChoiceButton, gbc_newChoiceButton);
    }
    
    okButton = new JButton("OK");
    okButton.setEnabled(false);
    GridBagConstraints gbc_btnOk = new GridBagConstraints();
    gbc_btnOk.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnOk.insets = new Insets(0, 0, 0, 5);
    gbc_btnOk.gridx = 0;
    gbc_btnOk.gridy = 5;
    panel.add(okButton, gbc_btnOk);
    
    cancelButton = new JButton("Cancel");
    GridBagConstraints gbc_btnCancel = new GridBagConstraints();
    gbc_btnCancel.insets = new Insets(0, 0, 0, 5);
    gbc_btnCancel.fill = GridBagConstraints.HORIZONTAL;
    gbc_btnCancel.gridx = 1;
    gbc_btnCancel.gridy = 5;
    panel.add(cancelButton, gbc_btnCancel);
  }
  
  private void updateUIState()
  {
    okButton.setEnabled(
        (getCurrentBid() != null && (getCurrentChoice() == null || !showOptions)) ||
        (getCurrentChoice() != null && getCurrentOption() != null));
  
    boolean createBidEnabled = getCurrentSpeedRun() != null && 
      !StringUtils.isEmptyOrNull(bidNameField.getText());

    newChallengeButton.setEnabled(createBidEnabled);
    
    if (showOptions)
    {
      newOptionButton.setEnabled(getCurrentChoice() != null && !StringUtils.isEmptyOrNull(optionNameField.getText()));
    }
    else
    {
      newChoiceButton.setEnabled(createBidEnabled);
    }
  }
  
  private class ActionHandler implements DocumentListener, ActionListener, ListSelectionListener
  {
    @Override
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == okButton)
        {
          returnSelectedBid();
        }
        else if (ev.getSource() == cancelButton)
        {
          closeDialog();
        }
        else if (ev.getSource() == newChallengeButton)
        {
          createNewChallenge();
        }
        else if (ev.getSource() == newChoiceButton)
        {
          createNewChoice();
        }
        else if (ev.getSource() == newOptionButton)
        {
          createNewOption();
        }
        else if (ev.getSource() == searchButton)
        {
          runSpeedRunSearch();
          runBidSearch();
        }
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(OldBidSearchDialog.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }

    @Override
    public void valueChanged(ListSelectionEvent ev)
    {
      if (ev.getSource() == speedRunList)
      {
        if (!speedRunList.isSelectionEmpty())
        {
          runBidFilter();
        }
      }
      else if (ev.getSource() == bidNameList)
      {
        if (showOptions)
        {
          runOptionFilter();
        }
      }
      
      updateUIState();
    }
    
    private void documentUpdate(DocumentEvent d)
    {
      /*if (d.getDocument() == speedRunField.getDocument())
      {
        if (!speedRunList.isSelectionEmpty())
        {
          runBidFilter();
        }
      }
      else if (d.getDocument() == bidNameField.getDocument())
      {
        runBidFilter();
        
        if (showOptions)
        {
          runOptionFilter();
        }
      }
      else */if (showOptions && d.getDocument() == optionNameField.getDocument())
      {
        runOptionFilter();
      }
      
      updateUIState();
    }

    @Override
    public void changedUpdate(DocumentEvent arg0)
    {
      this.documentUpdate(arg0);
    }

    @Override
    public void insertUpdate(DocumentEvent arg0)
    {
      this.documentUpdate(arg0);
    }

    @Override
    public void removeUpdate(DocumentEvent arg0)
    {
      this.documentUpdate(arg0);
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    this.okButton.addActionListener(this.actionHandler);
    this.cancelButton.addActionListener(this.actionHandler);
    this.speedRunList.addListSelectionListener(this.actionHandler);
    this.bidNameList.addListSelectionListener(this.actionHandler);
    this.newChallengeButton.addActionListener(this.actionHandler);
    this.searchButton.addActionListener(this.actionHandler);
    this.speedRunField.getDocument().addDocumentListener(this.actionHandler);
    this.bidNameField.getDocument().addDocumentListener(this.actionHandler);
    
    if (this.showOptions)
    {
      this.optionNameField.getDocument().addDocumentListener(this.actionHandler);
    }
    
    if (this.showOptions)
    {
      this.optionNameList.addListSelectionListener(this.actionHandler);
      this.newOptionButton.addActionListener(this.actionHandler);
    }
    else 
    {
      this.newChoiceButton.addActionListener(this.actionHandler);
    }
    
    if (this.showOptions)
    {
      this.tabOrder = new FocusTraversalManager(new Component[]
      {
        this.speedRunField,
        this.speedRunList,
        this.bidNameField,
        this.bidNameList,
        this.searchButton,
        this.newChallengeButton,
        this.newChoiceButton,
        this.optionNameField,
        this.optionNameList,
        this.newOptionButton,
        this.okButton,
        this.cancelButton
      });
    }
    else
    {
      this.tabOrder = new FocusTraversalManager(new Component[]
      {
        this.speedRunField,
        this.speedRunList,
        this.bidNameField,
        this.bidNameList,
        this.searchButton,
        this.newChallengeButton,
        this.newChoiceButton,
        this.okButton,
        this.cancelButton
      });
    }
    this.setFocusTraversalPolicy(this.tabOrder);
  }
  
  /**
   * @wbp.parser.constructor
   */
  public OldBidSearchDialog(JFrame parent, SpeedRunSearch speedRunSearcher, BidSearch searcher)
  {
    this(parent, speedRunSearcher, searcher, true);
  }
  
  public OldBidSearchDialog(JFrame parent, SpeedRunSearch speedRunSearcher, BidSearch searcher, boolean showOptions)
  {
    super(parent, true);
    
    this.showOptions = showOptions;
    this.searcher = searcher;
    this.speedRunSearcher = speedRunSearcher;
    this.selectedBid = null;
    this.selectedOption = null;

    this.initializeGUI();
    this.initializeGUIEvents();
  }
  
  private void createNewChallenge()
  {
    this.selectedBid = this.searcher.createChallengeIfAble(this.getCurrentSpeedRun(), this.bidNameField.getText());
    this.closeDialog();
  }
  
  private void createNewChoice()
  {
    this.selectedBid = this.searcher.createChoiceIfAble(this.getCurrentSpeedRun(), this.bidNameField.getText());
    this.closeDialog();
  }
  
  private void createNewOption()
  {
    Choice c = this.getCurrentChoice();
    if (c != null)
    {
      this.selectedBid = c;
      this.selectedOption = this.searcher.createOptionIfAble(c.getId(), this.optionNameField.getText());
      this.closeDialog();
    }
    else
    {
      throw new RuntimeException("Error, no choice is set to create this option.");
    }
  }
  
  private SpeedRun getCurrentSpeedRun()
  {
    return (SpeedRun) this.speedRunList.getSelectedValue();
  }
  
  private Bid getCurrentBid()
  {
    return (Bid) this.bidNameList.getSelectedValue();
  }
  
  private Choice getCurrentChoice()
  {
    Bid b = this.getCurrentBid();
    
    if (b != null && b.getType() == BidType.CHOICE)
    {
      return (Choice) b;
    }
    else
    {
      return null;
    }
  }
  
  private ChoiceOption getCurrentOption()
  {
    return (ChoiceOption) this.optionNameList.getSelectedValue();
  }
  
  private void runSpeedRunSearch()
  {
    List<SpeedRun> filtered = this.speedRunSearcher.searchSpeedRuns(new SpeedRunSearchParams(this.speedRunField.getText()));
  
    DefaultListModel data = new DefaultListModel();
    
    for (SpeedRun s : filtered)
    {
      data.addElement(s);
    }
    
    this.speedRunList.setModel(data);
  }
  
  private void runBidSearch()
  {
    List<Bid> filtered = this.searcher.searchBids(new BidSearchParams(this.bidNameField.getText(), this.getCurrentSpeedRun()));
    
    DefaultListModel data = new DefaultListModel();
    
    for (Bid s : filtered)
    {
      data.addElement(s);
    }
    
    this.bidNameList.setModel(data);
  }
  
  private void runBidFilter()
  {
    DefaultListModel data = new DefaultListModel();
    SpeedRun run = this.getCurrentSpeedRun();
    Bid currentBid = this.getCurrentBid();
    
    if (run != null)
    {
      List<Bid> filtered = this.searcher.filterBids(new BidSearchParams(this.bidNameField.getText(), run));
      
      for (Bid s : filtered)
      {
        data.addElement(s);
      }
    }
    
    this.bidNameList.setModel(data);
    this.bidNameList.setSelectedValue(currentBid, true);
  }
  
  private void runOptionFilter()
  {
    DefaultListModel data = new DefaultListModel();
    Choice currentChoice = this.getCurrentChoice();
    ChoiceOption currentOption = this.getCurrentOption();
    
    if (currentChoice != null)
    {
      List<ChoiceOption> filtered = this.searcher.filterChoiceOptions(new ChoiceOptionSearchParams(this.optionNameField.getText(), currentChoice));
      for (ChoiceOption s : filtered)
      {
        data.addElement(s);
      }
    }
    
    this.optionNameList.setModel(data);
    this.optionNameList.setSelectedValue(currentOption, true);
  }
  
  public void returnSelectedBid()
  {
    Bid result = this.getCurrentBid();
    if (result == null)
    {
      JOptionPane.showMessageDialog(this, "No bid is selected.", "Error", JOptionPane.OK_OPTION);
    }
    else
    {
      this.selectedBid = result;
      if (this.selectedBid.getType() == BidType.CHOICE && this.showOptions)
      {
        this.selectedOption = this.getCurrentOption();
      }
      this.closeDialog();
    }
  }
  
  public Bid getSelectedBid()
  {
    return this.selectedBid;
  }
  
  public ChoiceOption getSelectedOption()
  {
    return this.selectedOption;
  }
  
  private void closeDialog()
  {
    this.setVisible(false);
    this.dispose();
  }
}
