package org.springframework.cache;

public abstract interface Cache
{
  public abstract String getName();
  
  public abstract Object getNativeCache();
  
  public abstract ValueWrapper get(Object paramObject);
  
  public abstract void put(Object paramObject1, Object paramObject2);
  
  public abstract void evict(Object paramObject);
  
  public abstract void clear();
  
  public static abstract interface ValueWrapper
  {
    public abstract Object get();
  }
}


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.Cache
 * JD-Core Version:    0.7.0.1
 */