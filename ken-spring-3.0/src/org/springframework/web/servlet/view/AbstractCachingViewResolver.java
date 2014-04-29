/*   1:    */ package org.springframework.web.servlet.view;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Locale;
/*   5:    */ import java.util.Map;
/*   6:    */ import org.apache.commons.logging.Log;
/*   7:    */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*   8:    */ import org.springframework.web.servlet.View;
/*   9:    */ import org.springframework.web.servlet.ViewResolver;
/*  10:    */ 
/*  11:    */ public abstract class AbstractCachingViewResolver
/*  12:    */   extends WebApplicationObjectSupport
/*  13:    */   implements ViewResolver
/*  14:    */ {
/*  15: 43 */   private boolean cache = true;
/*  16: 46 */   private boolean cacheUnresolved = false;
/*  17: 49 */   private final Map<Object, View> viewCache = new HashMap();
/*  18:    */   
/*  19:    */   public void setCache(boolean cache)
/*  20:    */   {
/*  21: 59 */     this.cache = cache;
/*  22:    */   }
/*  23:    */   
/*  24:    */   public boolean isCache()
/*  25:    */   {
/*  26: 66 */     return this.cache;
/*  27:    */   }
/*  28:    */   
/*  29:    */   public void setCacheUnresolved(boolean cacheUnresolved)
/*  30:    */   {
/*  31: 80 */     this.cacheUnresolved = cacheUnresolved;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public boolean isCacheUnresolved()
/*  35:    */   {
/*  36: 87 */     return this.cacheUnresolved;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public View resolveViewName(String viewName, Locale locale)
/*  40:    */     throws Exception
/*  41:    */   {
/*  42: 91 */     if (!isCache()) {
/*  43: 92 */       return createView(viewName, locale);
/*  44:    */     }
/*  45: 95 */     Object cacheKey = getCacheKey(viewName, locale);
/*  46: 96 */     synchronized (this.viewCache)
/*  47:    */     {
/*  48: 97 */       View view = (View)this.viewCache.get(cacheKey);
/*  49: 98 */       boolean isCached = (this.cacheUnresolved) && (this.viewCache.containsKey(cacheKey));
/*  50: 99 */       if ((view == null) && (!isCached))
/*  51:    */       {
/*  52:101 */         view = createView(viewName, locale);
/*  53:102 */         this.viewCache.put(cacheKey, view);
/*  54:103 */         if (this.logger.isTraceEnabled()) {
/*  55:104 */           this.logger.trace("Cached view [" + cacheKey + "]");
/*  56:    */         }
/*  57:    */       }
/*  58:107 */       return view;
/*  59:    */     }
/*  60:    */   }
/*  61:    */   
/*  62:    */   protected Object getCacheKey(String viewName, Locale locale)
/*  63:    */   {
/*  64:120 */     return viewName + "_" + locale;
/*  65:    */   }
/*  66:    */   
/*  67:    */   public void removeFromCache(String viewName, Locale locale)
/*  68:    */   {
/*  69:133 */     if (!this.cache)
/*  70:    */     {
/*  71:134 */       this.logger.warn("View caching is SWITCHED OFF -- removal not necessary");
/*  72:    */     }
/*  73:    */     else
/*  74:    */     {
/*  75:137 */       Object cacheKey = getCacheKey(viewName, locale);
/*  76:    */       Object cachedView;
/*  77:139 */       synchronized (this.viewCache)
/*  78:    */       {
/*  79:140 */         cachedView = this.viewCache.remove(cacheKey);
/*  80:    */       }
/*  81:    */       Object cachedView;
/*  82:142 */       if (cachedView == null)
/*  83:    */       {
/*  84:144 */         if (this.logger.isDebugEnabled()) {
/*  85:145 */           this.logger.debug("No cached instance for view '" + cacheKey + "' was found");
/*  86:    */         }
/*  87:    */       }
/*  88:149 */       else if (this.logger.isDebugEnabled()) {
/*  89:150 */         this.logger.debug("Cache for view " + cacheKey + " has been cleared");
/*  90:    */       }
/*  91:    */     }
/*  92:    */   }
/*  93:    */   
/*  94:    */   public void clearCache()
/*  95:    */   {
/*  96:161 */     this.logger.debug("Clearing entire view cache");
/*  97:162 */     synchronized (this.viewCache)
/*  98:    */     {
/*  99:163 */       this.viewCache.clear();
/* 100:    */     }
/* 101:    */   }
/* 102:    */   
/* 103:    */   protected View createView(String viewName, Locale locale)
/* 104:    */     throws Exception
/* 105:    */   {
/* 106:182 */     return loadView(viewName, locale);
/* 107:    */   }
/* 108:    */   
/* 109:    */   protected abstract View loadView(String paramString, Locale paramLocale)
/* 110:    */     throws Exception;
/* 111:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.view.AbstractCachingViewResolver
 * JD-Core Version:    0.7.0.1
 */