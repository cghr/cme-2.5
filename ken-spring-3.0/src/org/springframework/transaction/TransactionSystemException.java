/*  1:   */ package org.springframework.transaction;
/*  2:   */ 
/*  3:   */ import org.springframework.util.Assert;
/*  4:   */ 
/*  5:   */ public class TransactionSystemException
/*  6:   */   extends TransactionException
/*  7:   */ {
/*  8:   */   private Throwable applicationException;
/*  9:   */   
/* 10:   */   public TransactionSystemException(String msg)
/* 11:   */   {
/* 12:38 */     super(msg);
/* 13:   */   }
/* 14:   */   
/* 15:   */   public TransactionSystemException(String msg, Throwable cause)
/* 16:   */   {
/* 17:47 */     super(msg, cause);
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void initApplicationException(Throwable ex)
/* 21:   */   {
/* 22:59 */     Assert.notNull(ex, "Application exception must not be null");
/* 23:60 */     if (this.applicationException != null) {
/* 24:61 */       throw new IllegalStateException("Already holding an application exception: " + this.applicationException);
/* 25:   */     }
/* 26:63 */     this.applicationException = ex;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public final Throwable getApplicationException()
/* 30:   */   {
/* 31:72 */     return this.applicationException;
/* 32:   */   }
/* 33:   */   
/* 34:   */   public Throwable getOriginalException()
/* 35:   */   {
/* 36:81 */     return this.applicationException != null ? this.applicationException : getCause();
/* 37:   */   }
/* 38:   */   
/* 39:   */   public boolean contains(Class exType)
/* 40:   */   {
/* 41:86 */     return (super.contains(exType)) || ((exType != null) && (exType.isInstance(this.applicationException)));
/* 42:   */   }
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.TransactionSystemException
 * JD-Core Version:    0.7.0.1
 */