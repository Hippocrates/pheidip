package pheidip.ui;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JMenuBar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import pheidip.logic.ChallengeControl;
import pheidip.logic.ChipinDocumentSource;
import pheidip.logic.ChipinFileDocumentSource;
import pheidip.logic.ChipinMergeProcess;
import pheidip.logic.ChipinTextDocumentSource;
import pheidip.logic.ChipinWebsiteDocumentSource;
import pheidip.logic.ChoiceControl;
import pheidip.logic.DonationControl;
import pheidip.logic.DonorControl;
import pheidip.logic.DonorSearch;
import pheidip.logic.ProgramInstance;
import pheidip.logic.SpeedRunControl;
import pheidip.logic.SpeedRunSearch;
import pheidip.objects.Donor;
import pheidip.objects.SpeedRun;
import pheidip.util.Reporter;

import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


@SuppressWarnings("serial")
public class MainWindow extends JFrame implements Reporter
{
  private JMenuBar menuBar;
  private JTabbedPane tabbedPane;
  private JTextField messageArea;
  
  private ProgramInstance instance;
  private JMenu fileMenu;
  private JMenuItem exitButton;
  private JMenuItem connectButton;
  private JMenu searchMenu;
  private JMenuItem searchDonorButton;
  private JMenu createMenu;
  private JMenuItem createNewDonorButton;
  private JMenu chipinMenu;
  private JMenuItem chipinTextMergeButton;
  private JMenuItem chipinFileMergeButton;
  private JSeparator chipinMenuSeperator;
  private JMenuItem chipinLoginButton;
  private JMenuItem chipinWebsiteMergeButton;
  private JMenuItem searchRunButton;
  private JMenuItem createNewRunButton;
  private ActionHandler actionHandler;
  
  private void shutdown()
  {
    this.setVisible(false);
    this.dispose();
  }
  
  private void initializeGUI()
  {
    // Initialise window
    this.setTitle("Donation Tracking Program");
    this.setSize(new Dimension(640, 480));
    
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.addWindowListener(new WindowEvents());
    getContentPane().setLayout(new BorderLayout(0, 0));
    
    // Initialise tabs area
    this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    this.getContentPane().add(this.tabbedPane, BorderLayout.CENTER);
    
    // Initialise menu
    this.menuBar = new JMenuBar();
    
    this.fileMenu = new JMenu("File");
    this.menuBar.add(this.fileMenu);
    
    this.connectButton = new JMenuItem("Connect...");
    this.fileMenu.add(this.connectButton);
    
    this.exitButton = new JMenuItem("Exit");
    this.fileMenu.add(this.exitButton);
    
    this.searchMenu = new JMenu("Search");
    this.searchMenu.setEnabled(false);
    this.menuBar.add(this.searchMenu);
    
    this.searchDonorButton = new JMenuItem("Search Donor...");
    this.searchMenu.add(this.searchDonorButton);
    
    searchRunButton = new JMenuItem("Search Run...");
    searchMenu.add(searchRunButton);
   
    this.setJMenuBar(this.menuBar);
    
    createMenu = new JMenu("Create");
    this.createMenu.setEnabled(false);
    menuBar.add(createMenu);
    
    createNewDonorButton = new JMenuItem("Create New Donor");
    createMenu.add(createNewDonorButton);
    
    createNewRunButton = new JMenuItem("Create New Run");
    createMenu.add(createNewRunButton);
    
    chipinMenu = new JMenu("Chipin");
    this.chipinMenu.setEnabled(false);
    menuBar.add(chipinMenu);
    
    chipinLoginButton = new JMenuItem("Log In...");
    chipinMenu.add(chipinLoginButton);
    
    chipinMenuSeperator = new JSeparator();
    chipinMenu.add(chipinMenuSeperator);
    
    chipinTextMergeButton = new JMenuItem("Merge from text...");
    chipinMenu.add(chipinTextMergeButton);
    
    chipinFileMergeButton = new JMenuItem("Merge from file...");
    chipinMenu.add(chipinFileMergeButton);
    
    chipinWebsiteMergeButton = new JMenuItem("Merge from chipin website");
    chipinWebsiteMergeButton.setEnabled(false);
    chipinMenu.add(chipinWebsiteMergeButton);
    
    // Initialise message area
    this.messageArea = new JTextField();
    this.messageArea.setEditable(false);
    this.getContentPane().add(this.messageArea, BorderLayout.SOUTH);
  }
  
