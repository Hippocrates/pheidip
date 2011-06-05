package pheidip.logic;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ChipinFileDocumentSource implements ChipinDocumentSource
{
  
  private InputStream inputFile;
  private Runnable fileCloseDelegate;

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
