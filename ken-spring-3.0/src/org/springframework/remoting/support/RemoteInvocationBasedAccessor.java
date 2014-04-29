/*  1:   */ package org.springframework.remoting.support;
/*  2:   */ 
/*  3:   */ import org.aopalliance.intercept.MethodInvocation;
/*  4:   */ 
/*  5:   */ public abstract class RemoteInvocationBasedAccessor
/*  6:   */   extends UrlBasedRemoteAccessor
/*  7:   */ {
/*  8:37 */   private RemoteInvocationFactory remoteInvocationFactory = new DefaultRemoteInvocationFactory();
/*  9:   */   
/* 10:   */   public void setRemoteInvocationFactory(RemoteInvocationFactory remoteInvocationFactory)
/* 11:   */   {
/* 12:47 */     this.remoteInvocationFactory = 
/* 13:48 */       (remoteInvocationFactory != null ? remoteInvocationFactory : new DefaultRemoteInvocationFactory());
/* 14:   */   }
/* 15:   */   
/* 16:   */   public RemoteInvocationFactory getRemoteInvocationFactory()
/* 17:   */   {
/* 18:55 */     return this.remoteInvocationFactory;
/* 19:   */   }
/* 20:   */   
/* 21:   */   protected RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation)
/* 22:   */   {
/* 23:71 */     return getRemoteInvocationFactory().createRemoteInvocation(methodInvocation);
/* 24:   */   }
/* 25:   */   
/* 26:   */   protected Object recreateRemoteInvocationResult(RemoteInvocationResult result)
/* 27:   */     throws Throwable
/* 28:   */   {
/* 29:85 */     return result.recreate();
/* 30:   */   }
/* 31:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.support.RemoteInvocationBasedAccessor
 * JD-Core Version:    0.7.0.1
 */