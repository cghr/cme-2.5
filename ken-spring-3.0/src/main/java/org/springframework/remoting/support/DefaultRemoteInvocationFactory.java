/*  1:   */ package org.springframework.remoting.support;
/*  2:   */ 
/*  3:   */ import org.aopalliance.intercept.MethodInvocation;
/*  4:   */ 
/*  5:   */ public class DefaultRemoteInvocationFactory
/*  6:   */   implements RemoteInvocationFactory
/*  7:   */ {
/*  8:   */   public RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation)
/*  9:   */   {
/* 10:31 */     return new RemoteInvocation(methodInvocation);
/* 11:   */   }
/* 12:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.support.DefaultRemoteInvocationFactory
 * JD-Core Version:    0.7.0.1
 */