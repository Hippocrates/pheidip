package pheidip.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pheidip.objects.Prize;

public class PrizeData extends DataInterface
{
  private PreparedStatement selectPrizeByIdStatement;
  private PreparedStatement deletePrizeStatement;
  private PreparedStatement updatePrizeStatement;
  private PreparedStatement insertPrizeStatement;

  public PrizeData(DonationDataAccess manager)
  {
    super(manager);
  }

  @Override
  void rebuildPreparedStatements()
  {
    try
    {
      this.selectPrizeByIdStatement = this.getConnection().prepareStatement("SELECT * FROM Prize WHERE Prize.prizeId = ?;");
    
      this.insertPrizeStatement = this.getConnection().prepareStatement("INSERT INTO Prize (prizeId,name,imageURL,description) VALUES (?,?,?,?);");
      
      this.updatePrizeStatement = this.getConnection().prepareStatement("UPDATE Prize SET name = ?, imageURL = ?, description = ? WHERE Prize.prizeId = ?;");
      
      this.deletePrizeStatement = this.getConnection().prepareStatement("DELETE FROM Prize WHERE Prize.prizeId = ?;");
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  private static Prize extractPrize(ResultSet set) throws SQLException
  {
    return new Prize(set.getInt("prizeId"), set.getString("name"), set.getString("imageURL"), set.getString("description"));
  }
  
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
  
  public synchronized void deletePrize(int prizeId)
  {
    try
    {
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
