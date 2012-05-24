package pheidip.db;

import java.io.File;
import java.util.List;

import pheidip.objects.Entity;
import pheidip.util.Pair;

import meta.MetaEntity;
import meta.search.MetaSearchEntity;

public interface DataAccess
{
  <E extends Entity> E loadInstance(MetaEntity spec, int id);
  //<E extends Entity> List<E> loadMultiple(MetaEntity spec, List<Integer> ids);
  <E extends Entity> void saveInstance(MetaEntity spec, E instance);
  <E extends Entity> void saveMultiple(MetaEntity spec, List<E> instances);
  <E extends Entity> void updateInstance(MetaEntity spec, E instance);
  <E extends Entity> List<E> updateMultiple(MetaEntity spec, List<E> instances);
  <E extends Entity> void deleteInstance(MetaEntity spec, E instance);
  //<E extends Entity> void deleteMultiple(MetaEntity spec, List<E> instances);
  
  
  
  <E extends Entity, S> Pair<Long, List<E>> searchEntityRange(MetaEntity instanceSpec, MetaSearchEntity searchSpec, S searchParams, int offset, int count, String... ordering);
  <E extends Entity, S> List<E> searchEntity(MetaEntity instanceSpec, MetaSearchEntity searchSpec, S searchParams, String... ordering);
  
  void connectToServer(DBConnectionParams params);
  void createMemoryConnection();
  void closeConnection();
  boolean isConnected();
  void openFileDatabase(File file);
}
