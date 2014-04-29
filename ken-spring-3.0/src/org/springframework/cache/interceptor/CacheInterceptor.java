/*  1:   */ package org.springframework.cache.interceptor;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.lang.reflect.Method;
/*  5:   */ import org.aopalliance.intercept.MethodInterceptor;
/*  6:   */ import org.aopalliance.intercept.MethodInvocation;
/*  7:   */ 
/*  8:   */ public class CacheInterceptor
/*  9:   */   extends CacheAspectSupport
/* 10:   */   implements MethodInterceptor, Serializable
/* 11:   */ {
/* 12:   */   private static class ThrowableWrapper
/* 13:   */     extends RuntimeException
/* 14:   */   {
/* 15:   */     private final Throwable original;
/* 16:   */     
/* 17:   */     ThrowableWrapper(Throwable original)
/* 18:   */     {
/* 19:48 */       this.original = original;
/* 20:   */     }
/* 21:   */   }
/* 22:   */   
/* 23:   */   public Object invoke(final MethodInvocation invocation)
/* 24:   */     throws Throwable
/* 25:   */   {
/* 26:53 */     Method method = invocation.getMethod();
/* 27:   */     
/* 28:55 */     CacheAspectSupport.Invoker aopAllianceInvoker = new CacheAspectSupport.Invoker()
/* 29:   */     {
/* 30:   */       public Object invoke()
/* 31:   */       {
/* 32:   */         try
/* 33:   */         {
/* 34:58 */           return invocation.proceed();
/* 35:   */         }
/* 36:   */         catch (Throwable ex)
/* 37:   */         {
/* 38:60 */           throw new CacheInterceptor.ThrowableWrapper(ex);
/* 39:   */         }
/* 40:   */       }
/* 41:   */     };
/* 42:   */     try
/* 43:   */     {
/* 44:66 */       return execute(aopAllianceInvoker, invocation.getThis(), method, invocation.getArguments());
/* 45:   */     }
/* 46:   */     catch (ThrowableWrapper th)
/* 47:   */     {
/* 48:68 */       throw th.original;
/* 49:   */     }
/* 50:   */   }
/* 51:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.cache.interceptor.CacheInterceptor
 * JD-Core Version:    0.7.0.1
 */