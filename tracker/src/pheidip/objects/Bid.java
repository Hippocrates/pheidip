package pheidip.objects;

public interface Bid
{
  public BidType getType();
  
  public String getName();
  public void setName(String name);
  public String getDescription();
  public void setDescription(String description);
  public BidState getBidState();
  public void setBidState(BidState bidState);
  public Integer getSpeedRunId();
  public void setSpeedRunId(int speedRunId);
  public SpeedRun getSpeedRun();
  public void setSpeedRun(SpeedRun run);
  public int getId();
  public void setId(int id);
}
