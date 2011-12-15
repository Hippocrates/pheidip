package pheidip.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLScriptRunner
{
  public static void runScript(Connection connection, Reader reader) throws IOException, SQLException
  {
    BufferedReader inputFile = new BufferedReader(reader);
    
    String inputLine = inputFile.readLine();
    StringBuilder sqlString = new StringBuilder();
    
    boolean inQuote = false;
    int bracketNesting = 0;
    int tickCount = 0;
    
    Statement statement = connection.createStatement();
    
    while (inputLine != null)
    {
      for (int i = 0; i < inputLine.length(); ++i)
      {
        final char currentChar = inputLine.charAt(i);
        
        if (!inQuote && currentChar == '-')
        {
          ++tickCount;
          
          if (tickCount == 2)
          {
            tickCount = 0;
            break;
          }
        }
        else
        {
          while (tickCount > 0)
          {
            sqlString.append('-');
            --tickCount;
          }
          
          switch (currentChar)
          {
          case '\'':
            inQuote = !inQuote;
            break;
          case '(':
            ++bracketNesting;
            break;
          case ')':
            --bracketNesting;
            break;
          }

          sqlString.append(currentChar);
          
          if (currentChar == ';' && !inQuote && bracketNesting == 0)
          {
            statement.executeUpdate(sqlString.toString());
            sqlString = new StringBuilder();
          }
        }
      }
      
      inputLine = inputFile.readLine();
    }
  }
}
