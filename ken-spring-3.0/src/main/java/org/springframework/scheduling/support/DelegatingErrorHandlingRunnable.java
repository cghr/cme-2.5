/*  1:   */ package org.springframework.scheduling.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.UndeclaredThrowableException;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ import org.springframework.util.ErrorHandler;
/*  6:   */ 
/*  7:   */ public class DelegatingErrorHandlingRunnable
/*  8:   */   implements Runnable
/*  9:   */ {
/* 10:   */   private final Runnable delegate;
/* 11:   */   private final ErrorHandler errorHandler;
/* 12:   */   
/* 13:   */   public DelegatingErrorHandlingRunnable(Runnable delegate, ErrorHandler errorHandler)
/* 14:   */   {
/* 15:45 */     Assert.notNull(delegate, "Delegate must not be null");
/* 16:46 */     Assert.notNull(errorHandler, "ErrorHandler must not be null");
/* 17:47 */     this.delegate = delegate;
/* 18:48 */     this.errorHandler = errorHandler;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public void run()
/* 22:   */   {
/* 23:   */     try
/* 24:   */     {
/* 25:53 */       this.delegate.run();
/* 26:   */     }
/* 27:   */     catch (UndeclaredThrowableException ex)
/* 28:   */     {
/* 29:56 */       this.errorHandler.handleError(ex.getUndeclaredThrowable());
/* 30:   */     }
/* 31:   */     catch (Throwable ex)
/* 32:   */     {
/* 33:59 */       this.errorHandler.handleError(ex);
/* 34:   */     }
/* 35:   */   }
/* 36:   */   
/* 37:   */   public String toString()
/* 38:   */   {
/* 39:65 */     return "DelegatingErrorHandlingRunnable for " + this.delegate;
/* 40:   */   }
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.support.DelegatingErrorHandlingRunnable
 * JD-Core Version:    0.7.0.1
 */