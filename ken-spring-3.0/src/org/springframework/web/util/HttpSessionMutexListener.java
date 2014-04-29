/*  1:   */ package org.springframework.web.util;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import javax.servlet.http.HttpSession;
/*  5:   */ import javax.servlet.http.HttpSessionEvent;
/*  6:   */ import javax.servlet.http.HttpSessionListener;
/*  7:   */ 
/*  8:   */ public class HttpSessionMutexListener
/*  9:   */   implements HttpSessionListener
/* 10:   */ {
/* 11:   */   public void sessionCreated(HttpSessionEvent event)
/* 12:   */   {
/* 13:48 */     event.getSession().setAttribute(WebUtils.SESSION_MUTEX_ATTRIBUTE, new Mutex(null));
/* 14:   */   }
/* 15:   */   
/* 16:   */   public void sessionDestroyed(HttpSessionEvent event)
/* 17:   */   {
/* 18:52 */     event.getSession().removeAttribute(WebUtils.SESSION_MUTEX_ATTRIBUTE);
/* 19:   */   }
/* 20:   */   
/* 21:   */   private static class Mutex
/* 22:   */     implements Serializable
/* 23:   */   {}
/* 24:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.web.util.HttpSessionMutexListener
 * JD-Core Version:    0.7.0.1
 */