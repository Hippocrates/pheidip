package pheidip.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pheidip.objects.Donor;

// TODO: 
// All of the error conditions need better exception messages
// for example, we can generally identify what the issue is, based 
// on which method we're in, and the sql code

public class DonorData
{
  private DonationDataAccess manager;
  private Connection connection;
  
  private PreparedStatement selectDonorByID;
  private PreparedStatement selectDonorByEmail;
  private PreparedStatement selectDonorByAlias;
  private PreparedStatement selectAllDonors;
  private PreparedStatement deleteDonorStatement;
  private PreparedStatement createDonorStatement;
  private PreparedStatement updateDonorEmailStatement;
  private PreparedStatement updateDonorAliasStatement;
  private PreparedStatement updateDonorFirstNameStatement;
  private PreparedStatement updateDonorLastNameStatement;

  public DonorData(DonationDataAccess manager)
  {
    this.manager = manager;
    
    if (manager.isConnected())
    {
      this.setConnection(manager.getConnection());
    }
  }
  
  void setConnection(Connection connection)
  {
    this.connection = connection;
    
    if (this.connection != null)
    {
      this.rebuildPreparedStatements();
    }
  }
  
  private void rebuildPreparedStatements()
  {
    try
    {
      this.selectDonorByID = this.connection.prepareStatement("SELECT * FROM Donor WHERE Donor.donorId = ?;");
      this.selectDonorByEmail = this.connection.prepareStatement("SELECT * FROM Donor WHERE Donor.email = ?;");
      this.selectDonorByAlias = this.connection.prepareStatement("SELECT * FROM Donor WHERE Donor.alias = ?;");
      this.selectAllDonors = this.connection.prepareStatement("SELECT * FROM Donor;");
    
      this.deleteDonorStatement = this.connection.prepareStatement("DELETE FROM Donor WHERE Donor.donorId = ?;");
      
      this.createDonorStatement = this.connection.prepareStatement("INSERT INTO Donor (donorId, email, alias, firstName, lastName) VALUES(?,?,?,?,?);");

      this.updateDonorEmailStatement = this.connection.prepareStatement("UPDATE Donor SET email = ? WHERE donorId = ?;");
      this.updateDonorAliasStatement = this.connection.prepareStatement("UPDATE Donor SET alias = ? WHERE donorId = ?;");
      this.updateDonorFirstNameStatement = this.connection.prepareStatement("UPDATE Donor SET firstName = ? WHERE donorId = ?;");
      this.updateDonorLastNameStatement = this.connection.prepareStatement("UPDATE Donor SET lastName = ? WHERE donorId = ?;");
    } 
    catch (SQLException e)
    {
      this.manager.handleSQLException(e);
    }
  }
  
  public Donor getDonorById(int donorId)
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
      this.manager.handleSQLException(e);
    }
    
    return result;
  }
  
  public Donor getDonorByEmail(String email)
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
      this.manager.handleSQLException(e);
    }
    
    return result;
  }

  public Donor getDonorByAlias(String alias)
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
      this.manager.handleSQLException(e);
    }
    
    return result;
  }
  
  public List<Donor> getAllDonors()
  {
    List<Donor> result = null;
    
    try
    {
      ResultSet rows = this.selectAllDonors.executeQuery();
      
      result = DonorData.extractDonorList(rows);
    } 
    catch (SQLException e)
    {
      this.manager.handleSQLException(e);
    }
    
    return result;
  }

  public void deleteDonor(int id)
  {
    try
    {
      this.deleteDonorStatement.setInt(1, id);
      int deleted = this.deleteDonorStatement.executeUpdate();
      
      if (deleted != 1)
      {
        throw new RuntimeException("Error, trying to delete donor with id = " + id + ", does not exist.");
      }
    } 
    catch (SQLException e)
    {
      this.manager.handleSQLException(e);
    }
  }
  
  public void createDonor(Donor newDonor)
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
      this.manager.handleSQLException(e);
    }
  }
  
  public void setDonorEmail(int id, String newEmail)
  {
    this.runStringFieldUpdate(this.updateDonorEmailStatement, id, newEmail.toLowerCase());
  }
  
  public void setDonorAlias(int id, String newAlias)
  {
    this.runStringFieldUpdate(this.updateDonorAliasStatement, id, newAlias.toLowerCase());
  }
  
  public void setDonorFirstName(int id, String newFirstName)
  {
    this.runStringFieldUpdate(this.updateDonorFirstNameStatement, id, newFirstName);
  }
  
  public void setDonorLastName(int id, String newLastName)
  {
    this.runStringFieldUpdate(this.updateDonorLastNameStatement, id, newLastName);
  }
  
  // todo: add a string parameter representing the column being updated
  private void runStringFieldUpdate(PreparedStatement sql, int id, String value)
  {
    try
    {
      sql.setInt(2, id);
      sql.setString(1, value);
      
      int created = sql.executeUpdate();
      
      if (created != 1)
      {
        throw new RuntimeException("Error, update of donor failed.");
      }
    }
    catch (SQLException e)
    {
      this.manager.handleSQLException(e);
    }
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
}
