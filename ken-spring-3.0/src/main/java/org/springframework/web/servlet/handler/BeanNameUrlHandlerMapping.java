/*  1:   */ package org.springframework.web.servlet.handler;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.springframework.context.ApplicationContext;
/*  6:   */ import org.springframework.util.StringUtils;
/*  7:   */ 
/*  8:   */ public class BeanNameUrlHandlerMapping
/*  9:   */   extends AbstractDetectingUrlHandlerMapping
/* 10:   */ {
/* 11:   */   protected String[] determineUrlsForHandler(String beanName)
/* 12:   */   {
/* 13:58 */     List<String> urls = new ArrayList();
/* 14:59 */     if (beanName.startsWith("/")) {
/* 15:60 */       urls.add(beanName);
/* 16:   */     }
/* 17:62 */     String[] aliases = getApplicationContext().getAliases(beanName);
/* 18:63 */     for (String alias : aliases) {
/* 19:64 */       if (alias.startsWith("/")) {
/* 20:65 */         urls.add(alias);
/* 21:   */       }
/* 22:   */     }
/* 23:68 */     return StringUtils.toStringArray(urls);
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping
 * JD-Core Version:    0.7.0.1
 */