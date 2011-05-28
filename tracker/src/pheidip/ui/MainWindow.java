package pheidip.ui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import javax.swing.JMenuBar;

import pheidip.logic.ProgramInstance;
import pheidip.objects.Donor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
  
  private void shutdown()
  {
    this.setVisible(false);
    this.dispose();
  }
  
  public MainWindow()
  {
    // Initialise program logic
    this.instance = new ProgramInstance();
    
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
    this.connectButton.addActionListener(new ActionListener()
    {
      @Override
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
    this.fileMenu.add(this.connectButton);
    
    this.exitButton = new JMenuItem("Exit");
    this.exitButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        MainWindow.this.confirmClose();
      }
    });
    this.fileMenu.add(this.exitButton);
    
    this.searchMenu = new JMenu("Search");
    this.menuBar.add(this.searchMenu);
    
    this.searchDonorButton = new JMenuItem("Search Donor...");
    this.searchDonorButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
        MainWindow.this.openSearchDonorDialog();
      }
    });
    this.searchMenu.add(this.searchDonorButton);
   
    this.setJMenuBar(this.menuBar);
    
    // Initialise message area
    this.messageArea = new JTextField();
    this.messageArea.setEditable(false);
    this.getContentPane().add(this.messageArea, BorderLayout.SOUTH);
  }

  public void insertTab(JPanel panel)
  {
    // TODO: make this only accept a panel that can confirm its own closing
    // (TabHeader would need to change as well)
    this.tabbedPane.add(panel);
    int index = this.tabbedPane.indexOfComponent(panel);
    
    if (index != -1)
    {
      this.tabbedPane.setTabComponentAt(index, new TabHeader(this.tabbedPane));
    }
  }
  
  private void openConnectDialog()
  {
    DatabaseConnectDialog dialog = new DatabaseConnectDialog(this, this.instance.getDonationDatabase());
    dialog.setVisible(true);
    
    if (this.instance.getDonationDatabase().isConnected())
    {
      this.connectButton.setText("Disconnect...");
    }
  }
  
  private void openSearchDonorDialog()
  {
    DonorSearchDialog dialog = new DonorSearchDialog(null, this.instance.getDonationDatabase());
    
    dialog.setVisible(true);
    
    Donor result = dialog.getResult();
    
    if (result != null)
    {
      DonorPanel panel = new DonorPanel(this.instance.getDonationDatabase(), result.getId());
      panel.setName(result.toString());
      this.insertTab(panel);
    }
  }
  
  private void openDisconnectDialog()
  {
    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to disconnect?", "Confirm Disconnect...", JOptionPane.YES_NO_OPTION);
    
    if (result == JOptionPane.YES_OPTION)
    {
      this.instance.getDonationDatabase().closeConnection();
      this.connectButton.setText("Connect...");
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
