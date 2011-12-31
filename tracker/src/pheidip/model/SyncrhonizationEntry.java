package pheidip.model;

class SyncrhonizationEntry
{
  private ObjectProperty source;
  private ObjectProperty destination;
  private ConverterMethod converter;
  
  public SyncrhonizationEntry(ObjectProperty source, ObjectProperty destination, ConverterMethod converter)
  {
    this.source = source;
    this.destination = destination;
    this.converter = converter;
  }
  
  public ObjectProperty getDestination()
  {
    return this.destination;
  }
  
  public ObjectProperty getSource()
  {
    return this.source;
  }
  
  public void synchronize()
  {
    this.destination.setValue(this.converter.convert(this.source.getValue()));
  }
}