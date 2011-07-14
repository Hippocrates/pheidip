package pheidip.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pheidip.logic.PrizeControl;
import pheidip.objects.Donor;
import pheidip.objects.Prize;

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class PrizePanel extends EntityPanel
{
  private MainWindow owner;
  private PrizeControl control;
  
  private ActionHandler actionHandler;
  private JTextField nameField;
  private JButton refreshButton;
  private JButton deletePrizeButton;
  private JLabel lblName;
  private JButton saveButton;
  private JLabel lblDescription;
  private JScrollPane scrollPane;
  private JTextArea descriptionTextArea;
  private JLabel lblWinner;
  private JTextField winnerField;
  private JButton openDonorButton;
  private JButton assignWinnerButton;
  private JButton removeWinnerButton;
  private JButton manualAssignButton;
  private FocusTraversalManager tabOrder;
  private JTextField imageURLField;
  private JLabel lblImageUrl;

  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{90, 106, 97, 0, 88, 65, 79, 0};
    gridBagLayout.rowHeights = new int[]{21, 21, 0, 118, 0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    lblName = new JLabel("Name:");
    GridBagConstraints gbc_lblName = new GridBagConstraints();
    gbc_lblName.anchor = GridBagConstraints.EAST;
    gbc_lblName.insets = new Insets(0, 0, 5, 5);
    gbc_lblName.gridx = 0;
    gbc_lblName.gridy = 0;
    add(lblName, gbc_lblName);
    
    nameField = new JTextField();
    GridBagConstraints gbc_nameField = new GridBagConstraints();
    gbc_nameField.gridwidth = 3;
    gbc_nameField.insets = new Insets(0, 0, 5, 5);
    gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
    gbc_nameField.gridx = 1;
    gbc_nameField.gridy = 0;
    add(nameField, gbc_nameField);
    nameField.setColumns(10);
    
    refreshButton = new JButton("Refresh");
    GridBagConstraints gbc_refreshButton = new GridBagConstraints();
    gbc_refreshButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_refreshButton.insets = new Insets(0, 0, 5, 5);
    gbc_refreshButton.gridx = 4;
    gbc_refreshButton.gridy = 0;
    add(refreshButton, gbc_refreshButton);
    
    deletePrizeButton = new JButton("Delete Prize");
    GridBagConstraints gbc_deletePrizeButton = new GridBagConstraints();
    gbc_deletePrizeButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_deletePrizeButton.insets = new Insets(0, 0, 5, 0);
    gbc_deletePrizeButton.gridx = 6;
    gbc_deletePrizeButton.gridy = 0;
    add(deletePrizeButton, gbc_deletePrizeButton);
    
    lblImageUrl = new JLabel("Image URL:");
    GridBagConstraints gbc_lblImageUrl = new GridBagConstraints();
    gbc_lblImageUrl.insets = new Insets(0, 0, 5, 5);
    gbc_lblImageUrl.anchor = GridBagConstraints.EAST;
    gbc_lblImageUrl.gridx = 0;
    gbc_lblImageUrl.gridy = 1;
    add(lblImageUrl, gbc_lblImageUrl);
    
    imageURLField = new JTextField();
    GridBagConstraints gbc_imageURLField = new GridBagConstraints();
    gbc_imageURLField.gridwidth = 3;
    gbc_imageURLField.insets = new Insets(0, 0, 5, 5);
    gbc_imageURLField.fill = GridBagConstraints.HORIZONTAL;
    gbc_imageURLField.gridx = 1;
    gbc_imageURLField.gridy = 1;
    add(imageURLField, gbc_imageURLField);
    imageURLField.setColumns(10);
    
    saveButton = new JButton("Save");
    GridBagConstraints gbc_saveButton = new GridBagConstraints();
    gbc_saveButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_saveButton.insets = new Insets(0, 0, 5, 5);
    gbc_saveButton.gridx = 4;
    gbc_saveButton.gridy = 1;
    add(saveButton, gbc_saveButton);
    
    lblDescription = new JLabel("Description:");
    GridBagConstraints gbc_lblDescription = new GridBagConstraints();
    gbc_lblDescription.anchor = GridBagConstraints.EAST;
    gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
    gbc_lblDescription.gridx = 0;
    gbc_lblDescription.gridy = 2;
    add(lblDescription, gbc_lblDescription);
    
    scrollPane = new JScrollPane();
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.gridheight = 2;
    gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
    gbc_scrollPane.gridwidth = 5;
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridx = 1;
    gbc_scrollPane.gridy = 2;
    add(scrollPane, gbc_scrollPane);
    
    descriptionTextArea = new JTextArea();
    descriptionTextArea.setLineWrap(true);
    descriptionTextArea.setWrapStyleWord(true);
    scrollPane.setViewportView(descriptionTextArea);
    
    lblWinner = new JLabel("Winner:");
    GridBagConstraints gbc_lblWinner = new GridBagConstraints();
    gbc_lblWinner.anchor = GridBagConstraints.EAST;
    gbc_lblWinner.insets = new Insets(0, 0, 5, 5);
    gbc_lblWinner.gridx = 0;
    gbc_lblWinner.gridy = 4;
    add(lblWinner, gbc_lblWinner);
    
    winnerField = new JTextField();
    GridBagConstraints gbc_winnerField = new GridBagConstraints();
    gbc_winnerField.gridwidth = 3;
    gbc_winnerField.insets = new Insets(0, 0, 5, 5);
    gbc_winnerField.fill = GridBagConstraints.HORIZONTAL;
    gbc_winnerField.gridx = 1;
    gbc_winnerField.gridy = 4;
    add(winnerField, gbc_winnerField);
    winnerField.setColumns(10);
    
    openDonorButton = new JButton("Open Donor");
    GridBagConstraints gbc_openDonorButton = new GridBagConstraints();
    gbc_openDonorButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_openDonorButton.insets = new Insets(0, 0, 5, 5);
    gbc_openDonorButton.gridx = 4;
    gbc_openDonorButton.gridy = 4;
    add(openDonorButton, gbc_openDonorButton);
    
    assignWinnerButton = new JButton("Assign");
    GridBagConstraints gbc_assignWinnerButton = new GridBagConstraints();
    gbc_assignWinnerButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_assignWinnerButton.insets = new Insets(0, 0, 5, 5);
    gbc_assignWinnerButton.gridx = 1;
    gbc_assignWinnerButton.gridy = 5;
    add(assignWinnerButton, gbc_assignWinnerButton);
    
    removeWinnerButton = new JButton("Remove");
    GridBagConstraints gbc_removeWinnerButton = new GridBagConstraints();
    gbc_removeWinnerButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_removeWinnerButton.insets = new Insets(0, 0, 5, 5);
    gbc_removeWinnerButton.gridx = 2;
    gbc_removeWinnerButton.gridy = 5;
    add(removeWinnerButton, gbc_removeWinnerButton);
    
    manualAssignButton = new JButton("Manual Assign");
    GridBagConstraints gbc_manualAssignButton = new GridBagConstraints();
    gbc_manualAssignButton.insets = new Insets(0, 0, 0, 5);
    gbc_manualAssignButton.gridx = 1;
    gbc_manualAssignButton.gridy = 6;
    add(manualAssignButton, gbc_manualAssignButton);
  }
  
  class ActionHandler implements ActionListener
  {
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == refreshButton)
        {
          refreshContent();
        }
        else if (ev.getSource() == saveButton)
        {
          saveContent();
        }
        else if (ev.getSource() == deletePrizeButton)
        {
          deleteContent();
        }
        else if (ev.getSource() == assignWinnerButton)
        {
          assignWinner();
        }
        else if (ev.getSource() == removeWinnerButton)
        {
          removeWinner();
        }
        else if (ev.getSource() == openDonorButton)
        {
          openWinner();
        }
        else if (ev.getSource() == manualAssignButton)
        {
          manuallyAssignWinner();
        }
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(PrizePanel.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    this.refreshButton.addActionListener(this.actionHandler);
    this.saveButton.addActionListener(this.actionHandler);
    this.deletePrizeButton.addActionListener(this.actionHandler);
    this.assignWinnerButton.addActionListener(this.actionHandler);
    this.removeWinnerButton.addActionListener(this.actionHandler);
    this.openDonorButton.addActionListener(this.actionHandler);
    this.manualAssignButton.addActionListener(this.actionHandler);
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {      
      this.nameField,
      this.imageURLField,
      this.descriptionTextArea,
      this.refreshButton,
      this.saveButton,
      this.assignWinnerButton,
      this.manualAssignButton,
      this.removeWinnerButton,
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
  
  public PrizePanel(MainWindow owner, PrizeControl control)
  {
    this.owner = owner;
    this.control = control;
    
    this.initializeGUI();
    this.initializeGUIEvents();
  }
  
  private void openWinner()
  {
    Donor winner = this.control.getPrizeWinner();
    
    if (winner != null)
    {
      this.owner.openDonorTab(winner.getId());
    }
  }
  
  private void manuallyAssignWinner()
  {
    DonorSearchDialog dialog = new DonorSearchDialog(this.owner, this.control.getDonorSearcher());
    dialog.setVisible(true);
    
    if (dialog.getResult() != null)
    {
      this.assignPrizeToDonor(dialog.getResult());
    }
  }
  
  private void assignPrizeToDonor(Donor d)
  {
    this.control.setPrizeWinner(d.getId());
    this.refreshContent();
  }
  
  private void assignWinner()
  {
    PrizeAssignmentDialog dialog = new PrizeAssignmentDialog(this.owner, this.control.getPrizeAssign());
    dialog.setVisible(true);
    
    if (dialog.getResult() != null)
    {
      this.assignPrizeToDonor(dialog.getResult());
    }
  }
  
  private void removeWinner()
  {
    int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to undo assigning this prize?", "Confirm removal", JOptionPane.YES_NO_OPTION);
  
    if (response == JOptionPane.YES_OPTION)
    {
      this.control.removePrizeWinner();
      this.refreshContent();
    }
  }
  
  public int getPrizeId()
  {
    return this.control.getPrizeId();
  }

  @Override
  public void refreshContent()
  {
    Prize data = this.control.getData();
    
    this.setHeaderText("Prize: " + data.getName());
    
    Donor winner = this.control.getPrizeWinner();
    
    this.nameField.setText(data.getName());
    this.imageURLField.setText(data.getImageURL());
    this.descriptionTextArea.setText(data.getDescription());
    
    if (winner != null)
    {
      this.winnerField.setText(winner.toString());
      this.assignWinnerButton.setEnabled(false);
      this.removeWinnerButton.setEnabled(true);
      this.openDonorButton.setEnabled(true);
      this.manualAssignButton.setEnabled(false);
    }
    else
    {
      this.winnerField.setText("");
      this.assignWinnerButton.setEnabled(true);
      this.removeWinnerButton.setEnabled(false);
      this.openDonorButton.setEnabled(false);
      this.manualAssignButton.setEnabled(true);
    }
  }

  @Override
  public void saveContent()
  {
    this.control.updateData(this.nameField.getText(), this.imageURLField.getText(), this.descriptionTextArea.getText());
    this.refreshContent();
  }

  @Override
  public void deleteContent()
  {
    this.control.deletePrize();
    this.owner.removeTab(this);
  }

  @Override
  public boolean confirmClose()
  {
    return true;
  }

}
