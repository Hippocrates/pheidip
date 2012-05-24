package pheidip.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import meta.MetaEntity;
import meta.reflect.MetaEntityReflector;
import meta.search.MetaSearchEntity;
import meta.search.MetaSearchUtils;
import pheidip.db.DataAccess;
import pheidip.db.hibernate.HibernateDataAccess;
import pheidip.logic.chipin.ChipinLoginManager;
import pheidip.logic.gdocs.GoogleSpreadSheetLoginManager;
import pheidip.objects.Bid;
import pheidip.objects.Challenge;
import pheidip.objects.ChallengeBid;
import pheidip.objects.Choice;
import pheidip.objects.ChoiceBid;
import pheidip.objects.ChoiceOption;
import pheidip.objects.Donation;
import pheidip.objects.Donor;
import pheidip.objects.Entity;
import pheidip.objects.Prize;
import pheidip.objects.SpeedRun;

public class ProgramInstance
{
  private static final List<Class<? extends Entity>> entityClasses;

  private static final Map<Class<? extends Entity>, MetaEntity> metaEntities;
  private static final Map<Class<? extends Entity>, MetaSearchEntity> metaSearchEntities;
  
  static
  {
    ArrayList<Class<? extends Entity>> entities = new ArrayList<Class<? extends Entity>>();
    
    entities.add(Donor.class);
    entities.add(Donation.class);
    entities.add(Bid.class);
    entities.add(Choice.class);
    entities.add(Challenge.class);
    entities.add(ChoiceOption.class);
    entities.add(SpeedRun.class);
    entities.add(ChoiceBid.class);
    entities.add(ChallengeBid.class);
    entities.add(Prize.class);
    
    entityClasses = Collections.unmodifiableList(entities);
    
    HashMap<Class<? extends Entity>, MetaEntity> metaMap = new HashMap<Class<? extends Entity>, MetaEntity>();
    
    for (Class<? extends Entity> clazz : entityClasses)
    {
      MetaEntity entityDesc = MetaEntityReflector.getMetaEntity(clazz);
      
      metaMap.put(clazz, entityDesc);
    }
    
    metaEntities = Collections.unmodifiableMap(metaMap);
    
    HashMap<Class<? extends Entity>, MetaSearchEntity> metaSearchMap = new HashMap<Class<? extends Entity>, MetaSearchEntity>();
    
    for (Class<? extends Entity> clazz : entityClasses)
    {
      MetaEntity entityDesc = metaEntities.get(clazz);
      MetaSearchEntity searchDesc = MetaSearchUtils.createMetaSearchEntity(entityDesc.getName() + "Search", 
          MetaSearchUtils.createSearchDescription(entityDesc.getEntityDescription()));
      
      metaSearchMap.put(clazz, searchDesc);
    }
    
    metaSearchEntities = Collections.unmodifiableMap(metaSearchMap);
  }
  
  @Getter
  private DataAccess dataAccess;
  
  @Getter
  private ChipinLoginManager chipinLogin;
  
  @Getter
  private GoogleSpreadSheetLoginManager googleLogin;

  private Map<Class<? extends Entity>, EntityControl<? extends Entity>> entityControls;
  private Map<Class<? extends Entity>, EntitySearch<? extends Entity>> entitySearches;
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public ProgramInstance()
  {
    this.dataAccess = new HibernateDataAccess();
    this.chipinLogin = new ChipinLoginManager();
    this.googleLogin = new GoogleSpreadSheetLoginManager();
    
    this.entityControls = new HashMap<Class<? extends Entity>, EntityControl<? extends Entity>>();
    
    for (Entry<Class<? extends Entity>, MetaEntity> m : metaEntities.entrySet())
    {
      this.entityControls.put(m.getKey(), new EntityControl(this.dataAccess, m.getValue()));
    }
    
    this.entitySearches = new HashMap<Class<? extends Entity>, EntitySearch<? extends Entity>>();
    
    for (Entry<Class<? extends Entity>, MetaSearchEntity> m : metaSearchEntities.entrySet())
    {
      this.entitySearches.put(m.getKey(), new EntitySearch(this.dataAccess, metaEntities.get(m.getKey()), m.getValue()));
    }
  }
  
  public void shutdown()
  {
    if (this.dataAccess.isConnected())
    {
      this.dataAccess.closeConnection();
    }
  }
  
  @Override
  protected void finalize()
  {
    this.shutdown();
  }
  
  @SuppressWarnings("unchecked")
  public <E extends Entity> EntityControl<E> getEntityControl(Class<E> clazz)
  {
    return (EntityControl<E>) this.entityControls.get(clazz);
  }
  
  @SuppressWarnings("unchecked")
  public <E extends Entity> EntitySearch<E> getEntitySearch(Class<E> clazz)
  {
    return (EntitySearch<E>) this.entitySearches.get(clazz);
  }
}
