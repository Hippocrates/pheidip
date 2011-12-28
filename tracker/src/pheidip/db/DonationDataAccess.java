package pheidip.db;

import java.io.File;

public interface DonationDataAccess 
{
	public DonorData getDonorData();

	public DonationData getDonationData();

<<<<<<< HEAD
	public BidData getBids();

	public SpeedRunData getSpeedRuns();
=======
	public BidData getBidData();

	public SpeedRunData getSpeedRunData();
>>>>>>> master

	public PrizeData getPrizeData();

	public void connectToDatabaseServer(DBType type, String server,
			String dbname, String user, String password);

	public void openFileDatabase(File location);

	public void createMemoryDatabase();

	public DBType getConnectionType();

	public boolean isConnected();

	public void closeConnection();

}