package pheidip.logic.chipin;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.StringUtils;

import meta.MetaEntity;
import meta.reflect.MetaEntityReflector;

import pheidip.db.DataAccess;
import pheidip.logic.AbstractExternalProcess;
import pheidip.logic.EntitySearch;
import pheidip.logic.EntitySearchInstance;
import pheidip.logic.ProgramInstance;
import pheidip.model.PropertyReflectSupport;
import pheidip.objects.Donation;
import pheidip.objects.DonationBidState;
import pheidip.objects.Donor;

public class ChipinMergeProcess extends AbstractExternalProcess
{
  private ProgramInstance instance;
  private ChipinDonationSource donationSource;
  private EntitySearch<Donation> donationSearch;
  private EntitySearch<Donor> donorSearch;
  private MetaEntity donationEntity;
  private MetaEntity donorEntity;

  public ChipinMergeProcess(ProgramInstance instance, ChipinDonationSource documentSource)
  {
    this(instance, documentSource, null);
  }
  
  public ChipinMergeProcess(ProgramInstance instance, ChipinDonationSource documentSource, ProcessStateCallback listener)
  {
    super(listener);
    
    this.instance = instance;
    this.donationSource = documentSource;
    
    this.donationSearch = this.instance.getEntitySearch(Donation.class);
    this.donorSearch = this.instance.getEntitySearch(Donor.class);
    this.donationEntity = MetaEntityReflector.getMetaEntity(Donation.class);
    this.donorEntity = MetaEntityReflector.getMetaEntity(Donor.class);
  }

  @Override
  public void run()
  {
    try
    {
      this.resetState(ExternalProcessState.RUNNING, 0.1, "Retreiving donations from chipin...");
      Thread.sleep(0);
      
      List<ChipinDonation> chipinDonations = this.donationSource.provideChipinDonations();

      Map<String, ChipinDonation> chipinDonationMap = ChipinDonations.mapDonations(chipinDonations);

      Map<String, ChipinDonation> oldChipinDonationMap = this.instance.getChipinLogin().getKnownDonations();
      
      for (Map.Entry<String, ChipinDonation> entry : oldChipinDonationMap.entrySet())
      {
        ChipinDonation found = chipinDonationMap.get(entry.getKey());
        
        if (found != null)
        {
          if (StringUtils.isNullOrEmpty(entry.getValue().getComment()) == StringUtils.isNullOrEmpty(found.getComment()))
          {
            chipinDonationMap.remove(entry.getKey());
          }
        }
      }
      
      if (chipinDonationMap.size() > 0)
      {
        Date minDate = null;
        Date maxDate = null;
        
        for (Map.Entry<String, ChipinDonation> entry : chipinDonationMap.entrySet())
        {
          ChipinDonation donation = entry.getValue();
          
          if (minDate == null || donation.getTimeStamp().compareTo(minDate) < 0)
          {
            minDate = donation.getTimeStamp();
          }
          
          if (maxDate == null || donation.getTimeStamp().compareTo(maxDate) > 0)
          {
            maxDate = donation.getTimeStamp();
          }
          
          this.instance.getChipinLogin().addKnownDonation(donation);
        }
        
        this.resetState(ExternalProcessState.RUNNING, 0.3, "Reading current donation set...");
        Thread.sleep(0);
  
        EntitySearchInstance<Donation> donationSearchInstance = this.donationSearch.createSearchInstance();
        EntitySearchInstance<Donor> donorSearchInstance = this.donorSearch.createSearchInstance();
  
        Object params = donationSearchInstance.getSearchParams();
        
        PropertyReflectSupport.setProperty(params, "bidState", EnumSet.allOf(DonationBidState.class));
        PropertyReflectSupport.setProperty(params, "lo_timeReceived", minDate);
        PropertyReflectSupport.setProperty(params, "hi_timeReceived", maxDate);
        
        donorSearchInstance.setPageSize(Integer.MAX_VALUE);
  
        donorSearchInstance.runSearch();
        
        List<Donor> allDonors = donorSearchInstance.getResults();
  
        List<Donation> donationsToInsert = new ArrayList<Donation>();
        List<Donation> donationsToUpdate = new ArrayList<Donation>();
        List<Donor> donorsToInsert = new ArrayList<Donor>();
        
        this.resetState(ExternalProcessState.RUNNING, 0.4, "Merging donations into database...");
        Thread.sleep(0);
        
        double step = (1.0 - 0.4) / chipinDonationMap.size();
  
        donationSearchInstance.runSearch();
        List<Donation> databaseDonations = donationSearchInstance.getResults();
        
        while(databaseDonations.size() > 0)
        {
          donationsToUpdate.addAll(ChipinDonations.updateMergeTable(chipinDonationMap, databaseDonations));
  
          if (donationSearchInstance.isNextPageAvailable())
          {
            donationSearchInstance.nextPage();
            databaseDonations = donationSearchInstance.getResults();
          }
          else
          {
            break;
          }
          
          this.resetState(ExternalProcessState.RUNNING, 1.0 - step*chipinDonationMap.size(), "Merging donations into database...");
          Thread.sleep(0);
        }
  
        ChipinDonations.buildInsertTables(chipinDonationMap, allDonors, donorsToInsert, donationsToInsert);
  
        DataAccess database = this.instance.getDataAccess();
        
        database.updateMultiple(this.donationEntity, donationsToUpdate);
        
        database.saveMultiple(this.donorEntity, donorsToInsert);
        database.saveMultiple(this.donationEntity, donationsToInsert);
  
        Thread.sleep(0);
      }
      
      this.resetState(ExternalProcessState.COMPLETED, 1.0, "Merge operation complete.");
    }
    catch (InterruptedException e)
    {
      this.resetState(ExternalProcessState.CANCELLED, 0.0, "Merge operation cancelled.");
    }
    catch (Exception e)
    {
      this.resetState(ExternalProcessState.FAILED, 0.0, "The merge operation failed.");
    }
  }
  
  @Override
  public String getProcessName()
  {
    return "Chipin Merge";
  }
}
