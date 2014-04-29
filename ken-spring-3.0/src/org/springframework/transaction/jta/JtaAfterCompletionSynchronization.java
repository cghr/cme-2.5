/*  1:   */ package org.springframework.transaction.jta;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import javax.transaction.Synchronization;
/*  5:   */ import org.springframework.transaction.support.TransactionSynchronization;
/*  6:   */ import org.springframework.transaction.support.TransactionSynchronizationUtils;
/*  7:   */ 
/*  8:   */ public class JtaAfterCompletionSynchronization
/*  9:   */   implements Synchronization
/* 10:   */ {
/* 11:   */   private final List<TransactionSynchronization> synchronizations;
/* 12:   */   
/* 13:   */   public JtaAfterCompletionSynchronization(List<TransactionSynchronization> synchronizations)
/* 14:   */   {
/* 15:48 */     this.synchronizations = synchronizations;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void beforeCompletion() {}
/* 19:   */   
/* 20:   */   public void afterCompletion(int status)
/* 21:   */   {
/* 22:56 */     switch (status)
/* 23:   */     {
/* 24:   */     case 3: 
/* 25:   */       try
/* 26:   */       {
/* 27:59 */         TransactionSynchronizationUtils.invokeAfterCommit(this.synchronizations);
/* 28:   */       }
/* 29:   */       finally
/* 30:   */       {
/* 31:62 */         TransactionSynchronizationUtils.invokeAfterCompletion(
/* 32:63 */           this.synchronizations, 0);
/* 33:   */       }
/* 34:65 */       break;
/* 35:   */     case 4: 
/* 36:67 */       TransactionSynchronizationUtils.invokeAfterCompletion(
/* 37:68 */         this.synchronizations, 1);
/* 38:69 */       break;
/* 39:   */     default: 
/* 40:71 */       TransactionSynchronizationUtils.invokeAfterCompletion(
/* 41:72 */         this.synchronizations, 2);
/* 42:   */     }
/* 43:   */   }
/* 44:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.jta.JtaAfterCompletionSynchronization
 * JD-Core Version:    0.7.0.1
 */