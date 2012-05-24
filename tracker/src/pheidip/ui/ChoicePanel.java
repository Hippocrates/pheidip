package pheidip.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import pheidip.logic.EntityControlInstance;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceBid;
import pheidip.objects.ChoiceOption;
import pheidip.objects.BidState;
import pheidip.objects.SpeedRun;

import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;

import lombok.Getter;
import meta.reflect.MetaEntityReflector;

@SuppressWarnings("serial")
public class ChoicePanel extends EntityPanel
{
  private MainWindow owner;
  private EntityControlInstance<Choice> choiceControl;
  private ListTableModel<ChoiceOption> optionsTableData;
  
  private ActionHandler actionHandler;
  private JTextField nameField;
  private JTable optionTable;
  private JButton renameOptionButton;
  private JButton deleteOptionButton;
  private JScrollPane scrollPane;
  private JButton refreshButton;
  private JButton addOptionButton;
  private JButton saveButton;
  private JButton deleteChoiceButton;
  private JLabel nameLabel;
  private FocusTraversalManager tabOrder;
  private JScrollPane descriptionScrollPane;
  private JLabel lblDescription;
  private JTextArea descriptionTextArea;
  private JLabel stateLabel;
  private JComboBox stateComboBox;
  private JLabel lblRun;
  private JButton openDonationButton;
  private JScrollPane bidsTableScrollPane;
  private JTable bidsTable;

  private ListTableModel<ChoiceBid> bidsTableData;
  private EntitySelector<SpeedRun> runSelector;
  
