/*   1:    */ package org.springframework.cache.ehcache;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.util.Set;
/*   5:    */ import net.sf.ehcache.Cache;
/*   6:    */ import net.sf.ehcache.CacheException;
/*   7:    */ import net.sf.ehcache.CacheManager;
/*   8:    */ import net.sf.ehcache.Ehcache;
/*   9:    */ import net.sf.ehcache.bootstrap.BootstrapCacheLoader;
/*  10:    */ import net.sf.ehcache.constructs.blocking.BlockingCache;
/*  11:    */ import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
/*  12:    */ import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;
/*  13:    */ import net.sf.ehcache.constructs.blocking.UpdatingCacheEntryFactory;
/*  14:    */ import net.sf.ehcache.constructs.blocking.UpdatingSelfPopulatingCache;
/*  15:    */ import net.sf.ehcache.event.CacheEventListener;
/*  16:    */ import net.sf.ehcache.event.RegisteredEventListeners;
/*  17:    */ import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
/*  18:    */ import org.apache.commons.logging.Log;
/*  19:    */ import org.apache.commons.logging.LogFactory;
/*  20:    */ import org.springframework.beans.factory.BeanNameAware;
/*  21:    */ import org.springframework.beans.factory.FactoryBean;
/*  22:    */ import org.springframework.beans.factory.InitializingBean;
/*  23:    */ import org.springframework.util.Assert;
/*  24:    */ 
/*  25:    */ public class EhCacheFactoryBean
/*  26:    */   implements FactoryBean<Ehcache>, BeanNameAware, InitializingBean
/*  27:    */ {
/*  28: 66 */   protected final Log logger = LogFactory.getLog(getClass());
/*  29:    */   private CacheManager cacheManager;
/*  30:    */   private String cacheName;
/*  31: 72 */   private int maxElementsInMemory = 10000;
/*  32: 74 */   private int maxElementsOnDisk = 10000000;
/*  33: 76 */   private MemoryStoreEvictionPolicy memoryStoreEvictionPolicy = MemoryStoreEvictionPolicy.LRU;
/*  34: 78 */   private boolean overflowToDisk = true;
/*  35: 80 */   private boolean eternal = false;
/*  36: 82 */   private int timeToLive = 120;
/*  37: 84 */   private int timeToIdle = 120;
/*  38: 86 */   private boolean diskPersistent = false;
/*  39: 88 */   private int diskExpiryThreadIntervalSeconds = 120;
/*  40: 90 */   private int diskSpoolBufferSize = 0;
/*  41: 92 */   private boolean clearOnFlush = true;
/*  42: 94 */   private boolean blocking = false;
/*  43:    */   private CacheEntryFactory cacheEntryFactory;
/*  44:    */   private BootstrapCacheLoader bootstrapCacheLoader;
/*  45:    */   private Set<CacheEventListener> cacheEventListeners;
/*  46:102 */   private boolean disabled = false;
/*  47:    */   private String beanName;
/*  48:    */   private Ehcache cache;
/*  49:    */   
/*  50:    */   public void setCacheManager(CacheManager cacheManager)
/*  51:    */   {
/*  52:121 */     this.cacheManager = cacheManager;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public void setCacheName(String cacheName)
/*  56:    */   {
/*  57:129 */     this.cacheName = cacheName;
/*  58:    */   }
/*  59:    */   
/*  60:    */   public void setMaxElementsInMemory(int maxElementsInMemory)
/*  61:    */   {
/*  62:137 */     this.maxElementsInMemory = maxElementsInMemory;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public void setMaxElementsOnDisk(int maxElementsOnDisk)
/*  66:    */   {
/*  67:145 */     this.maxElementsOnDisk = maxElementsOnDisk;
/*  68:    */   }
/*  69:    */   
/*  70:    */   public void setMemoryStoreEvictionPolicy(MemoryStoreEvictionPolicy memoryStoreEvictionPolicy)
/*  71:    */   {
/*  72:155 */     Assert.notNull(memoryStoreEvictionPolicy, "memoryStoreEvictionPolicy must not be null");
/*  73:156 */     this.memoryStoreEvictionPolicy = memoryStoreEvictionPolicy;
/*  74:    */   }
/*  75:    */   
/*  76:    */   public void setOverflowToDisk(boolean overflowToDisk)
/*  77:    */   {
/*  78:164 */     this.overflowToDisk = overflowToDisk;
/*  79:    */   }
/*  80:    */   
/*  81:    */   public void setEternal(boolean eternal)
/*  82:    */   {
/*  83:172 */     this.eternal = eternal;
/*  84:    */   }
/*  85:    */   
/*  86:    */   public void setTimeToLive(int timeToLive)
/*  87:    */   {
/*  88:181 */     this.timeToLive = timeToLive;
/*  89:    */   }
/*  90:    */   
/*  91:    */   public void setTimeToIdle(int timeToIdle)
/*  92:    */   {
/*  93:190 */     this.timeToIdle = timeToIdle;
/*  94:    */   }
/*  95:    */   
/*  96:    */   public void setDiskPersistent(boolean diskPersistent)
/*  97:    */   {
/*  98:198 */     this.diskPersistent = diskPersistent;
/*  99:    */   }
/* 100:    */   
/* 101:    */   public void setDiskExpiryThreadIntervalSeconds(int diskExpiryThreadIntervalSeconds)
/* 102:    */   {
/* 103:206 */     this.diskExpiryThreadIntervalSeconds = diskExpiryThreadIntervalSeconds;
/* 104:    */   }
/* 105:    */   
/* 106:    */   public void setDiskSpoolBufferSize(int diskSpoolBufferSize)
/* 107:    */   {
/* 108:214 */     this.diskSpoolBufferSize = diskSpoolBufferSize;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void setClearOnFlush(boolean clearOnFlush)
/* 112:    */   {
/* 113:222 */     this.clearOnFlush = clearOnFlush;
/* 114:    */   }
/* 115:    */   
/* 116:    */   public void setBlocking(boolean blocking)
/* 117:    */   {
/* 118:234 */     this.blocking = blocking;
/* 119:    */   }
/* 120:    */   
/* 121:    */   public void setCacheEntryFactory(CacheEntryFactory cacheEntryFactory)
/* 122:    */   {
/* 123:252 */     this.cacheEntryFactory = cacheEntryFactory;
/* 124:    */   }
/* 125:    */   
/* 126:    */   public void setBootstrapCacheLoader(BootstrapCacheLoader bootstrapCacheLoader)
/* 127:    */   {
/* 128:260 */     this.bootstrapCacheLoader = bootstrapCacheLoader;
/* 129:    */   }
/* 130:    */   
/* 131:    */   public void setCacheEventListeners(Set<CacheEventListener> cacheEventListeners)
/* 132:    */   {
/* 133:268 */     this.cacheEventListeners = cacheEventListeners;
/* 134:    */   }
/* 135:    */   
/* 136:    */   public void setDisabled(boolean disabled)
/* 137:    */   {
/* 138:276 */     this.disabled = disabled;
/* 139:    */   }
/* 140:    */   
/* 141:    */   public void setBeanName(String name)
/* 142:    */   {
/* 143:280 */     this.beanName = name;
/* 144:    */   }
/* 145:    */   
/* 146:    */   public void afterPropertiesSet()
/* 147:    */     throws CacheException, IOException
/* 148:    */   {
/* 149:286 */     if (this.cacheManager == null)
/* 150:    */     {
/* 151:287 */       if (this.logger.isDebugEnabled()) {
/* 152:288 */         this.logger.debug("Using default EHCache CacheManager for cache region '" + this.cacheName + "'");
/* 153:    */       }
/* 154:290 */       this.cacheManager = CacheManager.getInstance();
/* 155:    */     }
/* 156:294 */     if (this.cacheName == null) {
/* 157:295 */       this.cacheName = this.beanName;
/* 158:    */     }
/* 159:    */     Ehcache rawCache;
/* 160:    */     Ehcache rawCache;
/* 161:301 */     if (this.cacheManager.cacheExists(this.cacheName))
/* 162:    */     {
/* 163:302 */       if (this.logger.isDebugEnabled()) {
/* 164:303 */         this.logger.debug("Using existing EHCache cache region '" + this.cacheName + "'");
/* 165:    */       }
/* 166:305 */       rawCache = this.cacheManager.getEhcache(this.cacheName);
/* 167:    */     }
/* 168:    */     else
/* 169:    */     {
/* 170:308 */       if (this.logger.isDebugEnabled()) {
/* 171:309 */         this.logger.debug("Creating new EHCache cache region '" + this.cacheName + "'");
/* 172:    */       }
/* 173:311 */       rawCache = createCache();
/* 174:312 */       this.cacheManager.addCache(rawCache);
/* 175:    */     }
/* 176:316 */     Ehcache decoratedCache = decorateCache(rawCache);
/* 177:317 */     if (decoratedCache != rawCache) {
/* 178:318 */       this.cacheManager.replaceCacheWithDecoratedCache(rawCache, decoratedCache);
/* 179:    */     }
/* 180:320 */     this.cache = decoratedCache;
/* 181:    */   }
/* 182:    */   
/* 183:    */   protected Cache createCache()
/* 184:    */   {
/* 185:328 */     Cache cache = !this.clearOnFlush ? 
/* 186:329 */       new Cache(this.cacheName, this.maxElementsInMemory, this.memoryStoreEvictionPolicy, 
/* 187:330 */       this.overflowToDisk, null, this.eternal, this.timeToLive, this.timeToIdle, 
/* 188:331 */       this.diskPersistent, this.diskExpiryThreadIntervalSeconds, null, 
/* 189:332 */       this.bootstrapCacheLoader, this.maxElementsOnDisk, this.diskSpoolBufferSize, 
/* 190:333 */       this.clearOnFlush) : 
/* 191:334 */       new Cache(this.cacheName, this.maxElementsInMemory, this.memoryStoreEvictionPolicy, 
/* 192:335 */       this.overflowToDisk, null, this.eternal, this.timeToLive, this.timeToIdle, 
/* 193:336 */       this.diskPersistent, this.diskExpiryThreadIntervalSeconds, null, 
/* 194:337 */       this.bootstrapCacheLoader, this.maxElementsOnDisk, this.diskSpoolBufferSize);
/* 195:339 */     if (this.cacheEventListeners != null) {
/* 196:340 */       for (CacheEventListener listener : this.cacheEventListeners) {
/* 197:341 */         cache.getCacheEventNotificationService().registerListener(listener);
/* 198:    */       }
/* 199:    */     }
/* 200:344 */     if (this.disabled) {
/* 201:345 */       cache.setDisabled(true);
/* 202:    */     }
/* 203:347 */     return cache;
/* 204:    */   }
/* 205:    */   
/* 206:    */   protected Ehcache decorateCache(Ehcache cache)
/* 207:    */   {
/* 208:356 */     if (this.cacheEntryFactory != null)
/* 209:    */     {
/* 210:357 */       if ((this.cacheEntryFactory instanceof UpdatingCacheEntryFactory)) {
/* 211:358 */         return new UpdatingSelfPopulatingCache(cache, (UpdatingCacheEntryFactory)this.cacheEntryFactory);
/* 212:    */       }
/* 213:361 */       return new SelfPopulatingCache(cache, this.cacheEntryFactory);
/* 214:    */     }
/* 215:364 */     if (this.blocking) {
/* 216:365 */       return new BlockingCache(cache);
/* 217:    */     }
/* 218:367 */     return cache;
/* 219:    */   }
/* 220:    */   
/* 221:    */   public Ehcache getObject()
/* 222:    */   {
/* 223:372 */     return this.cache;
/* 224:    */   }
/* 225:    */   
/* 226:    */   public Class<? extends Ehcache> getObjectType()
/* 227:    */   {
/* 228:376 */     return this.cache != null ? this.cache.getClass() : Ehcache.class;
/* 229:    */   }
/* 230:    */   
/* 231:    */   public boolean isSingleton()
/* 232:    */   {
/* 233:380 */     return true;
/* 234:    */   }
/* 235:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.ehcache.EhCacheFactoryBean
 * JD-Core Version:    0.7.0.1
 */