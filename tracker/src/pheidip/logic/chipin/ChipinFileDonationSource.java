package pheidip.logic.chipin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jsoup.Jsoup;

public class ChipinFileDonationSource implements ChipinDonationSource
{
  
  private InputStream inputFile;
  private Runnable fileCloseDelegate;

  public ChipinFileDonationSource(File targetFile)
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
          ChipinFileDonationSource.this.inputFile.close();
        } 
        catch (IOException e)
        {
          // what else do you want me to do?
        }
      }
    };
  }
  
  public ChipinFileDonationSource(InputStream inputFile, Runnable fileCloseDelegate)
  {
    this.inputFile = inputFile;
    this.fileCloseDelegate = fileCloseDelegate;
  }
  
  public List<ChipinDonation> provideChipinDonations()
  {
    try
    {
      return ChipinDonations.extractDonations(Jsoup.parse(this.inputFile, null, "www.chipin.com"));
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
