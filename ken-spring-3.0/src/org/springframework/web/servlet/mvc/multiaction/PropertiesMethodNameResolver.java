/*  1:   */ package org.springframework.web.servlet.mvc.multiaction;
/*  2:   */ 
/*  3:   */ import java.util.Enumeration;
/*  4:   */ import java.util.Properties;
/*  5:   */ import org.springframework.beans.factory.InitializingBean;
/*  6:   */ import org.springframework.util.AntPathMatcher;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ import org.springframework.util.PathMatcher;
/*  9:   */ 
/* 10:   */ public class PropertiesMethodNameResolver
/* 11:   */   extends AbstractUrlMethodNameResolver
/* 12:   */   implements InitializingBean
/* 13:   */ {
/* 14:   */   private Properties mappings;
/* 15:54 */   private PathMatcher pathMatcher = new AntPathMatcher();
/* 16:   */   
/* 17:   */   public void setMappings(Properties mappings)
/* 18:   */   {
/* 19:62 */     this.mappings = mappings;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void setPathMatcher(PathMatcher pathMatcher)
/* 23:   */   {
/* 24:71 */     Assert.notNull(pathMatcher, "PathMatcher must not be null");
/* 25:72 */     this.pathMatcher = pathMatcher;
/* 26:   */   }
/* 27:   */   
/* 28:   */   public void afterPropertiesSet()
/* 29:   */   {
/* 30:76 */     if ((this.mappings == null) || (this.mappings.isEmpty())) {
/* 31:77 */       throw new IllegalArgumentException("'mappings' property is required");
/* 32:   */     }
/* 33:   */   }
/* 34:   */   
/* 35:   */   protected String getHandlerMethodNameForUrlPath(String urlPath)
/* 36:   */   {
/* 37:84 */     String methodName = this.mappings.getProperty(urlPath);
/* 38:85 */     if (methodName != null) {
/* 39:86 */       return methodName;
/* 40:   */     }
/* 41:88 */     Enumeration propNames = this.mappings.propertyNames();
/* 42:89 */     while (propNames.hasMoreElements())
/* 43:   */     {
/* 44:90 */       String registeredPath = (String)propNames.nextElement();
/* 45:91 */       if (this.pathMatcher.match(registeredPath, urlPath)) {
/* 46:92 */         return (String)this.mappings.get(registeredPath);
/* 47:   */       }
/* 48:   */     }
/* 49:95 */     return null;
/* 50:   */   }
/* 51:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.multiaction.PropertiesMethodNameResolver
 * JD-Core Version:    0.7.0.1
 */