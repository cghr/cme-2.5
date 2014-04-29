/*  1:   */ package org.springframework.web.util;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletContextEvent;
/*  4:   */ import javax.servlet.ServletContextListener;
/*  5:   */ 
/*  6:   */ public class WebAppRootListener
/*  7:   */   implements ServletContextListener
/*  8:   */ {
/*  9:   */   public void contextInitialized(ServletContextEvent event)
/* 10:   */   {
/* 11:56 */     WebUtils.setWebAppRootSystemProperty(event.getServletContext());
/* 12:   */   }
/* 13:   */   
/* 14:   */   public void contextDestroyed(ServletContextEvent event)
/* 15:   */   {
/* 16:60 */     WebUtils.removeWebAppRootSystemProperty(event.getServletContext());
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.WebAppRootListener
 * JD-Core Version:    0.7.0.1
 */