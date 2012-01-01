package pheidip.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;

import pheidip.logic.PrizeAssign;
import pheidip.logic.PrizeAssignParams;
import pheidip.objects.Donor;
import pheidip.objects.PrizeDrawMethod;
import pheidip.util.FormatUtils;
import javax.swing.JComboBox;

@SuppressWarnings("serial")
public class PrizeAssignmentDialog extends JDialog
{
  private final JPanel contentPanel = new JPanel();
  private TimeControl donatedAfterTimeField;
  private TimeControl donatedBeforeTimeField;
  private JFormattedTextField minimumDonationField;
  private JLabel lblDonatedAfter;
  private JLabel lblDonatedBefore;
  private JLabel lblSingleDonationAbove;
  private JCheckBox minimumDonationCheckBox;
  private JCheckBox donatedBeforeCheckBox;
  private JCheckBox donatedAfterCheckBox;
  private ActionHandler actionHandler;
  private JButton cancelButton;
  private JButton drawButton;
  private FocusTraversalManager tabOrder;
  private Donor selectedDonor;
  private PrizeAssign assigner;
  private JLabel lblExcludeIfAlready;
  private JCheckBox excludeIfWonCheckBox;
  private JLabel lblDrawingMethod;
  private JComboBox drawMethodComboBox;
  private JFormattedTextField maxRaffleSizeField;
  private JLabel lblMaxRaffleSize;
  private JCheckBox maxRaffleSizeCheckBox;

