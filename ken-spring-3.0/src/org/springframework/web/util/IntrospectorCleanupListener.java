/*  1:   */ package org.springframework.web.util;
/*  2:   */ 
/*  3:   */ import java.beans.Introspector;
/*  4:   */ import javax.servlet.ServletContextEvent;
/*  5:   */ import javax.servlet.ServletContextListener;
/*  6:   */ import org.springframework.beans.CachedIntrospectionResults;
/*  7:   */ 
/*  8:   */ public class IntrospectorCleanupListener
/*  9:   */   implements ServletContextListener
/* 10:   */ {
/* 11:   */   public void contextInitialized(ServletContextEvent event)
/* 12:   */   {
/* 13:75 */     CachedIntrospectionResults.acceptClassLoader(Thread.currentThread().getContextClassLoader());
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void contextDestroyed(ServletContextEvent event)
/* 17:   */   {
/* 18:79 */     CachedIntrospectionResults.clearClassLoader(Thread.currentThread().getContextClassLoader());
/* 19:80 */     Introspector.flushCaches();
/* 20:   */   }
/* 21:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.IntrospectorCleanupListener
 * JD-Core Version:    0.7.0.1
 */