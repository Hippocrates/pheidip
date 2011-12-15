package pheidip.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import pheidip.logic.ChoiceControl;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceOption;
import pheidip.objects.BidState;
import pheidip.util.Pair;

import java.awt.Component;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class ChoicePanel extends EntityPanel
{
  private MainWindow owner;
  private ChoiceControl choiceControl;
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

  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{83, 93, 93, 0, 100, 85, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
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
    gbc_nameField.gridwidth = 3;
    gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
    gbc_nameField.gridx = 1;
    gbc_nameField.gridy = 0;
    add(nameField, gbc_nameField);
    nameField.setColumns(10);
    
    deleteChoiceButton = new JButton("Delete Choice");
    GridBagConstraints gbc_deleteChoiceButton = new GridBagConstraints();
    gbc_deleteChoiceButton.insets = new Insets(0, 0, 5, 0);
    gbc_deleteChoiceButton.gridx = 5;
    gbc_deleteChoiceButton.gridy = 0;
    add(deleteChoiceButton, gbc_deleteChoiceButton);
    
    stateLabel = new JLabel("State:");
    GridBagConstraints gbc_stateLabel = new GridBagConstraints();
    gbc_stateLabel.anchor = GridBagConstraints.EAST;
    gbc_stateLabel.insets = new Insets(0, 0, 5, 5);
    gbc_stateLabel.gridx = 0;
    gbc_stateLabel.gridy = 1;
    add(stateLabel, gbc_stateLabel);
    
    stateComboBox = new JComboBox(BidState.values());
    GridBagConstraints gbc_stateComboBox = new GridBagConstraints();
    gbc_stateComboBox.gridwidth = 3;
    gbc_stateComboBox.insets = new Insets(0, 0, 5, 5);
    gbc_stateComboBox.fill = GridBagConstraints.HORIZONTAL;
    gbc_stateComboBox.gridx = 1;
    gbc_stateComboBox.gridy = 1;
    add(stateComboBox, gbc_stateComboBox);
    
    lblDescription = new JLabel("Description");
    GridBagConstraints gbc_lblDescription = new GridBagConstraints();
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
    
    saveButton = new JButton("Save");
    GridBagConstraints gbc_saveButton = new GridBagConstraints();
    gbc_saveButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_saveButton.insets = new Insets(0, 0, 5, 5);
    gbc_saveButton.gridx = 1;
    gbc_saveButton.gridy = 3;
    add(saveButton, gbc_saveButton);
    
    refreshButton = new JButton("Refresh");
    GridBagConstraints gbc_refreshButton = new GridBagConstraints();
    gbc_refreshButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_refreshButton.insets = new Insets(0, 0, 5, 5);
    gbc_refreshButton.gridx = 2;
    gbc_refreshButton.gridy = 3;
    add(refreshButton, gbc_refreshButton);
    
    addOptionButton = new JButton("Add Option");
    GridBagConstraints gbc_addOptionButton = new GridBagConstraints();
    gbc_addOptionButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_addOptionButton.insets = new Insets(0, 0, 5, 5);
    gbc_addOptionButton.gridx = 1;
    gbc_addOptionButton.gridy = 5;
    add(addOptionButton, gbc_addOptionButton);
    
    renameOptionButton = new JButton("Rename Option");
    GridBagConstraints gbc_renameOptionButton = new GridBagConstraints();
    gbc_renameOptionButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_renameOptionButton.insets = new Insets(0, 0, 5, 5);
    gbc_renameOptionButton.gridx = 2;
    gbc_renameOptionButton.gridy = 5;
    add(renameOptionButton, gbc_renameOptionButton);
    
    deleteOptionButton = new JButton("Delete Option");
    GridBagConstraints gbc_deleteOptionButton = new GridBagConstraints();
    gbc_deleteOptionButton.insets = new Insets(0, 0, 5, 0);
    gbc_deleteOptionButton.gridx = 5;
    gbc_deleteOptionButton.gridy = 5;
    add(deleteOptionButton, gbc_deleteOptionButton);
    
    scrollPane = new JScrollPane();
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.gridwidth = 6;
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridx = 0;
    gbc_scrollPane.gridy = 6;
    add(scrollPane, gbc_scrollPane);
    
    optionTable = new JTable();
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
    
    this.descriptionTextArea.addKeyListener(new TabTraversalKeyListener(this.descriptionTextArea));
    
    this.optionTable.addKeyListener(new TabTraversalKeyListener(this.optionTable));
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {
        this.nameField,
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
  
  public ChoicePanel(MainWindow owner, ChoiceControl choiceControl)
  {
    this.owner = owner;
    this.choiceControl = choiceControl;

    this.initializeGUI();
    this.initializeGUIEvents();
  }

  @Override
  public void refreshContent()
  {
    this.choiceControl.refreshData();
    this.redrawContent();
  }

  @Override
  public void redrawContent()
  {
    Choice choice = this.choiceControl.getData();
    
    if (choice == null)
    {
      JOptionPane.showMessageDialog(this, "Error, this choice no longer exists", "Not Found", JOptionPane.ERROR_MESSAGE);
      this.owner.removeTab(this);
      return;
    }

    this.nameField.setText(choice.getName());
    this.descriptionTextArea.setText(choice.getDescription());
    this.stateComboBox.setSelectedItem(choice.getBidState());
    
    List<Pair<ChoiceOption,BigDecimal>> options = this.choiceControl.getOptionsWithTotals(true);
    
    CustomTableModel tableData = new CustomTableModel(new String[]
    {
        "Name",
        "Total Collected",
    },0);

    for(Pair<ChoiceOption,BigDecimal> option : options)
    {
      tableData.addRow(new Object[]
      {
        option.getFirst(),
        option.getSecond(),
      });
    }
    
    this.optionTable.setModel(tableData);
    
    this.setHeaderText(choice.toString());
  }
  
  public void saveContent()
  {
    Choice data = this.choiceControl.getData();
    data.setName(this.nameField.getText());
    data.setDescription(this.descriptionTextArea.getText());
    data.setBidState((BidState) this.stateComboBox.getSelectedItem());
    this.choiceControl.updateData(data);
    this.refreshContent();
  }
  
  private void addNewOption()
  {
    String name = JOptionPane.showInputDialog(this, "Please enter an option name.", "New Option", JOptionPane.OK_CANCEL_OPTION);
    
    if (name != null)
    {
      this.choiceControl.createNewOption(name);
      this.refreshContent();
    }
  }

  private ChoiceOption getSelectedOption()
  {
    int rowId = this.optionTable.getSelectedRow();
    ChoiceOption result = null;
    
    if (rowId != -1)
    {
      result = (ChoiceOption) this.optionTable.getValueAt(rowId, 0);
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
        this.choiceControl.deleteOption(selected);
        this.refreshContent();
      }
    }
  }

  @Override
  public void deleteContent()
  {
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this choice?", "Delete Choice?", JOptionPane.YES_NO_OPTION);
    
    if (result == JOptionPane.YES_OPTION)
    {
      this.choiceControl.deleteChoice();
      this.owner.removeTab(this);
    }
  }
  
  @Override
  public boolean confirmClose()
  {
    return true;
  }

  public int getChoiceId()
  {
    return this.choiceControl.getChoiceId();
  }
}