  public int getChoiceId()
  {
    return this.choiceControl.getId();
  }

  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{83, 93, 99, 104, 54, 43, 85, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
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
    gbc_nameField.insets = new Insets(0, 0, 5, 5);
    gbc_nameField.gridwidth = 2;
    gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
    gbc_nameField.gridx = 1;
    gbc_nameField.gridy = 0;
    add(nameField, gbc_nameField);
    nameField.setColumns(10);
    
    deleteChoiceButton = new JButton("Delete Choice");
    GridBagConstraints gbc_deleteChoiceButton = new GridBagConstraints();
    gbc_deleteChoiceButton.insets = new Insets(0, 0, 5, 0);
    gbc_deleteChoiceButton.gridx = 6;
    gbc_deleteChoiceButton.gridy = 0;
    add(deleteChoiceButton, gbc_deleteChoiceButton);
    
    lblRun = new JLabel("Run:");
    GridBagConstraints gbc_lblRun = new GridBagConstraints();
    gbc_lblRun.anchor = GridBagConstraints.EAST;
    gbc_lblRun.insets = new Insets(0, 0, 5, 5);
    gbc_lblRun.gridx = 0;
    gbc_lblRun.gridy = 1;
    add(lblRun, gbc_lblRun);
    
    runSelector = new EntitySelector<SpeedRun>(this.owner, SpeedRun.class);
    runSelector.setNullSelectionAllowed(true);
    runSelector.setNavigationAllowed(true);
    GridBagConstraints gbc_runSelector = new GridBagConstraints();
    gbc_runSelector.gridwidth = 4;
    gbc_runSelector.insets = new Insets(0, 0, 5, 5);
    gbc_runSelector.fill = GridBagConstraints.BOTH;
    gbc_runSelector.gridx = 1;
    gbc_runSelector.gridy = 1;
    add(runSelector, gbc_runSelector);
    
    stateLabel = new JLabel("State:");
    GridBagConstraints gbc_stateLabel = new GridBagConstraints();
    gbc_stateLabel.anchor = GridBagConstraints.EAST;
    gbc_stateLabel.insets = new Insets(0, 0, 5, 5);
    gbc_stateLabel.gridx = 0;
    gbc_stateLabel.gridy = 2;
    add(stateLabel, gbc_stateLabel);
    
    stateComboBox = new JComboBox(BidState.values());
    GridBagConstraints gbc_stateComboBox = new GridBagConstraints();
    gbc_stateComboBox.gridwidth = 2;
    gbc_stateComboBox.insets = new Insets(0, 0, 5, 5);
    gbc_stateComboBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_stateComboBox.gridx = 1;
    gbc_stateComboBox.gridy = 2;
    add(stateComboBox, gbc_stateComboBox);
    
    openDonationButton = new JButton("Open Donation");
    GridBagConstraints gbc_openDonationButton = new GridBagConstraints();
    gbc_openDonationButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_openDonationButton.insets = new Insets(0, 0, 5, 5);
    gbc_openDonationButton.gridx = 4;
    gbc_openDonationButton.gridy = 2;
    add(openDonationButton, gbc_openDonationButton);
    
    lblDescription = new JLabel("Description");
    GridBagConstraints gbc_lblDescription = new GridBagConstraints();
    gbc_lblDescription.insets = new Insets(0, 0, 5, 5);
    gbc_lblDescription.gridx = 0;
    gbc_lblDescription.gridy = 3;
    add(lblDescription, gbc_lblDescription);
    
    descriptionScrollPane = new JScrollPane();
    descriptionScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    GridBagConstraints gbc_descriptionScrollPane = new GridBagConstraints();
    gbc_descriptionScrollPane.gridwidth = 3;
    gbc_descriptionScrollPane.insets = new Insets(0, 0, 5, 5);
    gbc_descriptionScrollPane.fill = GridBagConstraints.BOTH;
    gbc_descriptionScrollPane.gridx = 1;
    gbc_descriptionScrollPane.gridy = 3;
    add(descriptionScrollPane, gbc_descriptionScrollPane);
    
    descriptionTextArea = new JTextArea();
    descriptionTextArea.setWrapStyleWord(true);
    descriptionTextArea.setLineWrap(true);
    descriptionScrollPane.setViewportView(descriptionTextArea);
    
    bidsTableScrollPane = new JScrollPane();
    GridBagConstraints gbc_bidsTableScrollPane = new GridBagConstraints();
    gbc_bidsTableScrollPane.gridwidth = 3;
    gbc_bidsTableScrollPane.insets = new Insets(0, 0, 5, 0);
    gbc_bidsTableScrollPane.fill = GridBagConstraints.BOTH;
    gbc_bidsTableScrollPane.gridx = 4;
    gbc_bidsTableScrollPane.gridy = 3;
    add(bidsTableScrollPane, gbc_bidsTableScrollPane);
    
    bidsTable = new JTable();
    bidsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    bidsTableScrollPane.setViewportView(bidsTable);
    
    saveButton = new JButton("Save");
    GridBagConstraints gbc_saveButton = new GridBagConstraints();
    gbc_saveButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_saveButton.insets = new Insets(0, 0, 5, 5);
    gbc_saveButton.gridx = 1;
    gbc_saveButton.gridy = 4;
    add(saveButton, gbc_saveButton);
    
    refreshButton = new JButton("Refresh");
    GridBagConstraints gbc_refreshButton = new GridBagConstraints();
    gbc_refreshButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_refreshButton.insets = new Insets(0, 0, 5, 5);
    gbc_refreshButton.gridx = 2;
    gbc_refreshButton.gridy = 4;
    add(refreshButton, gbc_refreshButton);
    
    addOptionButton = new JButton("Add Option");
    GridBagConstraints gbc_addOptionButton = new GridBagConstraints();
    gbc_addOptionButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_addOptionButton.insets = new Insets(0, 0, 5, 5);
    gbc_addOptionButton.gridx = 1;
    gbc_addOptionButton.gridy = 6;
    add(addOptionButton, gbc_addOptionButton);
    
    renameOptionButton = new JButton("Rename Option");
    GridBagConstraints gbc_renameOptionButton = new GridBagConstraints();
    gbc_renameOptionButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_renameOptionButton.insets = new Insets(0, 0, 5, 5);
    gbc_renameOptionButton.gridx = 2;
    gbc_renameOptionButton.gridy = 6;
    add(renameOptionButton, gbc_renameOptionButton);
    
    deleteOptionButton = new JButton("Delete Option");
    GridBagConstraints gbc_deleteOptionButton = new GridBagConstraints();
    gbc_deleteOptionButton.insets = new Insets(0, 0, 5, 0);
    gbc_deleteOptionButton.gridx = 6;
    gbc_deleteOptionButton.gridy = 6;
    add(deleteOptionButton, gbc_deleteOptionButton);
    
    scrollPane = new JScrollPane();
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.gridwidth = 7;
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridx = 0;
    gbc_scrollPane.gridy = 7;
    add(scrollPane, gbc_scrollPane);
    
    optionTable = new JTable();
    optionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    scrollPane.setViewportView(optionTable);
  }
  
