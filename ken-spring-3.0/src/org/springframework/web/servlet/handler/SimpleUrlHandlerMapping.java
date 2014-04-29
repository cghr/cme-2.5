/*   1:    */ package org.springframework.web.servlet.handler;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Map.Entry;
/*   6:    */ import java.util.Properties;
/*   7:    */ import org.apache.commons.logging.Log;
/*   8:    */ import org.springframework.beans.BeansException;
/*   9:    */ import org.springframework.util.CollectionUtils;
/*  10:    */ 
/*  11:    */ public class SimpleUrlHandlerMapping
/*  12:    */   extends AbstractUrlHandlerMapping
/*  13:    */ {
/*  14: 58 */   private final Map<String, Object> urlMap = new HashMap();
/*  15:    */   
/*  16:    */   public void setMappings(Properties mappings)
/*  17:    */   {
/*  18: 70 */     CollectionUtils.mergePropertiesIntoMap(mappings, this.urlMap);
/*  19:    */   }
/*  20:    */   
/*  21:    */   public void setUrlMap(Map<String, ?> urlMap)
/*  22:    */   {
/*  23: 82 */     this.urlMap.putAll(urlMap);
/*  24:    */   }
/*  25:    */   
/*  26:    */   public Map<String, ?> getUrlMap()
/*  27:    */   {
/*  28: 93 */     return this.urlMap;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public void initApplicationContext()
/*  32:    */     throws BeansException
/*  33:    */   {
/*  34:103 */     super.initApplicationContext();
/*  35:104 */     registerHandlers(this.urlMap);
/*  36:    */   }
/*  37:    */   
/*  38:    */   protected void registerHandlers(Map<String, Object> urlMap)
/*  39:    */     throws BeansException
/*  40:    */   {
/*  41:114 */     if (urlMap.isEmpty()) {
/*  42:115 */       this.logger.warn("Neither 'urlMap' nor 'mappings' set on SimpleUrlHandlerMapping");
/*  43:    */     } else {
/*  44:118 */       for (Map.Entry<String, Object> entry : urlMap.entrySet())
/*  45:    */       {
/*  46:119 */         String url = (String)entry.getKey();
/*  47:120 */         Object handler = entry.getValue();
/*  48:122 */         if (!url.startsWith("/")) {
/*  49:123 */           url = "/" + url;
/*  50:    */         }
/*  51:126 */         if ((handler instanceof String)) {
/*  52:127 */           handler = ((String)handler).trim();
/*  53:    */         }
/*  54:129 */         registerHandler(url, handler);
/*  55:    */       }
/*  56:    */     }
/*  57:    */   }
/*  58:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.SimpleUrlHandlerMapping
 * JD-Core Version:    0.7.0.1
 */