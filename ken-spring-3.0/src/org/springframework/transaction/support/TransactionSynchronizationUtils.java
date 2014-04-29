/*   1:    */ package org.springframework.transaction.support;
/*   2:    */ 
/*   3:    */ import java.util.List;
/*   4:    */ import org.apache.commons.logging.Log;
/*   5:    */ import org.apache.commons.logging.LogFactory;
/*   6:    */ import org.springframework.aop.scope.ScopedObject;
/*   7:    */ import org.springframework.core.InfrastructureProxy;
/*   8:    */ import org.springframework.util.Assert;
/*   9:    */ import org.springframework.util.ClassUtils;
/*  10:    */ 
/*  11:    */ public abstract class TransactionSynchronizationUtils
/*  12:    */ {
/*  13: 40 */   private static final Log logger = LogFactory.getLog(TransactionSynchronizationUtils.class);
/*  14: 42 */   private static final boolean aopAvailable = ClassUtils.isPresent(
/*  15: 43 */     "org.springframework.aop.scope.ScopedObject", TransactionSynchronizationUtils.class.getClassLoader());
/*  16:    */   
/*  17:    */   public static boolean sameResourceFactory(ResourceTransactionManager tm, Object resourceFactory)
/*  18:    */   {
/*  19: 53 */     return unwrapResourceIfNecessary(tm.getResourceFactory()).equals(unwrapResourceIfNecessary(resourceFactory));
/*  20:    */   }
/*  21:    */   
/*  22:    */   static Object unwrapResourceIfNecessary(Object resource)
/*  23:    */   {
/*  24: 62 */     Assert.notNull(resource, "Resource must not be null");
/*  25: 63 */     Object resourceRef = resource;
/*  26: 65 */     if ((resourceRef instanceof InfrastructureProxy)) {
/*  27: 66 */       resourceRef = ((InfrastructureProxy)resourceRef).getWrappedObject();
/*  28:    */     }
/*  29: 68 */     if (aopAvailable) {
/*  30: 70 */       resourceRef = ScopedProxyUnwrapper.unwrapIfNecessary(resourceRef);
/*  31:    */     }
/*  32: 72 */     return resourceRef;
/*  33:    */   }
/*  34:    */   
/*  35:    */   public static void triggerFlush()
/*  36:    */   {
/*  37: 82 */     for (TransactionSynchronization synchronization : ) {
/*  38: 83 */       synchronization.flush();
/*  39:    */     }
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static void triggerBeforeCommit(boolean readOnly)
/*  43:    */   {
/*  44: 94 */     for (TransactionSynchronization synchronization : ) {
/*  45: 95 */       synchronization.beforeCommit(readOnly);
/*  46:    */     }
/*  47:    */   }
/*  48:    */   
/*  49:    */   public static void triggerBeforeCompletion()
/*  50:    */   {
/*  51:104 */     for (TransactionSynchronization synchronization : ) {
/*  52:    */       try
/*  53:    */       {
/*  54:106 */         synchronization.beforeCompletion();
/*  55:    */       }
/*  56:    */       catch (Throwable tsex)
/*  57:    */       {
/*  58:109 */         logger.error("TransactionSynchronization.beforeCompletion threw exception", tsex);
/*  59:    */       }
/*  60:    */     }
/*  61:    */   }
/*  62:    */   
/*  63:    */   public static void triggerAfterCommit()
/*  64:    */   {
/*  65:121 */     invokeAfterCommit(TransactionSynchronizationManager.getSynchronizations());
/*  66:    */   }
/*  67:    */   
/*  68:    */   public static void invokeAfterCommit(List<TransactionSynchronization> synchronizations)
/*  69:    */   {
/*  70:131 */     if (synchronizations != null) {
/*  71:132 */       for (TransactionSynchronization synchronization : synchronizations) {
/*  72:133 */         synchronization.afterCommit();
/*  73:    */       }
/*  74:    */     }
/*  75:    */   }
/*  76:    */   
/*  77:    */   public static void triggerAfterCompletion(int completionStatus)
/*  78:    */   {
/*  79:149 */     List<TransactionSynchronization> synchronizations = TransactionSynchronizationManager.getSynchronizations();
/*  80:150 */     invokeAfterCompletion(synchronizations, completionStatus);
/*  81:    */   }
/*  82:    */   
/*  83:    */   public static void invokeAfterCompletion(List<TransactionSynchronization> synchronizations, int completionStatus)
/*  84:    */   {
/*  85:165 */     if (synchronizations != null) {
/*  86:166 */       for (TransactionSynchronization synchronization : synchronizations) {
/*  87:    */         try
/*  88:    */         {
/*  89:168 */           synchronization.afterCompletion(completionStatus);
/*  90:    */         }
/*  91:    */         catch (Throwable tsex)
/*  92:    */         {
/*  93:171 */           logger.error("TransactionSynchronization.afterCompletion threw exception", tsex);
/*  94:    */         }
/*  95:    */       }
/*  96:    */     }
/*  97:    */   }
/*  98:    */   
/*  99:    */   private static class ScopedProxyUnwrapper
/* 100:    */   {
/* 101:    */     public static Object unwrapIfNecessary(Object resource)
/* 102:    */     {
/* 103:184 */       if ((resource instanceof ScopedObject)) {
/* 104:185 */         return ((ScopedObject)resource).getTargetObject();
/* 105:    */       }
/* 106:188 */       return resource;
/* 107:    */     }
/* 108:    */   }
/* 109:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.TransactionSynchronizationUtils
 * JD-Core Version:    0.7.0.1
 */