package pheidip.db;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

public class DBConnectionParams
{
  @Getter @Setter @NotNull
  private DBType databaseType;
  
  @Getter @Setter @NotNull
  private String userName;
  
  @Getter @Setter @NotNull
  private String password;
  
  @Getter @Setter @NotNull
  private String databaseServer;
  
  @Getter @Setter @NotNull
  private String databaseName;
}
