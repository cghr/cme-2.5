/*  1:   */ package org.springframework.web.servlet.mvc.support;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.springframework.context.ApplicationContext;
/*  6:   */ import org.springframework.util.StringUtils;
/*  7:   */ 
/*  8:   */ public class ControllerBeanNameHandlerMapping
/*  9:   */   extends AbstractControllerUrlHandlerMapping
/* 10:   */ {
/* 11:43 */   private String urlPrefix = "";
/* 12:45 */   private String urlSuffix = "";
/* 13:   */   
/* 14:   */   public void setUrlPrefix(String urlPrefix)
/* 15:   */   {
/* 16:54 */     this.urlPrefix = (urlPrefix != null ? urlPrefix : "");
/* 17:   */   }
/* 18:   */   
/* 19:   */   public void setUrlSuffix(String urlSuffix)
/* 20:   */   {
/* 21:63 */     this.urlSuffix = (urlSuffix != null ? urlSuffix : "");
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected String[] buildUrlsForHandler(String beanName, Class beanClass)
/* 25:   */   {
/* 26:69 */     List<String> urls = new ArrayList();
/* 27:70 */     urls.add(generatePathMapping(beanName));
/* 28:71 */     String[] aliases = getApplicationContext().getAliases(beanName);
/* 29:72 */     for (String alias : aliases) {
/* 30:73 */       urls.add(generatePathMapping(alias));
/* 31:   */     }
/* 32:75 */     return StringUtils.toStringArray(urls);
/* 33:   */   }
/* 34:   */   
/* 35:   */   protected String generatePathMapping(String beanName)
/* 36:   */   {
/* 37:82 */     String name = "/" + beanName;
/* 38:83 */     StringBuilder path = new StringBuilder();
/* 39:84 */     if (!name.startsWith(this.urlPrefix)) {
/* 40:85 */       path.append(this.urlPrefix);
/* 41:   */     }
/* 42:87 */     path.append(name);
/* 43:88 */     if (!name.endsWith(this.urlSuffix)) {
/* 44:89 */       path.append(this.urlSuffix);
/* 45:   */     }
/* 46:91 */     return path.toString();
/* 47:   */   }
/* 48:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.mvc.support.ControllerBeanNameHandlerMapping
 * JD-Core Version:    0.7.0.1
 */