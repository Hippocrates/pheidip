package pheidip.logic.chipin;

import org.jsoup.nodes.Document;

public class ChipinWebsiteDocumentSource implements ChipinDocumentSource
{
  private ChipinLoginManager chipinLogin;

  public ChipinWebsiteDocumentSource(ChipinLoginManager chipinLogin)
  {
    this.chipinLogin = chipinLogin;
  }

  @Override
  public Document provideDocument()
  {
    return chipinLogin.getChipinPage();
  }
}
