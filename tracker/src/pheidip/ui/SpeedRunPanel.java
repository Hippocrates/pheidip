package pheidip.ui;

import pheidip.logic.ChallengeControl;
import pheidip.logic.ChoiceControl;
import pheidip.logic.SpeedRunControl;
import pheidip.objects.Bid;
import pheidip.objects.BidType;
import pheidip.objects.ChoiceOption;
import pheidip.objects.Prize;
import pheidip.objects.SpeedRun;
import pheidip.util.FormatUtils;
import pheidip.util.Pair;

import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;
import javax.swing.JFormattedTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JList;

@SuppressWarnings("serial")
public class SpeedRunPanel extends EntityPanel
{
  private MainWindow owner;
  private SpeedRunControl speedRunControl;
  private List<Bid> cachedRelatedBids;
  
  private JTextField nameField;
  private JTable bidTable;
  private JButton deleteButton;
  private JButton saveButton;
  private JButton refreshButton;
  private JButton openBidButton;
  private JButton newChoiceButton;
  private JButton newChallengeButton;
  private JScrollPane bidsScrollPane;
  private ActionHandler actionHandler;
  private FocusTraversalManager tabOrder;
  private JScrollPane descriptionScrollPane;
  private JLabel lblDescription;
  private JTextArea descriptionTextArea;
  private JLabel lblSortingKey;
  private JFormattedTextField sortKeyField;
  private JLabel lblStartTime;
  private TimeControl startTimeField;
  private JLabel lblEndTime;
  private TimeControl endTimeField;
  private JLabel lblRunners;
  private JTextField runnersTextField;
  private JButton openPrizeButton;
  private JLabel lblPrizesToBe;
  private JList prizeList;
  private JScrollPane prizesScrollPane;

  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{72, 110, 116, 114, 0, 0, 85, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 74, 0, 0, 0, 0, 37, 0, 131, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    JLabel nameLabel = new JLabel("Name:");
    GridBagConstraints gbc_nameLabel = new GridBagConstraints();
    gbc_nameLabel.insets = new Insets(0, 0, 5, 5);
    gbc_nameLabel.anchor = GridBagConstraints.EAST;
    gbc_nameLabel.gridx = 0;
    gbc_nameLabel.gridy = 0;
    add(nameLabel, gbc_nameLabel);
    
    nameField = new JTextField();
    GridBagConstraints gbc_nameField = new GridBagConstraints();
    gbc_nameField.insets = new Insets(0, 0, 5, 5);
    gbc_nameField.gridwidth = 2;
    gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
    gbc_nameField.gridx = 1;
    gbc_nameField.gridy = 0;
    add(nameField, gbc_nameField);
    nameField.setColumns(10);
    
    deleteButton = new JButton("Delete SpeedRun");
    GridBagConstraints gbc_deleteButton = new GridBagConstraints();
    gbc_deleteButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_deleteButton.insets = new Insets(0, 0, 5, 0);
    gbc_deleteButton.gridx = 6;
    gbc_deleteButton.gridy = 0;
    add(deleteButton, gbc_deleteButton);
    
    lblRunners = new JLabel("Runner(s):");
    GridBagConstraints gbc_lblRunners = new GridBagConstraints();
    gbc_lblRunners.anchor = GridBagConstraints.EAST;
    gbc_lblRunners.insets = new Insets(0, 0, 5, 5);
    gbc_lblRunners.gridx = 0;
    gbc_lblRunners.gridy = 1;
    add(lblRunners, gbc_lblRunners);
    
    runnersTextField = new JTextField();
    GridBagConstraints gbc_runnersTextField = new GridBagConstraints();
    gbc_runnersTextField.gridwidth = 2;
    gbc_runnersTextField.insets = new Insets(0, 0, 5, 5);
    gbc_runnersTextField.fill = GridBagConstraints.HORIZONTAL;
    gbc_runnersTextField.gridx = 1;
    gbc_runnersTextField.gridy = 1;
    add(runnersTextField, gbc_runnersTextField);
    runnersTextField.setColumns(10);
    
    lblDescription = new JLabel("Description:");
    GridBagConstraints gbc_lblDescription = new GridBagConstraints();
    gbc_lblDescription.anchor = GridBagConstraints.NORTH;
    gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
    gbc_lblDescription.gridx = 0;
    gbc_lblDescription.gridy = 2;
    add(lblDescription, gbc_lblDescription);
    
    descriptionScrollPane = new JScrollPane();
    descriptionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    GridBagConstraints gbc_descriptionScrollPane = new GridBagConstraints();
    gbc_descriptionScrollPane.gridwidth = 3;
    gbc_descriptionScrollPane.insets = new Insets(0, 0, 5, 5);
    gbc_descriptionScrollPane.fill = GridBagConstraints.BOTH;
    gbc_descriptionScrollPane.gridx = 1;
    gbc_descriptionScrollPane.gridy = 2;
    add(descriptionScrollPane, gbc_descriptionScrollPane);
    
    descriptionTextArea = new JTextArea();
    descriptionTextArea.setWrapStyleWord(true);
    descriptionTextArea.setLineWrap(true);
    descriptionScrollPane.setViewportView(descriptionTextArea);
    
    lblSortingKey = new JLabel("Sorting Key:");
    GridBagConstraints gbc_lblSortingKey = new GridBagConstraints();
    gbc_lblSortingKey.anchor = GridBagConstraints.EAST;
    gbc_lblSortingKey.insets = new Insets(0, 0, 5, 5);
    gbc_lblSortingKey.gridx = 0;
    gbc_lblSortingKey.gridy = 3;
    add(lblSortingKey, gbc_lblSortingKey);
    
    sortKeyField = new JFormattedTextField(FormatUtils.getIntegerFormat());
    GridBagConstraints gbc_sortKeyField = new GridBagConstraints();
    gbc_sortKeyField.gridwidth = 2;
    gbc_sortKeyField.insets = new Insets(0, 0, 5, 5);
    gbc_sortKeyField.fill = GridBagConstraints.HORIZONTAL;
    gbc_sortKeyField.gridx = 1;
    gbc_sortKeyField.gridy = 3;
    add(sortKeyField, gbc_sortKeyField);
    
    openPrizeButton = new JButton("Open Prize");
    GridBagConstraints gbc_openPrizeButton = new GridBagConstraints();
    gbc_openPrizeButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_openPrizeButton.insets = new Insets(0, 0, 5, 0);
    gbc_openPrizeButton.gridx = 6;
    gbc_openPrizeButton.gridy = 3;
    add(openPrizeButton, gbc_openPrizeButton);
    
    lblStartTime = new JLabel("Start Time:");
    GridBagConstraints gbc_lblStartTime = new GridBagConstraints();
    gbc_lblStartTime.anchor = GridBagConstraints.EAST;
    gbc_lblStartTime.insets = new Insets(0, 0, 5, 5);
    gbc_lblStartTime.gridx = 0;
    gbc_lblStartTime.gridy = 4;
    add(lblStartTime, gbc_lblStartTime);
    
    startTimeField = new TimeControl();
    GridBagConstraints gbc_startTimeField = new GridBagConstraints();
    gbc_startTimeField.gridwidth = 2;
    gbc_startTimeField.insets = new Insets(0, 0, 5, 5);
    gbc_startTimeField.fill = GridBagConstraints.HORIZONTAL;
    gbc_startTimeField.gridx = 1;
    gbc_startTimeField.gridy = 4;
    add(startTimeField, gbc_startTimeField);
    
    lblPrizesToBe = new JLabel("Prizes to be drawn:");
    GridBagConstraints gbc_lblPrizesToBe = new GridBagConstraints();
    gbc_lblPrizesToBe.anchor = GridBagConstraints.EAST;
    gbc_lblPrizesToBe.insets = new Insets(0, 0, 5, 5);
    gbc_lblPrizesToBe.gridx = 4;
    gbc_lblPrizesToBe.gridy = 4;
    add(lblPrizesToBe, gbc_lblPrizesToBe);
    
    prizesScrollPane = new JScrollPane();
    GridBagConstraints gbc_prizesScrollPane = new GridBagConstraints();
    gbc_prizesScrollPane.fill = GridBagConstraints.BOTH;
    gbc_prizesScrollPane.gridheight = 4;
    gbc_prizesScrollPane.gridwidth = 2;
    gbc_prizesScrollPane.insets = new Insets(0, 0, 5, 0);
    gbc_prizesScrollPane.gridx = 5;
    gbc_prizesScrollPane.gridy = 4;
    add(prizesScrollPane, gbc_prizesScrollPane);
    
    prizeList = new JList();
    prizesScrollPane.setViewportView(prizeList);
    
    lblEndTime = new JLabel("End Time:");
    GridBagConstraints gbc_lblEndTime = new GridBagConstraints();
    gbc_lblEndTime.anchor = GridBagConstraints.EAST;
    gbc_lblEndTime.insets = new Insets(0, 0, 5, 5);
    gbc_lblEndTime.gridx = 0;
    gbc_lblEndTime.gridy = 5;
    add(lblEndTime, gbc_lblEndTime);
    
    endTimeField = new TimeControl();
    GridBagConstraints gbc_endTimeField = new GridBagConstraints();
    gbc_endTimeField.gridwidth = 2;
    gbc_endTimeField.insets = new Insets(0, 0, 5, 5);
    gbc_endTimeField.fill = GridBagConstraints.HORIZONTAL;
    gbc_endTimeField.gridx = 1;
    gbc_endTimeField.gridy = 5;
    add(endTimeField, gbc_endTimeField);
    
    saveButton = new JButton("Save");
    GridBagConstraints gbc_saveButton = new GridBagConstraints();
    gbc_saveButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_saveButton.insets = new Insets(0, 0, 5, 5);
    gbc_saveButton.gridx = 1;
    gbc_saveButton.gridy = 6;
    add(saveButton, gbc_saveButton);
    
    refreshButton = new JButton("Refresh");
    GridBagConstraints gbc_refreshButton = new GridBagConstraints();
    gbc_refreshButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_refreshButton.insets = new Insets(0, 0, 5, 5);
    gbc_refreshButton.gridx = 2;
    gbc_refreshButton.gridy = 6;
    add(refreshButton, gbc_refreshButton);
    
    openBidButton = new JButton("Open Bid");
    GridBagConstraints gbc_openBidButton = new GridBagConstraints();
    gbc_openBidButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_openBidButton.insets = new Insets(0, 0, 5, 5);
    gbc_openBidButton.gridx = 2;
    gbc_openBidButton.gridy = 8;
    add(openBidButton, gbc_openBidButton);
    
    newChoiceButton = new JButton("New Choice");
    GridBagConstraints gbc_newChoiceButton = new GridBagConstraints();
    gbc_newChoiceButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_newChoiceButton.insets = new Insets(0, 0, 5, 5);
    gbc_newChoiceButton.gridx = 3;
    gbc_newChoiceButton.gridy = 8;
    add(newChoiceButton, gbc_newChoiceButton);
    
    newChallengeButton = new JButton("New Challenge");
    GridBagConstraints gbc_newChallengeButton = new GridBagConstraints();
    gbc_newChallengeButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_newChallengeButton.insets = new Insets(0, 0, 5, 5);
    gbc_newChallengeButton.gridx = 4;
    gbc_newChallengeButton.gridy = 8;
    add(newChallengeButton, gbc_newChallengeButton);
    
    bidsScrollPane = new JScrollPane();
    GridBagConstraints gbc_bidsScrollPane = new GridBagConstraints();
    gbc_bidsScrollPane.gridwidth = 7;
    gbc_bidsScrollPane.fill = GridBagConstraints.BOTH;
    gbc_bidsScrollPane.gridx = 0;
    gbc_bidsScrollPane.gridy = 9;
    add(bidsScrollPane, gbc_bidsScrollPane);
    
    bidTable = new JTable();
    bidTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    bidsScrollPane.setViewportView(bidTable);
  }
  
