/*  1:   */ package org.springframework.jdbc.support.lob;
/*  2:   */ 
/*  3:   */ import javax.transaction.Transaction;
/*  4:   */ import javax.transaction.TransactionManager;
/*  5:   */ import org.apache.commons.logging.Log;
/*  6:   */ import org.apache.commons.logging.LogFactory;
/*  7:   */ import org.springframework.transaction.TransactionSystemException;
/*  8:   */ import org.springframework.transaction.support.TransactionSynchronizationManager;
/*  9:   */ 
/* 10:   */ public abstract class LobCreatorUtils
/* 11:   */ {
/* 12:42 */   private static final Log logger = LogFactory.getLog(LobCreatorUtils.class);
/* 13:   */   
/* 14:   */   public static void registerTransactionSynchronization(LobCreator lobCreator, TransactionManager jtaTransactionManager)
/* 15:   */     throws IllegalStateException
/* 16:   */   {
/* 17:58 */     if (TransactionSynchronizationManager.isSynchronizationActive())
/* 18:   */     {
/* 19:59 */       logger.debug("Registering Spring transaction synchronization for LobCreator");
/* 20:60 */       TransactionSynchronizationManager.registerSynchronization(
/* 21:61 */         new SpringLobCreatorSynchronization(lobCreator));
/* 22:   */     }
/* 23:   */     else
/* 24:   */     {
/* 25:64 */       if (jtaTransactionManager != null) {
/* 26:   */         try
/* 27:   */         {
/* 28:66 */           int jtaStatus = jtaTransactionManager.getStatus();
/* 29:67 */           if ((jtaStatus == 0) || (jtaStatus == 1))
/* 30:   */           {
/* 31:68 */             logger.debug("Registering JTA transaction synchronization for LobCreator");
/* 32:69 */             jtaTransactionManager.getTransaction().registerSynchronization(
/* 33:70 */               new JtaLobCreatorSynchronization(lobCreator));
/* 34:71 */             return;
/* 35:   */           }
/* 36:   */         }
/* 37:   */         catch (Throwable ex)
/* 38:   */         {
/* 39:75 */           throw new TransactionSystemException(
/* 40:76 */             "Could not register synchronization with JTA TransactionManager", ex);
/* 41:   */         }
/* 42:   */       }
/* 43:79 */       throw new IllegalStateException("Active Spring transaction synchronization or active JTA transaction with specified [javax.transaction.TransactionManager] required");
/* 44:   */     }
/* 45:   */   }
/* 46:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.lob.LobCreatorUtils
 * JD-Core Version:    0.7.0.1
 */