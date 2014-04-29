/*  1:   */ package org.springframework.scheduling.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.InvocationTargetException;
/*  4:   */ import java.lang.reflect.Method;
/*  5:   */ import java.lang.reflect.UndeclaredThrowableException;
/*  6:   */ import org.springframework.util.ReflectionUtils;
/*  7:   */ 
/*  8:   */ public class ScheduledMethodRunnable
/*  9:   */   implements Runnable
/* 10:   */ {
/* 11:   */   private final Object target;
/* 12:   */   private final Method method;
/* 13:   */   
/* 14:   */   public ScheduledMethodRunnable(Object target, Method method)
/* 15:   */   {
/* 16:42 */     this.target = target;
/* 17:43 */     this.method = method;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public ScheduledMethodRunnable(Object target, String methodName)
/* 21:   */     throws NoSuchMethodException
/* 22:   */   {
/* 23:47 */     this.target = target;
/* 24:48 */     this.method = target.getClass().getMethod(methodName, new Class[0]);
/* 25:   */   }
/* 26:   */   
/* 27:   */   public Object getTarget()
/* 28:   */   {
/* 29:53 */     return this.target;
/* 30:   */   }
/* 31:   */   
/* 32:   */   public Method getMethod()
/* 33:   */   {
/* 34:57 */     return this.method;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public void run()
/* 38:   */   {
/* 39:   */     try
/* 40:   */     {
/* 41:63 */       ReflectionUtils.makeAccessible(this.method);
/* 42:64 */       this.method.invoke(this.target, new Object[0]);
/* 43:   */     }
/* 44:   */     catch (InvocationTargetException ex)
/* 45:   */     {
/* 46:67 */       ReflectionUtils.rethrowRuntimeException(ex.getTargetException());
/* 47:   */     }
/* 48:   */     catch (IllegalAccessException ex)
/* 49:   */     {
/* 50:70 */       throw new UndeclaredThrowableException(ex);
/* 51:   */     }
/* 52:   */   }
/* 53:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.support.ScheduledMethodRunnable
 * JD-Core Version:    0.7.0.1
 */