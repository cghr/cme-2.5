/*  1:   */ package org.springframework.web.util;
/*  2:   */ 
/*  3:   */ import javax.servlet.ServletContextEvent;
/*  4:   */ import javax.servlet.ServletContextListener;
/*  5:   */ 
/*  6:   */ public class Log4jConfigListener
/*  7:   */   implements ServletContextListener
/*  8:   */ {
/*  9:   */   public void contextInitialized(ServletContextEvent event)
/* 10:   */   {
/* 11:45 */     Log4jWebConfigurer.initLogging(event.getServletContext());
/* 12:   */   }
/* 13:   */   
/* 14:   */   public void contextDestroyed(ServletContextEvent event)
/* 15:   */   {
/* 16:49 */     Log4jWebConfigurer.shutdownLogging(event.getServletContext());
/* 17:   */   }
/* 18:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.Log4jConfigListener
 * JD-Core Version:    0.7.0.1
 */