  private class ActionHandler extends MouseAdapter implements ActionListener, ChangeListener
  {
    @Override
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == connectButton)
        {
          if (!MainWindow.this.instance.getDonationDatabase().isConnected())
          {
            MainWindow.this.openConnectDialog();
          }
          else
          {
            MainWindow.this.openDisconnectDialog();
          }
        }
        else if (ev.getSource() == exitButton)
        {
          MainWindow.this.confirmClose();
        }
        else if (ev.getSource() == searchDonorButton)
        {
            MainWindow.this.openSearchDonorDialog();
        }
        else if (ev.getSource() == searchRunButton)
        {
          MainWindow.this.openSearchSpeedRunDialog();
        }
        else if (ev.getSource() == createNewRunButton)
        {
          MainWindow.this.createNewSpeedRun();
        }
        else if (ev.getSource() == createNewDonorButton)
        {
          MainWindow.this.createNewDonor();
        }
        else if (ev.getSource() == chipinLoginButton)
        {
          if (!MainWindow.this.instance.getChipinLogin().isLoggedIn())
          {
            MainWindow.this.openChipinLoginDialog();
          }
          else
          {
            MainWindow.this.openChipinLogoutDialog();
          }
        }
        else if (ev.getSource() == chipinTextMergeButton)
        {
          MainWindow.this.openChipinTextMergeDialog();
        }
        else if (ev.getSource() == chipinFileMergeButton)
        {
          MainWindow.this.openChipinFileMergeDialog();
        }
        else if (ev.getSource() == chipinWebsiteMergeButton)
        {
          if (MainWindow.this.instance.getChipinLogin().isLoggedIn())
          {
            MainWindow.this.runChipinWebsiteMerge();
          }
        }
      }
      catch(Exception e)
      {
        MainWindow.this.report(e);
      }
    }

    @Override
    public void stateChanged(ChangeEvent ev)
    {
      if (ev.getSource() == tabbedPane)
      {
        refreshCurrentTab();
      }
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    this.connectButton.addActionListener(this.actionHandler);
    this.exitButton.addActionListener(this.actionHandler);
    this.searchDonorButton.addActionListener(this.actionHandler);
    this.searchRunButton.addActionListener(this.actionHandler);
    this.createNewRunButton.addActionListener(this.actionHandler);
    this.createNewDonorButton.addActionListener(this.actionHandler);
    this.chipinLoginButton.addActionListener(this.actionHandler);
    this.chipinTextMergeButton.addActionListener(this.actionHandler);
    this.chipinFileMergeButton.addActionListener(this.actionHandler);
    this.chipinWebsiteMergeButton.addActionListener(this.actionHandler);
    this.tabbedPane.addChangeListener(this.actionHandler);
  }

  public MainWindow()
  {
    // Initialise program logic
    this.instance = new ProgramInstance(this);
    
    this.initializeGUI();
    this.initializeGUIEvents();
  }

  private void insertTab(Component panel)
  {
    this.tabbedPane.add(panel);
    int index = this.tabbedPane.indexOfComponent(panel);

    if (index != -1)
    {
      TabHeader header = new TabHeader(this.tabbedPane);
      
      if (panel instanceof TabPanel)
      {
        ((TabPanel)panel).setTabHeader(header);
      }
      
      this.tabbedPane.setTabComponentAt(index, header);
      this.focusOnTab(index);
    }
  }
  
  private void refreshCurrentTab()
  {
    Component current = this.tabbedPane.getSelectedComponent();
    
    if (current != null && current instanceof TabPanel)
    {
      ((TabPanel)current).refreshContent();
    }
  }
  
  private void focusOnTab(int index)
  {
    this.tabbedPane.setSelectedIndex(index);
    this.tabbedPane.getComponentAt(index).requestFocus();
  }

  protected void removeTab(Component panel)
  {
    int index = this.tabbedPane.indexOfComponent(panel);

    if (index != -1)
    {
      this.tabbedPane.removeTabAt(index);
    }
  }
  
  protected void runChipinWebsiteMerge()
  {
    if (this.instance.getChipinLogin().isLoggedIn())
    {
      ChipinDocumentSource source = new ChipinWebsiteDocumentSource(this.instance.getChipinLogin());
      this.openChipinMergeTab(source);
    }
    else
    {
      throw new RuntimeException("Error, not logged in.");
    }
  }
 
  private void openChipinTextMergeDialog()
  {
    ChipinTextMergeDialog dialog = new ChipinTextMergeDialog(this);
    dialog.setVisible(true);
    
    if (dialog.getResultText() != null)
    {
      ChipinDocumentSource source = new ChipinTextDocumentSource(dialog.getResultText());
      this.openChipinMergeTab(source);
    }
  }

  protected void openChipinFileMergeDialog()
  {
    JFileChooser fileChooser = new JFileChooser();
    int result = fileChooser.showOpenDialog(this);
    
    if (result == JFileChooser.APPROVE_OPTION)
    {
      ChipinDocumentSource source = new ChipinFileDocumentSource(fileChooser.getSelectedFile());
      this.openChipinMergeTab(source);
    }
  }
 
  private void openChipinLogoutDialog()
  {
    if (this.instance.getChipinLogin().isLoggedIn())
    {
      int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out of www.chipin.com?", "Confirm logout...", JOptionPane.YES_NO_OPTION);
      
      if (result == JOptionPane.YES_OPTION)
      {
        this.instance.getChipinLogin().logOut();
        this.chipinLoginButton.setText("Log In...");
        this.chipinWebsiteMergeButton.setEnabled(false);
      }
    }
    else
    {
      throw new RuntimeException("Error, not logged in.");
    }
  }

  private void openChipinLoginDialog()
  {
    if (!this.instance.getChipinLogin().isLoggedIn())
    {
      ChipinLoginDialog dialog = new ChipinLoginDialog(this, this.instance.getChipinLogin());
      dialog.setVisible(true);
      
      if (this.instance.getChipinLogin().isLoggedIn())
      {
        this.chipinLoginButton.setText("Log Out...");
        this.chipinWebsiteMergeButton.setEnabled(true);
      }
    }
    else
    {
      throw new RuntimeException("Error, already logged in.");
    }
  }
  
  private void openChipinMergeTab(ChipinDocumentSource documentSource)
  {
    ChipinMergeTab tab = new ChipinMergeTab(new ChipinMergeProcess(this.instance.getDonationDatabase(), documentSource));
    this.insertTab(tab);
    tab.setHeaderText("Chipin Merge");
  }
  
  private void openConnectDialog()
  {
    DatabaseConnectDialog dialog = new DatabaseConnectDialog(this, this.instance.getDonationDatabase());
    dialog.setVisible(true);
    
    if (this.instance.getDonationDatabase().isConnected())
    {
      //TODO: Change this (and all the other UI state management)
      // to a single method called 'updateUIState' that will perform
      // all of these checks at once, and can be called whenever there
      // is a potential UI change in state
      // n.b. this isn't the only window that needs this done
      this.connectButton.setText("Disconnect...");
      this.createMenu.setEnabled(true);
      this.searchMenu.setEnabled(true);
      this.chipinMenu.setEnabled(true);
    }
  }
  
  private void openSearchDonorDialog()
  {
    DonorSearchDialog dialog = new DonorSearchDialog(null, new DonorSearch(this.instance.getDonationDatabase()));
    
    dialog.setVisible(true);
    
    Donor result = dialog.getResult();
    
    if (result != null)
    {
      this.openDonorTab(result.getId());
    }
  }
  
  private void openSearchSpeedRunDialog()
  {
    SpeedRunSearchDialog dialog = new SpeedRunSearchDialog(null, new SpeedRunSearch(this.instance.getDonationDatabase()));
    
    dialog.setVisible(true);
    
    SpeedRun result = dialog.getResult();
    
    if (result != null)
    {
      this.openSpeedRunTab(result.getId());
    }
  }
  
  protected void openSpeedRunTab(int speedRunId)
  {
    // prevent opening the same tab twice
    for (int i = 0; i < this.tabbedPane.getTabCount(); ++i)
    {
      Component target = this.tabbedPane.getComponentAt(i);
      if (target instanceof SpeedRunPanel && ((SpeedRunPanel)target).getSpeedRunId() == speedRunId)
      {
        this.focusOnTab(i);
        return;
      }
    }
    
    SpeedRunControl ctrl = new SpeedRunControl(this.instance.getDonationDatabase(), speedRunId);
    SpeedRunPanel panel = new SpeedRunPanel(this, ctrl);
    this.insertTab(panel);
    panel.refreshContent();
  }

  protected void openDonorTab(int donorId)
  {
    // prevent opening the same tab twice
    for (int i = 0; i < this.tabbedPane.getTabCount(); ++i)
    {
      Component target = this.tabbedPane.getComponentAt(i);
      if (target instanceof DonorPanel && ((DonorPanel)target).getDonorId() == donorId)
      {
        this.focusOnTab(i);
        return;
      }
    }
    
    DonorControl ctrl = new DonorControl(this.instance.getDonationDatabase(), donorId);
    DonorPanel panel = new DonorPanel(this, ctrl);
    this.insertTab(panel);
    panel.refreshContent();
  }
  
  protected void openChoiceTab(int choiceId)
  {
    // prevent opening the same tab twice
    for (int i = 0; i < this.tabbedPane.getTabCount(); ++i)
    {
      Component target = this.tabbedPane.getComponentAt(i);
      if (target instanceof DonationPanel && ((ChoicePanel)target).getChoiceId() == choiceId)
      {
        this.focusOnTab(i);
        return;
      }
    }
    
    ChoiceControl ctrl = new ChoiceControl(this.instance.getDonationDatabase(), choiceId);
    ChoicePanel panel = new ChoicePanel(this, ctrl);
    this.insertTab(panel);
    panel.refreshContent();
  }

  protected void openChallengeTab(int challengeId)
  {
    // prevent opening the same tab twice
    for (int i = 0; i < this.tabbedPane.getTabCount(); ++i)
    {
      Component target = this.tabbedPane.getComponentAt(i);
      if (target instanceof DonationPanel && ((ChallengePanel)target).getChallengeId() == challengeId)
      {
        this.focusOnTab(i);
        return;
      }
    }
    
    ChallengeControl ctrl = new ChallengeControl(this.instance.getDonationDatabase(), challengeId);
    ChallengePanel panel = new ChallengePanel(this, ctrl);
    this.insertTab(panel);
    panel.refreshContent();
  }
  
  protected void openDonationTab(int donationId)
  {
    // prevent opening the same tab twice
    for (int i = 0; i < this.tabbedPane.getTabCount(); ++i)
    {
      Component target = this.tabbedPane.getComponentAt(i);
      if (target instanceof DonationPanel && ((DonationPanel)target).getDonationId() == donationId)
      {
        this.focusOnTab(i);
        return;
      }
    }
    
    DonationControl ctrl = new DonationControl(this.instance.getDonationDatabase(), donationId);
    DonationPanel panel = new DonationPanel(this, ctrl);
    this.insertTab(panel);
    panel.refreshContent();
  }
  
  private void openDisconnectDialog()
  {
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to disconnect?", "Confirm Disconnect...", JOptionPane.YES_NO_OPTION);
    
    if (result == JOptionPane.YES_OPTION)
    {
      this.tabbedPane.removeAll();
      this.instance.getDonationDatabase().closeConnection();
      this.connectButton.setText("Connect...");
      this.createMenu.setEnabled(false);
      this.searchMenu.setEnabled(false);
      this.chipinMenu.setEnabled(false);
    }
  }
  
  private void confirmClose()
  {
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit", "Confirm Close...", JOptionPane.YES_NO_OPTION);
  
    if (result == JOptionPane.OK_OPTION)
    {
      this.shutdown();
    }
  }

  protected void createNewSpeedRun()
  {
    int newId = SpeedRunControl.createNewSpeedRun(this.instance.getDonationDatabase());
    this.openSpeedRunTab(newId);
  }
    
  private void createNewDonor()
  {
    int newId = DonorControl.createNewDonor(this.instance.getDonationDatabase());
    this.openDonorTab(newId);
  }

  private class WindowEvents implements WindowListener
  {
    public void windowActivated(WindowEvent arg0)
    {
      // pass
    }

    public void windowClosed(WindowEvent arg0)
    {
      MainWindow.this.shutdown();
      System.exit(0);
    }

    public void windowClosing(WindowEvent arg0)
    {
      MainWindow.this.confirmClose();
    }

    public void windowDeactivated(WindowEvent arg0)
    {
      // pass
    }

    public void windowDeiconified(WindowEvent arg0)
    {
      // pass
    }

    public void windowIconified(WindowEvent arg0)
    {
      // pass
    }

    public void windowOpened(WindowEvent arg0)
    {
      // pass
    }
  }

  @Override
  public void report(String report)
  {
    JOptionPane.showMessageDialog(this, report, "Error", JOptionPane.ERROR_MESSAGE);
    this.messageArea.setText(report);
  }

  @Override
  public void report(Exception report)
  {
    this.report(report.getMessage());
  }
}
