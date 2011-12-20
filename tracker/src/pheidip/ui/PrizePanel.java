package pheidip.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import pheidip.logic.PrizeControl;
import pheidip.objects.Donor;
import pheidip.objects.Prize;
import pheidip.objects.SpeedRun;
import pheidip.util.FormatUtils;

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.math.BigDecimal;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;
import javax.swing.JFormattedTextField;

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
  private Donor winner;
  private JLabel lblSortingKey;
  private JFormattedTextField sortKeyField;
  private JLabel lblTargetAmount;
  private JFormattedTextField targetAmountField;
  private JLabel lblStartGame;
  private JLabel lblEndGame;
  private JTextField startGameField;
  private JTextField endGameField;
  private JButton openStartGameButton;
  private JButton openEndGameButton;
  private JButton setStartGameButton;
  private JButton setEndGameButton;
  private JButton clearStartGameButton;
  private JButton clearEndGameButton;

  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{90, 106, 85, 97, 88, 68, 24, 0};
    gridBagLayout.rowHeights = new int[]{21, 21, 0, 0, 118, 0, 0, 0, 0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
    
    lblTargetAmount = new JLabel("Target Amount:");
    GridBagConstraints gbc_lblTargetAmount = new GridBagConstraints();
    gbc_lblTargetAmount.anchor = GridBagConstraints.EAST;
    gbc_lblTargetAmount.insets = new Insets(0, 0, 5, 5);
    gbc_lblTargetAmount.gridx = 0;
    gbc_lblTargetAmount.gridy = 2;
    add(lblTargetAmount, gbc_lblTargetAmount);
    
    targetAmountField = new JFormattedTextField(FormatUtils.getMoneyFormat());
    GridBagConstraints gbc_targetAmountField = new GridBagConstraints();
    gbc_targetAmountField.gridwidth = 2;
    gbc_targetAmountField.insets = new Insets(0, 0, 5, 5);
    gbc_targetAmountField.fill = GridBagConstraints.HORIZONTAL;
    gbc_targetAmountField.gridx = 1;
    gbc_targetAmountField.gridy = 2;
    add(targetAmountField, gbc_targetAmountField);
    
    lblDescription = new JLabel("Description:");
    GridBagConstraints gbc_lblDescription = new GridBagConstraints();
    gbc_lblDescription.anchor = GridBagConstraints.EAST;
    gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
    gbc_lblDescription.gridx = 0;
    gbc_lblDescription.gridy = 3;
    add(lblDescription, gbc_lblDescription);
    
    scrollPane = new JScrollPane();
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.gridheight = 2;
    gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
    gbc_scrollPane.gridwidth = 4;
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridx = 1;
    gbc_scrollPane.gridy = 3;
    add(scrollPane, gbc_scrollPane);
    
    descriptionTextArea = new JTextArea();
    descriptionTextArea.setLineWrap(true);
    descriptionTextArea.setWrapStyleWord(true);
    scrollPane.setViewportView(descriptionTextArea);
    
    lblSortingKey = new JLabel("Sorting Key:");
    GridBagConstraints gbc_lblSortingKey = new GridBagConstraints();
    gbc_lblSortingKey.anchor = GridBagConstraints.EAST;
    gbc_lblSortingKey.insets = new Insets(0, 0, 5, 5);
    gbc_lblSortingKey.gridx = 0;
    gbc_lblSortingKey.gridy = 5;
    add(lblSortingKey, gbc_lblSortingKey);
    
    sortKeyField = new JFormattedTextField(FormatUtils.getIntegerFormat());
    GridBagConstraints gbc_sortKeyField = new GridBagConstraints();
    gbc_sortKeyField.gridwidth = 2;
    gbc_sortKeyField.insets = new Insets(0, 0, 5, 5);
    gbc_sortKeyField.fill = GridBagConstraints.HORIZONTAL;
    gbc_sortKeyField.gridx = 1;
    gbc_sortKeyField.gridy = 5;
    add(sortKeyField, gbc_sortKeyField);
    
    lblStartGame = new JLabel("Start Game:");
    GridBagConstraints gbc_lblStartGame = new GridBagConstraints();
    gbc_lblStartGame.anchor = GridBagConstraints.EAST;
    gbc_lblStartGame.insets = new Insets(0, 0, 5, 5);
    gbc_lblStartGame.gridx = 0;
    gbc_lblStartGame.gridy = 6;
    add(lblStartGame, gbc_lblStartGame);
    
    startGameField = new JTextField();
    startGameField.setEditable(false);
    GridBagConstraints gbc_startGameField = new GridBagConstraints();
    gbc_startGameField.insets = new Insets(0, 0, 5, 5);
    gbc_startGameField.fill = GridBagConstraints.HORIZONTAL;
    gbc_startGameField.gridx = 1;
    gbc_startGameField.gridy = 6;
    add(startGameField, gbc_startGameField);
    startGameField.setColumns(10);
    
    openStartGameButton = new JButton("Open");
    GridBagConstraints gbc_openStartGameButton = new GridBagConstraints();
    gbc_openStartGameButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_openStartGameButton.insets = new Insets(0, 0, 5, 5);
    gbc_openStartGameButton.gridx = 2;
    gbc_openStartGameButton.gridy = 6;
    add(openStartGameButton, gbc_openStartGameButton);
    
    setStartGameButton = new JButton("Set...");
    GridBagConstraints gbc_setStartGameButton = new GridBagConstraints();
    gbc_setStartGameButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_setStartGameButton.insets = new Insets(0, 0, 5, 5);
    gbc_setStartGameButton.gridx = 3;
    gbc_setStartGameButton.gridy = 6;
    add(setStartGameButton, gbc_setStartGameButton);
    
    clearStartGameButton = new JButton("Clear");
    GridBagConstraints gbc_clearStartGameButton = new GridBagConstraints();
    gbc_clearStartGameButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_clearStartGameButton.insets = new Insets(0, 0, 5, 5);
    gbc_clearStartGameButton.gridx = 4;
    gbc_clearStartGameButton.gridy = 6;
    add(clearStartGameButton, gbc_clearStartGameButton);
    
    lblEndGame = new JLabel("End Game:");
    GridBagConstraints gbc_lblEndGame = new GridBagConstraints();
    gbc_lblEndGame.anchor = GridBagConstraints.EAST;
    gbc_lblEndGame.insets = new Insets(0, 0, 5, 5);
    gbc_lblEndGame.gridx = 0;
    gbc_lblEndGame.gridy = 7;
    add(lblEndGame, gbc_lblEndGame);
    
    endGameField = new JTextField();
    endGameField.setEditable(false);
    GridBagConstraints gbc_endGameField = new GridBagConstraints();
    gbc_endGameField.insets = new Insets(0, 0, 5, 5);
    gbc_endGameField.fill = GridBagConstraints.HORIZONTAL;
    gbc_endGameField.gridx = 1;
    gbc_endGameField.gridy = 7;
    add(endGameField, gbc_endGameField);
    endGameField.setColumns(10);
    
    openEndGameButton = new JButton("Open");
    openEndGameButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
      }
    });
    GridBagConstraints gbc_openEndGameButton = new GridBagConstraints();
    gbc_openEndGameButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_openEndGameButton.insets = new Insets(0, 0, 5, 5);
    gbc_openEndGameButton.gridx = 2;
    gbc_openEndGameButton.gridy = 7;
    add(openEndGameButton, gbc_openEndGameButton);
    
    setEndGameButton = new JButton("Set...");
    GridBagConstraints gbc_setEndGameButton = new GridBagConstraints();
    gbc_setEndGameButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_setEndGameButton.insets = new Insets(0, 0, 5, 5);
    gbc_setEndGameButton.gridx = 3;
    gbc_setEndGameButton.gridy = 7;
    add(setEndGameButton, gbc_setEndGameButton);
    
    clearEndGameButton = new JButton("Clear");
    GridBagConstraints gbc_clearEndGameButton = new GridBagConstraints();
    gbc_clearEndGameButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_clearEndGameButton.insets = new Insets(0, 0, 5, 5);
    gbc_clearEndGameButton.gridx = 4;
    gbc_clearEndGameButton.gridy = 7;
    add(clearEndGameButton, gbc_clearEndGameButton);
    
    lblWinner = new JLabel("Winner:");
    GridBagConstraints gbc_lblWinner = new GridBagConstraints();
    gbc_lblWinner.anchor = GridBagConstraints.EAST;
    gbc_lblWinner.insets = new Insets(0, 0, 5, 5);
    gbc_lblWinner.gridx = 0;
    gbc_lblWinner.gridy = 8;
    add(lblWinner, gbc_lblWinner);
    
    winnerField = new JTextField();
    winnerField.setEditable(false);
    GridBagConstraints gbc_winnerField = new GridBagConstraints();
    gbc_winnerField.gridwidth = 3;
    gbc_winnerField.insets = new Insets(0, 0, 5, 5);
    gbc_winnerField.fill = GridBagConstraints.HORIZONTAL;
    gbc_winnerField.gridx = 1;
    gbc_winnerField.gridy = 8;
    add(winnerField, gbc_winnerField);
    winnerField.setColumns(10);
    
    openDonorButton = new JButton("Open Donor");
    GridBagConstraints gbc_openDonorButton = new GridBagConstraints();
    gbc_openDonorButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_openDonorButton.insets = new Insets(0, 0, 5, 5);
    gbc_openDonorButton.gridx = 4;
    gbc_openDonorButton.gridy = 8;
    add(openDonorButton, gbc_openDonorButton);
    
    assignWinnerButton = new JButton("Assign");
    GridBagConstraints gbc_assignWinnerButton = new GridBagConstraints();
    gbc_assignWinnerButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_assignWinnerButton.insets = new Insets(0, 0, 5, 5);
    gbc_assignWinnerButton.gridx = 1;
    gbc_assignWinnerButton.gridy = 9;
    add(assignWinnerButton, gbc_assignWinnerButton);
    
    removeWinnerButton = new JButton("Remove");
    GridBagConstraints gbc_removeWinnerButton = new GridBagConstraints();
    gbc_removeWinnerButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_removeWinnerButton.insets = new Insets(0, 0, 5, 5);
    gbc_removeWinnerButton.gridx = 2;
    gbc_removeWinnerButton.gridy = 9;
    add(removeWinnerButton, gbc_removeWinnerButton);
    
    manualAssignButton = new JButton("Manual Assign");
    GridBagConstraints gbc_manualAssignButton = new GridBagConstraints();
    gbc_manualAssignButton.insets = new Insets(0, 0, 0, 5);
    gbc_manualAssignButton.gridx = 1;
    gbc_manualAssignButton.gridy = 10;
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
        else if (ev.getSource() == openStartGameButton)
        {
          openStartGame();
        }
        else if (ev.getSource() == openEndGameButton)
        {
          openEndGame();
        }
        else if (ev.getSource() == setStartGameButton)
        {
          setStartGame();
        }
        else if (ev.getSource() == setEndGameButton)
        {
          setEndGame();
        }
        else if (ev.getSource() == clearStartGameButton)
        {
          clearStartGame();
        }
        else if (ev.getSource() == clearEndGameButton)
        {
          clearEndGame();
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
    this.openStartGameButton.addActionListener(this.actionHandler);
    this.openEndGameButton.addActionListener(this.actionHandler);
    this.setStartGameButton.addActionListener(this.actionHandler);
    this.setEndGameButton.addActionListener(this.actionHandler);
    this.clearEndGameButton.addActionListener(this.actionHandler);
    this.clearStartGameButton.addActionListener(this.actionHandler);
    
    this.descriptionTextArea.addKeyListener(new TabTraversalKeyListener(this.descriptionTextArea));
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {      
      this.nameField,
      this.imageURLField,
      this.descriptionTextArea,
      this.sortKeyField,
      this.targetAmountField,
      this.openStartGameButton,
      this.setStartGameButton,
      this.clearStartGameButton,
      this.openEndGameButton,
      this.setEndGameButton,
      this.clearEndGameButton,
      this.refreshButton,
      this.saveButton,
      this.assignWinnerButton,
      this.manualAssignButton,
      this.removeWinnerButton,
    });
    
    this.setFocusTraversalPolicy(this.tabOrder);
    this.setFocusCycleRoot(true);
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
    Donor winner = this.control.getData().getWinner();
    
    if (winner != null)
    {
      this.owner.openDonorTab(winner.getId());
    }
  }
  
  private void openStartGame()
  {
    SpeedRun startGame = this.control.getData().getStartGame();
    
    if (startGame != null)
    {
      this.owner.openSpeedRunTab(startGame.getId());
    }
  }
  
  private void openEndGame()
  {
    SpeedRun endGame = this.control.getData().getEndGame();
    
    if (endGame != null)
    {
      this.owner.openSpeedRunTab(endGame.getId());
    }
  }
  
  private void setStartGame()
  {
    SpeedRunSearchDialog searcher = new SpeedRunSearchDialog(this.owner, this.control.getSpeedRunSearch());
    
    searcher.setVisible(true);
    
    if (searcher.getResult() != null)
    {
      this.control.setStartGame(searcher.getResult());
      this.redrawContent();
    }
  }
  
  private void setEndGame()
  {
    SpeedRunSearchDialog searcher = new SpeedRunSearchDialog(this.owner, this.control.getSpeedRunSearch());
    
    searcher.setVisible(true);
    
    if (searcher.getResult() != null)
    {
      this.control.setEndGame(searcher.getResult());
      this.redrawContent();
    }
  }
  
  
  private void clearStartGame()
  {
    this.control.setStartGame(null);
    this.redrawContent();
  }
  
  private void clearEndGame()
  {
    this.control.setEndGame(null);
    this.redrawContent();
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
    this.control.getData().setWinner(d);
    this.redrawContent();
  }
  
  private void assignWinner()
  {
    BigDecimal targetAmount = new BigDecimal(this.targetAmountField.getText());
    
    PrizeAssignmentDialog dialog = new PrizeAssignmentDialog(this.owner, this.control.getPrizeAssign(), targetAmount);
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
    this.control.refreshData();
    this.redrawContent();
  }
  
  @Override
  public void redrawContent()
  {
    Prize data = this.control.getData();
    
    this.setHeaderText("Prize: " + data.toString());
    
    this.winner = data.getWinner();
    
    this.nameField.setText(data.getName());
    this.imageURLField.setText(data.getImageURL());
    this.descriptionTextArea.setText(data.getDescription());
    this.sortKeyField.setText(""+data.getSortKey());
    this.targetAmountField.setText(data.getMinimumBid().toString());
    this.startGameField.setText(data.getStartGame() != null ? data.getStartGame().toString() : "");
    this.endGameField.setText(data.getEndGame() != null ? data.getEndGame().toString() : "");
    
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
    Prize data = this.control.getData();
    data.setName(this.nameField.getText());
    data.setImageURL(this.imageURLField.getText());
    data.setDescription(this.descriptionTextArea.getText());
    data.setSortKey(Integer.parseInt(this.sortKeyField.getText()));
    data.setMinimumBid(new BigDecimal(this.targetAmountField.getText()));
    data.setWinner(this.winner);
    this.control.updateData(data);
    this.refreshContent();
  }

  @Override
  public void deleteContent()
  {
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this prize?", "Confirm delete", JOptionPane.YES_NO_OPTION);
    
    if (result == JOptionPane.OK_OPTION)
    {
      this.control.deletePrize();
      this.owner.removeTab(this);
    }
  }

  @Override
  public boolean confirmClose()
  {
    return true;
  }

}
