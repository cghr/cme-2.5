/*   1:    */ package org.springframework.remoting.support;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.lang.reflect.InvocationTargetException;
/*   5:    */ 
/*   6:    */ public class RemoteInvocationResult
/*   7:    */   implements Serializable
/*   8:    */ {
/*   9:    */   private static final long serialVersionUID = 2138555143707773549L;
/*  10:    */   private Object value;
/*  11:    */   private Throwable exception;
/*  12:    */   
/*  13:    */   public RemoteInvocationResult(Object value)
/*  14:    */   {
/*  15: 50 */     this.value = value;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public RemoteInvocationResult(Throwable exception)
/*  19:    */   {
/*  20: 59 */     this.exception = exception;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public Object getValue()
/*  24:    */   {
/*  25: 69 */     return this.value;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public Throwable getException()
/*  29:    */   {
/*  30: 78 */     return this.exception;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public boolean hasException()
/*  34:    */   {
/*  35: 89 */     return this.exception != null;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public boolean hasInvocationTargetException()
/*  39:    */   {
/*  40: 98 */     return this.exception instanceof InvocationTargetException;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public Object recreate()
/*  44:    */     throws Throwable
/*  45:    */   {
/*  46:110 */     if (this.exception != null)
/*  47:    */     {
/*  48:111 */       Throwable exToThrow = this.exception;
/*  49:112 */       if ((this.exception instanceof InvocationTargetException)) {
/*  50:113 */         exToThrow = ((InvocationTargetException)this.exception).getTargetException();
/*  51:    */       }
/*  52:115 */       RemoteInvocationUtils.fillInClientStackTraceIfPossible(exToThrow);
/*  53:116 */       throw exToThrow;
/*  54:    */     }
/*  55:119 */     return this.value;
/*  56:    */   }
/*  57:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.support.RemoteInvocationResult
 * JD-Core Version:    0.7.0.1
 */