package pheidip.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ChipinFileDocumentSource implements ChipinDocumentSource
{
  
  private InputStream inputFile;
  private Runnable fileCloseDelegate;

  public ChipinFileDocumentSource(File targetFile)
  {
    try
    {
      this.inputFile = new FileInputStream(targetFile);
    } 
    catch (FileNotFoundException e)
    {
      throw new RuntimeException(e);
    }
    
    this.fileCloseDelegate = new Runnable()
    {
      public void run()
      {
        try
        {
          ChipinFileDocumentSource.this.inputFile.close();
        } 
        catch (IOException e)
        {
          // what else do you want me to do?
        }
      }
    };
  }
  
  public ChipinFileDocumentSource(InputStream inputFile, Runnable fileCloseDelegate)
  {
    this.inputFile = inputFile;
    this.fileCloseDelegate = fileCloseDelegate;
  }
  
  public Document provideDocument()
  {
    try
    {
      return Jsoup.parse(this.inputFile, null, "www.chipin.com");
    } 
    catch (IOException e)
    {
      return null;
    }
    finally
    {
      this.fileCloseDelegate.run();
    }
  }
  
}
