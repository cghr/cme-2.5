/*   1:    */ package org.springframework.scheduling.annotation;
/*   2:    */ 
/*   3:    */ import java.lang.annotation.Annotation;
/*   4:    */ import java.util.HashSet;
/*   5:    */ import java.util.LinkedHashSet;
/*   6:    */ import java.util.Set;
/*   7:    */ import java.util.concurrent.Executor;
/*   8:    */ import org.aopalliance.aop.Advice;
/*   9:    */ import org.springframework.aop.Pointcut;
/*  10:    */ import org.springframework.aop.interceptor.AsyncExecutionInterceptor;
/*  11:    */ import org.springframework.aop.support.AbstractPointcutAdvisor;
/*  12:    */ import org.springframework.aop.support.ComposablePointcut;
/*  13:    */ import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
/*  14:    */ import org.springframework.core.task.AsyncTaskExecutor;
/*  15:    */ import org.springframework.core.task.SimpleAsyncTaskExecutor;
/*  16:    */ import org.springframework.util.Assert;
/*  17:    */ 
/*  18:    */ public class AsyncAnnotationAdvisor
/*  19:    */   extends AbstractPointcutAdvisor
/*  20:    */ {
/*  21:    */   private Advice advice;
/*  22:    */   private Pointcut pointcut;
/*  23:    */   
/*  24:    */   public AsyncAnnotationAdvisor()
/*  25:    */   {
/*  26: 64 */     this(new SimpleAsyncTaskExecutor());
/*  27:    */   }
/*  28:    */   
/*  29:    */   public AsyncAnnotationAdvisor(Executor executor)
/*  30:    */   {
/*  31: 73 */     Set<Class<? extends Annotation>> asyncAnnotationTypes = new LinkedHashSet(2);
/*  32: 74 */     asyncAnnotationTypes.add(Async.class);
/*  33: 75 */     ClassLoader cl = AsyncAnnotationAdvisor.class.getClassLoader();
/*  34:    */     try
/*  35:    */     {
/*  36: 77 */       asyncAnnotationTypes.add(cl.loadClass("javax.ejb.Asynchronous"));
/*  37:    */     }
/*  38:    */     catch (ClassNotFoundException localClassNotFoundException) {}
/*  39: 82 */     this.advice = buildAdvice(executor);
/*  40: 83 */     this.pointcut = buildPointcut(asyncAnnotationTypes);
/*  41:    */   }
/*  42:    */   
/*  43:    */   public void setTaskExecutor(Executor executor)
/*  44:    */   {
/*  45: 90 */     this.advice = buildAdvice(executor);
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setAsyncAnnotationType(Class<? extends Annotation> asyncAnnotationType)
/*  49:    */   {
/*  50:103 */     Assert.notNull(asyncAnnotationType, "'asyncAnnotationType' must not be null");
/*  51:104 */     Set<Class<? extends Annotation>> asyncAnnotationTypes = new HashSet();
/*  52:105 */     asyncAnnotationTypes.add(asyncAnnotationType);
/*  53:106 */     this.pointcut = buildPointcut(asyncAnnotationTypes);
/*  54:    */   }
/*  55:    */   
/*  56:    */   public Advice getAdvice()
/*  57:    */   {
/*  58:111 */     return this.advice;
/*  59:    */   }
/*  60:    */   
/*  61:    */   public Pointcut getPointcut()
/*  62:    */   {
/*  63:115 */     return this.pointcut;
/*  64:    */   }
/*  65:    */   
/*  66:    */   protected Advice buildAdvice(Executor executor)
/*  67:    */   {
/*  68:120 */     if ((executor instanceof AsyncTaskExecutor)) {
/*  69:121 */       return new AsyncExecutionInterceptor((AsyncTaskExecutor)executor);
/*  70:    */     }
/*  71:124 */     return new AsyncExecutionInterceptor(executor);
/*  72:    */   }
/*  73:    */   
/*  74:    */   protected Pointcut buildPointcut(Set<Class<? extends Annotation>> asyncAnnotationTypes)
/*  75:    */   {
/*  76:134 */     ComposablePointcut result = null;
/*  77:135 */     for (Class<? extends Annotation> asyncAnnotationType : asyncAnnotationTypes)
/*  78:    */     {
/*  79:136 */       Pointcut cpc = new AnnotationMatchingPointcut(asyncAnnotationType, true);
/*  80:137 */       Pointcut mpc = new AnnotationMatchingPointcut(null, asyncAnnotationType);
/*  81:138 */       if (result == null) {
/*  82:139 */         result = new ComposablePointcut(cpc).union(mpc);
/*  83:    */       } else {
/*  84:142 */         result.union(cpc).union(mpc);
/*  85:    */       }
/*  86:    */     }
/*  87:145 */     return result;
/*  88:    */   }
/*  89:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.annotation.AsyncAnnotationAdvisor
 * JD-Core Version:    0.7.0.1
 */