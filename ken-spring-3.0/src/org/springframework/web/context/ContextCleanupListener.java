/*  1:   */ package org.springframework.web.context;
/*  2:   */ 
/*  3:   */ import java.util.Enumeration;
/*  4:   */ import javax.servlet.ServletContext;
/*  5:   */ import javax.servlet.ServletContextEvent;
/*  6:   */ import javax.servlet.ServletContextListener;
/*  7:   */ import org.apache.commons.logging.Log;
/*  8:   */ import org.apache.commons.logging.LogFactory;
/*  9:   */ import org.springframework.beans.factory.DisposableBean;
/* 10:   */ 
/* 11:   */ public class ContextCleanupListener
/* 12:   */   implements ServletContextListener
/* 13:   */ {
/* 14:43 */   private static final Log logger = LogFactory.getLog(ContextCleanupListener.class);
/* 15:   */   
/* 16:   */   public void contextInitialized(ServletContextEvent event) {}
/* 17:   */   
/* 18:   */   public void contextDestroyed(ServletContextEvent event)
/* 19:   */   {
/* 20:50 */     cleanupAttributes(event.getServletContext());
/* 21:   */   }
/* 22:   */   
/* 23:   */   static void cleanupAttributes(ServletContext sc)
/* 24:   */   {
/* 25:60 */     Enumeration attrNames = sc.getAttributeNames();
/* 26:61 */     while (attrNames.hasMoreElements())
/* 27:   */     {
/* 28:62 */       String attrName = (String)attrNames.nextElement();
/* 29:63 */       if (attrName.startsWith("org.springframework."))
/* 30:   */       {
/* 31:64 */         Object attrValue = sc.getAttribute(attrName);
/* 32:65 */         if ((attrValue instanceof DisposableBean)) {
/* 33:   */           try
/* 34:   */           {
/* 35:67 */             ((DisposableBean)attrValue).destroy();
/* 36:   */           }
/* 37:   */           catch (Throwable ex)
/* 38:   */           {
/* 39:70 */             logger.error("Couldn't invoke destroy method of attribute with name '" + attrName + "'", ex);
/* 40:   */           }
/* 41:   */         }
/* 42:   */       }
/* 43:   */     }
/* 44:   */   }
/* 45:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.context.ContextCleanupListener
 * JD-Core Version:    0.7.0.1
 */