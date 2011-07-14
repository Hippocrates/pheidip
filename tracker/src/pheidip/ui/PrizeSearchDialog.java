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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pheidip.logic.PrizeSearch;
import pheidip.objects.Prize;

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

  private void initializeGUI()
  {
    this.setTitle("Select a prize...");
    setBounds(100, 100, 450, 300);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    GridBagLayout gbl_contentPanel = new GridBagLayout();
    gbl_contentPanel.columnWidths = new int[]{87, 211, 137, 0};
    gbl_contentPanel.rowHeights = new int[]{0, 0, 0};
    gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
    gbl_contentPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
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
      gbc_prizeScrollPane.gridheight = 2;
      gbc_prizeScrollPane.insets = new Insets(0, 0, 5, 0);
      gbc_prizeScrollPane.fill = GridBagConstraints.BOTH;
      gbc_prizeScrollPane.gridx = 2;
      gbc_prizeScrollPane.gridy = 0;
      contentPanel.add(prizeScrollPane, gbc_prizeScrollPane);
      {
        prizeList = new JList();
        prizeScrollPane.setViewportView(prizeList);
      }
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
  
  class ActionHandler implements ActionListener, DocumentListener, ListSelectionListener
  {
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == okButton)
        {
          returnSelectedPrize();
        }
        else if (ev.getSource() == cancelButton)
        {
          cancelDialog();
        }
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(PrizeSearchDialog.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
    
    private void documentEvent(DocumentEvent ev)
    {
      try
      {
        if (ev.getDocument() == nameField.getDocument())
        {
          runFilters();
        }
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(PrizeSearchDialog.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }

    @Override
    public void changedUpdate(DocumentEvent ev)
    {
      this.documentEvent(ev);
    }

    @Override
    public void insertUpdate(DocumentEvent ev)
    {
      this.documentEvent(ev);
    }

    @Override
    public void removeUpdate(DocumentEvent ev)
    {
      this.documentEvent(ev);
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
    
    this.nameField.getDocument().addDocumentListener(this.actionHandler);
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
    
    this.runFilters();
  }
  
  public Prize getSelectedPrize()
  {
    return this.selectedPrize;
  }
  
  private void runFilters()
  {
    List<Prize> filtered = this.searcher.searchPrizes(this.nameField.getText());
    
    DefaultListModel listData = new DefaultListModel();
    
    for (Prize p : filtered)
    {
      listData.addElement(p);
    }
    
    this.prizeList.setModel(listData);
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
