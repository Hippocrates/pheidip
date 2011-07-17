package pheidip.db;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pheidip.objects.Bid;
import pheidip.objects.BidState;
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
  private PreparedStatement selectChoiceOptionTotalStatement;
  
  private PreparedStatement selectChallengeByIdStatement;
  private PreparedStatement selectChallengesBySpeedRunId;
  private PreparedStatement insertChallengeStatement;
  private PreparedStatement updateChallengeStatement;
  private PreparedStatement deleteChallengeStatement;
  private PreparedStatement selectChallengeTotalStatement;
  private PreparedStatement allowChallengeDeleteStatement;
  
  private PreparedStatement selectAllChallengesStatement;
  private PreparedStatement selectAllChoicesStatement;
  private PreparedStatement selectAllChoiceOptionsStatement;
  private PreparedStatement allowChoiceOptionDeleteStatement;
  
  public BidData(DonationDataAccess manager)
  {
    super(manager);
  }

  @Override
  void rebuildPreparedStatements()
  {
    try
    {
      this.allowChallengeDeleteStatement = this.getConnection().prepareStatement("SELECT COUNT(*) FROM ChallengeBid WHERE ChallengeBid.challengeId = ?;");
      this.allowChoiceOptionDeleteStatement = this.getConnection().prepareStatement("SELECT COUNT(*) FROM ChoiceBid WHERE ChoiceBid.optionId = ?;");
      
      this.selectAllChallengesStatement = this.getConnection().prepareStatement("SELECT * FROM Challenge;");
      this.selectAllChoicesStatement = this.getConnection().prepareStatement("SELECT * FROM Choice;");
      this.selectAllChoiceOptionsStatement = this.getConnection().prepareStatement("SELECT * FROM ChoiceOption;");
      
      this.selectChoiceByIdStatement = this.getConnection().prepareStatement("SELECT * FROM Choice WHERE Choice.choiceId = ?;");
      this.selectChoicesBySpeedRunId = this.getConnection().prepareStatement("SELECT * FROM Choice WHERE Choice.speedRunId = ?;");
      
      this.updateChoiceStatement = this.getConnection().prepareStatement("UPDATE Choice SET speedRunId = ?, name = ?, description = ?, bidState = ? WHERE Choice.choiceId = ?;");

      this.insertChoiceStatement = this.getConnection().prepareStatement("INSERT INTO Choice (choiceId, speedRunId, name, description, bidState) VALUES (?,?,?,?,?);");
    
      this.deleteChoiceStatement = this.getConnection().prepareStatement("DELETE FROM Choice WHERE Choice.choiceId = ?;");
      
      this.selectChoiceOptionByIdStatement = this.getConnection().prepareStatement("SELECT * FROM ChoiceOption WHERE ChoiceOption.optionId = ?;");
      this.selectChoiceOptionsByChoiceId = this.getConnection().prepareStatement("SELECT * FROM ChoiceOption WHERE ChoiceOption.choiceId = ?;");
      
      this.updateChoiceOptionStatement = this.getConnection().prepareStatement("UPDATE ChoiceOption SET choiceId = ?, Name = ? WHERE ChoiceOption.optionId = ?;");
      
      this.insertChoiceOptionStatement = this.getConnection().prepareStatement("INSERT INTO ChoiceOption (optionId, choiceId, name) VALUES (?,?,?);");
    
      this.deleteChoiceOptionStatement = this.getConnection().prepareStatement("DELETE FROM ChoiceOption WHERE ChoiceOption.optionId = ?;");
      
      this.selectChoiceOptionTotalStatement = this.getConnection().prepareStatement("SELECT SUM(amount) FROM ChoiceBid WHERE ChoiceBid.optionId = ?");
      
      this.selectChallengeByIdStatement = this.getConnection().prepareStatement("SELECT * FROM Challenge WHERE Challenge.challengeId = ?;");
      this.selectChallengesBySpeedRunId = this.getConnection().prepareStatement("SELECT * FROM Challenge WHERE Challenge.speedRunId = ?;");
      this.selectChallengeTotalStatement = this.getConnection().prepareStatement("SELECT SUM(amount) FROM ChallengeBid WHERE ChallengeBid.challengeId = ?;");
      
      this.updateChallengeStatement = this.getConnection().prepareStatement("UPDATE Challenge SET speedRunId = ?, name = ?, goalAmount = ?, description = ?, bidState = ? WHERE Challenge.challengeId = ?;");
      
      this.insertChallengeStatement = this.getConnection().prepareStatement("INSERT INTO Challenge (challengeId, speedRunId, name, goalAmount, description, bidState) VALUES (?,?,?,?,?,?);");
    
      this.deleteChallengeStatement = this.getConnection().prepareStatement("DELETE FROM Challenge WHERE Challenge.challengeId = ?;");
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  public synchronized List<Bid> getAllBids()
  {
    List<Bid> list = new ArrayList<Bid>();
    
    try
    {
      ResultSet challenges = this.selectAllChallengesStatement.executeQuery();
      ResultSet choices = this.selectAllChoicesStatement.executeQuery();
      
      while (challenges.next())
      {
        list.add(extractChallenge(challenges));
      }
      
      while (choices.next())
      {
        list.add(extractChoice(choices));
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return list;
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
      this.insertChoiceStatement.setString(4, choice.getDescription());
      this.insertChoiceStatement.setString(5, choice.getBidState().toString());
      
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
        result.getString("description"),
        BidState.valueOf(result.getString("bidState")),
        result.getInt("speedRunId"));
  }
  
  public synchronized void updateChoice(Choice choice)
  {
    try
    {
      this.updateChoiceStatement.setInt(5, choice.getId());
      this.updateChoiceStatement.setInt(1, choice.getSpeedRunId());
      this.updateChoiceStatement.setString(2, choice.getName());
      this.updateChoiceStatement.setString(3, choice.getDescription());
      this.updateChoiceStatement.setString(4, choice.getBidState().toString());
      
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
      this.allowChoiceOptionDeleteStatement.setInt(1, optionId);
      
      ResultSet results = this.allowChoiceOptionDeleteStatement.executeQuery();
      
      if (results.next())
      {
        int links = results.getInt(1);
        
        if (links > 0)
        {
          throw new SQLException("Error, violated constraint : '" + DonationDataConstraint.ChoiceBidFKOption.toString() + "'", "", JDBCManager.getCodeForError(this.getManager().getConnectionType(), SQLError.FOREIGN_KEY_VIOLATION));
        }
      }
      
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
      this.insertChallengeStatement.setString(5, challenge.getDescription());
      this.insertChallengeStatement.setString(6, challenge.getBidState().toString());
      
      int updated = this.insertChallengeStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Could not insert challenge.");
      }
    }
    catch (SQLException e)
    {      this.getManager().handleSQLException(e);
    }
  }
  
  private static Challenge extractChallenge(ResultSet result) throws SQLException
  {
    return new Challenge(
        result.getInt("challengeId"),
        result.getString("name"),
        result.getBigDecimal("goalAmount"),
        result.getString("description"),
        BidState.valueOf(result.getString("bidState")),
        result.getInt("speedRunId"));
  }

  public synchronized void updateChallenge(Challenge challenge)
  {
    try
    {
      this.updateChallengeStatement.setInt(6, challenge.getId());
      this.updateChallengeStatement.setInt(1, challenge.getSpeedRunId());
      this.updateChallengeStatement.setString(2, challenge.getName());
      this.updateChallengeStatement.setBigDecimal(3, challenge.getGoalAmount());
      this.updateChallengeStatement.setString(4, challenge.getDescription());
      this.updateChallengeStatement.setString(5, challenge.getBidState().toString());
      
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
  
  public synchronized BigDecimal getChoiceOptionTotal(int optionId)
  {
    BigDecimal result = null;
    
    try
    {
      this.selectChoiceOptionTotalStatement.setInt(1, optionId);
      
      ResultSet results = this.selectChoiceOptionTotalStatement.executeQuery();
      
      if (results.next())
      {
        result = results.getBigDecimal(1);
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    if (result == null)
    {
      result = BigDecimal.ZERO.setScale(2);
    }
    
    result = result.setScale(2);
    
    return result;
  }
  
  public synchronized BigDecimal getChallengeTotal(int challengeId)
  {
    BigDecimal result = null;
    
    try
    {
      this.selectChallengeTotalStatement.setInt(1, challengeId);
      
      ResultSet results = this.selectChallengeTotalStatement.executeQuery();
      
      if (results.next())
      {
        result = results.getBigDecimal(1);
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    if (result == null)
    {
      result = BigDecimal.ZERO.setScale(2);
    }
    
    result = result.setScale(2);
    
    return result;
  }

  public synchronized void deleteChallenge(int challengeId)
  {
    try
    {
      this.allowChallengeDeleteStatement.setInt(1, challengeId);
      
      ResultSet results = this.allowChallengeDeleteStatement.executeQuery();
      
      if (results.next())
      {
        int links = results.getInt(1);
        
        if (links > 0)
        {
          throw new SQLException("Error, violated constraint : '" + DonationDataConstraint.ChallengeBidFKChallenge.toString() + "'", "", JDBCManager.getCodeForError(this.getManager().getConnectionType(), SQLError.FOREIGN_KEY_VIOLATION));
        }
      }
      
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

  public List<ChoiceOption> getAllChoiceOptions()
  {
    List<ChoiceOption> list = new ArrayList<ChoiceOption>();
    
    try
    {
      ResultSet results = this.selectAllChoiceOptionsStatement.executeQuery();
      
      while (results.next())
      {
        list.add(extractChoiceOption(results));
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return list;
  }
}
