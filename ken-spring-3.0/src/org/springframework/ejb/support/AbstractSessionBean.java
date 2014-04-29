/*  1:   */ package org.springframework.ejb.support;
/*  2:   */ 
/*  3:   */ import javax.ejb.SessionContext;
/*  4:   */ 
/*  5:   */ public abstract class AbstractSessionBean
/*  6:   */   extends AbstractEnterpriseBean
/*  7:   */   implements SmartSessionBean
/*  8:   */ {
/*  9:   */   private SessionContext sessionContext;
/* 10:   */   
/* 11:   */   public void setSessionContext(SessionContext sessionContext)
/* 12:   */   {
/* 13:43 */     this.sessionContext = sessionContext;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public final SessionContext getSessionContext()
/* 17:   */   {
/* 18:51 */     return this.sessionContext;
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.ejb.support.AbstractSessionBean
 * JD-Core Version:    0.7.0.1
 */