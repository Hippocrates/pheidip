package pheidip.ui;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
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
import java.awt.event.KeyAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import pheidip.logic.BidSearch;
import pheidip.logic.ChallengeControl;
import pheidip.logic.ChoiceControl;
import pheidip.logic.DonationBidTask;
import pheidip.logic.DonationControl;
import pheidip.logic.DonationReadTask;
import pheidip.logic.DonationSearch;
import pheidip.logic.DonorControl;
import pheidip.logic.DonorSearch;
import pheidip.logic.PrizeControl;
import pheidip.logic.PrizeSearch;
import pheidip.logic.ProgramInstance;
import pheidip.logic.SpeedRunControl;
import pheidip.logic.SpeedRunSearch;
import pheidip.logic.chipin.ChipinDonationSource;
import pheidip.logic.chipin.ChipinFileDonationSource;
import pheidip.logic.chipin.ChipinMergeProcess;
import pheidip.logic.chipin.ChipinTextDonationSource;
import pheidip.logic.chipin.ChipinWebsiteDonationSource;
import pheidip.logic.gdocs.GoogleRefreshProcess;
import pheidip.objects.Bid;
import pheidip.objects.BidType;
import pheidip.objects.Challenge;
import pheidip.objects.Choice;
import pheidip.objects.Donation;
import pheidip.objects.Donor;
import pheidip.objects.Entity;
import pheidip.objects.Prize;
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
  private JMenuItem connectToDatabaseButton;
  private JMenu searchMenu;
  private JMenuItem searchDonorButton;
  private JMenu createMenu;
  private JMenuItem createNewDonorButton;
  private JMenu chipinMenu;
  private JMenuItem chipinTextMergeButton;
  private JMenuItem chipinFileMergeButton;
  private JMenuItem chipinLoginButton;
  private JMenuItem chipinWebsiteMergeButton;
  private JMenuItem searchRunButton;
  private JMenuItem createNewRunButton;
  private ActionHandler actionHandler;
  private JSeparator separator;
  private JMenuItem searchDonationButton;
  private JMenu tasksMenu;
  private JMenuItem processBidsButton;
  private JMenuItem readDonationsButton;
  private JMenuItem searchBidButton;
  private JMenuItem createNewPrizeButton;
  private JMenuItem searchPrizeButton;
  private JMenuItem googleLoginButton;
  private JMenu googleMenu;
  private JMenuItem googleRefreshRunsButton;
  
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
    
    this.connectToDatabaseButton = new JMenuItem("Connect To Database...");
    this.fileMenu.add(this.connectToDatabaseButton);
    
    chipinLoginButton = new JMenuItem("Log In To Chipin...");
    fileMenu.add(chipinLoginButton);
    
    googleLoginButton = new JMenuItem("Log In To Google...");
    fileMenu.add(googleLoginButton);
    
    separator = new JSeparator();
    fileMenu.add(separator);
    
    this.exitButton = new JMenuItem("Exit");
    this.fileMenu.add(this.exitButton);
    
    this.searchMenu = new JMenu("Search");
    this.searchMenu.setEnabled(false);
    this.menuBar.add(this.searchMenu);
    
    this.searchDonorButton = new JMenuItem("Search Donor...");
    this.searchMenu.add(this.searchDonorButton);
    
    searchDonationButton = new JMenuItem("Search Donation...");
    searchMenu.add(searchDonationButton);
    
    searchRunButton = new JMenuItem("Search Run...");
    searchMenu.add(searchRunButton);
    
    searchBidButton = new JMenuItem("Search Bid...");
    searchMenu.add(searchBidButton);
    
    searchPrizeButton = new JMenuItem("Search Prize...");
    searchMenu.add(searchPrizeButton);
   
    this.setJMenuBar(this.menuBar);
    
    createMenu = new JMenu("Create");
    this.createMenu.setEnabled(false);
    menuBar.add(createMenu);
    
    createNewDonorButton = new JMenuItem("Create New Donor");
    createMenu.add(createNewDonorButton);
    
    createNewRunButton = new JMenuItem("Create New Run");
    createMenu.add(createNewRunButton);
    
    createNewPrizeButton = new JMenuItem("Create New Prize");
    createMenu.add(createNewPrizeButton);
    
    tasksMenu = new JMenu("Tasks");
    tasksMenu.setEnabled(false);
    menuBar.add(tasksMenu);
    
    readDonationsButton = new JMenuItem("Read Donations...");
    tasksMenu.add(readDonationsButton);
    
    processBidsButton = new JMenuItem("Process Bids...");
    tasksMenu.add(processBidsButton);
    
    chipinMenu = new JMenu("Chipin");
    this.chipinMenu.setEnabled(false);
    menuBar.add(chipinMenu);
    
    chipinTextMergeButton = new JMenuItem("Merge from text...");
    chipinMenu.add(chipinTextMergeButton);
    
    chipinFileMergeButton = new JMenuItem("Merge from file...");
    chipinMenu.add(chipinFileMergeButton);
    
    chipinWebsiteMergeButton = new JMenuItem("Merge from chipin website");
    chipinWebsiteMergeButton.setEnabled(false);
    chipinMenu.add(chipinWebsiteMergeButton);
    
    googleMenu = new JMenu("Google");
    googleMenu.setEnabled(false);
    menuBar.add(googleMenu);
    
    googleRefreshRunsButton = new JMenuItem("Refresh Runs From Spreadsheet");
    googleMenu.add(googleRefreshRunsButton);
    
    // Initialise message area
    this.messageArea = new JTextField();
    this.messageArea.setEditable(false);
    this.getContentPane().add(this.messageArea, BorderLayout.SOUTH);
  }
  
  private class ActionHandler extends KeyAdapter implements ActionListener, ChangeListener
  {
    @Override
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == connectToDatabaseButton)
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
        else if (ev.getSource() == searchDonationButton)
        {
          MainWindow.this.openSearchDonationDialog();
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
        else if (ev.getSource() == googleLoginButton)
        {
          if (!MainWindow.this.instance.getGoogleLogin().isLoggedIn())
          {
            MainWindow.this.openGoogleLoginDialog();
          }
          else
          {
            MainWindow.this.openGoogleLogouDialog();
          }
        }
        else if (ev.getSource() == googleRefreshRunsButton)
        {
          MainWindow.this.openGoogleSpreadsheetUpdateTab();
        }
        else if (ev.getSource() == processBidsButton)
        {
          MainWindow.this.openProcessBidsTab();
        }
        else if (ev.getSource() == readDonationsButton)
        {
          MainWindow.this.openReadDonationsTab();
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
        else if (ev.getSource() == searchBidButton)
        {
          MainWindow.this.openSearchBidDialog();
        }
        else if (ev.getSource() == createNewPrizeButton)
        {
          MainWindow.this.createNewPrize();
        }
        else if (ev.getSource() == searchPrizeButton)
        {
          MainWindow.this.openSearchPrizeDialog();
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
        messageArea.setText("");
        redrawCurrentTab();
      }
    }
  }

  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    this.connectToDatabaseButton.addActionListener(this.actionHandler);
    this.exitButton.addActionListener(this.actionHandler);
    this.searchDonorButton.addActionListener(this.actionHandler);
    this.searchDonationButton.addActionListener(this.actionHandler);
    this.searchBidButton.addActionListener(this.actionHandler);
    this.searchRunButton.addActionListener(this.actionHandler);
    this.createNewRunButton.addActionListener(this.actionHandler);
    this.createNewDonorButton.addActionListener(this.actionHandler);
    this.chipinLoginButton.addActionListener(this.actionHandler);
    this.chipinTextMergeButton.addActionListener(this.actionHandler);
    this.chipinFileMergeButton.addActionListener(this.actionHandler);
    this.chipinWebsiteMergeButton.addActionListener(this.actionHandler);
    this.processBidsButton.addActionListener(this.actionHandler);
    this.tabbedPane.addChangeListener(this.actionHandler);
    this.readDonationsButton.addActionListener(this.actionHandler);
    this.createNewPrizeButton.addActionListener(this.actionHandler);
    this.searchPrizeButton.addActionListener(this.actionHandler);
    this.googleLoginButton.addActionListener(this.actionHandler);
    this.googleRefreshRunsButton.addActionListener(this.actionHandler);

    Action deleteAction = new AbstractAction() 
    {
        public void actionPerformed(ActionEvent e) 
        {
          MainWindow.this.deleteCurrentEntity();
        }
    };
    Action closeAction = new AbstractAction() 
    {
        public void actionPerformed(ActionEvent e) 
        {
          MainWindow.this.closeCurrentTab();
        }
    };
    Action saveAction = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e) 
      {
        MainWindow.this.saveCurrentTab();
      }
    };
    Action refreshAction = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e) 
      {
        MainWindow.this.redrawCurrentTab();
      }
    };
    Action chipinWebsiteMergeAction = new AbstractAction()
    {
      public void actionPerformed(ActionEvent e) 
      {
        try
        {
          MainWindow.this.runChipinWebsiteMerge();
        }
        catch(Exception error)
        {
          MainWindow.this.report(error);
        }
      }
    };

    this.tabbedPane.getActionMap().put(HotkeyAction.DELETE.toString(), deleteAction);
    this.tabbedPane.getActionMap().put(HotkeyAction.CLOSE.toString(), closeAction);
    this.tabbedPane.getActionMap().put(HotkeyAction.SAVE.toString(), saveAction);
    this.tabbedPane.getActionMap().put(HotkeyAction.REFRESH.toString(), refreshAction);
    this.tabbedPane.getActionMap().put(HotkeyAction.CHIPIN_MERGE.toString(), chipinWebsiteMergeAction);
    
    InputMap[] inputMaps = new InputMap[] {
      this.tabbedPane.getInputMap(JComponent.WHEN_FOCUSED),
      this.tabbedPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT),
    };
    
    for(InputMap i : inputMaps) 
    {
      for (HotkeyAction a : HotkeyAction.values())
      {
        i.put(a.getKeyStroke(), a.toString());
      }
    }
  }


  public MainWindow()
  {
    // Initialise program logic
    this.instance = new ProgramInstance(this);
    
    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.updateUIState();
  }
  
  public ProgramInstance getInstance()
  {
    return this.instance;
  }
  
  private void deleteCurrentEntity()
  {
    Component panel = this.tabbedPane.getSelectedComponent();
    
    if (panel instanceof EntityPanel)
    {
      EntityPanel entityHandle = (EntityPanel) panel;
      entityHandle.deleteContent();
    }
  }
  
  private void closeCurrentTab()
  {
    Component panel = this.tabbedPane.getSelectedComponent();
    this.removeTab(panel);
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
  

  private void saveCurrentTab()
  {
    Component current = this.tabbedPane.getSelectedComponent();
    
    if (current != null && current instanceof EntityPanel)
    {
      ((EntityPanel)current).saveContent();
    }
  }
  
  private void redrawCurrentTab()
  {
    Component current = this.tabbedPane.getSelectedComponent();
    
    if (current != null && current instanceof TabPanel)
    {
      ((TabPanel)current).redrawContent();
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
      ChipinDonationSource source = new ChipinWebsiteDonationSource(this.instance.getChipinLogin());
      this.openChipinMergeTab(source);
    }
    else
    {
      throw new RuntimeException("Cannot merge: not logged in to www.chipin.com.");
    }
  }
 
  private void openChipinTextMergeDialog()
  {
    ChipinTextMergeDialog dialog = new ChipinTextMergeDialog(this);
    dialog.setVisible(true);
    
    if (dialog.getResultText() != null)
    {
      ChipinDonationSource source = new ChipinTextDonationSource(dialog.getResultText());
      this.openChipinMergeTab(source);
    }
  }

  protected void openChipinFileMergeDialog()
  {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.addChoosableFileFilter(new ListFileFilter(new String[]{"html","htm","xml"}));
    int result = fileChooser.showOpenDialog(this);
    
    if (result == JFileChooser.APPROVE_OPTION)
    {
      ChipinDonationSource source = new ChipinFileDonationSource(fileChooser.getSelectedFile());
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
        this.updateUIState();
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
        this.updateUIState();
      }
    }
    else
    {
      throw new RuntimeException("Error, already logged in.");
    }
  }
  
  private void openChipinMergeTab(ChipinDonationSource documentSource)
  {
    // prevent opening the same tab twice
    for (int i = 0; i < this.tabbedPane.getTabCount(); ++i)
    {
      Component target = this.tabbedPane.getComponentAt(i);
      if (target instanceof ExternalProcessTab)
      {
        ExternalProcessTab processTab = (ExternalProcessTab) target;
        if (processTab.getProcess() instanceof ChipinMergeProcess)
        {
          this.focusOnTab(i);
          return;
        }
      }
    }
    
    ExternalProcessTab tab = new ExternalProcessTab(new ChipinMergeProcess(this.instance.getDonationDatabase(), documentSource));
    
    this.insertTab(tab);
  }
  
  private void openGoogleLoginDialog()
  {
    if (!this.instance.getGoogleLogin().isLoggedIn())
    {
      GoogleLoginDialog dialog = new GoogleLoginDialog(this, this.instance.getGoogleLogin());
      dialog.setVisible(true);
      
      if (this.instance.getGoogleLogin().isLoggedIn())
      {
        this.updateUIState();
      }
    }
    else
    {
      throw new RuntimeException("Error, already logged in.");
    }
  }

  private void openGoogleLogouDialog()
  {
    if (this.instance.getGoogleLogin().isLoggedIn())
    {
      int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out of google?", "Confirm logout...", JOptionPane.YES_NO_OPTION);
      
      if (result == JOptionPane.YES_OPTION)
      {
        this.instance.getGoogleLogin().logOut();
        this.updateUIState();
      }
    }
    else
    {
      throw new RuntimeException("Error, not logged in.");
    }
  }

  private void openGoogleSpreadsheetUpdateTab()
  {
    // prevent opening the same tab twice
    for (int i = 0; i < this.tabbedPane.getTabCount(); ++i)
    {
      Component target = this.tabbedPane.getComponentAt(i);
      if (target instanceof ExternalProcessTab)
      {
        ExternalProcessTab processTab = (ExternalProcessTab) target;
        if (processTab.getProcess() instanceof GoogleRefreshProcess)
        {
          this.focusOnTab(i);
          return;
        }
      }
    }
    
    ExternalProcessTab tab = new ExternalProcessTab(new GoogleRefreshProcess(this.instance.getDonationDatabase(), this.instance.getGoogleLogin(), false));
    
    this.insertTab(tab);
  }
  
  private void openConnectDialog()
  {
    DatabaseConnectDialog dialog = new DatabaseConnectDialog(this, this.instance.getDonationDatabase());
    dialog.setVisible(true);
    
    this.updateUIState();
  }
  
  private void updateUIState()
  {
    if (this.instance.getDonationDatabase().isConnected())
    {
      this.connectToDatabaseButton.setText("Disconnect From Database...");
      this.createMenu.setEnabled(true);
      this.searchMenu.setEnabled(true);
      this.chipinMenu.setEnabled(true);
      this.tasksMenu.setEnabled(true);
    }
    else
    {
      this.connectToDatabaseButton.setText("Connect To Database...");
      this.createMenu.setEnabled(false);
      this.searchMenu.setEnabled(false);
      this.chipinMenu.setEnabled(false);
      this.tasksMenu.setEnabled(false);
      this.googleMenu.setEnabled(false);
      this.tabbedPane.removeAll();
    }
    
    if (this.instance.getChipinLogin().isLoggedIn() && this.instance.getDonationDatabase().isConnected())
    {
      this.chipinLoginButton.setText("Log Out Of Chipin...");
      this.chipinWebsiteMergeButton.setEnabled(true);
    }
    else
    {
      this.chipinLoginButton.setText("Log In To Chipin...");
      this.chipinWebsiteMergeButton.setEnabled(false);
    }
    
    if (this.instance.getGoogleLogin().isLoggedIn() && this.instance.getDonationDatabase().isConnected())
    {
      this.googleLoginButton.setText("Log Out Of Google...");
      this.googleMenu.setEnabled(true);
    }
    else
    {
      this.googleLoginButton.setText("Log In To Google...");
      this.googleMenu.setEnabled(false);
    }
  }
  
  protected void createSearchDialog(Class<?> entityClass)
  {
    if (SpeedRun.class.isAssignableFrom(entityClass))
    {
      this.openSearchSpeedRunDialog();
    }
    else if (Bid.class.isAssignableFrom(entityClass))
    {
      this.openSearchBidDialog();
    }
    else if (Donor.class.isAssignableFrom(entityClass))
    {
      this.openSearchDonorDialog();
    }
    else if (Donation.class.isAssignableFrom(entityClass))
    {
      this.openSearchDonationDialog();
    }
    else if (Prize.class.isAssignableFrom(entityClass))
    {
      this.openSearchPrizeDialog();
    }
  }

  private void openSearchBidDialog()
  {
    BidSearchDialog dialog = new BidSearchDialog(this, new BidSearch(this.instance.getDonationDatabase()));
  
    dialog.setVisible(true);
    
    for (Bid result : dialog.getResults())
    {
      if (result.getType() == BidType.CHALLENGE)
      {
        this.openChallengeTab(result.getId());
      }
      else
      {
        this.openChoiceTab(result.getId());
      }
    }
  }

  private void openSearchDonationDialog()
  {
    DonationSearchDialog dialog = new DonationSearchDialog(this, new DonationSearch(this.instance.getDonationDatabase()));
    
    dialog.setVisible(true);

    for (Donation result : dialog.getResults())
    {
      this.openDonationTab(result.getId());
    }
  }
  
  private void openSearchDonorDialog()
  {
    DonorSearchDialog dialog = new DonorSearchDialog(this, new DonorSearch(this.instance.getDonationDatabase()));
    
    dialog.setVisible(true);
    
    for (Donor result : dialog.getResults())
    {
      this.openDonorTab(result.getId());
    }
  }
  
  private void openSearchSpeedRunDialog()
  {
    SpeedRunSearchDialog dialog = new SpeedRunSearchDialog(this, new SpeedRunSearch(this.instance.getDonationDatabase()));
    
    dialog.setVisible(true);
    
    for (SpeedRun result : dialog.getResults())
    {
      this.openSpeedRunTab(result.getId());
    }
  }
  
  private void openSearchPrizeDialog()
  {
    PrizeSearch searcher = new PrizeSearch(this.instance.getDonationDatabase());
    PrizeSearchDialog dialog = new PrizeSearchDialog(this, searcher);
    
    dialog.setVisible(true);
    
    for (Prize result : dialog.getResults())
    {
      this.openPrizeTab(result.getId());
    }
  }

  private void openReadDonationsTab()
  {
    // prevent opening the same tab twice
    for (int i = 0; i < this.tabbedPane.getTabCount(); ++i)
    {
      Component target = this.tabbedPane.getComponentAt(i);
      if (target instanceof DonationTaskPanel && ((DonationTaskPanel)target).getTaskName().equals(DonationReadTask.TASK_NAME))
      {
        this.focusOnTab(i);
        return;
      }
    }
    
    DonationReadTask task = new DonationReadTask(this.instance.getDonationDatabase());
    DonationTaskPanel panel = new DonationTaskPanel(this, task);
    
    this.insertTab(panel);
  }

  private void openProcessBidsTab()
  {
    // prevent opening the same tab twice
    for (int i = 0; i < this.tabbedPane.getTabCount(); ++i)
    {
      Component target = this.tabbedPane.getComponentAt(i);
      if (target instanceof DonationTaskPanel && ((DonationTaskPanel)target).getTaskName().equals(DonationBidTask.TASK_NAME))
      {
        this.focusOnTab(i);
        return;
      }
    }
    
    DonationBidTask task = new DonationBidTask(this.instance.getDonationDatabase());
    DonationTaskPanel panel = new DonationTaskPanel(this, task);
    
    this.insertTab(panel);
  }

  protected void openEntityTab(Entity entity)
  {
    if (entity instanceof SpeedRun)
    {
      this.openSpeedRunTab(entity.getId());
    }
    else if (entity instanceof Donor)
    {
      this.openDonorTab(entity.getId());
    }
    else if (entity instanceof Donation)
    {
      this.openDonationTab(entity.getId());
    }
    else if (entity instanceof Choice)
    {
      this.openChoiceTab(entity.getId());
    }
    else if (entity instanceof Challenge)
    {
      this.openChallengeTab(entity.getId());
    }
    else if (entity instanceof Prize)
    {
      this.openPrizeTab(entity.getId());
    }
    else
    {
      throw new RuntimeException("Error, cannot open a tab for entity of type : " + entity.getClass().getName());
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
    
    DonorControl ctrl = this.instance.createDonorControl(donorId);
    DonorPanel panel = new DonorPanel(this, ctrl);
    this.insertTab(panel);
  }
  
  protected void openChoiceTab(int choiceId)
  {
    // prevent opening the same tab twice
    for (int i = 0; i < this.tabbedPane.getTabCount(); ++i)
    {
      Component target = this.tabbedPane.getComponentAt(i);
      if (target instanceof ChoicePanel && ((ChoicePanel)target).getChoiceId() == choiceId)
      {
        this.focusOnTab(i);
        return;
      }
    }
    
    ChoiceControl ctrl = this.instance.createChoiceControl(choiceId);
    ChoicePanel panel = new ChoicePanel(this, ctrl);
    this.insertTab(panel);
  }

  protected void openChallengeTab(int challengeId)
  {
    // prevent opening the same tab twice
    for (int i = 0; i < this.tabbedPane.getTabCount(); ++i)
    {
      Component target = this.tabbedPane.getComponentAt(i);
      if (target instanceof ChallengePanel && ((ChallengePanel)target).getChallengeId() == challengeId)
      {
        this.focusOnTab(i);
        return;
      }
    }
    
    ChallengeControl ctrl = this.instance.createChallengeControl(challengeId);
    ChallengePanel panel = new ChallengePanel(this, ctrl);
    this.insertTab(panel);
  }
  
  protected void openPrizeTab(int prizeId)
  {
    // prevent opening the same tab twice
    for (int i = 0; i < this.tabbedPane.getTabCount(); ++i)
    {
      Component target = this.tabbedPane.getComponentAt(i);
      if (target instanceof PrizePanel && ((PrizePanel)target).getPrizeId() == prizeId)
      {
        this.focusOnTab(i);
        return;
      }
    }
    
    PrizeControl ctrl = this.instance.createPrizeControl(prizeId);
    PrizePanel panel = new PrizePanel(this, ctrl);
    this.insertTab(panel);
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
    
    DonationControl ctrl = this.instance.createDonationControl(donationId);
    DonationPanel panel = new DonationPanel(this, ctrl);
    this.insertTab(panel);
  }
  
  private void openDisconnectDialog()
  {
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to disconnect?", "Confirm Disconnect...", JOptionPane.YES_NO_OPTION);

    if (result == JOptionPane.YES_OPTION)
    {
      this.instance.getDonationDatabase().closeConnection();
      this.updateUIState();
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

  private void createNewPrize()
  {
    int newId = PrizeControl.createNewPrize(this.instance.getDonationDatabase());
    this.openPrizeTab(newId);
  }
  
  protected void createNewSpeedRun()
  {
    int newId = SpeedRunControl.createNewSpeedRun(this.instance.getDonationDatabase());
    this.openSpeedRunTab(newId);
  }
  
  protected DonationBidSearchDialog openDonationBidSearch()
  {
    return new DonationBidSearchDialog(this, new BidSearch(this.instance.getDonationDatabase()));
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
