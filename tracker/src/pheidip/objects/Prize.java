package pheidip.objects;

import java.math.BigDecimal;

import pheidip.util.IdUtils;
import pheidip.util.StringUtils;

public class Prize
{
  private int id = IdUtils.generateId();
  private String name;
  private String imageURL;
  private String description;
  private int sortKey;
  private Donor winner;
  private BigDecimal mimimumBid = new BigDecimal("5.00");
  private SpeedRun startGame;
  private SpeedRun endGame;
  
  public Prize()
  {
  }
  
  public Prize(int id, String name, String imageURL, String description, int sortKey, BigDecimal mimimumBid, Donor winner, SpeedRun startGame, SpeedRun endGame)
  {
    this.setId(id);
    this.setName(name);
    this.setImageURL(imageURL);
    this.setDescription(description);
    this.setWinner(winner);
    this.setSortKey(sortKey);
    this.setMinimumBid(mimimumBid);
    this.setStartGame(startGame);
    this.setEndGame(endGame);
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public void setId(int id)
  {
    this.id = id;
  }

  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = StringUtils.isEmptyOrNull(name) ? "#" + this.getId() : name.toLowerCase();
  }

  public String getImageURL()
  {
    return this.imageURL;
  }

  public void setImageURL(String imageURL)
  {
    this.imageURL = StringUtils.nullIfEmpty(imageURL);
  }

  public String getDescription()
  {
    return this.description;
  }
  
  public void setDescription(String description)
  {
    this.description = StringUtils.emptyIfNull(description);
  }

  public String toString()
  {
    return this.getName() == null ? "Prize#" + this.getId() : this.getName();
  }
  
  public int hashCode()
  {
    return this.getId();
  }
  
  public boolean equals(Object other)
  {
    if (other instanceof Prize)
    {
      return this.getId() == ((Prize)other).getId();
    }
    else
    {
      return false;
    }
  }

  public void setWinner(Donor winner)
  {
    this.winner = winner;
  }

  public Donor getWinner()
  {
    return winner;
  }

  public void setSortKey(int sortKey)
  {
    this.sortKey = sortKey;
  }

  public int getSortKey()
  {
    return sortKey;
  }

  public void setMinimumBid(BigDecimal minimumBid)
  {
    if (minimumBid == null || minimumBid.compareTo(BigDecimal.ZERO) <= 0)
    {
      throw new RuntimeException("Error, invalid minimum bid amount.");
    }
    
    this.mimimumBid = minimumBid;
  }

  public BigDecimal getMinimumBid()
  {
    return mimimumBid;
  }

  public void setStartGame(SpeedRun startGame)
  {
    this.startGame = startGame;
  }

  public SpeedRun getStartGame()
  {
    return startGame;
  }

  public void setEndGame(SpeedRun endGame)
  {
    this.endGame = endGame;
  }

  public SpeedRun getEndGame()
  {
    return endGame;
  }
}
