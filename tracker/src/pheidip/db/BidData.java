package pheidip.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pheidip.objects.Challenge;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceOption;


public class BidData extends DataInterface
{
  private PreparedStatement selectChoiceByIdStatement;
  private PreparedStatement selectChoicesBySpeedRunId;
  private PreparedStatement insertChoiceStatement;
  private PreparedStatement deleteChoiceStatement;
  private PreparedStatement updateChoiceStatement;
  
  private PreparedStatement selectChoiceOptionByIdStatement;
  private PreparedStatement selectChoiceOptionsByChoiceId;
  private PreparedStatement insertChoiceOptionStatement;
  private PreparedStatement updateChoiceOptionStatement;
  private PreparedStatement deleteChoiceOptionStatement;
  
  private PreparedStatement selectChallengeByIdStatement;
  private PreparedStatement selectChallengesBySpeedRunId;
  private PreparedStatement insertChallengeStatement;
  private PreparedStatement updateChallengeStatement;
  private PreparedStatement deleteChallengeStatement;
  
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
      
      this.updateChoiceStatement = this.getConnection().prepareStatement("UPDATE Choice SET speedRunId = ?, name = ? WHERE Choice.choiceId = ?;");

      this.insertChoiceStatement = this.getConnection().prepareStatement("INSERT INTO Choice (choiceId, speedRunId, name) VALUES (?,?,?);");
    
      this.deleteChoiceStatement = this.getConnection().prepareStatement("DELETE FROM Choice WHERE Choice.choiceId = ?;");
      
      this.selectChoiceOptionByIdStatement = this.getConnection().prepareStatement("SELECT * FROM ChoiceOption WHERE ChoiceOption.optionId = ?;");
      this.selectChoiceOptionsByChoiceId = this.getConnection().prepareStatement("SELECT * FROM ChoiceOption WHERE ChoiceOption.choiceId = ?;");
      
      this.updateChoiceOptionStatement = this.getConnection().prepareStatement("UPDATE ChoiceOption SET choiceId = ?, Name = ? WHERE ChoiceOption.optionId = ?;");
      
      this.insertChoiceOptionStatement = this.getConnection().prepareStatement("INSERT INTO ChoiceOption (optionId, choiceId, name) VALUES (?,?,?);");
    
      this.deleteChoiceOptionStatement = this.getConnection().prepareStatement("DELETE FROM ChoiceOption WHERE ChoiceOption.optionId = ?;");
      
      
      this.selectChallengeByIdStatement = this.getConnection().prepareStatement("SELECT * FROM Challenge WHERE Challenge.challengeId = ?;");
      this.selectChallengesBySpeedRunId = this.getConnection().prepareStatement("SELECT * FROM Challenge WHERE Challenge.speedRunId = ?;");
      
      this.updateChallengeStatement = this.getConnection().prepareStatement("UPDATE Challenge SET speedRunId = ?, name = ?, goalAmount = ? WHERE Challenge.challengeId = ?;");
      
      this.insertChallengeStatement = this.getConnection().prepareStatement("INSERT INTO Challenge (challengeId, speedRunId, name, goalAmount) VALUES (?,?,?,?);");
    
