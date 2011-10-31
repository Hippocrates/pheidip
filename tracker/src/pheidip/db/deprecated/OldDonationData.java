package pheidip.db.deprecated;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pheidip.db.DonationData;
import pheidip.objects.ChallengeBid;
import pheidip.objects.ChoiceBid;
import pheidip.objects.Donation;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationReadState;
import pheidip.objects.DonationCommentState;

public class OldDonationData extends OldDataInterface implements DonationData
{
  private PreparedStatement selectAllDonations;
  private PreparedStatement selectDonationById;
  private PreparedStatement selectDonationByDomainId;
  private PreparedStatement selectDonorDonations;
  private PreparedStatement selectDonorDonationTotal;
  private PreparedStatement updateDonationComment;
  private PreparedStatement updateDonationBidState;
  private PreparedStatement updateDonationReadState;
  private PreparedStatement deleteDonationStatement;
  private PreparedStatement insertDonationStatement;
  private PreparedStatement updateDonationStatement;
  private PreparedStatement updateDonationAmountStatement;
  private PreparedStatement selectDonationChallengeBidsStatement;
  private PreparedStatement selectDonationChoiceBidsStatement;
  private PreparedStatement insertDonationChallengeBidStatement;
  private PreparedStatement insertDonationChoiceBidStatement;
  private PreparedStatement updateDonationChallengeBidStatement;
  private PreparedStatement updateDonationChoiceBidStatement;
  private PreparedStatement deleteDonationChallengeBidStatement;
  private PreparedStatement deleteDonationChoiceBidStatement;
  private PreparedStatement selectDonationsWithBidPendingStatement;
  private PreparedStatement selectDonationsWithReadPendingStatement;
  private PreparedStatement selectDonationsInTimeRange;

  public OldDonationData(OldDonationDataAccess manager)
  {
    super(manager);
  }

