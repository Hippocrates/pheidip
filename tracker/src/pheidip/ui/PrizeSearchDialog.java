package pheidip.ui;

import java.awt.BorderLayout;
import java.awt.Component;

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
import pheidip.objects.PrizeSearchParams;
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
  private JCheckBox prizeNameCheckBox;
  private JButton prevButton;
  private JButton nextButton;
  private JLabel lblExcludeIfWon;

  private void initializeGUI()
  {
    this.setTitle("Select a prize...");
    setBounds(100, 100, 450, 300);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    GridBagLayout gbl_contentPanel = new GridBagLayout();
    gbl_contentPanel.columnWidths = new int[]{72, 0, 0, 91, 98, 86, 0};
    gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
    gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
    gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
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
      prizeNameCheckBox = new JCheckBox("");
      prizeNameCheckBox.setSelected(true);
      GridBagConstraints gbc_prizeNameCheckBox = new GridBagConstraints();
      gbc_prizeNameCheckBox.insets = new Insets(0, 0, 5, 5);
      gbc_prizeNameCheckBox.gridx = 1;
      gbc_prizeNameCheckBox.gridy = 0;
      contentPanel.add(prizeNameCheckBox, gbc_prizeNameCheckBox);
    }
    {
      nameField = new JTextField();
      GridBagConstraints gbc_nameField = new GridBagConstraints();
      gbc_nameField.gridwidth = 2;
      gbc_nameField.insets = new Insets(0, 0, 5, 5);
      gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
      gbc_nameField.gridx = 2;
      gbc_nameField.gridy = 0;
      contentPanel.add(nameField, gbc_nameField);
      nameField.setColumns(10);
    }
    {
      prizeScrollPane = new JScrollPane();
      GridBagConstraints gbc_prizeScrollPane = new GridBagConstraints();
      gbc_prizeScrollPane.insets = new Insets(0, 0, 5, 0);
      gbc_prizeScrollPane.gridwidth = 2;
      gbc_prizeScrollPane.gridheight = 5;
      gbc_prizeScrollPane.fill = GridBagConstraints.BOTH;
      gbc_prizeScrollPane.gridx = 4;
      gbc_prizeScrollPane.gridy = 0;
      contentPanel.add(prizeScrollPane, gbc_prizeScrollPane);
      {
        prizeList = new JList();
        prizeScrollPane.setViewportView(prizeList);
      }
    }
    {
      lblExcludeIfWon = new JLabel("Exclude If Won:");
      GridBagConstraints gbc_lblExcludeIfWon = new GridBagConstraints();
      gbc_lblExcludeIfWon.insets = new Insets(0, 0, 5, 5);
      gbc_lblExcludeIfWon.gridx = 0;
      gbc_lblExcludeIfWon.gridy = 1;
      contentPanel.add(lblExcludeIfWon, gbc_lblExcludeIfWon);
    }
    {
      excludeIfWonCheckBox = new JCheckBox("");
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
      gbc_searchButton.gridx = 2;
      gbc_searchButton.gridy = 2;
      contentPanel.add(searchButton, gbc_searchButton);
    }
    {
      createNewButton = new JButton("Create New...");
      createNewButton.setEnabled(false);
      GridBagConstraints gbc_createNewButton = new GridBagConstraints();
      gbc_createNewButton.fill = GridBagConstraints.HORIZONTAL;
      gbc_createNewButton.insets = new Insets(0, 0, 5, 5);
      gbc_createNewButton.gridx = 2;
      gbc_createNewButton.gridy = 3;
      contentPanel.add(createNewButton, gbc_createNewButton);
    }
    {
      prevButton = new JButton("Previous");
      prevButton.setEnabled(false);
      GridBagConstraints gbc_prevButton = new GridBagConstraints();
      gbc_prevButton.fill = GridBagConstraints.HORIZONTAL;
      gbc_prevButton.insets = new Insets(0, 0, 5, 5);
      gbc_prevButton.gridx = 4;
      gbc_prevButton.gridy = 5;
      contentPanel.add(prevButton, gbc_prevButton);
    }
    {
      nextButton = new JButton("Next");
      nextButton.setEnabled(false);
      GridBagConstraints gbc_nextButton = new GridBagConstraints();
      gbc_nextButton.fill = GridBagConstraints.HORIZONTAL;
      gbc_nextButton.insets = new Insets(0, 0, 5, 0);
      gbc_nextButton.gridx = 5;
      gbc_nextButton.gridy = 5;
      contentPanel.add(nextButton, gbc_nextButton);
    }
    {
      okButton = new JButton("OK");
      GridBagConstraints gbc_okButton = new GridBagConstraints();
      gbc_okButton.fill = GridBagConstraints.HORIZONTAL;
      gbc_okButton.insets = new Insets(0, 0, 0, 5);
      gbc_okButton.gridx = 4;
      gbc_okButton.gridy = 6;
      contentPanel.add(okButton, gbc_okButton);
      okButton.setEnabled(false);
      okButton.setActionCommand("OK");
      getRootPane().setDefaultButton(okButton);
    }
    {
      cancelButton = new JButton("Cancel");
      GridBagConstraints gbc_cancelButton = new GridBagConstraints();
      gbc_cancelButton.fill = GridBagConstraints.HORIZONTAL;
      gbc_cancelButton.gridx = 5;
      gbc_cancelButton.gridy = 6;
      contentPanel.add(cancelButton, gbc_cancelButton);
      cancelButton.setActionCommand("Cancel");
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
        else if (ev.getSource() == nextButton)
        {
          moveNextResults();
        }
        else if (ev.getSource() == prevButton)
        {
          movePrevResults();
        }
        else if (ev.getSource() == prizeNameCheckBox)
        {
          updateUIState();
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
      if (ev.getSource() == prizeList)
      {
          updateUIState();
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
    this.createNewButton.addActionListener(this.actionHandler);
    this.nextButton.addActionListener(this.actionHandler);
    this.prevButton.addActionListener(this.actionHandler);
    
    this.prizeNameCheckBox.addActionListener(this.actionHandler);
    
    this.getRootPane().setDefaultButton(this.searchButton);
    
    this.traversalManager = new FocusTraversalManager(new Component[]
    {
      this.nameField,
      this.prizeList,
      this.createNewButton,
      this.prizeList,
      this.prevButton,
      this.nextButton,
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
    
    this.updateUIState();
  }
  
  public Prize getSelectedPrize()
  {
    return this.selectedPrize;
  }

  private void moveNextResults()
  {
    this.fillList(this.searcher.getNext());
  }
  
  private void movePrevResults()
  {
    this.fillList(this.searcher.getPrev());
  }
  
  private void fillList(List<Prize> filtered)
  {
    DefaultListModel listData = new DefaultListModel();
    
    for (Prize p : filtered)
    {
      listData.addElement(p);
    }
    
    this.prizeList.setModel(listData);
    this.updateUIState();
  }
  
  private void updateUIState()
  {
    this.okButton.setEnabled(!this.prizeList.isSelectionEmpty());
    
    this.nameField.setEnabled(this.prizeNameCheckBox.isSelected());
    
    this.nextButton.setEnabled(this.searcher.hasNext());
    this.prevButton.setEnabled(this.searcher.hasPrev());
  }

  private void runSearch()
  {
    PrizeSearchParams params = new PrizeSearchParams(this.prizeNameCheckBox.isSelected() ? StringUtils.nullIfEmpty(this.nameField.getText()) : null, this.excludeIfWonCheckBox.isSelected());
    
    this.fillList(this.searcher.runSearch(params));
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
