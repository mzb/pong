package edu.pk.pj.pong;

import java.util.HashMap;
import java.util.Map;

public class ObjectCache<T> {

  private Map<String, T> instances = new HashMap<String, T>();
  
  public ObjectCache(T... objects) {
    for (T object : objects) {
      instances.put(object.getClass().getSimpleName(), object);
    }
  }
  
  public ObjectCache() {}
  
  public T getOrCreate(Class<? extends T> objectClass) throws ObjectCache.Error {
    String objectClassName = objectClass.getSimpleName();
    T object = instances.get(objectClassName);
    if (object == null) {
      try {
        object = objectClass.newInstance();
      } catch (InstantiationException e) {
        throw new ObjectCache.Error(e.toString());
      } catch (IllegalAccessException e) {
        throw new ObjectCache.Error(e.toString());
      }
      instances.put(objectClassName, object);
    }
    return object;
  }
  
  public T get(String className) {
    return instances.get(className);
  }
  
  
  public static class Error extends Exception {
    public Error(String msg) {
      super(msg);
    }
  }
}
