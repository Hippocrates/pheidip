package pheidip.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pheidip.objects.SpeedRun;

public class SpeedRunData extends DataInterface
{
  private PreparedStatement selectSpeedRunByIdStatement;
  private PreparedStatement insertSpeedRunStatement;
  private PreparedStatement deleteSpeedRunStatement;
  private PreparedStatement updateSpeedRunStatement;
  private PreparedStatement selectAllSpeedRunsStatement;
  
  public SpeedRunData(DonationDataAccess manager)
  {
    super(manager);
  }

  @Override
  void rebuildPreparedStatements()
  {
    try
    {
      this.selectSpeedRunByIdStatement = this.getConnection().prepareStatement("SELECT * FROM SpeedRun WHERE SpeedRun.speedRunId = ?;");
      this.insertSpeedRunStatement = this.getConnection().prepareStatement("INSERT INTO SpeedRun (speedRunId, name) VALUES (?,?);");
      this.deleteSpeedRunStatement = this.getConnection().prepareStatement("DELETE FROM SpeedRun WHERE SpeedRun.speedRunId = ?;");
      this.updateSpeedRunStatement = this.getConnection().prepareStatement("UPDATE SpeedRun SET name = ? WHERE SpeedRun.speedRunId = ?;");
      this.selectAllSpeedRunsStatement = this.getConnection().prepareStatement("SELECT * FROM SpeedRun;");
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }

  public synchronized SpeedRun getSpeedRunById(int runId)
  {
    SpeedRun result = null;
    
    try
    {
      this.selectSpeedRunByIdStatement.setInt(1, runId);
      
      ResultSet results = this.selectSpeedRunByIdStatement.executeQuery();
      
      if (results.next())
      {
        result = extractSpeedRun(results);
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }
  
  private static List<SpeedRun> extractSpeedRuns(ResultSet results) throws SQLException
  {
    List<SpeedRun> all = new ArrayList<SpeedRun>();
    
    while (results.next())
    {
      all.add(extractSpeedRun(results));
    }
    
    return all;
  }
  
  private static SpeedRun extractSpeedRun(ResultSet results) throws SQLException
  {
    return new SpeedRun(
        results.getInt("speedRunId"),
        results.getString("name"));
  }

  public synchronized void insertSpeedRun(SpeedRun speedRun)
  {
    try
    {
      this.insertSpeedRunStatement.setInt(1, speedRun.getId());
      this.insertSpeedRunStatement.setString(2, speedRun.getName());
      
      int updated = this.insertSpeedRunStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Error, could not insert speedrun.");
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }

  public synchronized void deleteSpeedRun(int runId)
  {
    try
    {
      this.deleteSpeedRunStatement.setInt(1, runId);
      
      int updated = this.deleteSpeedRunStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Error, could not delete speedrun.");
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }

  public synchronized void updateSpeedRun(SpeedRun run)
  {
    try
    {
      this.updateSpeedRunStatement.setInt(2, run.getId());
      this.updateSpeedRunStatement.setString(1, run.getName());
      
      int updated = this.updateSpeedRunStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Error, could not update speedrun name.");
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }

  public List<SpeedRun> getAllSpeedRuns()
  {
    List<SpeedRun> all = null;
    
    try
    {
      ResultSet results = this.selectAllSpeedRunsStatement.executeQuery();
      
      all = extractSpeedRuns(results);
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return all;
  }
  
}
