package pheidip.ui;

import pheidip.logic.DonationTask;

import java.awt.Component;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
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
  private FocusTraversalManager tabOrder;
  private JButton refreshButton;
  private JButton nextButton;
  private ActionHandler actionHandler;
  private DonationPanel donationPanel;

  private void initializeGUI()
  {
    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[]{79, 90, 295, 0};
    gridBagLayout.rowHeights = new int[]{27, 319, 0};
    gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
    gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
    setLayout(gridBagLayout);
    
    refreshButton = new JButton("Refresh");
    GridBagConstraints gbc_refreshButton = new GridBagConstraints();
    gbc_refreshButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_refreshButton.insets = new Insets(0, 0, 5, 5);
    gbc_refreshButton.gridx = 0;
    gbc_refreshButton.gridy = 0;
    add(refreshButton, gbc_refreshButton);
    
    nextButton = new JButton("Mark As Done");
    GridBagConstraints gbc_nextButton = new GridBagConstraints();
    gbc_nextButton.insets = new Insets(0, 0, 5, 5);
    gbc_nextButton.fill = GridBagConstraints.HORIZONTAL;
    gbc_nextButton.gridx = 1;
    gbc_nextButton.gridy = 0;
    add(nextButton, gbc_nextButton);
    
    donationScrollPane = new JScrollPane();
    GridBagConstraints gbc_donationScrollPane = new GridBagConstraints();
    gbc_donationScrollPane.insets = new Insets(0, 0, 0, 5);
    gbc_donationScrollPane.gridwidth = 2;
    gbc_donationScrollPane.fill = GridBagConstraints.BOTH;
    gbc_donationScrollPane.gridx = 0;
    gbc_donationScrollPane.gridy = 1;
    add(donationScrollPane, gbc_donationScrollPane);
    
    donationList = new JList();
    donationScrollPane.setViewportView(donationList);
    
    donationPanel = new DonationPanel(this.owner, null);
    GridBagConstraints gbc_donationPanel = new GridBagConstraints();
    gbc_donationPanel.fill = GridBagConstraints.BOTH;
    gbc_donationPanel.gridx = 2;
    gbc_donationPanel.gridy = 1;
    add(donationPanel, gbc_donationPanel);
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
        if (!event.getValueIsAdjusting() && event.getSource() == donationList)
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
    
    List<Component> tabArray = new ArrayList<Component>();
    tabArray.add(this.refreshButton);
    tabArray.add(this.nextButton);
    tabArray.add(this.donationList);

    this.tabOrder = new FocusTraversalManager(tabArray.toArray(new Component[tabArray.size()]));
    this.setFocusTraversalPolicy(this.tabOrder);
    this.setFocusTraversalPolicyProvider(true);
    this.setFocusCycleRoot(true);
    
  }

  private void nextDonation()
  {
    Donation d = this.getSelectedDonation();
    
    int selectedIndex = this.donationList.getSelectedIndex();
    
    if (d != null)
    {
      this.task.clearTask(d);
    }
    
    if (selectedIndex + 1 == this.donationList.getModel().getSize())
    {
      this.donationList.clearSelection();
    }
    else
    {
      this.donationList.setSelectedIndex(selectedIndex + 1);
      this.donationList.ensureIndexIsVisible(selectedIndex + 1);
    }
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
      if (this.control == null || this.control.getDonationId() != d.getId())
      {
        this.control = this.task.getControl(d);
      }
    }
    else
    {
      this.control = null;
    }
    
    refreshDonationView();
  }
  
  public DonationTaskPanel(MainWindow owner, DonationTask task)
  {
    this.owner = owner;
    this.task = task;
    
    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.refreshContent();
  }

  @Override
  public boolean confirmClose()
  {
    return true;
  }
  
  @Override
  public void refreshContent()
  {
    Donation currentInstance = null;
    Donation oldInstance = this.getSelectedDonation();
    DefaultListModel listData = new DefaultListModel();
    List<Donation> pendingDonations = this.task.refreshTaskList();
    
    for (Donation d : pendingDonations)
    {
      listData.addElement(d);
      if (oldInstance != null && d.getId() == oldInstance.getId())
      {
        currentInstance = d;
      }
    }
    
    this.donationList.setModel(listData);
    
    if (currentInstance != null)
    {
      this.donationList.setSelectedValue(currentInstance, true);
    }
    else
    {
      this.donationList.setSelectedIndex(0);
      this.donationList.ensureIndexIsVisible(0);
    }
    
    this.redrawContent();
  }
    
  public void redrawContent()
  {
    this.setHeaderText(this.task.taskName());
    this.openDonation();
  }

  private void refreshDonationView()
  {
    if (this.control != null)
    {
      Donation data = this.control.getData();
      if (data == null)
      {
        JOptionPane.showMessageDialog(this, "This donation no longer exists", "Error", JOptionPane.OK_OPTION);
        this.control = null;
      }
      else
      {
        this.nextButton.setEnabled(!this.task.isTaskCleared(data));
      }
    }
    else
    {      
      this.nextButton.setEnabled(false);
    }
    
    this.donationPanel.setDonationControl(this.control);
    this.donationPanel.disableDeletion();
    this.donationPanel.redrawContent();
  }

  @Override
  public void saveContent()
  {
    if (this.control != null)
    { 
      this.donationPanel.saveContent();
    }
  }

  @Override
  public void deleteContent()
  {
    if (this.control != null)
    {
      this.donationPanel.deleteContent();
    }
  }
  
  public String getTaskName()
  {
    return this.task.taskName();
  }
}
