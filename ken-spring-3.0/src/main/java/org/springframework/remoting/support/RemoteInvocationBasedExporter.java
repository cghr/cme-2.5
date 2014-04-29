/*   1:    */ package org.springframework.remoting.support;
/*   2:    */ 
/*   3:    */ import java.lang.reflect.InvocationTargetException;
/*   4:    */ import org.apache.commons.logging.Log;
/*   5:    */ 
/*   6:    */ public abstract class RemoteInvocationBasedExporter
/*   7:    */   extends RemoteExporter
/*   8:    */ {
/*   9: 35 */   private RemoteInvocationExecutor remoteInvocationExecutor = new DefaultRemoteInvocationExecutor();
/*  10:    */   
/*  11:    */   public void setRemoteInvocationExecutor(RemoteInvocationExecutor remoteInvocationExecutor)
/*  12:    */   {
/*  13: 45 */     this.remoteInvocationExecutor = remoteInvocationExecutor;
/*  14:    */   }
/*  15:    */   
/*  16:    */   public RemoteInvocationExecutor getRemoteInvocationExecutor()
/*  17:    */   {
/*  18: 52 */     return this.remoteInvocationExecutor;
/*  19:    */   }
/*  20:    */   
/*  21:    */   protected Object invoke(RemoteInvocation invocation, Object targetObject)
/*  22:    */     throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
/*  23:    */   {
/*  24: 74 */     if (this.logger.isTraceEnabled()) {
/*  25: 75 */       this.logger.trace("Executing " + invocation);
/*  26:    */     }
/*  27:    */     try
/*  28:    */     {
/*  29: 78 */       return getRemoteInvocationExecutor().invoke(invocation, targetObject);
/*  30:    */     }
/*  31:    */     catch (NoSuchMethodException ex)
/*  32:    */     {
/*  33: 81 */       if (this.logger.isDebugEnabled()) {
/*  34: 82 */         this.logger.warn("Could not find target method for " + invocation, ex);
/*  35:    */       }
/*  36: 84 */       throw ex;
/*  37:    */     }
/*  38:    */     catch (IllegalAccessException ex)
/*  39:    */     {
/*  40: 87 */       if (this.logger.isDebugEnabled()) {
/*  41: 88 */         this.logger.warn("Could not access target method for " + invocation, ex);
/*  42:    */       }
/*  43: 90 */       throw ex;
/*  44:    */     }
/*  45:    */     catch (InvocationTargetException ex)
/*  46:    */     {
/*  47: 93 */       if (this.logger.isDebugEnabled()) {
/*  48: 94 */         this.logger.debug("Target method failed for " + invocation, ex.getTargetException());
/*  49:    */       }
/*  50: 96 */       throw ex;
/*  51:    */     }
/*  52:    */   }
/*  53:    */   
/*  54:    */   protected RemoteInvocationResult invokeAndCreateResult(RemoteInvocation invocation, Object targetObject)
/*  55:    */   {
/*  56:    */     try
/*  57:    */     {
/*  58:114 */       Object value = invoke(invocation, targetObject);
/*  59:115 */       return new RemoteInvocationResult(value);
/*  60:    */     }
/*  61:    */     catch (Throwable ex)
/*  62:    */     {
/*  63:118 */       return new RemoteInvocationResult(ex);
/*  64:    */     }
/*  65:    */   }
/*  66:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.support.RemoteInvocationBasedExporter
 * JD-Core Version:    0.7.0.1
 */