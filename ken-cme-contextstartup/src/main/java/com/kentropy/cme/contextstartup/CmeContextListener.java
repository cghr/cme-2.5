/*  1:   */ package com.kentropy.cme.contextstartup;
/*  2:   */ 
/*  3:   */ import java.io.PrintStream;
/*  4:   */ import javax.servlet.ServletContext;
/*  5:   */ import javax.servlet.ServletContextEvent;
/*  6:   */ import javax.servlet.ServletContextListener;
/*  7:   */ import org.apache.log4j.PropertyConfigurator;
/*  8:   */ 
/*  9:   */ public class CmeContextListener
/* 10:   */   implements ServletContextListener
/* 11:   */ {
/* 12:   */   public void contextDestroyed(ServletContextEvent event) {}
/* 13:   */   
/* 14:   */   public void contextInitialized(ServletContextEvent event)
/* 15:   */   {
/* 16:28 */     ServletContext sc = event.getServletContext();
/* 17:29 */     String path = sc.getRealPath("/");
/* 18:   */     
/* 19:   */ 
/* 20:   */ 
/* 21:   */ 
/* 22:34 */     System.out.println("============== Started  context ===================");
/* 23:35 */     System.out.println("============== Log File ==> " + path + "data/logs/cme.log");
/* 24:   */     
/* 25:37 */     System.setProperty("logfile", path + "data/logs/cme.log");
/* 26:38 */     System.setProperty("cmebasepath", path);
/* 27:   */     
/* 28:   */ 
/* 29:   */ 
/* 30:   */ 
/* 31:43 */     PropertyConfigurator.configure(path + "WEB-INF/classes/log4j.properties");
/* 32:   */   }
/* 33:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-cme-contextstartup\ken-cme-contextstartup.jar
 * Qualified Name:     com.kentropy.cme.contextstartup.CmeContextListener
 * JD-Core Version:    0.7.0.1
 */