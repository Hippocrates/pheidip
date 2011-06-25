package pheidip.ui;

import pheidip.logic.ChallengeControl;
import pheidip.logic.ChoiceControl;
import pheidip.logic.SpeedRunControl;
import pheidip.objects.Bid;
import pheidip.objects.BidType;
import pheidip.objects.ChoiceOption;
import pheidip.objects.SpeedRun;
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

  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{94, 110, 116, 114, 85, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 131, 0};
    gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
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
    gbc_deleteButton.gridx = 4;
    gbc_deleteButton.gridy = 0;
    add(deleteButton, gbc_deleteButton);
    
    saveButton = new JButton("Save");
    GridBagConstraints gbc_saveButton = new GridBagConstraints();
    gbc_saveButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_saveButton.insets = new Insets(0, 0, 5, 5);
    gbc_saveButton.gridx = 1;
    gbc_saveButton.gridy = 1;
    add(saveButton, gbc_saveButton);
    
    refreshButton = new JButton("Refresh");
    GridBagConstraints gbc_refreshButton = new GridBagConstraints();
    gbc_refreshButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_refreshButton.insets = new Insets(0, 0, 5, 5);
    gbc_refreshButton.gridx = 1;
    gbc_refreshButton.gridy = 2;
    add(refreshButton, gbc_refreshButton);
    
    openBidButton = new JButton("Open Bid");
    GridBagConstraints gbc_openBidButton = new GridBagConstraints();
    gbc_openBidButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_openBidButton.insets = new Insets(0, 0, 5, 5);
    gbc_openBidButton.gridx = 2;
    gbc_openBidButton.gridy = 4;
    add(openBidButton, gbc_openBidButton);
    
    newChoiceButton = new JButton("New Choice");
    GridBagConstraints gbc_newChoiceButton = new GridBagConstraints();
    gbc_newChoiceButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_newChoiceButton.insets = new Insets(0, 0, 5, 5);
    gbc_newChoiceButton.gridx = 3;
    gbc_newChoiceButton.gridy = 4;
    add(newChoiceButton, gbc_newChoiceButton);
    
    newChallengeButton = new JButton("New Challenge");
    GridBagConstraints gbc_newChallengeButton = new GridBagConstraints();
    gbc_newChallengeButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_newChallengeButton.insets = new Insets(0, 0, 5, 0);
    gbc_newChallengeButton.gridx = 4;
    gbc_newChallengeButton.gridy = 4;
    add(newChallengeButton, gbc_newChallengeButton);
    
    bidsScrollPane = new JScrollPane();
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.gridwidth = 5;
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridx = 0;
    gbc_scrollPane.gridy = 5;
    add(bidsScrollPane, gbc_scrollPane);
    
    bidTable = new JTable();
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
      }
      catch(Exception e)
      {
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
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {
      this.nameField,
      this.saveButton,
      this.refreshButton,
      this.openBidButton,
      this.newChoiceButton,
      this.newChallengeButton,
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
    SpeedRun data = this.speedRunControl.getData();
    
    if (data == null)
    {
      JOptionPane.showMessageDialog(this, "Error, this run no longer exists", "Not Found", JOptionPane.ERROR_MESSAGE);
      this.owner.removeTab(this);
      return;
    }
    
    this.nameField.setText(data.getName());
    
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
        statusString = c.getTotalCollected().toString();
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
    this.speedRunControl.updateData(this.nameField.getText());
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