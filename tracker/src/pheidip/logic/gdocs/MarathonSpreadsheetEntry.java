package pheidip.logic.gdocs;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MarathonSpreadsheetEntry
{
  private Date startTime;
  private String gameName;
  private List<String> runners;
  private Date estimatedFinish;
  private List<String> commentators;
  private String comments;
  private List<String> prizes;
  
  public MarathonSpreadsheetEntry(Date startTime, String gameName, List<String> runners, Date estimatedFinish, List<String> commentators, String comments, List<String> prizes)
  {
    this.startTime = startTime;
    this.gameName = gameName;
    this.runners = Collections.unmodifiableList(runners);
    this.estimatedFinish = estimatedFinish;
    this.commentators = Collections.unmodifiableList(commentators);
    this.comments = comments;
    this.prizes = Collections.unmodifiableList(prizes);
  }
  
  public Date getStartTime()
  {
    return this.startTime;
  }
  
  public String getGameName()
  {
    return this.gameName;
  }
  
  public List<String> getRunners()
  {
    return this.runners;
  }
  
  public Date getEstimatedFinish()
  {
    return this.estimatedFinish;
  }
  
  public List<String> getCommentators()
  {
    return this.commentators;
  }
  
  public String getComments()
  {
    return this.comments;
  }
  
  public List<String> getPrizes()
  {
    return this.prizes;
  }
}
