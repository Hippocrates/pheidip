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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import pheidip.logic.ChipinDocumentSource;
import pheidip.logic.ChipinFileDocumentSource;
import pheidip.logic.ChipinMergeProcess;
import pheidip.logic.ChipinTextDocumentSource;
import pheidip.logic.ChipinWebsiteDocumentSource;
import pheidip.logic.DonationControl;
import pheidip.logic.DonorControl;
import pheidip.logic.ProgramInstance;
import pheidip.objects.Donor;
import javax.swing.JSeparator;


@SuppressWarnings("serial")
public class MainWindow extends JFrame
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
   
    this.setJMenuBar(this.menuBar);
    
    createMenu = new JMenu("Create");
    this.createMenu.setEnabled(false);
    menuBar.add(createMenu);
    
    createNewDonorButton = new JMenuItem("Create New Donor");
    createMenu.add(createNewDonorButton);
    
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
  
  private void initializeGUIEvents()
  {
    this.connectButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
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
    });
    
    this.exitButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        MainWindow.this.confirmClose();
      }
    });
    
    this.searchDonorButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        MainWindow.this.openSearchDonorDialog();
      }
    });
    
    this.createNewDonorButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        MainWindow.this.createNewDonor();
      }
    });
    
    chipinLoginButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent arg0) 
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
    });
    
    chipinTextMergeButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent arg0) 
      {
        MainWindow.this.openChipinTextMergeDialog();
      }
    });
    
    chipinFileMergeButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent arg0) 
      {
        MainWindow.this.openChipinFileMergeDialog();
      }
    });
    
    chipinWebsiteMergeButton.addActionListener(new ActionListener() 
    {
      public void actionPerformed(ActionEvent arg0) 
      {
        if (MainWindow.this.instance.getChipinLogin().isLoggedIn())
        {
          MainWindow.this.runChipinWebsiteMerge();
        }
      }
    });
  }

  public MainWindow()
  {
    // Initialise program logic
    this.instance = new ProgramInstance();
    
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
    DonorSearchDialog dialog = new DonorSearchDialog(null, this.instance.getDonationDatabase());
    
    dialog.setVisible(true);
    
    Donor result = dialog.getResult();
    
    if (result != null)
    {
      this.openDonorTab(result.getId());
    }
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

}
