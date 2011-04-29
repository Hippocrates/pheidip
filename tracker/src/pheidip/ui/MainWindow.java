package pheidip.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import java.awt.Dimension;

@SuppressWarnings("serial")
public class MainWindow extends JFrame
{
  private JMenuBar menuBar;
  private JTabbedPane tabbedPane;
  private JTextField messageArea;
  
  public MainWindow()
  {
    super("Donation Tracking Program");
    this.setSize(new Dimension(640, 480));
    
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    this.getContentPane().add(this.tabbedPane, BorderLayout.CENTER);
    
    this.menuBar = new JMenuBar();
    setJMenuBar(this.menuBar);
    
    this.messageArea = new JTextField();
    this.messageArea.setEditable(false);
    this.getContentPane().add(this.messageArea, BorderLayout.SOUTH);
  }
  
  public void insertTab(JPanel panel)
  {
    this.tabbedPane.add(panel);
    int index = this.tabbedPane.indexOfComponent(panel);
    
    if (index != -1)
    {
      this.tabbedPane.setTabComponentAt(index, new TabHeader(this.tabbedPane));
    }
  }
}
