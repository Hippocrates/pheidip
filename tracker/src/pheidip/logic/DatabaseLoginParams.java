package pheidip.logic;

import lombok.Data;

@Data
public class DatabaseLoginParams
{
  private String serverAddress;
  private String databaseName;
  private String userName;
  private String password;
}
