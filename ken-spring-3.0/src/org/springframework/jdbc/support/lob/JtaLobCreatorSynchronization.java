/*  1:   */ package org.springframework.jdbc.support.lob;
/*  2:   */ 
/*  3:   */ import javax.transaction.Synchronization;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public class JtaLobCreatorSynchronization
/*  7:   */   implements Synchronization
/*  8:   */ {
/*  9:   */   private final LobCreator lobCreator;
/* 10:37 */   private boolean beforeCompletionCalled = false;
/* 11:   */   
/* 12:   */   public JtaLobCreatorSynchronization(LobCreator lobCreator)
/* 13:   */   {
/* 14:45 */     Assert.notNull(lobCreator, "LobCreator must not be null");
/* 15:46 */     this.lobCreator = lobCreator;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public void beforeCompletion()
/* 19:   */   {
/* 20:53 */     this.beforeCompletionCalled = true;
/* 21:54 */     this.lobCreator.close();
/* 22:   */   }
/* 23:   */   
/* 24:   */   public void afterCompletion(int status)
/* 25:   */   {
/* 26:58 */     if (!this.beforeCompletionCalled) {
/* 27:61 */       this.lobCreator.close();
/* 28:   */     }
/* 29:   */   }
/* 30:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.jdbc.support.lob.JtaLobCreatorSynchronization
 * JD-Core Version:    0.7.0.1
 */