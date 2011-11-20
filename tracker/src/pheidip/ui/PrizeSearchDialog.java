package pheidip.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pheidip.logic.PrizeSearch;
import pheidip.objects.Prize;
import pheidip.util.StringUtils;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class PrizeSearchDialog extends JDialog
{
  private final JPanel contentPanel = new JPanel();
  private PrizeSearch searcher;
  private JTextField nameField;
  private ActionHandler actionHandler;
  private Prize selectedPrize;
  private JButton okButton;
  private JButton cancelButton;
  private JList prizeList;
  private JScrollPane prizeScrollPane;
  private JLabel nameLabel;
  private FocusTraversalManager traversalManager;
  private JButton createNewButton;
  private JButton searchButton;
  private JCheckBox excludeIfWonCheckBox;

  private void initializeGUI()
  {
    this.setTitle("Select a prize...");
    setBounds(100, 100, 450, 300);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    GridBagLayout gbl_contentPanel = new GridBagLayout();
    gbl_contentPanel.columnWidths = new int[]{72, 0, 91, 146, 0};
    gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
    gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
    gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
    contentPanel.setLayout(gbl_contentPanel);
    {
      nameLabel = new JLabel("Name:");
      GridBagConstraints gbc_nameLabel = new GridBagConstraints();
      gbc_nameLabel.insets = new Insets(0, 0, 5, 5);
      gbc_nameLabel.anchor = GridBagConstraints.EAST;
      gbc_nameLabel.gridx = 0;
      gbc_nameLabel.gridy = 0;
      contentPanel.add(nameLabel, gbc_nameLabel);
    }
    {
      nameField = new JTextField();
      GridBagConstraints gbc_nameField = new GridBagConstraints();
      gbc_nameField.gridwidth = 2;
      gbc_nameField.insets = new Insets(0, 0, 5, 5);
      gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
      gbc_nameField.gridx = 1;
      gbc_nameField.gridy = 0;
      contentPanel.add(nameField, gbc_nameField);
      nameField.setColumns(10);
    }
    {
      prizeScrollPane = new JScrollPane();
      GridBagConstraints gbc_prizeScrollPane = new GridBagConstraints();
      gbc_prizeScrollPane.gridheight = 5;
      gbc_prizeScrollPane.fill = GridBagConstraints.BOTH;
      gbc_prizeScrollPane.gridx = 3;
      gbc_prizeScrollPane.gridy = 0;
      contentPanel.add(prizeScrollPane, gbc_prizeScrollPane);
      {
        prizeList = new JList();
        prizeScrollPane.setViewportView(prizeList);
      }
    }
    {
      excludeIfWonCheckBox = new JCheckBox("Exclude If Won");
      GridBagConstraints gbc_excludeIfWonCheckBox = new GridBagConstraints();
      gbc_excludeIfWonCheckBox.insets = new Insets(0, 0, 5, 5);
      gbc_excludeIfWonCheckBox.gridx = 1;
      gbc_excludeIfWonCheckBox.gridy = 1;
      contentPanel.add(excludeIfWonCheckBox, gbc_excludeIfWonCheckBox);
    }
    {
      searchButton = new JButton("Search");
      GridBagConstraints gbc_searchButton = new GridBagConstraints();
      gbc_searchButton.fill = GridBagConstraints.HORIZONTAL;
      gbc_searchButton.insets = new Insets(0, 0, 5, 5);
      gbc_searchButton.gridx = 1;
      gbc_searchButton.gridy = 2;
      contentPanel.add(searchButton, gbc_searchButton);
    }
    {
      createNewButton = new JButton("Create New...");
      GridBagConstraints gbc_createNewButton = new GridBagConstraints();
      gbc_createNewButton.fill = GridBagConstraints.HORIZONTAL;
      gbc_createNewButton.insets = new Insets(0, 0, 5, 5);
      gbc_createNewButton.gridx = 1;
      gbc_createNewButton.gridy = 3;
      contentPanel.add(createNewButton, gbc_createNewButton);
    }
    {
      JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);
      {
        okButton = new JButton("OK");
        okButton.setEnabled(false);
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
      }
      {
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
      }
    }
  }
  
  class ActionHandler implements ActionListener, ListSelectionListener
  {
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == okButton)
        {
          returnSelectedPrize();
        }
        else if (ev.getSource() == createNewButton)
        {
          createFromFields();
        }
        else if (ev.getSource() == cancelButton)
        {
          cancelDialog();
        }
        else if (ev.getSource() == searchButton)
        {
          runSearch();
        }
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(PrizeSearchDialog.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }

    @Override
    public void valueChanged(ListSelectionEvent ev)
    {
      try
      {
        if (ev.getSource() == prizeList)
        {
          okButton.setEnabled(prizeList.getSelectedValue() != null);
        }
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(PrizeSearchDialog.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    this.searchButton.addActionListener(this.actionHandler);
    this.prizeList.addListSelectionListener(this.actionHandler);
    this.cancelButton.addActionListener(this.actionHandler);
    this.okButton.addActionListener(this.actionHandler);
    
    this.traversalManager = new FocusTraversalManager(new Component[]
    {
      this.nameField,
      this.prizeList,
      this.okButton,
      this.cancelButton,
    });
    
    this.setFocusTraversalPolicy(this.traversalManager);
  }
  
  public PrizeSearchDialog(JFrame parent, PrizeSearch searcher)
  {
    super(parent, true);
    
    this.searcher = searcher;
    this.selectedPrize = null;
    
    this.initializeGUI();
    this.initializeGUIEvents();
  }
  
  public Prize getSelectedPrize()
  {
    return this.selectedPrize;
  }
  
  private void runSearch()
  {
    List<Prize> filtered = this.searcher.searchPrizes(StringUtils.nullIfEmpty(this.nameField.getText()), this.excludeIfWonCheckBox.isSelected());
    
    DefaultListModel listData = new DefaultListModel();
    
    for (Prize p : filtered)
    {
      listData.addElement(p);
    }
    
    this.prizeList.setModel(listData);
  }
  
  private void createFromFields()
  {
    Prize p = this.searcher.createIfAble(this.nameField.getText());
    
    this.selectedPrize = p;
    this.closeDialog();
  }
  
  private void returnSelectedPrize()
  {
    this.selectedPrize = (Prize) this.prizeList.getSelectedValue();
    
    if (this.selectedPrize == null)
    {
      JOptionPane.showMessageDialog(this, "Error, no prize is selected.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    else
    {
      this.closeDialog();
    }
  }
  
  private void cancelDialog()
  {
    this.selectedPrize = null;
    this.closeDialog();
  }

  private void closeDialog()
  {
    this.setVisible(false);
    this.dispose();
  }
}
