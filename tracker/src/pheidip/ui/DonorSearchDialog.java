package pheidip.ui;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pheidip.logic.DonorSearch;
import pheidip.objects.Donor;
import pheidip.objects.DonorSearchParams;
import pheidip.util.StringUtils;

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class DonorSearchDialog extends JDialog
{
  private DonorSearch searcher;
  private JTextField firstNameField;
  private JTextField lastNameField;
  private JTextField aliasField;
  private JTextField emailField;
  private JList donorList;
  private List<Donor> results;
  private JPanel contentPanel;
  private JLabel firstNameLabel;
  private JScrollPane donorScrollPane;
  private JLabel lastNameLabel;
  private JLabel aliasLabel;
  private JLabel emailLabel;
  private JButton okButton;
  private JButton cancelButton;
  private ActionHandler actionHandler;
  private FocusTraversalManager tabOrder;
  private JButton createNewButton;
  private JButton searchButton;
  private JButton prevButton;
  private JButton nextButton;
  private JCheckBox firstNameCheckBox;
  private JCheckBox lastNameCheckBox;
  private JCheckBox aliasCheckBox;
  private JCheckBox emailCheckBox;
  
  private void initializeGUI()
  {
    this.setTitle("Find Donor...");
    setBounds(100, 100, 527, 300);
    
    contentPanel = new JPanel();
    this.setContentPane(contentPanel);
    
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    
    GridBagLayout gbl_panel = new GridBagLayout();
    gbl_panel.columnWidths = new int[]{0, 0, 66, 112, 90, 76, 0};
    gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
    contentPanel.setLayout(gbl_panel);

    firstNameLabel = new JLabel("First Name:");
    GridBagConstraints gbc_lblFirstName = new GridBagConstraints();
    gbc_lblFirstName.anchor = GridBagConstraints.EAST;
    gbc_lblFirstName.insets = new Insets(0, 0, 5, 5);
    gbc_lblFirstName.gridx = 0;
    gbc_lblFirstName.gridy = 0;
    contentPanel.add(firstNameLabel, gbc_lblFirstName);
    
    firstNameCheckBox = new JCheckBox("");
    firstNameCheckBox.setSelected(true);
    GridBagConstraints gbc_firstNameCheckBox = new GridBagConstraints();
    gbc_firstNameCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_firstNameCheckBox.gridx = 1;
    gbc_firstNameCheckBox.gridy = 0;
    contentPanel.add(firstNameCheckBox, gbc_firstNameCheckBox);

    firstNameField = new JTextField();
    GridBagConstraints gbc_textField = new GridBagConstraints();
    gbc_textField.gridwidth = 2;
    gbc_textField.insets = new Insets(0, 0, 5, 5);
    gbc_textField.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField.gridx = 2;
    gbc_textField.gridy = 0;
    contentPanel.add(firstNameField, gbc_textField);
    firstNameField.setColumns(10);

    donorScrollPane = new JScrollPane();
    donorScrollPane.setViewportBorder(new LineBorder(new Color(0, 0, 0)));
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridheight = 7;
    gbc_scrollPane.gridwidth = 2;
    gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
    gbc_scrollPane.gridx = 4;
    gbc_scrollPane.gridy = 0;
    contentPanel.add(donorScrollPane, gbc_scrollPane);
    {
      donorList = new JList();
      donorScrollPane.setViewportView(donorList);
    }

    lastNameLabel = new JLabel("Last Name:");
    GridBagConstraints gbc_lblLastName = new GridBagConstraints();
    gbc_lblLastName.anchor = GridBagConstraints.EAST;
    gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
    gbc_lblLastName.gridx = 0;
    gbc_lblLastName.gridy = 1;
    contentPanel.add(lastNameLabel, gbc_lblLastName);
    
    lastNameCheckBox = new JCheckBox("");
    lastNameCheckBox.setSelected(true);
    GridBagConstraints gbc_lastNameCheckBox = new GridBagConstraints();
    gbc_lastNameCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_lastNameCheckBox.gridx = 1;
    gbc_lastNameCheckBox.gridy = 1;
    contentPanel.add(lastNameCheckBox, gbc_lastNameCheckBox);

    lastNameField = new JTextField();
    GridBagConstraints gbc_textField_1 = new GridBagConstraints();
    gbc_textField_1.gridwidth = 2;
    gbc_textField_1.insets = new Insets(0, 0, 5, 5);
    gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField_1.gridx = 2;
    gbc_textField_1.gridy = 1;
    contentPanel.add(lastNameField, gbc_textField_1);
    lastNameField.setColumns(10);

    aliasLabel = new JLabel("Alias:");
    GridBagConstraints gbc_lblAlias = new GridBagConstraints();
    gbc_lblAlias.anchor = GridBagConstraints.EAST;
    gbc_lblAlias.insets = new Insets(0, 0, 5, 5);
    gbc_lblAlias.gridx = 0;
    gbc_lblAlias.gridy = 2;
    contentPanel.add(aliasLabel, gbc_lblAlias);
    
    aliasCheckBox = new JCheckBox("");
    aliasCheckBox.setSelected(true);
    GridBagConstraints gbc_aliasCheckBox = new GridBagConstraints();
    gbc_aliasCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_aliasCheckBox.gridx = 1;
    gbc_aliasCheckBox.gridy = 2;
    contentPanel.add(aliasCheckBox, gbc_aliasCheckBox);

    aliasField = new JTextField();
    GridBagConstraints gbc_textField_2 = new GridBagConstraints();
    gbc_textField_2.gridwidth = 2;
    gbc_textField_2.insets = new Insets(0, 0, 5, 5);
    gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField_2.gridx = 2;
    gbc_textField_2.gridy = 2;
    contentPanel.add(aliasField, gbc_textField_2);
    aliasField.setColumns(10);

    emailLabel = new JLabel("E-mail:");
    GridBagConstraints gbc_lblEmail = new GridBagConstraints();
    gbc_lblEmail.anchor = GridBagConstraints.EAST;
    gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
    gbc_lblEmail.gridx = 0;
    gbc_lblEmail.gridy = 3;
    contentPanel.add(emailLabel, gbc_lblEmail);
    
    emailCheckBox = new JCheckBox("");
    emailCheckBox.setSelected(true);
    GridBagConstraints gbc_emailCheckBox = new GridBagConstraints();
    gbc_emailCheckBox.insets = new Insets(0, 0, 5, 5);
    gbc_emailCheckBox.gridx = 1;
    gbc_emailCheckBox.gridy = 3;
    contentPanel.add(emailCheckBox, gbc_emailCheckBox);

    emailField = new JTextField();
    GridBagConstraints gbc_textField_3 = new GridBagConstraints();
    gbc_textField_3.gridwidth = 2;
    gbc_textField_3.insets = new Insets(0, 0, 5, 5);
    gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
    gbc_textField_3.gridx = 2;
    gbc_textField_3.gridy = 3;
    contentPanel.add(emailField, gbc_textField_3);
    emailField.setColumns(10);
    
    searchButton = new JButton("Search");
    GridBagConstraints gbc_searchButton = new GridBagConstraints();
    gbc_searchButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_searchButton.insets = new Insets(0, 0, 5, 5);
    gbc_searchButton.gridx = 2;
    gbc_searchButton.gridy = 4;
    contentPanel.add(searchButton, gbc_searchButton);
    
    createNewButton = new JButton("Create New");
    GridBagConstraints gbc_createNewButton = new GridBagConstraints();
    gbc_createNewButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_createNewButton.insets = new Insets(0, 0, 5, 5);
    gbc_createNewButton.gridx = 2;
    gbc_createNewButton.gridy = 5;
    contentPanel.add(createNewButton, gbc_createNewButton);
    
    prevButton = new JButton("Previous");
    prevButton.setEnabled(false);
    GridBagConstraints gbc_prevButton = new GridBagConstraints();
    gbc_prevButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_prevButton.insets = new Insets(0, 0, 5, 5);
    gbc_prevButton.gridx = 4;
    gbc_prevButton.gridy = 7;
    contentPanel.add(prevButton, gbc_prevButton);
    
    nextButton = new JButton("Next");
    nextButton.setEnabled(false);
    GridBagConstraints gbc_nextButton = new GridBagConstraints();
    gbc_nextButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_nextButton.insets = new Insets(0, 0, 5, 0);
    gbc_nextButton.gridx = 5;
    gbc_nextButton.gridy = 7;
    contentPanel.add(nextButton, gbc_nextButton);

    okButton = new JButton("OK");
    okButton.setEnabled(false);
    GridBagConstraints gbc_okButton = new GridBagConstraints();
    gbc_okButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_okButton.insets = new Insets(0, 0, 0, 5);
    gbc_okButton.gridx = 4;
    gbc_okButton.gridy = 8;
    contentPanel.add(okButton, gbc_okButton);
    getRootPane().setDefaultButton(okButton);

    cancelButton = new JButton("Cancel");
    GridBagConstraints gbc_cancelButton = new GridBagConstraints();
    gbc_cancelButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_cancelButton.gridx = 5;
    gbc_cancelButton.gridy = 8;
    contentPanel.add(cancelButton, gbc_cancelButton);
  }
  
  private class ActionHandler implements ActionListener, ListSelectionListener
  {
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == okButton)
        {
          returnSelectedDonor();
        }
        else if (ev.getSource() == cancelButton)
        {
          closeDialog();
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
        else if (ev.getSource() == firstNameCheckBox ||
            ev.getSource() == lastNameCheckBox ||
            ev.getSource() == aliasCheckBox ||
            ev.getSource() == emailCheckBox)
        {
          updateUIState();
        }
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(DonorSearchDialog.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent ev)
    {
      if (ev.getSource() == donorList)
      {
        updateUIState();
      }
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    this.searchButton.addActionListener(this.actionHandler);
    this.createNewButton.addActionListener(this.actionHandler);
    this.okButton.addActionListener(this.actionHandler);
    this.cancelButton.addActionListener(this.actionHandler);
    this.prevButton.addActionListener(this.actionHandler);
    this.nextButton.addActionListener(this.actionHandler);
    this.donorList.addListSelectionListener(this.actionHandler);
    this.firstNameCheckBox.addActionListener(this.actionHandler);
    this.lastNameCheckBox.addActionListener(this.actionHandler);
    this.aliasCheckBox.addActionListener(this.actionHandler);
    this.emailCheckBox.addActionListener(this.actionHandler);
    
    this.getRootPane().setDefaultButton(this.searchButton);
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {
      this.firstNameCheckBox,
      this.firstNameField,
      this.lastNameCheckBox,
      this.lastNameField,
      this.aliasCheckBox,
      this.aliasField,
      this.emailCheckBox,
      this.emailField,
      this.searchButton,
      this.createNewButton,
      this.donorList,
      this.prevButton,
      this.nextButton,
      this.okButton,
      this.cancelButton,
    });

    this.setFocusTraversalPolicy(this.tabOrder);
  }

  public DonorSearchDialog(JDialog parent, DonorSearch searcher)
  {
    super(parent, true);
    this.init(searcher);
  }
  
  /**
   * @wbp.parser.constructor
   */
  public DonorSearchDialog(JFrame parent, DonorSearch searcher)
  {
    super(parent, true);
    this.init(searcher);
  }
  
  private void init(DonorSearch searcher)
  {
    this.searcher = searcher;
    this.results = new ArrayList<Donor>();
    
    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.updateUIState();
  }
  
  public Donor getResult()
  {
    return this.results == null ? null : this.results.size() > 0 ? this.results.iterator().next() : null;
  }
  
  public List<Donor> getResults()
  {
    return Collections.unmodifiableList(this.results);
  }
  
  private void createFromFields()
  {
    Donor result = this.searcher.createIfAble(new DonorSearchParams(    
        this.firstNameField.getText(),
        this.lastNameField.getText(),
        this.emailField.getText(),
        this.aliasField.getText()));
    
    this.results = new ArrayList<Donor>();
    this.results.add(result);
    this.closeDialog();
  }
  
  private void returnSelectedDonor()
  {
    this.results = new ArrayList<Donor>();
    
    if (this.donorList.getSelectedValue() == null)
    {
      JOptionPane.showMessageDialog(this, "No donor is selected.", "Error", JOptionPane.OK_OPTION);
    }
    else
    {
      for (int i : this.donorList.getSelectedIndices())
      {
        this.results.add((Donor) this.donorList.getModel().getElementAt(i));
      }

      this.closeDialog();
    }
  }

  private void closeDialog()
  {
    this.setVisible(false);
    this.dispose();
  }
  
  private void fillList(List<Donor> filtered)
  {
    DefaultListModel listData = new DefaultListModel();
    
    for (Donor d : filtered)
    {
      listData.addElement(d);
    }
    
    this.donorList.setModel(listData);
    this.updateUIState();
  }
  
  private void updateUIState()
  {
    this.okButton.setEnabled(!this.donorList.isSelectionEmpty());
    
    this.nextButton.setEnabled(this.searcher.getHasNext());
    this.prevButton.setEnabled(this.searcher.getHasPrevious());
    
    this.firstNameField.setEnabled(this.firstNameCheckBox.isSelected());
    this.lastNameField.setEnabled(this.lastNameCheckBox.isSelected());
    this.aliasField.setEnabled(this.aliasCheckBox.isSelected());
    this.emailField.setEnabled(this.emailCheckBox.isSelected());
  }
  
  private void moveNextResults()
  {
    this.fillList(this.searcher.moveNext());
  }
  
  private void movePrevResults()
  {
    this.fillList(this.searcher.movePrevious());
  }

  private void runSearch()
  {
    DonorSearchParams params = new DonorSearchParams(
        this.firstNameCheckBox.isSelected() ? StringUtils.nullIfEmpty(this.firstNameField.getText()) : null,
        this.lastNameCheckBox.isSelected() ? StringUtils.nullIfEmpty(this.lastNameField.getText()) : null,
        this.emailCheckBox.isSelected() ? StringUtils.nullIfEmpty(this.emailField.getText()) : null,
        this.aliasCheckBox.isSelected() ? StringUtils.nullIfEmpty(this.aliasField.getText()) : null);
    
    this.fillList(this.searcher.runSearch(params));
  }
}
