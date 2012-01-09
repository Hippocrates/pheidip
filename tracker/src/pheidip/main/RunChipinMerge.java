package pheidip.main;

import java.util.Scanner;

import pheidip.db.DBType;
import pheidip.logic.DonationDatabaseManager;
import pheidip.logic.chipin.ChipinLoginManager;
import pheidip.logic.chipin.ChipinMergeProcess;
import pheidip.logic.chipin.ChipinWebsiteDonationSource;

public class RunChipinMerge
{
  public static void main(String[] args)
  {
	DonationDatabaseManager dbManager = null;
	ChipinLoginManager chipinManager = null;
	
	try
	{
		dbManager = new DonationDatabaseManager();
		
		Scanner data = new Scanner(System.in);
		
		String url = data.nextLine().trim();
		String dbName = data.nextLine().trim();
		String userName = data.nextLine().trim();
		String password = data.nextLine().trim();
		
		String chipinUserName = data.nextLine().trim();
		String chipinPasswd = data.nextLine().trim();
		String chipinId = data.nextLine().trim();
		
		dbManager.connectToServer(DBType.MYSQL, url, dbName, userName, password);
		
		chipinManager = new ChipinLoginManager();
		
		chipinManager.logIn(chipinUserName, chipinPasswd, chipinId);
		
		ChipinMergeProcess process = new ChipinMergeProcess(dbManager, new ChipinWebsiteDonationSource(chipinManager));
	
		process.run();
		
		System.out.println("Completed");
	}
	finally
	{
		if (dbManager != null)
		{
			dbManager.closeConnection();
		}
		
		if (chipinManager != null)
		{
			chipinManager.logOut();
		}
	}
	
  }
}
