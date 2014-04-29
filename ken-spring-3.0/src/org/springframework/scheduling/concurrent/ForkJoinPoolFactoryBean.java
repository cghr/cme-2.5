/*   1:    */ package org.springframework.scheduling.concurrent;
/*   2:    */ 
/*   3:    */ import java.util.concurrent.ForkJoinPool;
/*   4:    */ import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
/*   5:    */ import org.springframework.beans.factory.DisposableBean;
/*   6:    */ import org.springframework.beans.factory.FactoryBean;
/*   7:    */ import org.springframework.beans.factory.InitializingBean;
/*   8:    */ 
/*   9:    */ public class ForkJoinPoolFactoryBean
/*  10:    */   implements FactoryBean<ForkJoinPool>, InitializingBean, DisposableBean
/*  11:    */ {
/*  12: 41 */   private int parallelism = Runtime.getRuntime().availableProcessors();
/*  13: 43 */   private ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory = ForkJoinPool.defaultForkJoinWorkerThreadFactory;
/*  14:    */   private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
/*  15: 47 */   private boolean asyncMode = false;
/*  16:    */   private ForkJoinPool forkJoinPool;
/*  17:    */   
/*  18:    */   public void setParallelism(int parallelism)
/*  19:    */   {
/*  20: 56 */     this.parallelism = parallelism;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public void setThreadFactory(ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory)
/*  24:    */   {
/*  25: 64 */     this.threadFactory = threadFactory;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler)
/*  29:    */   {
/*  30: 72 */     this.uncaughtExceptionHandler = uncaughtExceptionHandler;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setAsyncMode(boolean asyncMode)
/*  34:    */   {
/*  35: 82 */     this.asyncMode = asyncMode;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void afterPropertiesSet()
/*  39:    */   {
/*  40: 86 */     this.forkJoinPool = 
/*  41: 87 */       new ForkJoinPool(this.parallelism, this.threadFactory, this.uncaughtExceptionHandler, this.asyncMode);
/*  42:    */   }
/*  43:    */   
/*  44:    */   public ForkJoinPool getObject()
/*  45:    */   {
/*  46: 92 */     return this.forkJoinPool;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public Class<?> getObjectType()
/*  50:    */   {
/*  51: 96 */     return ForkJoinPool.class;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public boolean isSingleton()
/*  55:    */   {
/*  56:100 */     return true;
/*  57:    */   }
/*  58:    */   
/*  59:    */   public void destroy()
/*  60:    */   {
/*  61:105 */     this.forkJoinPool.shutdown();
/*  62:    */   }
/*  63:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.concurrent.ForkJoinPoolFactoryBean
 * JD-Core Version:    0.7.0.1
 */