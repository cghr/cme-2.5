/*  1:   */ package org.springframework.remoting.support;
/*  2:   */ 
/*  3:   */ public abstract class RemoteAccessor
/*  4:   */   extends RemotingSupport
/*  5:   */ {
/*  6:   */   private Class serviceInterface;
/*  7:   */   
/*  8:   */   public void setServiceInterface(Class serviceInterface)
/*  9:   */   {
/* 10:49 */     if ((serviceInterface != null) && (!serviceInterface.isInterface())) {
/* 11:50 */       throw new IllegalArgumentException("'serviceInterface' must be an interface");
/* 12:   */     }
/* 13:52 */     this.serviceInterface = serviceInterface;
/* 14:   */   }
/* 15:   */   
/* 16:   */   public Class getServiceInterface()
/* 17:   */   {
/* 18:59 */     return this.serviceInterface;
/* 19:   */   }
/* 20:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.support.RemoteAccessor
 * JD-Core Version:    0.7.0.1
 */