  private void initializeGUI()
  {
    setTitle("Set Assignment Parameters...");
    setBounds(100, 100, 450, 300);
    getContentPane().setLayout(new BorderLayout());
    contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    getContentPane().add(contentPanel, BorderLayout.CENTER);
    GridBagLayout gbl_contentPanel = new GridBagLayout();
    gbl_contentPanel.columnWidths = new int[]{64, 32, 0, 0};
    gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
    gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
    gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
    contentPanel.setLayout(gbl_contentPanel);
    {
      lblDrawingMethod = new JLabel("Drawing Method:");
      GridBagConstraints gbc_lblDrawingMethod = new GridBagConstraints();
      gbc_lblDrawingMethod.anchor = GridBagConstraints.EAST;
      gbc_lblDrawingMethod.insets = new Insets(0, 0, 5, 5);
      gbc_lblDrawingMethod.gridx = 0;
      gbc_lblDrawingMethod.gridy = 0;
      contentPanel.add(lblDrawingMethod, gbc_lblDrawingMethod);
    }
    {
      drawMethodComboBox = new JComboBox(PrizeDrawMethod.values());
      GridBagConstraints gbc_drawMethodComboBox = new GridBagConstraints();
      gbc_drawMethodComboBox.insets = new Insets(0, 0, 5, 0);
      gbc_drawMethodComboBox.fill = GridBagConstraints.HORIZONTAL;
      gbc_drawMethodComboBox.gridx = 2;
      gbc_drawMethodComboBox.gridy = 0;
      contentPanel.add(drawMethodComboBox, gbc_drawMethodComboBox);
    }
    {
      lblMaxRaffleSize = new JLabel("Max Raffle Size:");
      GridBagConstraints gbc_lblMaxRaffleSize = new GridBagConstraints();
      gbc_lblMaxRaffleSize.anchor = GridBagConstraints.EAST;
      gbc_lblMaxRaffleSize.insets = new Insets(0, 0, 5, 5);
      gbc_lblMaxRaffleSize.gridx = 0;
      gbc_lblMaxRaffleSize.gridy = 1;
      contentPanel.add(lblMaxRaffleSize, gbc_lblMaxRaffleSize);
    }
    {
      maxRaffleSizeCheckBox = new JCheckBox("");
      GridBagConstraints gbc_maxRaffleSizeCheckBox = new GridBagConstraints();
      gbc_maxRaffleSizeCheckBox.insets = new Insets(0, 0, 5, 5);
      gbc_maxRaffleSizeCheckBox.gridx = 1;
      gbc_maxRaffleSizeCheckBox.gridy = 1;
      contentPanel.add(maxRaffleSizeCheckBox, gbc_maxRaffleSizeCheckBox);
    }
    {
      maxRaffleSizeField = new JFormattedTextField(FormatUtils.getIntegerFormat());
      maxRaffleSizeField.setText("0");
      maxRaffleSizeField.setHorizontalAlignment(SwingConstants.TRAILING);
      GridBagConstraints gbc_maxRaffleSizeField = new GridBagConstraints();
      gbc_maxRaffleSizeField.insets = new Insets(0, 0, 5, 0);
      gbc_maxRaffleSizeField.fill = GridBagConstraints.HORIZONTAL;
      gbc_maxRaffleSizeField.gridx = 2;
      gbc_maxRaffleSizeField.gridy = 1;
      contentPanel.add(maxRaffleSizeField, gbc_maxRaffleSizeField);
    }
    {
      lblDonatedAfter = new JLabel("Donated After:");
      GridBagConstraints gbc_lblDonatedAfter = new GridBagConstraints();
      gbc_lblDonatedAfter.anchor = GridBagConstraints.EAST;
      gbc_lblDonatedAfter.insets = new Insets(0, 0, 5, 5);
      gbc_lblDonatedAfter.gridx = 0;
      gbc_lblDonatedAfter.gridy = 2;
      contentPanel.add(lblDonatedAfter, gbc_lblDonatedAfter);
    }
    {
      donatedAfterCheckBox = new JCheckBox("");
      donatedAfterCheckBox.setSelected(true);
      GridBagConstraints gbc_donatedAfterCheckBox = new GridBagConstraints();
      gbc_donatedAfterCheckBox.insets = new Insets(0, 0, 5, 5);
      gbc_donatedAfterCheckBox.gridx = 1;
      gbc_donatedAfterCheckBox.gridy = 2;
      contentPanel.add(donatedAfterCheckBox, gbc_donatedAfterCheckBox);
    }
    {
      donatedAfterTimeField = new TimeControl();
      GridBagConstraints gbc_donatedAfterTimeField = new GridBagConstraints();
      gbc_donatedAfterTimeField.insets = new Insets(0, 0, 5, 0);
      gbc_donatedAfterTimeField.fill = GridBagConstraints.HORIZONTAL;
      gbc_donatedAfterTimeField.gridx = 2;
      gbc_donatedAfterTimeField.gridy = 2;
      contentPanel.add(donatedAfterTimeField, gbc_donatedAfterTimeField);
    }
    {
      lblDonatedBefore = new JLabel("Donated Before:");
      GridBagConstraints gbc_lblDonatedBefore = new GridBagConstraints();
      gbc_lblDonatedBefore.anchor = GridBagConstraints.EAST;
      gbc_lblDonatedBefore.insets = new Insets(0, 0, 5, 5);
      gbc_lblDonatedBefore.gridx = 0;
      gbc_lblDonatedBefore.gridy = 3;
      contentPanel.add(lblDonatedBefore, gbc_lblDonatedBefore);
    }
    {
      donatedBeforeCheckBox = new JCheckBox("");
      donatedBeforeCheckBox.setSelected(true);
      GridBagConstraints gbc_donatedBeforeCheckBox = new GridBagConstraints();
      gbc_donatedBeforeCheckBox.insets = new Insets(0, 0, 5, 5);
      gbc_donatedBeforeCheckBox.gridx = 1;
      gbc_donatedBeforeCheckBox.gridy = 3;
      contentPanel.add(donatedBeforeCheckBox, gbc_donatedBeforeCheckBox);
    }
    {
      donatedBeforeTimeField = new TimeControl();
      GridBagConstraints gbc_donatedBeforeTimeField = new GridBagConstraints();
      gbc_donatedBeforeTimeField.insets = new Insets(0, 0, 5, 0);
      gbc_donatedBeforeTimeField.fill = GridBagConstraints.HORIZONTAL;
      gbc_donatedBeforeTimeField.gridx = 2;
      gbc_donatedBeforeTimeField.gridy = 3;
      contentPanel.add(donatedBeforeTimeField, gbc_donatedBeforeTimeField);
    }
    {
      lblSingleDonationAbove = new JLabel("Minimum Donation:");
      GridBagConstraints gbc_lblSingleDonationAbove = new GridBagConstraints();
      gbc_lblSingleDonationAbove.anchor = GridBagConstraints.EAST;
      gbc_lblSingleDonationAbove.insets = new Insets(0, 0, 5, 5);
      gbc_lblSingleDonationAbove.gridx = 0;
      gbc_lblSingleDonationAbove.gridy = 4;
      contentPanel.add(lblSingleDonationAbove, gbc_lblSingleDonationAbove);
    }
    {
      minimumDonationCheckBox = new JCheckBox("");
      minimumDonationCheckBox.setSelected(true);
      GridBagConstraints gbc_minimumDonationCheckBox = new GridBagConstraints();
      gbc_minimumDonationCheckBox.insets = new Insets(0, 0, 5, 5);
      gbc_minimumDonationCheckBox.gridx = 1;
      gbc_minimumDonationCheckBox.gridy = 4;
      contentPanel.add(minimumDonationCheckBox, gbc_minimumDonationCheckBox);
    }
    {
      minimumDonationField = new JFormattedTextField(FormatUtils.getMoneyFormat());
      minimumDonationField.setText("5.00");
      minimumDonationField.setHorizontalAlignment(SwingConstants.TRAILING);
      GridBagConstraints gbc_minimumDonationField = new GridBagConstraints();
      gbc_minimumDonationField.insets = new Insets(0, 0, 5, 0);
      gbc_minimumDonationField.fill = GridBagConstraints.HORIZONTAL;
      gbc_minimumDonationField.gridx = 2;
      gbc_minimumDonationField.gridy = 4;
      contentPanel.add(minimumDonationField, gbc_minimumDonationField);
      minimumDonationField.setColumns(10);
    }
    {
      lblExcludeIfAlready = new JLabel("Exclude If Already Won:");
      GridBagConstraints gbc_lblExcludeIfAlready = new GridBagConstraints();
      gbc_lblExcludeIfAlready.insets = new Insets(0, 0, 0, 5);
      gbc_lblExcludeIfAlready.gridx = 0;
      gbc_lblExcludeIfAlready.gridy = 5;
      contentPanel.add(lblExcludeIfAlready, gbc_lblExcludeIfAlready);
    }
    {
      excludeIfWonCheckBox = new JCheckBox("");
      excludeIfWonCheckBox.setSelected(true);
      GridBagConstraints gbc_excludeIfWonCheckBox = new GridBagConstraints();
      gbc_excludeIfWonCheckBox.insets = new Insets(0, 0, 0, 5);
      gbc_excludeIfWonCheckBox.gridx = 1;
      gbc_excludeIfWonCheckBox.gridy = 5;
      contentPanel.add(excludeIfWonCheckBox, gbc_excludeIfWonCheckBox);
    }
    {
      JPanel buttonPane = new JPanel();
      buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
      getContentPane().add(buttonPane, BorderLayout.SOUTH);
      {
        drawButton = new JButton("Draw");
        drawButton.setActionCommand("OK");
        buttonPane.add(drawButton);
        getRootPane().setDefaultButton(drawButton);
      }
      {
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
      }
    }
  }
  
