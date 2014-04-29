/*  1:   */ package org.springframework.beans.factory.support;
/*  2:   */ 
/*  3:   */ import java.security.AccessControlContext;
/*  4:   */ import java.security.AccessController;
/*  5:   */ 
/*  6:   */ public class SimpleSecurityContextProvider
/*  7:   */   implements SecurityContextProvider
/*  8:   */ {
/*  9:   */   private final AccessControlContext acc;
/* 10:   */   
/* 11:   */   public SimpleSecurityContextProvider()
/* 12:   */   {
/* 13:39 */     this(null);
/* 14:   */   }
/* 15:   */   
/* 16:   */   public SimpleSecurityContextProvider(AccessControlContext acc)
/* 17:   */   {
/* 18:50 */     this.acc = acc;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public AccessControlContext getAccessControlContext()
/* 22:   */   {
/* 23:55 */     return this.acc != null ? this.acc : AccessController.getContext();
/* 24:   */   }
/* 25:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.SimpleSecurityContextProvider
 * JD-Core Version:    0.7.0.1
 */