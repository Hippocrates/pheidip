package pheidip.ui;

import pheidip.logic.DonationTask;

import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import pheidip.logic.DonationControl;
import pheidip.objects.Donation;

@SuppressWarnings("serial")
public class DonationTaskPanel extends EntityPanel
{
  private MainWindow owner;
  private DonationTask task;
  private DonationControl control;
  private JScrollPane donationScrollPane;
  private JList donationList;
  private JTextField amountField;
  private FocusTraversalManager tabOrder;
  private JButton refreshButton;
  private JButton nextButton;
  private JTextField donorTextField;
  private DonationBidsPanel donationBidsPanel;
  private ActionHandler actionHandler;
  private JTextArea commentTextArea;

  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{89, 88, 93, 92, 99, 0, 0};
    gridBagLayout.rowHeights = new int[]{27, 0, 223, 23, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    refreshButton = new JButton("Refresh");
    GridBagConstraints gbc_refreshButton = new GridBagConstraints();
    gbc_refreshButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_refreshButton.insets = new Insets(0, 0, 5, 5);
    gbc_refreshButton.gridx = 0;
    gbc_refreshButton.gridy = 0;
    add(refreshButton, gbc_refreshButton);
    
    nextButton = new JButton("Next");
    GridBagConstraints gbc_nextButton = new GridBagConstraints();
    gbc_nextButton.insets = new Insets(0, 0, 5, 5);
    gbc_nextButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_nextButton.gridx = 1;
    gbc_nextButton.gridy = 0;
    add(nextButton, gbc_nextButton);
    
    JLabel lblDonor = new JLabel("Donor:");
    GridBagConstraints gbc_lblDonor = new GridBagConstraints();
    gbc_lblDonor.anchor = GridBagConstraints.EAST;
    gbc_lblDonor.insets = new Insets(0, 0, 5, 5);
    gbc_lblDonor.gridx = 2;
    gbc_lblDonor.gridy = 0;
    add(lblDonor, gbc_lblDonor);
    
    donorTextField = new JTextField();
    donorTextField.setEditable(false);
    GridBagConstraints gbc_donorTextField = new GridBagConstraints();
    gbc_donorTextField.gridwidth = 2;
    gbc_donorTextField.insets = new Insets(0, 0, 5, 5);
    gbc_donorTextField.fill = GridBagConstraints.HORIZONTAL;
    gbc_donorTextField.gridx = 3;
    gbc_donorTextField.gridy = 0;
    add(donorTextField, gbc_donorTextField);
    donorTextField.setColumns(10);
    
    donationScrollPane = new JScrollPane();
    GridBagConstraints gbc_donationScrollPane = new GridBagConstraints();
    gbc_donationScrollPane.gridheight = 3;
    gbc_donationScrollPane.insets = new Insets(0, 0, 0, 5);
    gbc_donationScrollPane.gridwidth = 2;
    gbc_donationScrollPane.fill = GridBagConstraints.BOTH;
    gbc_donationScrollPane.gridx = 0;
    gbc_donationScrollPane.gridy = 1;
    add(donationScrollPane, gbc_donationScrollPane);
    
    donationList = new JList();
    donationScrollPane.setViewportView(donationList);
    
    JLabel lblComment = new JLabel("Comment:");
    GridBagConstraints gbc_lblComment = new GridBagConstraints();
    gbc_lblComment.insets = new Insets(0, 0, 5, 5);
    gbc_lblComment.gridx = 2;
    gbc_lblComment.gridy = 1;
    add(lblComment, gbc_lblComment);
    
    JLabel lblAmount = new JLabel("Amount:");
    GridBagConstraints gbc_lblAmount = new GridBagConstraints();
    gbc_lblAmount.anchor = GridBagConstraints.EAST;
    gbc_lblAmount.insets = new Insets(0, 0, 5, 5);
    gbc_lblAmount.gridx = 3;
    gbc_lblAmount.gridy = 1;
    add(lblAmount, gbc_lblAmount);
    
    amountField = new JTextField();
    amountField.setEditable(false);
    GridBagConstraints gbc_amountField = new GridBagConstraints();
    gbc_amountField.gridwidth = 2;
    gbc_amountField.anchor = GridBagConstraints.NORTH;
    gbc_amountField.insets = new Insets(0, 0, 5, 0);
    gbc_amountField.fill = GridBagConstraints.HORIZONTAL;
    gbc_amountField.gridx = 4;
    gbc_amountField.gridy = 1;
    add(amountField, gbc_amountField);
    amountField.setColumns(10);
    
    JScrollPane scrollPane = new JScrollPane();
    GridBagConstraints gbc_scrollPane = new GridBagConstraints();
    gbc_scrollPane.gridwidth = 4;
    gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
    gbc_scrollPane.fill = GridBagConstraints.BOTH;
    gbc_scrollPane.gridx = 2;
    gbc_scrollPane.gridy = 2;
    add(scrollPane, gbc_scrollPane);
    
    commentTextArea = new JTextArea();
    commentTextArea.setEditable(false);
    scrollPane.setViewportView(commentTextArea);
    commentTextArea.setWrapStyleWord(true);
    commentTextArea.setLineWrap(true);
    
    donationBidsPanel = new DonationBidsPanel(this.owner, null);
    GridBagConstraints gbc_donationBidsPanel = new GridBagConstraints();
    gbc_donationBidsPanel.gridwidth = 4;
    gbc_donationBidsPanel.fill = GridBagConstraints.BOTH;
    gbc_donationBidsPanel.gridx = 2;
    gbc_donationBidsPanel.gridy = 3;
    add(donationBidsPanel, gbc_donationBidsPanel);
  }
  
