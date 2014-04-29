/*  1:   */ package org.springframework.remoting.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.InvocationTargetException;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public class DefaultRemoteInvocationExecutor
/*  7:   */   implements RemoteInvocationExecutor
/*  8:   */ {
/*  9:   */   public Object invoke(RemoteInvocation invocation, Object targetObject)
/* 10:   */     throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
/* 11:   */   {
/* 12:36 */     Assert.notNull(invocation, "RemoteInvocation must not be null");
/* 13:37 */     Assert.notNull(targetObject, "Target object must not be null");
/* 14:38 */     return invocation.invoke(targetObject);
/* 15:   */   }
/* 16:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.support.DefaultRemoteInvocationExecutor
 * JD-Core Version:    0.7.0.1
 */