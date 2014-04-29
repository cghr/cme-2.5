/*   1:    */ package org.springframework.scheduling.support;
/*   2:    */ 
/*   3:    */ import org.apache.commons.logging.Log;
/*   4:    */ import org.apache.commons.logging.LogFactory;
/*   5:    */ import org.springframework.util.ErrorHandler;
/*   6:    */ import org.springframework.util.ReflectionUtils;
/*   7:    */ 
/*   8:    */ public abstract class TaskUtils
/*   9:    */ {
/*  10: 45 */   public static final ErrorHandler LOG_AND_SUPPRESS_ERROR_HANDLER = new LoggingErrorHandler();
/*  11: 52 */   public static final ErrorHandler LOG_AND_PROPAGATE_ERROR_HANDLER = new PropagatingErrorHandler();
/*  12:    */   
/*  13:    */   public static DelegatingErrorHandlingRunnable decorateTaskWithErrorHandler(Runnable task, ErrorHandler errorHandler, boolean isRepeatingTask)
/*  14:    */   {
/*  15: 66 */     if ((task instanceof DelegatingErrorHandlingRunnable)) {
/*  16: 67 */       return (DelegatingErrorHandlingRunnable)task;
/*  17:    */     }
/*  18: 69 */     ErrorHandler eh = errorHandler != null ? errorHandler : getDefaultErrorHandler(isRepeatingTask);
/*  19: 70 */     return new DelegatingErrorHandlingRunnable(task, eh);
/*  20:    */   }
/*  21:    */   
/*  22:    */   public static ErrorHandler getDefaultErrorHandler(boolean isRepeatingTask)
/*  23:    */   {
/*  24: 80 */     return isRepeatingTask ? LOG_AND_SUPPRESS_ERROR_HANDLER : LOG_AND_PROPAGATE_ERROR_HANDLER;
/*  25:    */   }
/*  26:    */   
/*  27:    */   static class LoggingErrorHandler
/*  28:    */     implements ErrorHandler
/*  29:    */   {
/*  30: 91 */     private final Log logger = LogFactory.getLog(LoggingErrorHandler.class);
/*  31:    */     
/*  32:    */     public void handleError(Throwable t)
/*  33:    */     {
/*  34: 94 */       if (this.logger.isErrorEnabled()) {
/*  35: 95 */         this.logger.error("Unexpected error occurred in scheduled task.", t);
/*  36:    */       }
/*  37:    */     }
/*  38:    */   }
/*  39:    */   
/*  40:    */   static class PropagatingErrorHandler
/*  41:    */     extends TaskUtils.LoggingErrorHandler
/*  42:    */   {
/*  43:    */     public void handleError(Throwable t)
/*  44:    */     {
/*  45:108 */       super.handleError(t);
/*  46:109 */       ReflectionUtils.rethrowRuntimeException(t);
/*  47:    */     }
/*  48:    */   }
/*  49:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.scheduling.support.TaskUtils
 * JD-Core Version:    0.7.0.1
 */