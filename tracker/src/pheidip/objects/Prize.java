package pheidip.objects;

import java.math.BigDecimal;

import pheidip.util.StringUtils;

public class Prize extends Entity
{
  private String name = "Prize#" + this.getId();
  private String imageURL = null;
  private String description = "";
  private int sortKey = this.getId();
  private Donor winner = null;
  private BigDecimal mimimumBid = new BigDecimal("5.00");
  private SpeedRun startGame = null;
  private SpeedRun endGame = null;
  
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

  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    String oldName = this.name;
    this.name = StringUtils.isEmptyOrNull(name) ? "Prize#" + this.getId() : name.toLowerCase();
    this.firePropertyChange("name", oldName, this.name);
  }

  public String getImageURL()
  {
    return this.imageURL;
  }

  public void setImageURL(String imageURL)
  {
    String oldImageURL = this.imageURL;
    this.imageURL = StringUtils.nullIfEmpty(imageURL);
    this.firePropertyChange("imageURL", oldImageURL, this.imageURL);
  }

  public String getDescription()
  {
    return this.description;
  }
  
  public void setDescription(String description)
  {
    String oldDescription = this.description;
    this.description = StringUtils.emptyIfNull(description);
    this.firePropertyChange("description", oldDescription, this.description);
  }

  public Donor getWinner()
  {
    return winner;
  }

  public void setWinner(Donor winner)
  {
    Donor oldWinner = this.winner;
    this.winner = winner;
    this.firePropertyChange("winner", oldWinner, this.winner);
  }

  public int getSortKey()
  {
    return sortKey;
  }

  public void setSortKey(int sortKey)
  {
    int oldSortKey = this.sortKey;
    this.sortKey = sortKey;
    this.firePropertyChange("sortKey", oldSortKey, this.sortKey);
  }

  public BigDecimal getMinimumBid()
  {
    return mimimumBid;
  }
  
  public void setMinimumBid(BigDecimal minimumBid)
  {
    if (minimumBid == null || minimumBid.compareTo(BigDecimal.ZERO) < 0)
    {
      throw new RuntimeException("Error, invalid minimum bid amount.");
    }
    
    BigDecimal oldMinimumBid = this.mimimumBid;
    this.mimimumBid = minimumBid;
    this.firePropertyChange("minimumBid", oldMinimumBid, this.mimimumBid);
  }

  public SpeedRun getStartGame()
  {
    return startGame;
  }

  public void setStartGame(SpeedRun startGame)
  {
    SpeedRun oldStartGame = this.startGame;
    this.startGame = startGame;
    this.firePropertyChange("startGame", oldStartGame, this.startGame);
  }

  public SpeedRun getEndGame()
  {
    return endGame;
  }

  public void setEndGame(SpeedRun endGame)
  {
    SpeedRun oldEndGame = this.endGame;
    this.endGame = endGame;
    this.firePropertyChange("endGame", oldEndGame, this.endGame);
  }
  
  public String toString()
  {
    return this.getName();
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
}