  private class ActionHandler implements ActionListener, ListSelectionListener
  {
    public void actionPerformed(ActionEvent event)
    {
      try
      {
        if (event.getSource() == nextButton)
        {
          nextDonation();
        }
        else if (event.getSource() == refreshButton)
        {
          refreshContent();
        }
      }
      catch (Exception e)
      {
        owner.report(e);
      }
    }

    @Override
    public void valueChanged(ListSelectionEvent event)
    {
      try
      {
        if (event.getSource() == donationList)
        {
          openDonation();
        }
      }
      catch (Exception e)
      {
        owner.report(e);
      }
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    this.nextButton.addActionListener(this.actionHandler);
    this.refreshButton.addActionListener(this.actionHandler);
    this.donationList.addListSelectionListener(this.actionHandler);
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {
      this.refreshButton,
      this.nextButton,
      this.donationList,
      this.donationBidsPanel,
    });
    this.setFocusTraversalPolicy(this.tabOrder);
    this.setFocusTraversalPolicyProvider(true);
    this.setFocusCycleRoot(true);
    
  }

  private void nextDonation()
  {
    this.donationList.setSelectedIndex(this.donationList.getSelectedIndex() + 1);
  }
  
  private Donation getSelectedDonation()
  {
    return (Donation) this.donationList.getSelectedValue();
  }
  
  private void openDonation()
  {
    Donation d = getSelectedDonation();
    
    if (d != null)
    {
      this.control = this.task.getControl(d.getId());
      this.task.clearTask(this.control.getDonationId());
      refreshDonationView();
    }
    else
    {
      this.control = null;
    }
  }
  
  public DonationTaskPanel(MainWindow owner, DonationTask task)
  {
    this.owner = owner;
    this.task = task;
    
    this.initializeGUI();
    this.initializeGUIEvents();
  }

  @Override
  public boolean confirmClose()
  {
    return true;
  }

  @Override
  public void refreshContent()
  {
    Donation current = this.getSelectedDonation();
    
    DefaultListModel listData = new DefaultListModel();
    List<Donation> pendingDonations = this.task.refreshTaskList();
    
    for (Donation d : pendingDonations)
    {
      listData.addElement(d);
    }
    
    this.donationList.setModel(listData);
    
    if (current != null)
    {
      this.donationList.setSelectedValue(current, true);
    }
    else
    {
      this.donationList.setSelectedIndex(0);
    }
    
    this.setHeaderText(this.task.taskName());
    
    this.refreshDonationView();
  }

  private void refreshDonationView()
  {
    if (this.control != null)
    {
      Donation data = this.control.getData();
      
      this.amountField.setText(data.getAmount().toString());
      this.donorTextField.setText(this.control.getDonationDonor().toString());
      this.commentTextArea.setText(data.getComment());
    }
    else
    {
      this.amountField.setText("");
      this.donorTextField.setText("");
      this.commentTextArea.setText("");
    }
    
    this.donationBidsPanel.setControl(this.control);
  }

  @Override
  public void saveContent()
  {
    // not used
  }

  @Override
  public void deleteContent()
  {
    // not used
  }
  
  public String getTaskName()
  {
    return this.task.taskName();
  }
}
