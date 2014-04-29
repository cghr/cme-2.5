/*   1:    */ package org.springframework.cache.ehcache;
/*   2:    */ 
/*   3:    */ import java.io.IOException;
/*   4:    */ import java.io.InputStream;
/*   5:    */ import net.sf.ehcache.CacheException;
/*   6:    */ import net.sf.ehcache.CacheManager;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.apache.commons.logging.LogFactory;
/*   9:    */ import org.springframework.beans.factory.DisposableBean;
/*  10:    */ import org.springframework.beans.factory.FactoryBean;
/*  11:    */ import org.springframework.beans.factory.InitializingBean;
/*  12:    */ import org.springframework.core.io.Resource;
/*  13:    */ 
/*  14:    */ public class EhCacheManagerFactoryBean
/*  15:    */   implements FactoryBean<CacheManager>, InitializingBean, DisposableBean
/*  16:    */ {
/*  17: 57 */   protected final Log logger = LogFactory.getLog(getClass());
/*  18:    */   private Resource configLocation;
/*  19: 61 */   private boolean shared = false;
/*  20:    */   private String cacheManagerName;
/*  21:    */   private CacheManager cacheManager;
/*  22:    */   
/*  23:    */   public void setConfigLocation(Resource configLocation)
/*  24:    */   {
/*  25: 76 */     this.configLocation = configLocation;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setShared(boolean shared)
/*  29:    */   {
/*  30: 87 */     this.shared = shared;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setCacheManagerName(String cacheManagerName)
/*  34:    */   {
/*  35: 95 */     this.cacheManagerName = cacheManagerName;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void afterPropertiesSet()
/*  39:    */     throws IOException, CacheException
/*  40:    */   {
/*  41:100 */     this.logger.info("Initializing EHCache CacheManager");
/*  42:101 */     if (this.configLocation != null)
/*  43:    */     {
/*  44:102 */       InputStream is = this.configLocation.getInputStream();
/*  45:    */       try
/*  46:    */       {
/*  47:104 */         this.cacheManager = (this.shared ? CacheManager.create(is) : new CacheManager(is));
/*  48:    */       }
/*  49:    */       finally
/*  50:    */       {
/*  51:107 */         is.close();
/*  52:    */       }
/*  53:    */     }
/*  54:    */     else
/*  55:    */     {
/*  56:111 */       this.cacheManager = (this.shared ? CacheManager.create() : new CacheManager());
/*  57:    */     }
/*  58:113 */     if (this.cacheManagerName != null) {
/*  59:114 */       this.cacheManager.setName(this.cacheManagerName);
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   public CacheManager getObject()
/*  64:    */   {
/*  65:120 */     return this.cacheManager;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public Class<? extends CacheManager> getObjectType()
/*  69:    */   {
/*  70:124 */     return this.cacheManager != null ? this.cacheManager.getClass() : CacheManager.class;
/*  71:    */   }
/*  72:    */   
/*  73:    */   public boolean isSingleton()
/*  74:    */   {
/*  75:128 */     return true;
/*  76:    */   }
/*  77:    */   
/*  78:    */   public void destroy()
/*  79:    */   {
/*  80:133 */     this.logger.info("Shutting down EHCache CacheManager");
/*  81:134 */     this.cacheManager.shutdown();
/*  82:    */   }
/*  83:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.ehcache.EhCacheManagerFactoryBean
 * JD-Core Version:    0.7.0.1
 */