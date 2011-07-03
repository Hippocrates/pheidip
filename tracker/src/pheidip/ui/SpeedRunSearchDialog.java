package pheidip.ui;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pheidip.logic.SpeedRunSearch;
import pheidip.objects.SpeedRun;

import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class SpeedRunSearchDialog extends JDialog
{
  private SpeedRunSearch searcher;
  private SpeedRun resultRun;
  private JTextField nameField;
  private JLabel nameLabel;
  private JList speedRunList;
  private JButton okButton;
  private JButton cancelButton;
  private ActionHandler actionHandler;
  private JScrollPane scrollPane;
  private FocusTraversalManager tabOrder;
  private JButton createNewButton;

  private void initializeGUI()
  {
    this.setTitle("Find Run...");
    setBounds(100, 100, 450, 300);
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{0, 235, 103, 102, 99, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
    getContentPane().setLayout(gridBagLayout);
    
    nameLabel = new JLabel("Name:");
    GridBagConstraints gbc_nameLabel = new GridBagConstraints();
    gbc_nameLabel.insets = new Insets(0, 0, 5, 5);
    gbc_nameLabel.anchor = GridBagConstraints.EAST;
    gbc_nameLabel.gridx = 0;
    gbc_nameLabel.gridy = 0;
    getContentPane().add(nameLabel, gbc_nameLabel);
    
    nameField = new JTextField();
    GridBagConstraints gbc_nameField = new GridBagConstraints();
    gbc_nameField.gridwidth = 2;
    gbc_nameField.insets = new Insets(0, 0, 5, 5);
    gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
    gbc_nameField.gridx = 1;
    gbc_nameField.gridy = 0;
    getContentPane().add(nameField, gbc_nameField);
    nameField.setColumns(10);
    
    scrollPane = new JScrollPane();
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.gridwidth = 2;
    gbc_scrollPane.gridheight = 3;
    gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridx = 3;
    gbc_scrollPane.gridy = 0;
    getContentPane().add(scrollPane, gbc_scrollPane);
    
    speedRunList = new JList();
    scrollPane.setViewportView(speedRunList);
    
    createNewButton = new JButton("Create New");
    GridBagConstraints gbc_createNewButton = new GridBagConstraints();
    gbc_createNewButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_createNewButton.insets = new Insets(0, 0, 5, 5);
    gbc_createNewButton.gridx = 1;
    gbc_createNewButton.gridy = 1;
    getContentPane().add(createNewButton, gbc_createNewButton);
    
    okButton = new JButton("OK");
    GridBagConstraints gbc_okButton = new GridBagConstraints();
    gbc_okButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_okButton.insets = new Insets(0, 0, 0, 5);
    gbc_okButton.gridx = 3;
    gbc_okButton.gridy = 3;
    getContentPane().add(okButton, gbc_okButton);
    
    cancelButton = new JButton("Cancel");
    GridBagConstraints gbc_cancelButton = new GridBagConstraints();
    gbc_cancelButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_cancelButton.gridx = 4;
    gbc_cancelButton.gridy = 3;
    getContentPane().add(cancelButton, gbc_cancelButton);
  }
  
  private class ActionHandler implements ActionListener, DocumentListener, ListSelectionListener
  {
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == okButton)
        {
          SpeedRunSearchDialog.this.returnSelectedRun();
        }
        else if (ev.getSource() == cancelButton)
        {
          SpeedRunSearchDialog.this.cancelSelection();
        }
        else if (ev.getSource() == createNewButton)
        {
          createFromFields();
        }
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(SpeedRunSearchDialog.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    
    private void documentChanged(DocumentEvent ev)
    {
      if (ev.getDocument() == nameField.getDocument())
      {
        SpeedRunSearchDialog.this.runFilters();
      }
    }

    public void changedUpdate(DocumentEvent ev)
    {
      this.documentChanged(ev);
    }

    public void insertUpdate(DocumentEvent ev)
    {
      this.documentChanged(ev);
    }

    public void removeUpdate(DocumentEvent ev)
    {
      this.documentChanged(ev);
    }

    @Override
    public void valueChanged(ListSelectionEvent ev)
    {
      if (ev.getSource() == speedRunList)
      {
        okButton.setEnabled(!speedRunList.isSelectionEmpty());
      }
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    this.nameField.getDocument().addDocumentListener(this.actionHandler);
    okButton.addActionListener(this.actionHandler);
    cancelButton.addActionListener(this.actionHandler);
    this.speedRunList.addListSelectionListener(this.actionHandler);
    this.createNewButton.addActionListener(this.actionHandler);
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {
      this.nameField,
      this.speedRunList,
      this.createNewButton,
      this.okButton,
      this.cancelButton,
    });
    this.setFocusTraversalPolicy(this.tabOrder);
  }

  /**
   * Create the dialog.
   */
  public SpeedRunSearchDialog(JFrame parent, SpeedRunSearch searcher)
  {
    super(parent, true);
    
    this.searcher = searcher;
    this.resultRun = null;
    
    this.initializeGUI();
    this.initializeGUIEvents();

    this.runFilters();
  }
  
  public SpeedRun getResult()
  {
    return this.resultRun;
  }
  
  private void createFromFields()
  {
    SpeedRun s = this.searcher.createIfAble(this.nameField.getText());
    
    this.resultRun = s;
    this.closeDialog();
  }
  
  private void returnSelectedRun()
  {
    SpeedRun result = (SpeedRun) this.speedRunList.getSelectedValue();
    if (result == null)
    {
      JOptionPane.showMessageDialog(this, "No run is selected.", "Error", JOptionPane.OK_OPTION);
    }
    else
    {
      this.resultRun = result;
      this.closeDialog();
    }
  }
  
  private void cancelSelection()
  {
    this.closeDialog();
  }
  
  private void closeDialog()
  {
    this.setVisible(false);
    this.dispose();
  }
  
  private void runFilters()
  {
    List<SpeedRun> filtered = this.searcher.searchSpeedRuns(this.nameField.getText());
    
    DefaultListModel listData = new DefaultListModel();
    
    for (SpeedRun s : filtered)
    {
      listData.addElement(s);
    }
    
    this.speedRunList.setModel(listData);
    this.speedRunList.setEnabled(this.speedRunList.getModel().getSize() > 0);
  }
}
