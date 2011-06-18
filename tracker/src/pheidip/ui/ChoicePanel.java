package pheidip.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import pheidip.logic.ChoiceControl;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceOption;

import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class ChoicePanel extends TabPanel
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
  private int[] optionTableIds;

  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{0, 93, 93, 0, 100, 85, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
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
    gbc_refreshButton.gridx = 2;
    gbc_refreshButton.gridy = 1;
    add(refreshButton, gbc_refreshButton);
    
    addOptionButton = new JButton("Add Option");
    GridBagConstraints gbc_addOptionButton = new GridBagConstraints();
    gbc_addOptionButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_addOptionButton.insets = new Insets(0, 0, 5, 5);
    gbc_addOptionButton.gridx = 1;
    gbc_addOptionButton.gridy = 3;
    add(addOptionButton, gbc_addOptionButton);
    
    renameOptionButton = new JButton("Rename Option");
    GridBagConstraints gbc_renameOptionButton = new GridBagConstraints();
    gbc_renameOptionButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_renameOptionButton.insets = new Insets(0, 0, 5, 5);
    gbc_renameOptionButton.gridx = 2;
    gbc_renameOptionButton.gridy = 3;
    add(renameOptionButton, gbc_renameOptionButton);
    
    deleteOptionButton = new JButton("Delete Option");
    GridBagConstraints gbc_deleteOptionButton = new GridBagConstraints();
    gbc_deleteOptionButton.insets = new Insets(0, 0, 5, 0);
    gbc_deleteOptionButton.gridx = 5;
    gbc_deleteOptionButton.gridy = 3;
    add(deleteOptionButton, gbc_deleteOptionButton);
    
    scrollPane = new JScrollPane();
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.gridwidth = 6;
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridx = 0;
    gbc_scrollPane.gridy = 4;
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
          deleteChoice();
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
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {
        this.nameField,
        this.saveButton,
        this.refreshButton,
        this.addOptionButton,
        this.renameOptionButton,
        this.deleteOptionButton
    });
    this.setFocusTraversalPolicy(this.tabOrder);
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
    Choice choice = this.choiceControl.getData();
    
    this.nameField.setText(choice.getName());
    
    List<ChoiceOption> donations = this.choiceControl.getOptions();
    
    CustomTableModel tableData = new CustomTableModel(new String[]
    {
        "Name",
        "Total Collected",
    },0);

    this.optionTableIds = new int[donations.size()];
    
    for(int i = 0; i < donations.size(); ++i)
    {
      ChoiceOption d = donations.get(i);
      this.optionTableIds[i] = d.getId();
      tableData.addRow(new Object[]
      {
        d.getName(),
        this.choiceControl.getOptionTotal(d.getId()),
      });
    }
    
    this.optionTable.setModel(tableData);
    
    this.setHeaderText(choice.toString());
  }
  
  private void saveContent()
  {
    this.choiceControl.updateData(this.nameField.getText());
    this.refreshContent();
  }
  
  private void addNewOption()
  {
    
  }
  
  private void renameCurrentOption()
  {
    
  }
  
  private void deleteCurrentOption()
  {
    
  }
  
  private void deleteChoice()
  {
    this.choiceControl.deleteChoice();
    this.owner.removeTab(this);
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
