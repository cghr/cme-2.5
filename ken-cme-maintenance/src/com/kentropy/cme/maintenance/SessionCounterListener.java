/*  1:   */ package com.kentropy.cme.maintenance;
/*  2:   */ 
/*  3:   */ import javax.servlet.http.HttpSessionEvent;
/*  4:   */ import javax.servlet.http.HttpSessionListener;
/*  5:   */ 
/*  6:   */ public class SessionCounterListener
/*  7:   */   implements HttpSessionListener
/*  8:   */ {
/*  9:   */   private static int totalActiveSessions;
/* 10:   */   
/* 11:   */   public static int getTotalActiveSession()
/* 12:   */   {
/* 13:11 */     return totalActiveSessions;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void sessionCreated(HttpSessionEvent arg0)
/* 17:   */   {
/* 18:16 */     totalActiveSessions += 1;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void sessionDestroyed(HttpSessionEvent arg0)
/* 22:   */   {
/* 23:22 */     totalActiveSessions -= 1;
/* 24:24 */     if (totalActiveSessions < 1) {
/* 25:25 */       totalActiveSessions = 1;
/* 26:   */     }
/* 27:   */   }
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-cme-maintenance\ken-cme-maintenance.jar
 * Qualified Name:     com.kentropy.cme.maintenance.SessionCounterListener
 * JD-Core Version:    0.7.0.1
 */