  protected void rebuildPreparedStatements()
  {
    try
    {
      this.selectDonationsInTimeRange = this.getConnection().prepareStatement("SELECT * FROM Donation WHERE (? IS NULL OR ? <= Donation.timeReceived) AND (? IS NULL OR ? >= Donation.timeReceived);");
      
      this.selectAllDonations = this.getConnection().prepareStatement("SELECT * FROM Donation");
      this.updateDonationReadState = this.getConnection().prepareStatement("UPDATE Donation SET readState = ? WHERE Donation.donationId = ?;");
      this.selectDonationsWithReadPendingStatement = this.getConnection().prepareStatement("SELECT * FROM Donation WHERE (Donation.readState = 'PENDING' OR Donation.readState = 'FLAGGED') OR (Donation.readState = 'AMOUNT_READ' AND Donation.comment IS NOT NULL) ORDER BY Donation.timeReceived ASC;");
      this.selectDonationsWithBidPendingStatement = this.getConnection().prepareStatement("SELECT * FROM Donation WHERE (Donation.bidState = 'PENDING' OR Donation.bidState = 'FLAGGED') AND Donation.comment IS NOT NULL ORDER BY Donation.timeReceived ASC;");
      
      this.updateDonationChallengeBidStatement = this.getConnection().prepareStatement("UPDATE ChallengeBid SET amount = ? WHERE ChallengeBid.challengeBidId = ?;");
      this.updateDonationChoiceBidStatement = this.getConnection().prepareStatement("UPDATE ChoiceBid SET amount = ? WHERE ChoiceBid.choiceBidId = ?;");
      this.deleteDonationChallengeBidStatement = this.getConnection().prepareStatement("DELETE FROM ChallengeBid WHERE ChallengeBid.challengeBidId = ?;");
      this.deleteDonationChoiceBidStatement = this.getConnection().prepareStatement("DELETE FROM ChoiceBid WHERE ChoiceBid.choiceBidId = ?;");
      
      this.insertDonationChallengeBidStatement = this.getConnection().prepareStatement("INSERT INTO ChallengeBid (challengeBidId, amount, challengeId, donationId) VALUES (?,?,?,?);");
      this.insertDonationChoiceBidStatement = this.getConnection().prepareStatement("INSERT INTO ChoiceBid (choiceBidId, amount, optionId, donationId) VALUES (?,?,?,?);");
      
      this.selectDonationChallengeBidsStatement = this.getConnection().prepareStatement("SELECT * FROM ChallengeBid WHERE ChallengeBid.donationId = ?;");
      this.selectDonationChoiceBidsStatement = this.getConnection().prepareStatement("SELECT * FROM ChoiceBid WHERE ChoiceBid.donationId = ?;");
      
      this.selectDonationById = this.getConnection().prepareStatement("SELECT * FROM Donation WHERE Donation.donationId = ?;");
      this.selectDonorDonations = this.getConnection().prepareStatement("SELECT * FROM Donation WHERE Donation.donorId = ?;");
      this.selectDonationByDomainId = this.getConnection().prepareStatement("SELECT * FROM Donation WHERE Donation.domain = ? AND Donation.domainId = ?;");
      this.selectDonorDonationTotal = this.getConnection().prepareStatement("SELECT SUM(Donation.amount) FROM Donation WHERE Donation.donorId = ?;");

      this.updateDonationComment = this.getConnection().prepareStatement("UPDATE Donation SET comment = ? WHERE Donation.donationId = ?;");
      this.updateDonationBidState = this.getConnection().prepareStatement("UPDATE Donation SET bidState = ? WHERE Donation.donationId = ?;");
      this.updateDonationStatement = this.getConnection().prepareStatement("UPDATE Donation SET donorId = ?, domain = ?, domainId = ?, bidState = ?, readState = ?, commentState = ?, amount = ?, timeReceived = ?, comment = ? WHERE donationId = ?;");
      this.updateDonationAmountStatement = this.getConnection().prepareStatement("UPDATE Donation SET amount = ? WHERE Donation.donationId = ?;");
      
      this.deleteDonationStatement = this.getConnection().prepareStatement("DELETE FROM Donation WHERE Donation.donationId = ?;");
    
      this.insertDonationStatement = this.getConnection().prepareStatement("INSERT INTO Donation (donationId, donorId, domain, domainId, bidState, readState, commentState, amount, timeReceived, comment) VALUES (?,?,?,?,?,?,?,?,?,?);");
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationData#getDonationById(int)
 */
@Override
synchronized public Donation getDonationById(int id)
  {
    Donation result = null;
    
    try
    {
      this.selectDonationById.setInt(1, id);
      
      ResultSet rows = this.selectDonationById.executeQuery();
      
      if (rows.next())
      {
        result = OldDonationData.extractDonation(rows);
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationData#getDonorDonations(int)
 */
@Override
synchronized public List<Donation> getDonorDonations(int donorId)
  {
    List<Donation> result = null;
    
    try
    {
      this.selectDonorDonations.setInt(1, donorId);
      
      ResultSet rows = this.selectDonorDonations.executeQuery();
      
      result = extractDonationList(rows);
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }
  
  private static List<Donation> extractDonationList(ResultSet rows) throws SQLException
  {
    List<Donation> result = new ArrayList<Donation>();
    
    while (rows.next())
    {
      result.add(OldDonationData.extractDonation(rows));
    }
    
    return result;
  }

  /* (non-Javadoc)
 * @see pheidip.db.DonationData#getDonorDonationTotal(int)
 */
@Override
synchronized public BigDecimal getDonorDonationTotal(int id)
  {
    BigDecimal result = null;
    
    try
    {
      this.selectDonorDonationTotal.setInt(1, id);
      
      ResultSet sum = this.selectDonorDonationTotal.executeQuery();
      
      if (sum.next())
      {
        result = sum.getBigDecimal(1);
      }
      
      if (result == null)
      {
        result = BigDecimal.ZERO;
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }
  
  private static Donation extractDonation(ResultSet row) throws SQLException
  {
    return new Donation(
        row.getInt("donationId"),
        DonationDomain.valueOf(row.getString("domain")),
        row.getString("domainId"),
        DonationBidState.valueOf(row.getString("bidState")), 
        DonationReadState.valueOf(row.getString("readState")),
        DonationCommentState.valueOf(row.getString("commentState")),
        row.getBigDecimal("amount"), 
        new java.util.Date( row.getTimestamp("timeReceived").getTime()),
        row.getInt("donorId"),
        row.getString("comment")
        );
  }
  
  private static ChallengeBid extractChallengeBid(ResultSet row) throws SQLException
  {
    return new ChallengeBid(
        row.getInt("challengeBidId"),
        row.getBigDecimal("amount"),
        row.getInt("challengeId"),
        row.getInt("donationId"));
  }
  
  private static List<ChallengeBid> extractChallengeBidList(ResultSet row) throws SQLException
  {
    List<ChallengeBid> list = new ArrayList<ChallengeBid>();
    
    while (row.next())
    {
      list.add(extractChallengeBid(row));
    }
    
    return list;
  }
  
  private static ChoiceBid extractChoiceBid(ResultSet row) throws SQLException
  {
    return new ChoiceBid(
        row.getInt("choiceBidId"),
        row.getBigDecimal("amount"),
        row.getInt("optionId"),
        row.getInt("donationId"));
  }
  
  private static List<ChoiceBid> extractChoiceBidList(ResultSet row) throws SQLException
  {
    List<ChoiceBid> list = new ArrayList<ChoiceBid>();
    
    while (row.next())
    {
      list.add(extractChoiceBid(row));
    }
    
    return list;
  }

  /* (non-Javadoc)
 * @see pheidip.db.DonationData#setDonationComment(int, java.lang.String)
 */
@Override
synchronized public void setDonationComment(int id, String comment)
  {
    this.runStringFieldUpdate(this.updateDonationComment, id, comment);
  }
  
  // todo: add a string parameter representing the name of the column being updated
  private void runStringFieldUpdate(PreparedStatement sql, int id, String value)
  {
    try
    {
      sql.setInt(2, id);
      sql.setString(1, value);
      
      int created = sql.executeUpdate();
      
      if (created != 1)
      {
        throw new RuntimeException("Error, update of donation failed.");
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }

  /* (non-Javadoc)
 * @see pheidip.db.DonationData#deleteDonation(int)
 */
@Override
synchronized public void deleteDonation(int id)
  {
    try
    {
      this.deleteDonationStatement.setInt(1, id);
      
      int updated = this.deleteDonationStatement.executeUpdate();
      
      if (updated != 1)
      {
         // I think this means not found, these row checks should probably be improved
        throw new RuntimeException("Error, could not delete donation with id = " + id + ".");
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationData#setDonationBidState(int, pheidip.objects.DonationBidState)
 */
@Override
synchronized public void setDonationBidState(int id, DonationBidState bidState)
  {
    this.runStringFieldUpdate(this.updateDonationBidState, id, bidState.toString());
  }

  /* (non-Javadoc)
 * @see pheidip.db.DonationData#setDonationAmount(int, java.math.BigDecimal)
 */
@Override
synchronized public void setDonationAmount(int donationId, BigDecimal amount)
  {
    try
    {
      Donation d = this.getDonationById(donationId);
      
      if (d.getDomain() != DonationDomain.LOCAL)
      {
        throw new RuntimeException("Error, trying to update the amount of a non-local donation.");
      }
      
      this.updateDonationAmountStatement.setBigDecimal(1, amount.setScale(2));
      this.updateDonationAmountStatement.setInt(2, donationId);
      
      int updated = this.updateDonationAmountStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Could not update donation amount.");
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }

  /* (non-Javadoc)
 * @see pheidip.db.DonationData#insertDonation(pheidip.objects.Donation)
 */
@Override
synchronized public void insertDonation(Donation d)
  {
    try
    {
      this.insertDonationStatement.setInt(1, d.getId());
      this.insertDonationStatement.setInt(2, d.getDonorId());
      this.insertDonationStatement.setString(3, d.getDomain().toString());
      this.insertDonationStatement.setString(4, d.getDomainId());
      this.insertDonationStatement.setString(5, d.getBidState().toString());
      this.insertDonationStatement.setString(6, d.getReadState().toString());
      this.insertDonationStatement.setString(7, d.getCommentState().toString());
      this.insertDonationStatement.setBigDecimal(8, d.getAmount());
      this.insertDonationStatement.setTimestamp(9, new Timestamp(d.getTimeReceived().getTime()));
      this.insertDonationStatement.setString(10, d.getComment());
      
      int updated = this.insertDonationStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Could not insert donation.");
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationData#updateDonation(pheidip.objects.Donation)
 */
@Override
synchronized public void updateDonation(Donation updated)
  {
    try
    {
      this.updateDonationStatement.setInt(1, updated.getDonorId());
      this.updateDonationStatement.setString(2, updated.getDomain().toString());
      this.updateDonationStatement.setString(3, updated.getDomainId());
      this.updateDonationStatement.setString(4, updated.getBidState().toString());
      this.updateDonationStatement.setString(5, updated.getReadState().toString());
      this.updateDonationStatement.setString(6, updated.getCommentState().toString());
      this.updateDonationStatement.setBigDecimal(7, updated.getAmount());
      this.updateDonationStatement.setTimestamp(8, new Timestamp(updated.getTimeReceived().getTime()));
      this.updateDonationStatement.setString(9, updated.getComment());
      this.updateDonationStatement.setInt(10, updated.getId());
      
      this.updateDonationStatement.execute();
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }

  /* (non-Javadoc)
 * @see pheidip.db.DonationData#getDonationByDomainId(pheidip.objects.DonationDomain, java.lang.String)
 */
@Override
synchronized public Donation getDonationByDomainId(DonationDomain domain, String domainId)
  {
    Donation result = null;
    
    try
    {
      this.selectDonationByDomainId.setString(1, domain.toString());
      this.selectDonationByDomainId.setString(2, domainId);
    
      ResultSet rows = this.selectDonationByDomainId.executeQuery();
      
      if (rows.next())
      {
        result = OldDonationData.extractDonation(rows);
      }
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationData#getChallengeBidsByDonationId(int)
 */
@Override
synchronized public List<ChallengeBid> getChallengeBidsByDonationId(int donationId)
  {
    List<ChallengeBid> result = null;
    
    try
    {
      this.selectDonationChallengeBidsStatement.setInt(1, donationId);
      
      ResultSet results = this.selectDonationChallengeBidsStatement.executeQuery();
      
      result = extractChallengeBidList(results);
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationData#getChoiceBidsByDonationId(int)
 */
@Override
public synchronized List<ChoiceBid> getChoiceBidsByDonationId(int donationId)
  {
    List<ChoiceBid> result = null;
    
    try
    {
      this.selectDonationChoiceBidsStatement.setInt(1, donationId);
      
      ResultSet results = this.selectDonationChoiceBidsStatement.executeQuery();
      
      result = extractChoiceBidList(results);
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return result;
  }

  /* (non-Javadoc)
 * @see pheidip.db.DonationData#attachChallengeBid(pheidip.objects.ChallengeBid)
 */
@Override
public synchronized void attachChallengeBid(ChallengeBid created)
  {
    try
    {
      this.insertDonationChallengeBidStatement.setInt(1,created.getId());
      this.insertDonationChallengeBidStatement.setBigDecimal(2,created.getAmount());
      this.insertDonationChallengeBidStatement.setInt(3,created.getChallengeId());
      this.insertDonationChallengeBidStatement.setInt(4,created.getDonationId());
      
      int updated = this.insertDonationChallengeBidStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Could not insert donation challenge bid.");
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationData#attachChoiceBid(pheidip.objects.ChoiceBid)
 */
@Override
public synchronized void attachChoiceBid(ChoiceBid created)
  {
    try
    {
      this.insertDonationChoiceBidStatement.setInt(1,created.getId());
      this.insertDonationChoiceBidStatement.setBigDecimal(2,created.getAmount());
      this.insertDonationChoiceBidStatement.setInt(3,created.getOptionId());
      this.insertDonationChoiceBidStatement.setInt(4,created.getDonationId());
      
      int updated = this.insertDonationChoiceBidStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Could not insert donation choice bid.");
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationData#updateChallengeBidAmount(int, java.math.BigDecimal)
 */
@Override
public synchronized void updateChallengeBidAmount(int challengeBidId, BigDecimal newAmount)
  {
    try
    {
      this.updateDonationChallengeBidStatement.setInt(2, challengeBidId);
      this.updateDonationChallengeBidStatement.setBigDecimal(1, newAmount);
      
      int updated = this.updateDonationChallengeBidStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Could not update donation challenge bid amount.");
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationData#updateChoiceBidAmount(int, java.math.BigDecimal)
 */
@Override
public synchronized void updateChoiceBidAmount(int choiceBidId, BigDecimal newAmount)
  {
    try
    {
      this.updateDonationChoiceBidStatement.setInt(2, choiceBidId);
      this.updateDonationChoiceBidStatement.setBigDecimal(1, newAmount);
      
      int updated = this.updateDonationChoiceBidStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Could not update donation choice bid amount.");
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationData#removeChallengeBid(int)
 */
@Override
public synchronized void removeChallengeBid(int challengeBidId)
  {
    try
    {
      this.deleteDonationChallengeBidStatement.setInt(1, challengeBidId);
      
      int updated = this.deleteDonationChallengeBidStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Could not delete donation challenge bid.");
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationData#removeChoiceBid(int)
 */
@Override
public synchronized void removeChoiceBid(int choiceBidId)
  {
    try
    {
      this.deleteDonationChoiceBidStatement.setInt(1, choiceBidId);
      
      int updated = this.deleteDonationChoiceBidStatement.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Could not delete donation choice bid.");
      }
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationData#getDonationsWithPendingBids()
 */
@Override
public synchronized List<Donation> getDonationsWithPendingBids()
  {
    List<Donation> list = null;
    
    try
    {
      ResultSet results = this.selectDonationsWithBidPendingStatement.executeQuery();
      
      list = extractDonationList(results);
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return list;
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationData#getDonationsToBeRead()
 */
@Override
public synchronized List<Donation> getDonationsToBeRead()
  {
    List<Donation> list = null;
    
    try
    {
      ResultSet results = this.selectDonationsWithReadPendingStatement.executeQuery();
      
      list = extractDonationList(results);
    }
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return list;
  }

  /* (non-Javadoc)
 * @see pheidip.db.DonationData#setDonationReadState(int, pheidip.objects.DonationReadState)
 */
@Override
public synchronized void setDonationReadState(int donationId, DonationReadState readState)
  {
    try
    {
      this.updateDonationReadState.setInt(2, donationId);
      this.updateDonationReadState.setString(1, readState.toString());
      
      int updated = this.updateDonationReadState.executeUpdate();
      
      if (updated != 1)
      {
        throw new RuntimeException("Error, could not update donation read state.");
      }
    } 
    catch (SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
  }
  
  /* (non-Javadoc)
 * @see pheidip.db.DonationData#getDonationsInTimeRange(java.util.Date, java.util.Date)
 */
@Override
public synchronized List<Donation> getDonationsInTimeRange(Date lo, Date hi)
  {
    List<Donation> list = null;
    
    try
    {
      Timestamp loTimestamp = lo == null ? null : new Timestamp(lo.getTime());
      Timestamp hiTimestamp = hi == null ? null : new Timestamp(hi.getTime());
      
      this.selectDonationsInTimeRange.setTimestamp(1, loTimestamp);
      this.selectDonationsInTimeRange.setTimestamp(2, loTimestamp);
      this.selectDonationsInTimeRange.setTimestamp(3, hiTimestamp);
      this.selectDonationsInTimeRange.setTimestamp(4, hiTimestamp);
      
      ResultSet results = this.selectDonationsInTimeRange.executeQuery();
      
      list = extractDonationList(results);
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return list;
  }

  /* (non-Javadoc)
 * @see pheidip.db.DonationData#getAllDonations()
 */
@Override
public synchronized List<Donation> getAllDonations()
  {
    List<Donation> list = null;
    
    try
    {
      ResultSet results = selectAllDonations.executeQuery();
      
      list = extractDonationList(results);
    }
    catch(SQLException e)
    {
      this.getManager().handleSQLException(e);
    }
    
    return list;
  }
}