  private class ActionHandler extends MouseAdapter implements ActionListener
  {
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == refreshButton)
        {
          SpeedRunPanel.this.refreshContent();
        }
        else if (ev.getSource() == saveButton)
        {
          SpeedRunPanel.this.saveContent();
        }
        else if (ev.getSource() == openBidButton)
        {
          SpeedRunPanel.this.openSelectedBid();
        }
        else if (ev.getSource() == newChoiceButton)
        {
          SpeedRunPanel.this.createNewChoice();
        }
        else if (ev.getSource() == newChallengeButton)
        {
          SpeedRunPanel.this.createNewChallenge();
        }
        else if (ev.getSource() == deleteButton)
        {
          SpeedRunPanel.this.deleteContent();
        }
        else if (ev.getSource() == openPrizeButton)
        {
          SpeedRunPanel.this.openSelectedPrize();
        }
      }
      catch(Exception e)
      {
        e.printStackTrace();
        owner.report(e);
      }
    }
    
    public void mouseClicked(MouseEvent ev) 
    {
      if (ev.getSource() == bidTable)
      {
        if (ev.getClickCount() == 2)
        {
          SpeedRunPanel.this.openSelectedBid();
        }
      }
      else if (ev.getSource() == prizeList)
      {
        if (ev.getClickCount() == 2)
        {
          SpeedRunPanel.this.openSelectedPrize();
        }
      }
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    refreshButton.addActionListener(this.actionHandler);
    saveButton.addActionListener(this.actionHandler);
    openBidButton.addActionListener(this.actionHandler);
    newChoiceButton.addActionListener(this.actionHandler);
    newChallengeButton.addActionListener(this.actionHandler);
    deleteButton.addActionListener(this.actionHandler);
    bidTable.addMouseListener(this.actionHandler);
    prizeList.addMouseListener(this.actionHandler);
    this.openPrizeButton.addActionListener(this.actionHandler);
    
    this.descriptionTextArea.addKeyListener(new TabTraversalKeyListener(this.descriptionTextArea));
    
    this.bidTable.addKeyListener(new TabTraversalKeyListener(this.bidTable));
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {
      this.nameField,
      this.runnersTextField,
      this.descriptionTextArea,
      this.sortKeyField,
      this.saveButton,
      this.refreshButton,
      this.openBidButton,
      this.newChoiceButton,
      this.newChallengeButton,
      this.bidTable,
      this.openPrizeButton,
      this.prizeList,
    });
    this.setFocusTraversalPolicy(this.tabOrder);
  }

  public SpeedRunPanel(MainWindow owner, SpeedRunControl control)
  {
    this.owner = owner;
    this.speedRunControl = control;
    
    this.initializeGUI();
    this.initializeGUIEvents();
  }
  
  public boolean isFocusCycleRoot()
  {
    return true;
  }
  
  public FocusTraversalPolicy getFocusTraversalPolicy() 
  {
    return this.tabOrder;
  }

  @Override
  public boolean confirmClose()
  {
    return true;
  }
  
  @Override
  public void refreshContent()
  {
    this.speedRunControl.refreshData();
    this.redrawContent();
  }
  
  private Prize getSelectedPrize()
  {
    return (Prize) this.prizeList.getSelectedValue();
  }
  
  private void openSelectedPrize()
  {
    Prize p = this.getSelectedPrize();
    
    if (p != null)
    {
      this.owner.openPrizeTab(p.getId());
    }
  }

  @Override
  public void redrawContent()
  {
    SpeedRun data = this.speedRunControl.getData();
    
    if (data == null)
    {
      JOptionPane.showMessageDialog(this, "Error, this run no longer exists", "Not Found", JOptionPane.ERROR_MESSAGE);
      this.owner.removeTab(this);
      return;
    }
    
    this.nameField.setText(data.getName());
    this.runnersTextField.setText(data.getRunners());
    this.descriptionTextArea.setText(data.getDescription());
    this.sortKeyField.setText("" + data.getSortKey());
    this.startTimeField.setTimeValue(data.getStartTime());
    this.endTimeField.setTimeValue(data.getEndTime());
    
    DefaultListModel prizeData = new DefaultListModel();
    
    for (Prize p : data.getPrizeEndGame())
    {
      prizeData.addElement(p);
    }
    
    this.prizeList.setModel(prizeData);
    
    cachedRelatedBids = this.speedRunControl.getAllBids();
    
    CustomTableModel tableData = new CustomTableModel(new String[]
    {
        "Bid",
        "Status",
    },0);
    
    for (Bid b : cachedRelatedBids)
    {
      String statusString = "";
      
      if (b.getType() == BidType.CHALLENGE)
      {
        ChallengeControl c = this.speedRunControl.getChallengeControl(b.getId());
        statusString = "$" + c.getTotalCollected().toString();
      }
      else
      {
        ChoiceControl c = this.speedRunControl.getChoiceControl(b.getId());
        List<Pair<ChoiceOption,BigDecimal>> options = c.getOptionsWithTotals(true);
        String[] strings = new String[options.size()];
        
        for (int i = 0; i < strings.length; ++i)
        {
          statusString += options.get(i).getFirst() + " : $" + options.get(i).getSecond().toString();
          
          if (i != strings.length - 1)
          {
            statusString += ", ";
          }
        }
      }
      
      tableData.addRow(
          new Object[]
          {
              b,
              statusString,
          });
    }
    
    this.bidTable.setModel(tableData);
    
    this.setHeaderText("Run: " + data.getName());
  }
  
  public void saveContent()
  {
    SpeedRun data = this.speedRunControl.getData();
    data.setName(this.nameField.getText());
    data.setRunners(this.runnersTextField.getText());
    data.setDescription(this.descriptionTextArea.getText());
    data.setSortKey(Integer.parseInt(this.sortKeyField.getText()));
    data.setStartTime(this.startTimeField.getTimeValue());
    data.setEndTime(this.endTimeField.getTimeValue());
    this.speedRunControl.updateData(data);
    this.refreshContent();
  }

  public void deleteContent()
  {
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this speed run?", "Confirm delete", JOptionPane.YES_NO_OPTION);
    
    if (result == JOptionPane.OK_OPTION)
    {
      this.speedRunControl.deleteSpeedRun();
      this.owner.removeTab(this);
    }
  }
  
  private void openSelectedBid()
  {
    int selectedRow = this.bidTable.getSelectedRow();
    
    if (selectedRow != -1)
    {
      Bid current = (Bid)this.bidTable.getValueAt(selectedRow, 0);
      if (current.getType() == BidType.CHOICE)
      {
        this.owner.openChoiceTab(current.getId());
      }
      else if (current.getType() == BidType.CHALLENGE)
      {
        this.owner.openChallengeTab(current.getId());
      }
    }
  }

  public int getSpeedRunId()
  {
    return this.speedRunControl.getSpeedRunId();
  }
  
  public void createNewChallenge()
  {
    String result = JOptionPane.showInputDialog(this, "Please enter a challenge name...");
    
    if (result != null)
    {
      int created = this.speedRunControl.createNewChallenge(result);
      this.owner.openChallengeTab(created);
    }
  }

  public void createNewChoice()
  {
    String result = JOptionPane.showInputDialog(this, "Please enter a choice name...");
    
    if (result != null)
    {
      int created = this.speedRunControl.createNewChoice(result);
      this.owner.openChoiceTab(created);
    }
  }
}
