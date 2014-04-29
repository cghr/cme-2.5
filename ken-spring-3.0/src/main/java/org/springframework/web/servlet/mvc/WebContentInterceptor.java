/*   1:    */ package org.springframework.web.servlet.mvc;
/*   2:    */ 
/*   3:    */ import java.util.Enumeration;
/*   4:    */ import java.util.HashMap;
/*   5:    */ import java.util.Map;
/*   6:    */ import java.util.Properties;
/*   7:    */ import javax.servlet.ServletException;
/*   8:    */ import javax.servlet.http.HttpServletRequest;
/*   9:    */ import javax.servlet.http.HttpServletResponse;
/*  10:    */ import org.apache.commons.logging.Log;
/*  11:    */ import org.springframework.util.AntPathMatcher;
/*  12:    */ import org.springframework.util.Assert;
/*  13:    */ import org.springframework.util.PathMatcher;
/*  14:    */ import org.springframework.web.servlet.HandlerInterceptor;
/*  15:    */ import org.springframework.web.servlet.ModelAndView;
/*  16:    */ import org.springframework.web.servlet.support.WebContentGenerator;
/*  17:    */ import org.springframework.web.util.UrlPathHelper;
/*  18:    */ 
/*  19:    */ public class WebContentInterceptor
/*  20:    */   extends WebContentGenerator
/*  21:    */   implements HandlerInterceptor
/*  22:    */ {
/*  23: 50 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*  24: 52 */   private Map<String, Integer> cacheMappings = new HashMap();
/*  25: 54 */   private PathMatcher pathMatcher = new AntPathMatcher();
/*  26:    */   
/*  27:    */   public WebContentInterceptor()
/*  28:    */   {
/*  29: 60 */     super(false);
/*  30:    */   }
/*  31:    */   
/*  32:    */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath)
/*  33:    */   {
/*  34: 74 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
/*  35:    */   }
/*  36:    */   
/*  37:    */   public void setUrlDecode(boolean urlDecode)
/*  38:    */   {
/*  39: 88 */     this.urlPathHelper.setUrlDecode(urlDecode);
/*  40:    */   }
/*  41:    */   
/*  42:    */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/*  43:    */   {
/*  44:102 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/*  45:103 */     this.urlPathHelper = urlPathHelper;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setCacheMappings(Properties cacheMappings)
/*  49:    */   {
/*  50:119 */     this.cacheMappings.clear();
/*  51:120 */     Enumeration propNames = cacheMappings.propertyNames();
/*  52:121 */     while (propNames.hasMoreElements())
/*  53:    */     {
/*  54:122 */       String path = (String)propNames.nextElement();
/*  55:123 */       this.cacheMappings.put(path, Integer.valueOf(cacheMappings.getProperty(path)));
/*  56:    */     }
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void setPathMatcher(PathMatcher pathMatcher)
/*  60:    */   {
/*  61:135 */     Assert.notNull(pathMatcher, "PathMatcher must not be null");
/*  62:136 */     this.pathMatcher = pathMatcher;
/*  63:    */   }
/*  64:    */   
/*  65:    */   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*  66:    */     throws ServletException
/*  67:    */   {
/*  68:143 */     String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
/*  69:144 */     if (this.logger.isDebugEnabled()) {
/*  70:145 */       this.logger.debug("Looking up cache seconds for [" + lookupPath + "]");
/*  71:    */     }
/*  72:148 */     Integer cacheSeconds = lookupCacheSeconds(lookupPath);
/*  73:149 */     if (cacheSeconds != null)
/*  74:    */     {
/*  75:150 */       if (this.logger.isDebugEnabled()) {
/*  76:151 */         this.logger.debug("Applying " + cacheSeconds + " cache seconds to [" + lookupPath + "]");
/*  77:    */       }
/*  78:153 */       checkAndPrepare(request, response, cacheSeconds.intValue(), handler instanceof LastModified);
/*  79:    */     }
/*  80:    */     else
/*  81:    */     {
/*  82:156 */       if (this.logger.isDebugEnabled()) {
/*  83:157 */         this.logger.debug("Applying default cache seconds to [" + lookupPath + "]");
/*  84:    */       }
/*  85:159 */       checkAndPrepare(request, response, handler instanceof LastModified);
/*  86:    */     }
/*  87:162 */     return true;
/*  88:    */   }
/*  89:    */   
/*  90:    */   protected Integer lookupCacheSeconds(String urlPath)
/*  91:    */   {
/*  92:176 */     Integer cacheSeconds = (Integer)this.cacheMappings.get(urlPath);
/*  93:177 */     if (cacheSeconds == null) {
/*  94:179 */       for (String registeredPath : this.cacheMappings.keySet()) {
/*  95:180 */         if (this.pathMatcher.match(registeredPath, urlPath)) {
/*  96:181 */           cacheSeconds = (Integer)this.cacheMappings.get(registeredPath);
/*  97:    */         }
/*  98:    */       }
/*  99:    */     }
/* 100:185 */     return cacheSeconds;
/* 101:    */   }
/* 102:    */   
/* 103:    */   public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
/* 104:    */     throws Exception
/* 105:    */   {}
/* 106:    */   
/* 107:    */   public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/* 108:    */     throws Exception
/* 109:    */   {}
/* 110:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.WebContentInterceptor
 * JD-Core Version:    0.7.0.1
 */