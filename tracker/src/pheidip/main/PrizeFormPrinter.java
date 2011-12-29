package pheidip.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import pheidip.db.PrizeData;
import pheidip.logic.DonationDatabaseManager;
import pheidip.objects.Donor;
import pheidip.objects.Prize;
import pheidip.ui.DatabaseConnectDialog;
import pheidip.ui.UIConfiguration;
import pheidip.util.StringUtils;

public final class PrizeFormPrinter
{
  public static void main(String[] args) throws java.io.IOException
  {
    UIConfiguration.setDefaultConfiguration();

    DonationDatabaseManager db = new DonationDatabaseManager();
    
    DatabaseConnectDialog dialog = new DatabaseConnectDialog(null, db);
    dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    dialog.setVisible(true);
    
    if (db.isConnected())
    {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1)
        {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        
        String template = fileData.toString();
        
        PrizeData prizeData = db.getDataAccess().getPrizeData();

        List<Prize> prizes = prizeData.getAllPrizes();
        
        List<Donor> winners = new ArrayList<Donor>();
        
        int maxPrizeSize = 0;
        int maxEmailSize = 0;
        
        for (Prize p : prizes)
        {
          if (p.getName() != null)
          {
            maxPrizeSize = Math.max(maxPrizeSize, p.getName().length());
          }
          
          Donor d = p.getWinner();
          
          if (d != null)
          {
            maxEmailSize = Math.max(maxEmailSize, d.getEmail().length());
          }
          
          winners.add(d);
        }
        
        db.closeConnection();
        
        for (int i = 0; i < prizes.size(); ++i)
        {
          Donor winner = winners.get(i);
          Prize prize = prizes.get(i);
          
          if (winner == null)
          {
            //System.out.println(String.format("Prize %1$s had no winner.", prize.getName()));
          }
          else
          {
            System.out.println(String.format(
                template,
                    StringUtils.emptyIfNull(winner.getFirstName()),
                    StringUtils.emptyIfNull(winner.getLastName()),
                    StringUtils.emptyIfNull(winner.getEmail()),
                    StringUtils.emptyIfNull(prize.getName())));
            System.out.println();
          }
          
        }
    }
  }
}
