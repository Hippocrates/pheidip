package pheidip.ui;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pheidip.logic.SpeedRunSearch;
import pheidip.objects.SpeedRun;
import pheidip.objects.SpeedRunSearchParams;
import pheidip.util.StringUtils;

import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class SpeedRunSearchDialog extends JDialog
{
  private SpeedRunSearch searcher;
  private List<SpeedRun> results;
  private JTextField nameField;
  private JLabel nameLabel;
  private JList speedRunList;
  private JButton okButton;
  private JButton cancelButton;
  private ActionHandler actionHandler;
  private JScrollPane scrollPane;
  private FocusTraversalManager tabOrder;
  private JButton createNewButton;
  private JButton searchButton;
  private JCheckBox nameFieldCheckBox;
  private JButton prevButton;
  private JButton nextButton;

  private void initializeGUI()
  {
    this.setTitle("Find Run...");
    setBounds(100, 100, 450, 300);
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{0, 0, 91, 103, 102, 99, 0};
    gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
    getContentPane().setLayout(gridBagLayout);
    
    nameLabel = new JLabel("Name:");
    GridBagConstraints gbc_nameLabel = new GridBagConstraints();
    gbc_nameLabel.insets = new Insets(0, 0, 5, 5);
    gbc_nameLabel.anchor = GridBagConstraints.EAST;
    gbc_nameLabel.gridx = 0;
    gbc_nameLabel.gridy = 0;
    getContentPane().add(nameLabel, gbc_nameLabel);
    
    nameFieldCheckBox = new JCheckBox("");
    nameFieldCheckBox.setSelected(true);
    GridBagConstraints gbc_nameFieldCheckBox = new GridBagConstraints();
    gbc_nameFieldCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_nameFieldCheckBox.gridx = 1;
    gbc_nameFieldCheckBox.gridy = 0;
    getContentPane().add(nameFieldCheckBox, gbc_nameFieldCheckBox);
    
    nameField = new JTextField();
    GridBagConstraints gbc_nameField = new GridBagConstraints();
    gbc_nameField.gridwidth = 2;
    gbc_nameField.insets = new Insets(0, 0, 5, 5);
    gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
    gbc_nameField.gridx = 2;
    gbc_nameField.gridy = 0;
    getContentPane().add(nameField, gbc_nameField);
    nameField.setColumns(10);
    
    scrollPane = new JScrollPane();
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.gridwidth = 2;
    gbc_scrollPane.gridheight = 4;
    gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridx = 4;
    gbc_scrollPane.gridy = 0;
    getContentPane().add(scrollPane, gbc_scrollPane);
    
    speedRunList = new JList();
    scrollPane.setViewportView(speedRunList);
    
    searchButton = new JButton("Search");
    GridBagConstraints gbc_searchButton = new GridBagConstraints();
    gbc_searchButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_searchButton.insets = new Insets(0, 0, 5, 5);
    gbc_searchButton.gridx = 2;
    gbc_searchButton.gridy = 1;
    getContentPane().add(searchButton, gbc_searchButton);
    
    createNewButton = new JButton("Create New");
    createNewButton.setEnabled(false);
    GridBagConstraints gbc_createNewButton = new GridBagConstraints();
    gbc_createNewButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_createNewButton.insets = new Insets(0, 0, 5, 5);
    gbc_createNewButton.gridx = 2;
    gbc_createNewButton.gridy = 2;
    getContentPane().add(createNewButton, gbc_createNewButton);
    
    prevButton = new JButton("Previous");
    prevButton.setEnabled(false);
    GridBagConstraints gbc_prevButton = new GridBagConstraints();
    gbc_prevButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_prevButton.insets = new Insets(0, 0, 5, 5);
    gbc_prevButton.gridx = 4;
    gbc_prevButton.gridy = 4;
    getContentPane().add(prevButton, gbc_prevButton);
    
    nextButton = new JButton("Next");
    nextButton.setEnabled(false);
    GridBagConstraints gbc_nextButton = new GridBagConstraints();
    gbc_nextButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_nextButton.insets = new Insets(0, 0, 5, 0);
    gbc_nextButton.gridx = 5;
    gbc_nextButton.gridy = 4;
    getContentPane().add(nextButton, gbc_nextButton);
    
    okButton = new JButton("OK");
    okButton.setEnabled(false);
    GridBagConstraints gbc_okButton = new GridBagConstraints();
    gbc_okButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_okButton.insets = new Insets(0, 0, 0, 5);
    gbc_okButton.gridx = 4;
    gbc_okButton.gridy = 5;
    getContentPane().add(okButton, gbc_okButton);
    
    cancelButton = new JButton("Cancel");
    GridBagConstraints gbc_cancelButton = new GridBagConstraints();
    gbc_cancelButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_cancelButton.gridx = 5;
    gbc_cancelButton.gridy = 5;
    getContentPane().add(cancelButton, gbc_cancelButton);
    
    this.pack();
  }
  
  private class ActionHandler implements ActionListener, ListSelectionListener, DocumentListener
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
        else if (ev.getSource() == searchButton)
        {
          runSearch();
        }
        else if (ev.getSource() == prevButton)
        {
          movePrevResults();
        }
        else if (ev.getSource() == nextButton)
        {
          moveNextResults();
        }
        else if (ev.getSource() == nameFieldCheckBox)
        {
          updateUIState();
        }
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(SpeedRunSearchDialog.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent ev)
    {
      if (ev.getSource() == speedRunList)
      {
        okButton.setEnabled(!speedRunList.isSelectionEmpty());
      }
    }
    
    private void documentEvent(DocumentEvent ev)
    {
      updateUIState();
    }

    @Override
    public void changedUpdate(DocumentEvent arg0)
    {
      this.documentEvent(arg0);
    }

    @Override
    public void insertUpdate(DocumentEvent arg0)
    {
      this.documentEvent(arg0);
    }

    @Override
    public void removeUpdate(DocumentEvent arg0)
    {
      this.documentEvent(arg0);
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    this.searchButton.addActionListener(this.actionHandler);
    okButton.addActionListener(this.actionHandler);
    cancelButton.addActionListener(this.actionHandler);
    this.speedRunList.addListSelectionListener(this.actionHandler);
    this.createNewButton.addActionListener(this.actionHandler);
    this.prevButton.addActionListener(this.actionHandler);
    this.nextButton.addActionListener(this.actionHandler);
    this.nameField.getDocument().addDocumentListener(this.actionHandler);
    this.nameFieldCheckBox.addActionListener(this.actionHandler);
    
    this.getRootPane().setDefaultButton(this.searchButton);
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {
      this.nameFieldCheckBox,
      this.nameField,
      this.searchButton,
      this.speedRunList,
      this.createNewButton,
      this.prevButton,
      this.nextButton,
      this.okButton,
      this.cancelButton,
    });
    this.setFocusTraversalPolicy(this.tabOrder);
  }

  /**
   * Create the dialog.
   * @wbp.parser.constructor
   */
  public SpeedRunSearchDialog(JFrame parent, SpeedRunSearch searcher)
  {
    super(parent, true);
    this.init(searcher);
  }
  
  public SpeedRunSearchDialog(JDialog parent, SpeedRunSearch searcher)
  {
    super(parent, true);
    
    this.init(searcher);
  }
  
  private void init(SpeedRunSearch searcher)
  {
    this.searcher = searcher;
    this.results = new ArrayList<SpeedRun>();
    
    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.updateUIState();
  }
  
  public SpeedRun getResult()
  {
    return this.results == null ? null : this.results.size() > 0 ? this.results.iterator().next() : null;
  }
  
  public List<SpeedRun> getResults()
  {
    return Collections.unmodifiableList(this.results);
  }
  
  private void createFromFields()
  {
    SpeedRun s = this.searcher.createIfAble(new SpeedRunSearchParams(this.nameField.getText()));
    
    this.results = new ArrayList<SpeedRun>();
    this.results.add(s);
    this.closeDialog();
  }
  
  private void returnSelectedRun()
  {
    this.results = new ArrayList<SpeedRun>();
    
    if (this.speedRunList.getSelectedValue() == null)
    {
      JOptionPane.showMessageDialog(this, "No run is selected.", "Error", JOptionPane.OK_OPTION);
    }
    else
    {
      for (int i : this.speedRunList.getSelectedIndices())
      {
        this.results.add((SpeedRun) this.speedRunList.getModel().getElementAt(i));
      }
      
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

  private void moveNextResults()
  {
    this.fillList(this.searcher.getNext());
  }
  
  private void movePrevResults()
  {
    this.fillList(this.searcher.getPrev());
  }
  
  private void fillList(List<SpeedRun> filtered)
  {
    DefaultListModel listData = new DefaultListModel();
    
    for (SpeedRun s : filtered)
    {
      listData.addElement(s);
    }
    
    this.speedRunList.setModel(listData);
    this.updateUIState(); 
  }
  
  private void updateUIState()
  {
    this.okButton.setEnabled(!this.speedRunList.isSelectionEmpty());
    this.nameField.setEnabled(this.nameFieldCheckBox.isSelected());
    this.createNewButton.setEnabled(!StringUtils.isEmptyOrNull(this.nameField.getText()));
    this.nextButton.setEnabled(this.searcher.hasNext());
    this.prevButton.setEnabled(this.searcher.hasPrev());
  }

  private void runSearch()
  {
    SpeedRunSearchParams params = new SpeedRunSearchParams(this.nameFieldCheckBox.isSelected() ? StringUtils.nullIfEmpty(this.nameField.getText()) : null);
    
    this.fillList(this.searcher.runSearch(params));
  }
}