  class ActionHandler implements ActionListener
  {
    public void actionPerformed(ActionEvent ev)
    {
      try
      {
        if (ev.getSource() == drawButton)
        {
          runDrawing();
        }
        else if (ev.getSource() == cancelButton)
        {
          cancelDrawing();
        }
        else if (ev.getSource() == drawMethodComboBox)
        {
          if (drawMethodComboBox.getSelectedItem() == PrizeDrawMethod.RANDOM_WEIGHTED_DRAW)
          {
            maxRaffleSizeCheckBox.setEnabled(true);
            maxRaffleSizeField.setEnabled(maxRaffleSizeCheckBox.isSelected());
          }
          else
          {
            maxRaffleSizeCheckBox.setEnabled(false);
            maxRaffleSizeField.setEnabled(false);
          }
        }
        else if (ev.getSource() == donatedAfterCheckBox)
        {
          donatedAfterTimeField.setEnabled(donatedAfterCheckBox.isSelected());
        }
        else if (ev.getSource() == donatedBeforeCheckBox)
        {
          donatedBeforeTimeField.setEnabled(donatedBeforeCheckBox.isSelected());
        }
        else if (ev.getSource() == minimumDonationCheckBox)
        {
          minimumDonationField.setEnabled(minimumDonationCheckBox.isSelected());
        }
        else if (ev.getSource() == maxRaffleSizeCheckBox)
        {
          maxRaffleSizeField.setEnabled(maxRaffleSizeCheckBox.isSelected());
        }
      }
      catch(Exception e)
      {
        JOptionPane.showMessageDialog(PrizeAssignmentDialog.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
  
  private void initializeGUIEvents()
  {
    this.actionHandler = new ActionHandler();
    
    this.drawButton.addActionListener(this.actionHandler);
    this.cancelButton.addActionListener(this.actionHandler);
    this.donatedAfterCheckBox.addActionListener(this.actionHandler);
    this.donatedBeforeCheckBox.addActionListener(this.actionHandler);
    this.minimumDonationCheckBox.addActionListener(this.actionHandler);
    this.maxRaffleSizeCheckBox.addActionListener(this.actionHandler);
    this.drawMethodComboBox.addActionListener(this.actionHandler);

    this.tabOrder = new FocusTraversalManager(new Component[]
    {
      this.drawMethodComboBox,
      this.maxRaffleSizeCheckBox,
      this.maxRaffleSizeField,
      this.donatedAfterCheckBox,
      this.donatedAfterTimeField,
      this.donatedBeforeCheckBox,
      this.donatedBeforeTimeField,
      this.minimumDonationCheckBox,
      this.minimumDonationField,
      this.drawButton,
      this.cancelButton,
    });
    
    this.setFocusTraversalPolicy(this.tabOrder);
  }
  
  public PrizeAssignmentDialog(JFrame owner, PrizeAssign assigner, PrizeDrawMethod defaultMethod, BigDecimal defaultAmount, Date defaultStartTime, Date defaultEndTime)
  {
    super(owner, true);
    
    this.assigner = assigner;
    this.selectedDonor = null;
    
    this.initializeGUI();
    this.initializeGUIEvents();
    
    this.minimumDonationField.setText(defaultAmount.toString());
    
    if (defaultStartTime != null)
    {
      this.donatedAfterTimeField.setTimeValue(defaultStartTime);
      this.donatedAfterCheckBox.setSelected(true);
    }
    else
    {
      this.donatedAfterCheckBox.setSelected(false);
    }
    
    if (defaultEndTime != null)
    {
      this.donatedBeforeTimeField.setTimeValue(defaultEndTime);
      this.donatedBeforeCheckBox.setSelected(true);
    }
    else
    {
      this.donatedAfterCheckBox.setSelected(false);
    }
    
    if (defaultMethod != null)
    {
      this.drawMethodComboBox.setSelectedItem(defaultMethod);
    }
  }
  
  public Donor getResult()
  {
    return this.selectedDonor;
  }
  
  private void closeDialog()
  {
    this.setVisible(false);
    this.dispose();
  }
  
  private void runDrawing()
  {
    PrizeAssignParams params = new PrizeAssignParams();
    params.method = (PrizeDrawMethod) this.drawMethodComboBox.getSelectedItem();
    params.excludeIfAlreadyWon = this.excludeIfWonCheckBox.isSelected();
    params.donatedAfter = this.donatedAfterCheckBox.isSelected() ? this.donatedAfterTimeField.getTimeValue() : null;
    params.donatedBefore = this.donatedBeforeCheckBox.isSelected() ? this.donatedBeforeTimeField.getTimeValue() : null;
    params.targetAmount = this.minimumDonationCheckBox.isSelected() ? new BigDecimal(this.minimumDonationField.getText()) : null;
    
    if (params.method == PrizeDrawMethod.RANDOM_WEIGHTED_DRAW && this.maxRaffleSizeCheckBox.isSelected())
    {
      params.maxRaffleTickets = Integer.parseInt(this.maxRaffleSizeField.getText());
    }
    else
    {
      params.maxRaffleTickets = 0;
    }
    
    this.selectedDonor = this.assigner.selectWinner(params);
    
    this.closeDialog();
  }
  
  private void cancelDrawing()
  {
    this.selectedDonor = null;
    this.closeDialog();
  }
}
