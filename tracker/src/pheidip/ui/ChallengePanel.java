package pheidip.ui;

import pheidip.logic.ChallengeControl;
import pheidip.objects.Challenge;

import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.math.BigDecimal;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class ChallengePanel extends EntityPanel
{
  private MainWindow owner;
  private ChallengeControl challengeControl;
  private JTextField nameField;
  private JTextField amountField;
  private JButton saveButton;
  private JButton refreshButton;
  private JLabel totalCollectedLabel;
  private JTextField totalAmountField;
  private JButton deleteChallengeButton;
  private JLabel nameLabel;
  private JLabel goalAmountLabel;
  private ActionHandler actionHandler;
  private FocusTraversalManager tabOrder;
  
  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{0, 107, 103, 32, 0, 93, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    nameLabel = new JLabel("Name:");
    GridBagConstraints gbc_nameLabel = new GridBagConstraints();
    gbc_nameLabel.insets = new Insets(0, 0, 5, 5);
    gbc_nameLabel.anchor = GridBagConstraints.EAST;
    gbc_nameLabel.gridx = 0;
    gbc_nameLabel.gridy = 0;
    add(nameLabel, gbc_nameLabel);
    
    nameField = new JTextField();
    GridBagConstraints gbc_nameField = new GridBagConstraints();
    gbc_nameField.gridwidth = 3;
    gbc_nameField.insets = new Insets(0, 0, 5, 5);
    gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
    gbc_nameField.gridx = 1;
    gbc_nameField.gridy = 0;
    add(nameField, gbc_nameField);
    nameField.setColumns(10);
    
    deleteChallengeButton = new JButton("Delete Challenge");
    GridBagConstraints gbc_deleteChallengeButton = new GridBagConstraints();
    gbc_deleteChallengeButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_deleteChallengeButton.insets = new Insets(0, 0, 5, 0);
    gbc_deleteChallengeButton.gridx = 5;
    gbc_deleteChallengeButton.gridy = 0;
    add(deleteChallengeButton, gbc_deleteChallengeButton);
    
    goalAmountLabel = new JLabel("Goal Amount:");
    GridBagConstraints gbc_goalAmountLabel = new GridBagConstraints();
    gbc_goalAmountLabel.anchor = GridBagConstraints.EAST;
    gbc_goalAmountLabel.insets = new Insets(0, 0, 5, 5);
    gbc_goalAmountLabel.gridx = 0;
    gbc_goalAmountLabel.gridy = 1;
    add(goalAmountLabel, gbc_goalAmountLabel);
    
    amountField = new JTextField();
    GridBagConstraints gbc_amountField = new GridBagConstraints();
    gbc_amountField.gridwidth = 3;
    gbc_amountField.insets = new Insets(0, 0, 5, 5);
    gbc_amountField.fill = GridBagConstraints.HORIZONTAL;
    gbc_amountField.gridx = 1;
    gbc_amountField.gridy = 1;
    add(amountField, gbc_amountField);
    amountField.setColumns(10);
    
    saveButton = new JButton("Save");
    GridBagConstraints gbc_saveButton = new GridBagConstraints();
    gbc_saveButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_saveButton.insets = new Insets(0, 0, 5, 5);
    gbc_saveButton.gridx = 1;
    gbc_saveButton.gridy = 2;
    add(saveButton, gbc_saveButton);
    
    refreshButton = new JButton("Refresh");
    GridBagConstraints gbc_refreshButton = new GridBagConstraints();
    gbc_refreshButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_refreshButton.insets = new Insets(0, 0, 5, 5);
    gbc_refreshButton.gridx = 2;
    gbc_refreshButton.gridy = 2;
    add(refreshButton, gbc_refreshButton);
    
    totalCollectedLabel = new JLabel("Total Collected:");
    GridBagConstraints gbc_totalCollectedLabel = new GridBagConstraints();
    gbc_totalCollectedLabel.anchor = GridBagConstraints.EAST;
    gbc_totalCollectedLabel.insets = new Insets(0, 0, 0, 5);
    gbc_totalCollectedLabel.gridx = 0;
    gbc_totalCollectedLabel.gridy = 4;
    add(totalCollectedLabel, gbc_totalCollectedLabel);
    
    totalAmountField = new JTextField();
    totalAmountField.setEditable(false);
    GridBagConstraints gbc_totalAmountField = new GridBagConstraints();
    gbc_totalAmountField.insets = new Insets(0, 0, 0, 5);
    gbc_totalAmountField.gridwidth = 3;
    gbc_totalAmountField.fill = GridBagConstraints.HORIZONTAL;
    gbc_totalAmountField.gridx = 1;
    gbc_totalAmountField.gridy = 4;
    add(totalAmountField, gbc_totalAmountField);
    totalAmountField.setColumns(10);
  }
  
  private class ActionHandler extends MouseAdapter implements ActionListener
  {
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == saveButton)
        {
          ChallengePanel.this.saveContent();
        }
        else if (ev.getSource() == refreshButton)
        {
          ChallengePanel.this.refreshContent();
        }
        else if (ev.getSource() == deleteChallengeButton)
        {
          ChallengePanel.this.deleteContent();
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
    
    this.saveButton.addActionListener(this.actionHandler);
    this.refreshButton.addActionListener(this.actionHandler);
    this.deleteChallengeButton.addActionListener(this.actionHandler);
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {
      this.amountField,
      this.saveButton,
      this.refreshButton,
    });
    this.setFocusTraversalPolicy(this.tabOrder);
  }
  
  public boolean isFocusCycleRoot()
  {
    return true;
  }
  
  public FocusTraversalPolicy getFocusTraversalPolicy() 
  {
    return this.tabOrder;
  }

  public ChallengePanel(MainWindow owner, ChallengeControl challengeControl)
  {
    this.owner = owner;
    this.challengeControl = challengeControl;
    
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
    Challenge data = this.challengeControl.getData();
    
    if (data == null)
    {
      JOptionPane.showMessageDialog(this, "Error, this challenge no longer exists", "Not Found", JOptionPane.ERROR_MESSAGE);
      this.owner.removeTab(this);
      return;
    }
    
    this.nameField.setText(data.getName());
    this.amountField.setText(data.getGoalAmount().toString());
    this.totalAmountField.setText(this.challengeControl.getTotalCollected().toString());
    
    this.setHeaderText(data.toString());
  }

  public int getChallengeId()
  {
    return this.challengeControl.getChallengeId();
  }

  public void deleteContent()
  {
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this challenge?", "Confirm delete", JOptionPane.YES_NO_OPTION);
    
    if (result == JOptionPane.YES_OPTION)
    {
      this.challengeControl.deleteChallenge();
      this.owner.removeTab(this);
    }
  }

  public void saveContent()
  {
    this.challengeControl.updateData(this.nameField.getText(), new BigDecimal(this.amountField.getText()));
  }
}
