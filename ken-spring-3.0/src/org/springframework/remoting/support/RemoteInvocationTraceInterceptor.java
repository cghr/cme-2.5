/*  1:   */ package org.springframework.remoting.support;
/*  2:   */ 
/*  3:   */ import java.lang.reflect.Method;
/*  4:   */ import org.aopalliance.intercept.MethodInterceptor;
/*  5:   */ import org.aopalliance.intercept.MethodInvocation;
/*  6:   */ import org.apache.commons.logging.Log;
/*  7:   */ import org.apache.commons.logging.LogFactory;
/*  8:   */ import org.springframework.util.ClassUtils;
/*  9:   */ 
/* 10:   */ public class RemoteInvocationTraceInterceptor
/* 11:   */   implements MethodInterceptor
/* 12:   */ {
/* 13:48 */   protected static final Log logger = LogFactory.getLog(RemoteInvocationTraceInterceptor.class);
/* 14:   */   private final String exporterNameClause;
/* 15:   */   
/* 16:   */   public RemoteInvocationTraceInterceptor()
/* 17:   */   {
/* 18:57 */     this.exporterNameClause = "";
/* 19:   */   }
/* 20:   */   
/* 21:   */   public RemoteInvocationTraceInterceptor(String exporterName)
/* 22:   */   {
/* 23:66 */     this.exporterNameClause = (exporterName + " ");
/* 24:   */   }
/* 25:   */   
/* 26:   */   public Object invoke(MethodInvocation invocation)
/* 27:   */     throws Throwable
/* 28:   */   {
/* 29:71 */     Method method = invocation.getMethod();
/* 30:72 */     if (logger.isDebugEnabled()) {
/* 31:73 */       logger.debug("Incoming " + this.exporterNameClause + "remote call: " + 
/* 32:74 */         ClassUtils.getQualifiedMethodName(method));
/* 33:   */     }
/* 34:   */     try
/* 35:   */     {
/* 36:77 */       Object retVal = invocation.proceed();
/* 37:78 */       if (logger.isDebugEnabled()) {
/* 38:79 */         logger.debug("Finished processing of " + this.exporterNameClause + "remote call: " + 
/* 39:80 */           ClassUtils.getQualifiedMethodName(method));
/* 40:   */       }
/* 41:82 */       return retVal;
/* 42:   */     }
/* 43:   */     catch (Throwable ex)
/* 44:   */     {
/* 45:85 */       if (((ex instanceof RuntimeException)) || ((ex instanceof Error)))
/* 46:   */       {
/* 47:86 */         if (logger.isWarnEnabled()) {
/* 48:87 */           logger.warn("Processing of " + this.exporterNameClause + "remote call resulted in fatal exception: " + 
/* 49:88 */             ClassUtils.getQualifiedMethodName(method), ex);
/* 50:   */         }
/* 51:   */       }
/* 52:92 */       else if (logger.isInfoEnabled()) {
/* 53:93 */         logger.info("Processing of " + this.exporterNameClause + "remote call resulted in exception: " + 
/* 54:94 */           ClassUtils.getQualifiedMethodName(method), ex);
/* 55:   */       }
/* 56:97 */       throw ex;
/* 57:   */     }
/* 58:   */   }
/* 59:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.remoting.support.RemoteInvocationTraceInterceptor
 * JD-Core Version:    0.7.0.1
 */