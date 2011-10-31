package pheidip.db.deprecated;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pheidip.db.DonationDataConstraint;
import pheidip.db.JDBCManager;
import pheidip.db.PrizeData;
import pheidip.db.SQLError;
import pheidip.objects.Prize;

public class OldPrizeData extends OldDataInterface implements PrizeData
{
  private PreparedStatement allowDeletePrizeStatement;
  private PreparedStatement selectPrizeByIdStatement;
  private PreparedStatement deletePrizeStatement;
  private PreparedStatement updatePrizeStatement;
  private PreparedStatement insertPrizeStatement;
  private PreparedStatement selectPrizeByDonorIdStatement;
  private PreparedStatement attachWinnerToPrizeStatement;
  private PreparedStatement removeWinnerFromPrizeStatement;
  private PreparedStatement selectAllPrizesStatement;

  public OldPrizeData(OldDonationDataAccess manager)
  {
    super(manager);
  }

  @Override
  void rebuildPreparedStatements()
  {
    try
    {
      this.allowDeletePrizeStatement = this.getConnection().prepareStatement("SELECT COUNT(*) FROM Prize WHERE Prize.prizeId = ? AND Prize.donorId IS NOT NULL;");
      this.selectPrizeByIdStatement = this.getConnection().prepareStatement("SELECT * FROM Prize WHERE Prize.prizeId = ?;");
    
      this.insertPrizeStatement = this.getConnection().prepareStatement("INSERT INTO Prize (prizeId,name,imageURL,description) VALUES (?,?,?,?);");
      
      this.updatePrizeStatement = this.getConnection().prepareStatement("UPDATE Prize SET name = ?, imageURL = ?, description = ? WHERE Prize.prizeId = ?;");
      
      this.deletePrizeStatement = this.getConnection().prepareStatement("DELETE FROM Prize WHERE Prize.prizeId = ?;");
    
      this.selectPrizeByDonorIdStatement = this.getConnection().prepareStatement("SELECT * FROM Prize WHERE Prize.donorId = ?;");
      this.attachWinnerToPrizeStatement = this.getConnection().prepareStatement("UPDATE Prize SET donorId = ? WHERE Prize.prizeId = ?;");
      this.removeWinnerFromPrizeStatement = this.getConnection().prepareStatement("UPDATE Prize SET donorId = NULL WHERE Prize.prizeId = ?;");
      
      this.selectAllPrizesStatement = this.getConnection().prepareStatement("SELECT * FROM PRIZE;");
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  private static Prize extractPrize(ResultSet set) throws SQLException
  {
    return new Prize(set.getInt("prizeId"), set.getString("name"), set.getString("imageURL"), set.getString("description"), (Integer) set.getObject("donorId"));
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.PrizeData#insertPrize(pheidip.objects.Prize)
 */
@Override
public synchronized void insertPrize(Prize toAdd)
  {
    try
    {
      this.insertPrizeStatement.setInt(1, toAdd.getId());
      this.insertPrizeStatement.setString(2, toAdd.getName());
      this.insertPrizeStatement.setString(3, toAdd.getImageURL());
      this.insertPrizeStatement.setString(4, toAdd.getDescription());
      
      int updated = this.insertPrizeStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Error, could not insert prize.");
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.PrizeData#updatePrize(pheidip.objects.Prize)
 */
@Override
public synchronized void updatePrize(Prize toUpdate)
  {
    try
    {
      this.updatePrizeStatement.setInt(4, toUpdate.getId());
      this.updatePrizeStatement.setString(1, toUpdate.getName());
      this.updatePrizeStatement.setString(2, toUpdate.getImageURL());
      this.updatePrizeStatement.setString(3, toUpdate.getDescription());
      
      int updated = this.updatePrizeStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Error, could not update prize.");
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.PrizeData#getPrizeById(int)
 */
@Override
public synchronized Prize getPrizeById(int prizeId)
  {
    Prize result = null;
    
    try
    {
      this.selectPrizeByIdStatement.setInt(1, prizeId);
      
      ResultSet queryResults = this.selectPrizeByIdStatement.executeQuery();
      
      if (queryResults.next())
      {
        result = extractPrize(queryResults);
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.PrizeData#getAllPrizes()
 */
@Override
public synchronized List<Prize> getAllPrizes()
  {
    List<Prize> result = new ArrayList<Prize>();
    
    try
    {
      ResultSet queryResults = this.selectAllPrizesStatement.executeQuery();
      
      while (queryResults.next())
      {
        result.add(extractPrize(queryResults));
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.PrizeData#getPrizeByDonorId(int)
 */
@Override
public synchronized Prize getPrizeByDonorId(int donorId)
  {
    Prize result = null;
    
    try
    {
      this.selectPrizeByDonorIdStatement.setInt(1, donorId);
      
      ResultSet queryResults = this.selectPrizeByDonorIdStatement.executeQuery();
      
      if (queryResults.next())
      {
        result = extractPrize(queryResults);
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.PrizeData#setPrizeWinner(int, int)
 */
@Override
public synchronized void setPrizeWinner(int prizeId, int donorId)
  {
    try
    {
      this.attachWinnerToPrizeStatement.setInt(2, prizeId);
      this.attachWinnerToPrizeStatement.setInt(1, donorId);
      
      int updated = this.attachWinnerToPrizeStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Error, could not set prize winner.");
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.PrizeData#removePrizeWinner(int)
 */
@Override
public synchronized void removePrizeWinner(int prizeId)
  {
    try
    {
      this.removeWinnerFromPrizeStatement.setInt(1, prizeId);

      int updated = this.removeWinnerFromPrizeStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Error, could not remove prize winner.");
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.PrizeData#deletePrize(int)
 */
@Override
public synchronized void deletePrize(int prizeId)
  {
    try
    {
      this.allowDeletePrizeStatement.setInt(1, prizeId);
      
      ResultSet results = this.allowDeletePrizeStatement.executeQuery();
      
      if (results.next())
      {
        int count = results.getInt(1);
        if (count > 0)
        {
          throw new SQLException("Error, violated constraint : '" + DonationDataConstraint.PrizeFKDonor.toString() + "'", "", JDBCManager.getCodeForError(this.getManager().getConnectionType(), SQLError.FOREIGN_KEY_VIOLATION));
        }
      }
      
      this.deletePrizeStatement.setInt(1, prizeId);
      
      int updated = this.deletePrizeStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Error, could not delete prize.");
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }

}
