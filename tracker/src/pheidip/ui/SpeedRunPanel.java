package pheidip.ui;

import pheidip.logic.SpeedRunControl;
import pheidip.objects.Bid;
import pheidip.objects.SpeedRun;
import pheidip.util.StringUtils;

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
import java.util.List;

@SuppressWarnings("serial")
public class SpeedRunPanel extends TabPanel
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
  
  private void initializeGUIEvents()
  {
    refreshButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e) 
      {
        SpeedRunPanel.this.refreshContent();
      }
    });
    
    saveButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e) 
      {
        SpeedRunPanel.this.saveContent();
      }
    });
    
    openBidButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e) 
      {
        SpeedRunPanel.this.openSelectedBid();
      }
    });
    
    newChoiceButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e) 
      {

      }
    });
    
    newChallengeButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e) 
      {

      }
    });
    
    deleteButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent e) 
      {
        SpeedRunPanel.this.deleteSpeedRun();
      }
    });
    
    bidTable.addMouseListener(new MouseAdapter() 
    {
      @Override
      public void mouseClicked(MouseEvent e) 
      {
        if (e.getClickCount() == 2)
        {
          SpeedRunPanel.this.openSelectedBid();
        }
      }
    });
  }

  public SpeedRunPanel(MainWindow owner, SpeedRunControl control)
  {
    this.owner = owner;
    this.speedRunControl = control;
    
    this.initializeGUI();
    this.initializeGUIEvents();
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
    
    this.nameField.setText(data.getName());
    
    cachedRelatedBids = this.speedRunControl.getAllBids();
    
    CustomTableModel tableData = new CustomTableModel(new String[]
    {
        "Type",
        "Name",
    },0);
    
    for (Bid b : cachedRelatedBids)
    {
      tableData.addRow(
          new Object[]
          {
              StringUtils.symbolToNatural(b.getType().toString()),
              b.getName(),
          });
    }
    
    this.bidTable.setModel(tableData);
    
    this.setHeaderText("Run: " + data.getName());
  }
  
  private void saveContent()
  {
    this.speedRunControl.updateData(this.nameField.getText());
    this.refreshContent();
  }

  
  private void deleteSpeedRun()
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
    // TODO Auto-generated method stub
  }

  public int getSpeedRunId()
  {
    return this.speedRunControl.getSpeedRunId();
  }
}
