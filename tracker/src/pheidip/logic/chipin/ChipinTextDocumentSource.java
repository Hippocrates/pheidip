package pheidip.logic.chipin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ChipinTextDocumentSource implements ChipinDocumentSource
{
  private String htmlText;
  
  public ChipinTextDocumentSource(String htmlText)
  {
    this.htmlText = htmlText;
  }

  @Override
  public Document provideDocument()
  {
    return Jsoup.parse(this.htmlText);
  }
}