  private class ActionHandler extends MouseAdapter implements ActionListener
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
        else if (ev.getSource() == deleteChoiceButton)
        {
          deleteContent();
        }
        else if (ev.getSource() == renameOptionButton)
        {
          renameCurrentOption();
        }
        else if (ev.getSource() == deleteOptionButton)
        {
          deleteCurrentOption();
        }
        else if (ev.getSource() == addOptionButton)
        {
          addNewOption();
        }
        else if (ev.getSource() == openDonationButton)
        {
          openSelectedBid();
        }
      }
      catch(Exception e)
      {
        owner.report(e);
      }
    }
    
    @Override
    public void mouseClicked(MouseEvent event)
    {
      try
      {
        if (event.getSource() == optionTable)
        {
          updateUIState();
        }
        else if (event.getSource() == bidsTable)
        {
          if (event.getClickCount() == 2)
          {
            ChoicePanel.this.openSelectedBid();
          }
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
    this.addOptionButton.addActionListener(this.actionHandler);
    this.deleteOptionButton.addActionListener(this.actionHandler);
    this.renameOptionButton.addActionListener(this.actionHandler);
    this.deleteChoiceButton.addActionListener(this.actionHandler);
    this.optionTable.addMouseListener(this.actionHandler);
    
    this.openDonationButton.addActionListener(this.actionHandler);
    this.bidsTable.addMouseListener(this.actionHandler);
    
    this.descriptionTextArea.addKeyListener(new TabTraversalKeyListener(this.descriptionTextArea));
    
    this.optionTable.addKeyListener(new TabTraversalKeyListener(this.optionTable));
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {
        this.nameField,
        this.runSelector,
        this.stateComboBox,
        this.descriptionTextArea,
        this.saveButton,
        this.refreshButton,
        this.addOptionButton,
        this.renameOptionButton,
        this.deleteOptionButton,
        this.optionTable,
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
  
  public ChoicePanel(MainWindow owner, Choice choice)
  {
    this.owner = owner;
    this.choiceControl = new EntityControlInstance<Choice>(this.owner.getInstance().getEntityControl(Choice.class), choice);

    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.refreshContent();
  }

  @Override
  public void refreshContent()
  {
    this.choiceControl.refreshInstance();

    if (!this.choiceControl.isValid())
    {
      JOptionPane.showMessageDialog(this, "Error, this choice no longer exists", "Not Found", JOptionPane.ERROR_MESSAGE);
      this.owner.removeTab(this);
      return;
    }
    
    Choice data = this.choiceControl.getInstance();

    this.nameField.setText(data.getName());
    this.descriptionTextArea.setText(data.getDescription());
    this.stateComboBox.setSelectedItem(data.getBidState());
    this.runSelector.setEntity(data.getSpeedRun());
    
    List<ChoiceOption> options = new ArrayList<ChoiceOption>(data.getOptions());
    
    Collections.sort(options, new Comparator<ChoiceOption>()
        {
          @Override
          public int compare(ChoiceOption x, ChoiceOption y)
          {
            return x.getTotalCollected().compareTo(y.getTotalCollected());
          }
        });
    
    this.optionsTableData = new ListTableModel<ChoiceOption>(ChoiceOption.class, "name", "totalCollected");
    this.optionsTableData.setCollection(options);
    
    this.optionTable.setModel(this.optionsTableData);

    this.setHeaderText(data.toString());
    
    this.updateUIState();
  }
  
  private void updateUIState()
  {
    ChoiceOption currentOption = this.getSelectedOption();
    
    this.bidsTableData = new ListTableModel<ChoiceBid>(ChoiceBid.class, 
      "amount",
      "donorName");
    
    if (currentOption == null)
    {
      this.bidsTable.setEnabled(false);
      this.deleteOptionButton.setEnabled(false);
      this.renameOptionButton.setEnabled(false);
      this.openDonationButton.setEnabled(false);
    }
    else
    {
      this.bidsTable.setEnabled(true);
      this.deleteOptionButton.setEnabled(true);
      this.renameOptionButton.setEnabled(true);
      this.openDonationButton.setEnabled(true);
      
      this.bidsTableData.setCollection(currentOption.getBids());
    }
      
    this.bidsTable.setModel(this.bidsTableData);
  }

  @Override
  public void redrawContent()
  {
    if (!this.choiceControl.isValid())
    {
      JOptionPane.showMessageDialog(this, "Error, this choice no longer exists", "Not Found", JOptionPane.ERROR_MESSAGE);
      this.owner.removeTab(this);
      return;
    }
    
    Choice data = this.choiceControl.getInstance();
    
    List<ChoiceOption> options = new ArrayList<ChoiceOption>(data.getOptions());
    
    Collections.sort(options, new Comparator<ChoiceOption>()
        {
          @Override
          public int compare(ChoiceOption x, ChoiceOption y)
          {
            return x.getTotalCollected().compareTo(y.getTotalCollected());
          }
        });
    
    this.optionsTableData = new ListTableModel<ChoiceOption>(ChoiceOption.class, "name", "totalCollected");
    this.optionsTableData.setCollection(options);
    
    this.optionTable.setModel(this.optionsTableData);
    
    this.updateUIState();
  }
  
  public void saveContent()
  {
    Choice data = this.choiceControl.getInstance();
    data.setName(this.nameField.getText());
    data.setDescription(this.descriptionTextArea.getText());
    data.setBidState((BidState) this.stateComboBox.getSelectedItem());
    data.setSpeedRun(this.runSelector.getEntity());
    this.choiceControl.saveInstance();
    this.refreshContent();
  }

  public void openSelectedBid()
  {
    int selectedRow = this.bidsTable.getSelectedRow();
      
    if (selectedRow != -1)
    {
      this.owner.openDonationTab(this.bidsTableData.getRow(selectedRow).getDonation());
    }
  }
  
  private void addNewOption()
  {
    String name = JOptionPane.showInputDialog(this, "Please enter an option name.", "New Option", JOptionPane.OK_CANCEL_OPTION);
    
    if (name != null)
    {
      ChoiceOption added = new ChoiceOption();
      added.setName(name);
      added.setChoice(this.choiceControl.getInstance());
      added.setTotalCollected(new BigDecimal("0.00"));
      this.choiceControl.getInstance().getOptions().add(added);
      this.choiceControl.getControl().getDataAccess().saveInstance(MetaEntityReflector.getMetaEntity(ChoiceOption.class), added);
      this.redrawContent();
    }
  }

  private ChoiceOption getSelectedOption()
  {
    int rowId = this.optionTable.getSelectedRow();
    ChoiceOption result = null;
    
    if (rowId != -1)
    {
      result = (ChoiceOption) this.optionsTableData.getRow(rowId);
    }
    
    return result;
  }
  
  private void renameCurrentOption()
  {
    ChoiceOption selected = getSelectedOption();
    
    if (selected != null)
    {
      String name = JOptionPane.showInputDialog(this, "Please enter a new name for this option.", "Rename Option", JOptionPane.OK_CANCEL_OPTION);
      
      if (name != null)
      {
        selected.setName(name);
        this.redrawContent();
      }
    }
  }
  
  private void deleteCurrentOption()
  {
    ChoiceOption selected = getSelectedOption();
    
    if (selected != null)
    {
      int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this option?", "Delete Option?", JOptionPane.YES_NO_OPTION);
      
      if (result == JOptionPane.YES_OPTION)
      {
        this.choiceControl.getInstance().getOptions().remove(selected);
        this.redrawContent();
      }
    }
  }

  @Override
  public void deleteContent()
  {
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this choice?", "Delete Choice?", JOptionPane.YES_NO_OPTION);
    
    if (result == JOptionPane.YES_OPTION)
    {
      this.choiceControl.deleteInstance();
      this.owner.removeTab(this);
    }
  }
  
  @Override
  public boolean confirmClose()
  {
    return true;
  }
}
