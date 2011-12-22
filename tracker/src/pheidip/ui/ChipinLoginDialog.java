package pheidip.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

import pheidip.logic.chipin.ChipinLoginManager;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ChipinLoginDialog extends JDialog
{
  private JPanel buttonPane;
  private JButton loginButton;
  private JButton cancelButton;
  private JPanel centerPanel;
  private JLabel loginNameLabel;
  private JLabel loginPasswordLabel;
  private JLabel lblChipinId;
  private JTextField loginNameField;
  private JPasswordField loginPasswordField;
  private JTextField chipinIdField;
  private ChipinLoginManager loginManager;
  private FocusTraversalManager tabOrder;
  private ActionHandler actionHandler;

  private void initializeGUI()
  {
    setBounds(100, 100, 450, 187);
    setTitle("Connect to www.chipin.com...");
    getContentPane().setLayout(new BorderLayout());
    {
      buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);
      {
        loginButton = new JButton("Log In");
        loginButton.setActionCommand("OK");
        buttonPane.add(loginButton);
        getRootPane().setDefaultButton(loginButton);
      }
      {
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
      }
    }
    {
      centerPanel = new JPanel();
      centerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
      getContentPane().add(centerPanel, BorderLayout.CENTER);
      GridBagLayout gbl_centerPanel = new GridBagLayout();
      gbl_centerPanel.columnWidths = new int[]{0, 0, 0};
      gbl_centerPanel.rowHeights = new int[]{0, 0, 0, 0};
      gbl_centerPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
      gbl_centerPanel.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
      centerPanel.setLayout(gbl_centerPanel);
      {
        loginNameLabel = new JLabel("Login:");
        GridBagConstraints gbc_loginNameLabel = new GridBagConstraints();
        gbc_loginNameLabel.anchor = GridBagConstraints.EAST;
        gbc_loginNameLabel.insets = new Insets(0, 0, 5, 5);
        gbc_loginNameLabel.gridx = 0;
        gbc_loginNameLabel.gridy = 0;
        centerPanel.add(loginNameLabel, gbc_loginNameLabel);
      }
      {
        loginNameField = new JTextField();
        GridBagConstraints gbc_loginNameField = new GridBagConstraints();
        gbc_loginNameField.insets = new Insets(0, 0, 5, 0);
        gbc_loginNameField.fill = GridBagConstraints.HORIZONTAL;
        gbc_loginNameField.gridx = 1;
        gbc_loginNameField.gridy = 0;
        centerPanel.add(loginNameField, gbc_loginNameField);
        loginNameField.setColumns(10);
      }
      {
        loginPasswordLabel = new JLabel("Password:");
        GridBagConstraints gbc_loginPasswordLabel = new GridBagConstraints();
        gbc_loginPasswordLabel.anchor = GridBagConstraints.EAST;
        gbc_loginPasswordLabel.insets = new Insets(0, 0, 5, 5);
        gbc_loginPasswordLabel.gridx = 0;
        gbc_loginPasswordLabel.gridy = 1;
        centerPanel.add(loginPasswordLabel, gbc_loginPasswordLabel);
      }
      {
        loginPasswordField = new JPasswordField();
        GridBagConstraints gbc_loginPasswordField = new GridBagConstraints();
        gbc_loginPasswordField.insets = new Insets(0, 0, 5, 0);
        gbc_loginPasswordField.fill = GridBagConstraints.HORIZONTAL;
        gbc_loginPasswordField.gridx = 1;
        gbc_loginPasswordField.gridy = 1;
        centerPanel.add(loginPasswordField, gbc_loginPasswordField);
      }
      {
        lblChipinId = new JLabel("Chipin Id:");
        GridBagConstraints gbc_lblChipinId = new GridBagConstraints();
        gbc_lblChipinId.anchor = GridBagConstraints.EAST;
        gbc_lblChipinId.insets = new Insets(0, 0, 0, 5);
        gbc_lblChipinId.gridx = 0;
        gbc_lblChipinId.gridy = 2;
        centerPanel.add(lblChipinId, gbc_lblChipinId);
      }
      {
        chipinIdField = new JTextField();
        GridBagConstraints gbc_chipinIdField = new GridBagConstraints();
        gbc_chipinIdField.fill = GridBagConstraints.HORIZONTAL;
        gbc_chipinIdField.gridx = 1;
        gbc_chipinIdField.gridy = 2;
        centerPanel.add(chipinIdField, gbc_chipinIdField);
        chipinIdField.setColumns(10);
      }
    }
  }
  
  private class ActionHandler implements ActionListener
  {
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == loginButton)
        {
          ChipinLoginDialog.this.loginClicked();
        }
        else if (ev.getSource() == cancelButton)
        {
          ChipinLoginDialog.this.closeDialog();
        }
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(ChipinLoginDialog.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    loginButton.addActionListener(this.actionHandler);
    cancelButton.addActionListener(this.actionHandler);
    
    this.tabOrder = new FocusTraversalManager(new Component[]
    {
      this.loginNameField,
      this.loginPasswordField,
      this.chipinIdField,
      this.loginButton,
      this.cancelButton,
    });
    this.setFocusTraversalPolicy(this.tabOrder);
  }

  /**
   * Create the dialog.
   */
  public ChipinLoginDialog(JFrame parent, ChipinLoginManager loginManager)
  {
    super(parent, true);
    
    this.loginManager = loginManager;
    
    if (this.loginManager.isLoggedIn())
    {
      throw new RuntimeException("Error, already logged in to chipin.");
    }
    
    this.initializeGUI();
    this.initializeGUIEvents();
  }
  
  private void loginClicked()
  {
    this.loginManager.logIn(
        this.loginNameField.getText(),
        new String(this.loginPasswordField.getPassword()),
        this.chipinIdField.getText());
    
    if (this.loginManager.isLoggedIn())
    {
      this.closeDialog();
    }
    else
    {
      JOptionPane.showMessageDialog(this, "Could not log in to www.chipin.com", "Login Failed", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  private void closeDialog()
  {
    this.setVisible(false);
    this.dispose();
  }

}
