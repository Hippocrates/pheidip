package pheidip.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pheidip.objects.Choice;


public class BidData extends DataInterface
{
  private PreparedStatement selectChoiceByIdStatement;
  private PreparedStatement selectChoicesBySpeedRunId;
  private PreparedStatement insertChoiceStatement;
  private PreparedStatement setChoiceNameStatement;
  
  public BidData(DonationDataAccess manager)
  {
    super(manager);
  }

  @Override
  void rebuildPreparedStatements()
  {
    try
    {
      this.selectChoiceByIdStatement = this.getConnection().prepareStatement("SELECT * FROM Choice WHERE Choice.choiceId = ?;");
      this.selectChoicesBySpeedRunId = this.getConnection().prepareStatement("SELECT * FROM Choice WHERE Choice.speedRunId = ?;");
      
      this.setChoiceNameStatement = this.getConnection().prepareStatement("UPDATE Choice SET Name = ? WHERE Choice.choiceId = ?;");
      
      this.insertChoiceStatement = this.getConnection().prepareStatement("INSERT INTO Choice (choiceId, speedRunId, name) VALUES (?,?,?);");
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }

  public synchronized Choice getChoiceById(int choiceId)
  {
    Choice result = null;
    
    try
    {
      this.selectChoiceByIdStatement.setInt(1, choiceId);
      
      ResultSet results = this.selectChoiceByIdStatement.executeQuery();
      
      if (results.next())
      {
        result = extractChoice(results);
      }
    } 
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }

  public synchronized void insertChoice(Choice choice)
  {
    try
    {
      this.insertChoiceStatement.setInt(1, choice.getId());
      this.insertChoiceStatement.setInt(2, choice.getSpeedRunId());
      this.insertChoiceStatement.setString(3, choice.getName());
      
      int updated = this.insertChoiceStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Could not insert choice.");
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  private static Choice extractChoice(ResultSet result) throws SQLException
  {
    return new Choice(
        result.getInt("choiceId"),
        result.getString("name"),
        result.getInt("SpeedRunId"));
  }

  public synchronized void setChoiceName(int choiceId, String newName)
  {
    runStringFieldUpdate(this.setChoiceNameStatement, choiceId, newName);
  }
  
  private void runStringFieldUpdate(PreparedStatement sql, int id, String value)
  {
    try
    {
      sql.setInt(2, id);
      sql.setString(1, value);
      
      int created = sql.executeUpdate();
      
      if (created != 1)
      {
        throw new RuntimeException("Error, update of choice failed.");
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }

  public synchronized List<Choice> getChoicesBySpeedrun(int speedRunId)
  {
    List<Choice> results = null;
    
    try
    {
      this.selectChoicesBySpeedRunId.setInt(1, speedRunId);
      
      ResultSet queryResult = this.selectChoicesBySpeedRunId.executeQuery();
    
      results = new ArrayList<Choice>();
      
      while (queryResult.next())
      {
        results.add(BidData.extractChoice(queryResult));
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return results;
  }

}
