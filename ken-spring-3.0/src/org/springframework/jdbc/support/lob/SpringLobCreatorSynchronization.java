/*  1:   */ package org.springframework.jdbc.support.lob;
/*  2:   */ 
/*  3:   */ import org.springframework.transaction.support.TransactionSynchronizationAdapter;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public class SpringLobCreatorSynchronization
/*  7:   */   extends TransactionSynchronizationAdapter
/*  8:   */ {
/*  9:   */   public static final int LOB_CREATOR_SYNCHRONIZATION_ORDER = 800;
/* 10:   */   private final LobCreator lobCreator;
/* 11:46 */   private boolean beforeCompletionCalled = false;
/* 12:   */   
/* 13:   */   public SpringLobCreatorSynchronization(LobCreator lobCreator)
/* 14:   */   {
/* 15:54 */     Assert.notNull(lobCreator, "LobCreator must not be null");
/* 16:55 */     this.lobCreator = lobCreator;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public int getOrder()
/* 20:   */   {
/* 21:60 */     return 800;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void beforeCompletion()
/* 25:   */   {
/* 26:69 */     this.beforeCompletionCalled = true;
/* 27:70 */     this.lobCreator.close();
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void afterCompletion(int status)
/* 31:   */   {
/* 32:75 */     if (!this.beforeCompletionCalled) {
/* 33:79 */       this.lobCreator.close();
/* 34:   */     }
/* 35:   */   }
/* 36:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.lob.SpringLobCreatorSynchronization
 * JD-Core Version:    0.7.0.1
 */