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
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JPasswordField;

import pheidip.logic.gdocs.GoogleSpreadSheetLoginManager;

@SuppressWarnings("serial")
public class GoogleLoginDialog extends JDialog
{

  private final JPanel contentPanel = new JPanel();
  private JTextField googleIdTextField;
  private JPasswordField passwordField;
  private JTextField documentIdField;
  private GoogleSpreadSheetLoginManager loginManager;
  private JButton loginButton;
  private JButton cancelButton;
  private FocusTraversalManager tabOrder;
  private ActionListener actionHandler;

  private void initializeGUI()
  {
    setBounds(100, 100, 450, 181);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    GridBagLayout gbl_contentPanel = new GridBagLayout();
    gbl_contentPanel.columnWidths = new int[]{76, 10, 0};
    gbl_contentPanel.rowHeights = new int[]{10, 0, 0, 0};
    gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
    gbl_contentPanel.rowWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
    contentPanel.setLayout(gbl_contentPanel);
    {
      JLabel lblGoogleId = new JLabel("Google Id:");
      GridBagConstraints gbc_lblGoogleId = new GridBagConstraints();
      gbc_lblGoogleId.anchor = GridBagConstraints.EAST;
      gbc_lblGoogleId.insets = new Insets(0, 0, 5, 5);
      gbc_lblGoogleId.gridx = 0;
      gbc_lblGoogleId.gridy = 0;
      contentPanel.add(lblGoogleId, gbc_lblGoogleId);
    }
    {
      googleIdTextField = new JTextField();
      GridBagConstraints gbc_googleIdTextField = new GridBagConstraints();
      gbc_googleIdTextField.insets = new Insets(0, 0, 5, 0);
      gbc_googleIdTextField.fill = GridBagConstraints.HORIZONTAL;
      gbc_googleIdTextField.gridx = 1;
      gbc_googleIdTextField.gridy = 0;
      contentPanel.add(googleIdTextField, gbc_googleIdTextField);
      googleIdTextField.setColumns(10);
    }
    {
      JLabel lblPassword = new JLabel("Password:");
      GridBagConstraints gbc_lblPassword = new GridBagConstraints();
      gbc_lblPassword.anchor = GridBagConstraints.EAST;
      gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
      gbc_lblPassword.gridx = 0;
      gbc_lblPassword.gridy = 1;
      contentPanel.add(lblPassword, gbc_lblPassword);
    }
    {
      passwordField = new JPasswordField();
      GridBagConstraints gbc_passwordField = new GridBagConstraints();
      gbc_passwordField.insets = new Insets(0, 0, 5, 0);
      gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
      gbc_passwordField.gridx = 1;
      gbc_passwordField.gridy = 1;
      contentPanel.add(passwordField, gbc_passwordField);
    }
    {
      JLabel lblSpreadsheetDocumentId = new JLabel("Spreadsheet Document Id:");
      GridBagConstraints gbc_lblSpreadsheetDocumentId = new GridBagConstraints();
      gbc_lblSpreadsheetDocumentId.anchor = GridBagConstraints.EAST;
      gbc_lblSpreadsheetDocumentId.insets = new Insets(0, 0, 0, 5);
      gbc_lblSpreadsheetDocumentId.gridx = 0;
      gbc_lblSpreadsheetDocumentId.gridy = 2;
      contentPanel.add(lblSpreadsheetDocumentId, gbc_lblSpreadsheetDocumentId);
    }
    {
      documentIdField = new JTextField();
      GridBagConstraints gbc_documentIdField = new GridBagConstraints();
      gbc_documentIdField.fill = GridBagConstraints.HORIZONTAL;
      gbc_documentIdField.gridx = 1;
      gbc_documentIdField.gridy = 2;
      contentPanel.add(documentIdField, gbc_documentIdField);
      documentIdField.setColumns(10);
    }
    {
      JPanel buttonPane = new JPanel();
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
  }
  
  private class ActionHandler implements ActionListener
  {
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == loginButton)
        {
          loginClicked();
        }
        else if (ev.getSource() == cancelButton)
        {
          closeDialog();
        }
      }
      catch (Exception e)
      {
        JOptionPane.showMessageDialog(GoogleLoginDialog.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
      this.googleIdTextField,
      this.passwordField,
      this.documentIdField,
      this.loginButton,
      this.cancelButton,
    });
    this.setFocusTraversalPolicy(this.tabOrder);
  }

  public GoogleLoginDialog(JFrame parent, GoogleSpreadSheetLoginManager loginManager)
  {
    super(parent, true);
    
    this.loginManager = loginManager;
    
    if (this.loginManager.isLoggedIn())
    {
      throw new RuntimeException("Error, already logged in to google.");
    }
    
    this.initializeGUI();
    this.initializeGUIEvents();
  }
  
  private void loginClicked()
  {
    this.loginManager.logIn(
        this.googleIdTextField.getText(),
        new String(this.passwordField.getPassword()),
        this.documentIdField.getText());
    
    if (this.loginManager.isLoggedIn())
    {
      this.closeDialog();
    }
    else
    {
      JOptionPane.showMessageDialog(this, "Could not log in to google", "Login Failed", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  private void closeDialog()
  {
    this.setVisible(false);
    this.dispose();
  }

}