      this.deleteChallengeStatement = this.getConnection().prepareStatement("DELETE FROM Challenge WHERE Challenge.challengeId = ?;");
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
        result.getInt("speedRunId"));
  }
  
  public synchronized void updateChoice(Choice choice)
  {
    try
    {
      this.updateChoiceStatement.setInt(3, choice.getId());
      this.updateChoiceStatement.setString(2, choice.getName());
      this.updateChoiceStatement.setInt(1, choice.getSpeedRunId());
      
      int updated = this.updateChoiceStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Error, could not update choice");
      }
    }
    catch(SQLException e)
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

  public synchronized void deleteChoice(int choiceId)
  {
    try
    {
      this.deleteChoiceStatement.setInt(1, choiceId);
      
      int updated = this.deleteChoiceStatement.executeUpdate();
    
      if (updated != 1)
      {
        throw new RuntimeException("Error, deletion of choice failed.");
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }

  public synchronized ChoiceOption getChoiceOptionById(int optionId)
  {
    ChoiceOption result = null;
    
    try
    {
      this.selectChoiceOptionByIdStatement.setInt(1, optionId);
      
      ResultSet results = this.selectChoiceOptionByIdStatement.executeQuery();
      
      if (results.next())
      {
        result = extractChoiceOption(results);
      }
    } 
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }

  public synchronized void insertChoiceOption(ChoiceOption choiceOption)
  {
    try
    {
      this.insertChoiceOptionStatement.setInt(1, choiceOption.getId());
      this.insertChoiceOptionStatement.setInt(2, choiceOption.getChoiceId());
      this.insertChoiceOptionStatement.setString(3, choiceOption.getName());
      
      int updated = this.insertChoiceOptionStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Could not insert choiceOption.");
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  private static ChoiceOption extractChoiceOption(ResultSet result) throws SQLException
  {
    return new ChoiceOption(
        result.getInt("optionId"),
        result.getString("name"),
        result.getInt("choiceId"));
  }

  public synchronized void updateChoiceOption(ChoiceOption option)
  {
    try
    {
      this.updateChoiceOptionStatement.setInt(3, option.getId());
      this.updateChoiceOptionStatement.setInt(1, option.getChoiceId());
      this.updateChoiceOptionStatement.setString(2, option.getName());
      
      int created = this.updateChoiceOptionStatement.executeUpdate();
      
      if (created != 1)
      {
        throw new RuntimeException("Error, update of choiceOption failed.");
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  public synchronized List<ChoiceOption> getChoiceOptionsByChoiceId(int choiceId)
  {
    List<ChoiceOption> results = null;
    
    try
    {
      this.selectChoiceOptionsByChoiceId.setInt(1, choiceId);
      
      ResultSet queryResult = this.selectChoiceOptionsByChoiceId.executeQuery();
    
      results = new ArrayList<ChoiceOption>();
      
      while (queryResult.next())
      {
        results.add(BidData.extractChoiceOption(queryResult));
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return results;
  }

  public synchronized void deleteChoiceOption(int optionId)
  {
    try
    {
      this.deleteChoiceOptionStatement.setInt(1, optionId);
      
      int updated = this.deleteChoiceOptionStatement.executeUpdate();
    
      if (updated != 1)
      {
        throw new RuntimeException("Error, deletion of choiceOption failed.");
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  public synchronized Challenge getChallengeById(int challengeId)
  {
    Challenge result = null;
    
    try
    {
      this.selectChallengeByIdStatement.setInt(1, challengeId);
      
      ResultSet results = this.selectChallengeByIdStatement.executeQuery();
      
      if (results.next())
      {
        result = extractChallenge(results);
      }
    } 
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }

  public synchronized void insertChallenge(Challenge challenge)
  {
    try
    {
      this.insertChallengeStatement.setInt(1, challenge.getId());
      this.insertChallengeStatement.setInt(2, challenge.getSpeedRunId());
      this.insertChallengeStatement.setString(3, challenge.getName());
      this.insertChallengeStatement.setBigDecimal(4, challenge.getGoalAmount());
      
      int updated = this.insertChallengeStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Could not insert challenge.");
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  private static Challenge extractChallenge(ResultSet result) throws SQLException
  {
    return new Challenge(
        result.getInt("challengeId"),
        result.getString("name"),
        result.getBigDecimal("goalAmount"),
        result.getInt("speedRunId"));
  }

  public synchronized void updateChallenge(Challenge challenge)
  {
    try
    {
      this.updateChallengeStatement.setInt(4, challenge.getId());
      this.updateChallengeStatement.setInt(1, challenge.getSpeedRunId());
      this.updateChallengeStatement.setString(1, challenge.getName());
      this.updateChallengeStatement.setBigDecimal(1, challenge.getGoalAmount());
      
      int updated = this.updateChallengeStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Could not update challenge.");
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }

  public synchronized List<Challenge> getChallengesBySpeedrun(int speedRunId)
  {
    List<Challenge> results = null;
    
    try
    {
      this.selectChallengesBySpeedRunId.setInt(1, speedRunId);
      
      ResultSet queryResult = this.selectChallengesBySpeedRunId.executeQuery();
    
      results = new ArrayList<Challenge>();
      
      while (queryResult.next())
      {
        results.add(BidData.extractChallenge(queryResult));
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return results;
  }

  public synchronized void deleteChallenge(int challengeId)
  {
    try
    {
      this.deleteChallengeStatement.setInt(1, challengeId);
      
      int updated = this.deleteChallengeStatement.executeUpdate();
    
      if (updated != 1)
      {
        throw new RuntimeException("Error, deletion of challenge failed.");
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
}
