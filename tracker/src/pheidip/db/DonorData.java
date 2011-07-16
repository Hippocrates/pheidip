package pheidip.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pheidip.objects.Donor;

public class DonorData extends DataInterface
{
  private PreparedStatement selectDonorByID;
  private PreparedStatement selectDonorByEmail;
  private PreparedStatement selectDonorByAlias;
  private PreparedStatement selectAllDonors;
  private PreparedStatement selectAllDonorsWithoutPrizes;
  private PreparedStatement deleteDonorStatement;
  private PreparedStatement createDonorStatement;
  private PreparedStatement updateDonorStatement;
  private PreparedStatement deleteAllDonorsStatement;
  private PreparedStatement allowDeleteDonorStatement;
  private PreparedStatement selectPrizeWinnerStatement;
  
  public DonorData(DonationDataAccess manager)
  {
    super(manager);
  }

  void rebuildPreparedStatements()
  {
    try
    {
      this.selectAllDonorsWithoutPrizes = this.getConnection().prepareStatement("SELECT * FROM Donor WHERE Donor.donorId NOT IN (SELECT Prize.donorId FROM Prize);");
      this.allowDeleteDonorStatement = this.getConnection().prepareStatement("SELECT COUNT(*) FROM Donation, Prize WHERE Donation.donorId = ? OR Prize.donorId = ?;");
      this.selectDonorByID = this.getConnection().prepareStatement("SELECT * FROM Donor WHERE Donor.donorId = ?;");
      this.selectDonorByEmail = this.getConnection().prepareStatement("SELECT * FROM Donor WHERE Donor.email = ?;");
      this.selectDonorByAlias = this.getConnection().prepareStatement("SELECT * FROM Donor WHERE Donor.alias = ?;");
      this.selectAllDonors = this.getConnection().prepareStatement("SELECT * FROM Donor;");
    
      this.deleteDonorStatement = this.getConnection().prepareStatement("DELETE FROM Donor WHERE Donor.donorId = ?;");
      this.deleteAllDonorsStatement = this.getConnection().prepareStatement("DELETE FROM Donor;");
      
      this.createDonorStatement = this.getConnection().prepareStatement("INSERT INTO Donor (donorId, email, alias, firstName, lastName) VALUES(?,?,?,?,?);");
      this.updateDonorStatement = this.getConnection().prepareStatement("UPDATE Donor SET email = ?, alias = ?, firstName = ?, lastName = ? WHERE donorId = ?;");
    
      this.selectPrizeWinnerStatement = this.getConnection().prepareStatement("SELECT * FROM Donor, Prize WHERE Prize.prizeId = ? AND Prize.donorId = Donor.donorId;");
    } 
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  synchronized public Donor getDonorById(int donorId)
  {
    Donor result = null;
    
    try
    {
      this.selectDonorByID.setInt(1, donorId);
      
      ResultSet rows = this.selectDonorByID.executeQuery();
      
      if (rows.next())
      {
        result = DonorData.extractDonor(rows);
      }
    } 
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }
  
  synchronized public Donor getDonorByEmail(String email)
  {
    Donor result = null;
    
    try
    {
      this.selectDonorByEmail.setString(1, email);
      
      ResultSet rows = this.selectDonorByEmail.executeQuery();
      
      if (rows.next())
      {
        result = DonorData.extractDonor(rows);
      }
    } 
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }

  synchronized public Donor getDonorByAlias(String alias)
  {
    Donor result = null;
    
    try
    {
      this.selectDonorByAlias.setString(1, alias);
      
      ResultSet rows = this.selectDonorByAlias.executeQuery();
      
      if (rows.next())
      {
        result = DonorData.extractDonor(rows);
      }
    } 
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }
  
  synchronized public List<Donor> getAllDonors()
  {
    List<Donor> result = null;
    
    try
    {
      ResultSet rows = this.selectAllDonors.executeQuery();
      
      result = DonorData.extractDonorList(rows);
    } 
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }
  
  synchronized public List<Donor> getDonorsWithoutPrizes()
  {
    List<Donor> result = null;
    
    try
    {
      ResultSet rows = this.selectAllDonorsWithoutPrizes.executeQuery();
      
      result = DonorData.extractDonorList(rows);
    } 
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }

  synchronized public void deleteDonor(int id)
  {
    try
    {
      this.allowDeleteDonorStatement.setInt(1, id);
      this.allowDeleteDonorStatement.setInt(2, id);
      
      ResultSet results = this.allowDeleteDonorStatement.executeQuery();
      
      if (results.next())
      {
        int count = results.getInt(1);
        if (count > 0)
        {
          throw new SQLException("Error, violated constraint : '" + DonationDataConstraint.DonationFKDonor.toString() + "'", "", JDBCManager.getCodeForError(this.getManager().getConnectionType(), SQLError.FOREIGN_KEY_VIOLATION));
        }
      }
      
      this.deleteDonorStatement.setInt(1, id);
      int deleted = this.deleteDonorStatement.executeUpdate();
      
      if (deleted != 1)
      {
        throw new RuntimeException("Error, trying to delete donor with id = " + id + ", does not exist.");
      }
    } 
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  synchronized public void createDonor(Donor newDonor)
  {
    try
    {
      this.createDonorStatement.setInt(1, newDonor.getId());
      this.createDonorStatement.setString(2, newDonor.getEmail());
      this.createDonorStatement.setString(3, newDonor.getAlias());
      this.createDonorStatement.setString(4, newDonor.getFirstName());
      this.createDonorStatement.setString(5, newDonor.getLastName());
      
      int created = this.createDonorStatement.executeUpdate();
      
      if (created != 1)
      {
        throw new RuntimeException("Error, insert of new donor failed.");
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  synchronized public void updateDonor(Donor donor)
  {
    try
    {
      this.updateDonorStatement.setString(1, donor.getEmail());
      this.updateDonorStatement.setString(2, donor.getAlias());
      this.updateDonorStatement.setString(3, donor.getFirstName());
      this.updateDonorStatement.setString(4, donor.getLastName());
      this.updateDonorStatement.setInt(5, donor.getId());
      
      this.updateDonorStatement.execute();
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  synchronized public Donor getPrizeWinner(int prizeId)
  {
    Donor result = null;
    
    try
    {
      this.selectPrizeWinnerStatement.setInt(1, prizeId);
      
      ResultSet rows = this.selectPrizeWinnerStatement.executeQuery();
      
      if (rows.next())
      {
        result = DonorData.extractDonor(rows);
      }
    } 
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }
  
  private static List<Donor> extractDonorList(ResultSet rows) throws SQLException
  {
    List<Donor> result = new ArrayList<Donor>();
    
    while (rows.next())
    {
      result.add(DonorData.extractDonor(rows));
    }
    
    return result;
  }
  
  private static Donor extractDonor(ResultSet row) throws SQLException
  {
    return new Donor(
        row.getInt("donorId"),
        row.getString("email"),
        row.getString("alias"),
        row.getString("firstName"),
        row.getString("lastName") );
  }

  public void deleteAllDonors()
  {
    try
    {
      int updated = this.deleteAllDonorsStatement.executeUpdate();
      
      if (updated < 0)
      {
        throw new RuntimeException("Error, could not delete all donors.");
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
}
