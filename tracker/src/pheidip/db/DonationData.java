package pheidip.db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import pheidip.objects.Donation;
import pheidip.objects.DonationAnnounceState;
import pheidip.objects.DonationBidState;
import pheidip.objects.DonationDomain;
import pheidip.objects.DonationPaymentState;

public class DonationData
{
  private ConnectionManager manager;
  private Connection connection;
  
  private PreparedStatement selectDonationById;
  private PreparedStatement selectDonationByDomainId;
  private PreparedStatement selectDonorDonations;
  private PreparedStatement selectDonorDonationTotal;
  private PreparedStatement updateDonationComment;
  private PreparedStatement updateDonationPaymentState;
  private PreparedStatement updateDonationAnnounceState;
  private PreparedStatement updateDonationBidState;
  private PreparedStatement deleteDonationStatement;
  private PreparedStatement insertDonationStatement;

  public DonationData(ConnectionManager manager)
  {
    if (!manager.isConnected())
    {
      throw new RuntimeException("Error, manager is not connected -> later, this may not be an error.");
    }
    
    this.manager = manager;
    this.connection = manager.getConnection();
    
    this.rebuildPreparedStatements();
  }

  private void rebuildPreparedStatements()
  {
    try
    {
      this.selectDonationById = this.connection.prepareStatement("SELECT * FROM Donation WHERE Donation.donationId = ?;");
      this.selectDonorDonations = this.connection.prepareStatement("SELECT * FROM Donation WHERE Donation.donorId = ?;");
      this.selectDonationByDomainId = this.connection.prepareStatement("SELECT * FROM Donation WHERE Donation.domain = ? AND Donation.domainId = ?;");
      this.selectDonorDonationTotal = this.connection.prepareStatement("SELECT SUM(Donation.amount) FROM Donation WHERE Donation.donorId = ?;");
    
      this.updateDonationComment = this.connection.prepareStatement("UPDATE Donation SET comment = ? WHERE Donation.donationId = ?;");
      this.updateDonationPaymentState = this.connection.prepareStatement("UPDATE Donation SET paymentState = ? WHERE Donation.donationId = ?;");
      this.updateDonationAnnounceState = this.connection.prepareStatement("UPDATE Donation SET announceState = ? WHERE Donation.donationId = ?;");
      this.updateDonationBidState = this.connection.prepareStatement("UPDATE Donation SET bidState = ? WHERE Donation.donationId = ?;");
      
      this.deleteDonationStatement = this.connection.prepareStatement("DELETE FROM Donation WHERE Donation.donationId = ?;");
    
      this.insertDonationStatement = this.connection.prepareStatement("INSERT INTO Donation (donationId, donorId, domain, domainId, paymentState, announceState, bidState, amount, timeReceived, comment) VALUES (?,?,?,?,?,?,?,?,?,?);");
    }
    catch (SQLException e)
    {
      this.manager.handleSQLException(e);
    }
  }
  
  public Donation getDonationById(int id)
  {
    Donation result = null;
    
    try
    {
      this.selectDonationById.setInt(1, id);
      
      ResultSet rows = this.selectDonationById.executeQuery();
      
      if (rows.next())
      {
        result = DonationData.extractDonation(rows);
      }
    }
    catch (SQLException e)
    {
      this.manager.handleSQLException(e);
    }
    
    return result;
  }
  
  public List<Donation> getDonorDonations(int donorId)
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
      this.manager.handleSQLException(e);
    }
    
    return result;
  }
  
  private static List<Donation> extractDonationList(ResultSet rows) throws SQLException
  {
    List<Donation> result = new ArrayList<Donation>();
    
    while (rows.next())
    {
      result.add(DonationData.extractDonation(rows));
    }
    
    return result;
  }

  public BigDecimal getDonorDonationTotal(int id)
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
    }
    catch (SQLException e)
    {
      this.manager.handleSQLException(e);
    }
    
    return result;
  }
  
  private static Donation extractDonation(ResultSet row) throws SQLException
  {
    return new Donation(
        row.getInt("donationId"),
        DonationDomain.valueOf(row.getString("domain")),
        row.getString("domainId"),
        DonationPaymentState.valueOf(row.getString("paymentState")),
        DonationAnnounceState.valueOf(row.getString("announceState")), 
        DonationBidState.valueOf(row.getString("bidState")), 
        row.getBigDecimal("amount"), 
        new java.util.Date( row.getTimestamp("timeReceived").getTime()),
        row.getInt("donorId"),
        row.getString("comment")
        );
  }

  public void setDonationComment(int id, String comment)
  {
    this.runStringFieldUpdate(this.updateDonationComment, id, comment);
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
        throw new RuntimeException("Error, update of donation failed.");
      }
    }
    catch (SQLException e)
    {
      this.manager.handleSQLException(e);
    }
  }

  public void deleteDonation(int id)
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
      this.manager.handleSQLException(e);
    }
  }

  public void setDonationPaymentState(int id, DonationPaymentState paymentState)
  {
    this.runStringFieldUpdate(this.updateDonationPaymentState, id, paymentState.toString());
  }

  public void setDonationAnnounceState(int id, DonationAnnounceState announceState)
  {
    this.runStringFieldUpdate(this.updateDonationAnnounceState, id, announceState.toString());
  }

  public void setDonationBidState(int id, DonationBidState bidState)
  {
    this.runStringFieldUpdate(this.updateDonationBidState, id, bidState.toString());
  }

  public void insertDonation(Donation d)
  {
    try
    {
      this.insertDonationStatement.setInt(1, d.getId());
      this.insertDonationStatement.setInt(2, d.getDonorId());
      this.insertDonationStatement.setString(3, d.getDomain().toString());
      this.insertDonationStatement.setString(4, d.getDomainId());
      this.insertDonationStatement.setString(5, d.getPaymentState().toString());
      this.insertDonationStatement.setString(6, d.getAnnounceState().toString());
      this.insertDonationStatement.setString(7, d.getBidState().toString());
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
      this.manager.handleSQLException(e);
    }
  }

  public Donation getDonationByDomainId(DonationDomain domain, String domainId)
  {
    Donation result = null;
    
    try
    {
      this.selectDonationByDomainId.setString(1, domain.toString());
      this.selectDonationByDomainId.setString(2, domainId);
    
      ResultSet rows = this.selectDonationByDomainId.executeQuery();
      
      if (rows.next())
      {
        result = DonationData.extractDonation(rows);
      }
    }
    catch (SQLException e)
    {
      this.manager.handleSQLException(e);
    }
    
    return result;
  }
}
