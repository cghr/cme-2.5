/*  1:   */ package org.springframework.jca.work;
/*  2:   */ 
/*  3:   */ import javax.resource.spi.work.Work;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public class DelegatingWork
/*  7:   */   implements Work
/*  8:   */ {
/*  9:   */   private final Runnable delegate;
/* 10:   */   
/* 11:   */   public DelegatingWork(Runnable delegate)
/* 12:   */   {
/* 13:41 */     Assert.notNull(delegate, "Delegate must not be null");
/* 14:42 */     this.delegate = delegate;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public final Runnable getDelegate()
/* 18:   */   {
/* 19:49 */     return this.delegate;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public void run()
/* 23:   */   {
/* 24:57 */     this.delegate.run();
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void release() {}
/* 28:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jca.work.DelegatingWork
 * JD-Core Version:    0.7.0.1
 */