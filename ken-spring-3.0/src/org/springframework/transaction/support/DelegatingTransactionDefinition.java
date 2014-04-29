/*  1:   */ package org.springframework.transaction.support;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import org.springframework.transaction.TransactionDefinition;
/*  5:   */ import org.springframework.util.Assert;
/*  6:   */ 
/*  7:   */ public abstract class DelegatingTransactionDefinition
/*  8:   */   implements TransactionDefinition, Serializable
/*  9:   */ {
/* 10:   */   private final TransactionDefinition targetDefinition;
/* 11:   */   
/* 12:   */   public DelegatingTransactionDefinition(TransactionDefinition targetDefinition)
/* 13:   */   {
/* 14:43 */     Assert.notNull(targetDefinition, "Target definition must not be null");
/* 15:44 */     this.targetDefinition = targetDefinition;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public int getPropagationBehavior()
/* 19:   */   {
/* 20:49 */     return this.targetDefinition.getPropagationBehavior();
/* 21:   */   }
/* 22:   */   
/* 23:   */   public int getIsolationLevel()
/* 24:   */   {
/* 25:53 */     return this.targetDefinition.getIsolationLevel();
/* 26:   */   }
/* 27:   */   
/* 28:   */   public int getTimeout()
/* 29:   */   {
/* 30:57 */     return this.targetDefinition.getTimeout();
/* 31:   */   }
/* 32:   */   
/* 33:   */   public boolean isReadOnly()
/* 34:   */   {
/* 35:61 */     return this.targetDefinition.isReadOnly();
/* 36:   */   }
/* 37:   */   
/* 38:   */   public String getName()
/* 39:   */   {
/* 40:65 */     return this.targetDefinition.getName();
/* 41:   */   }
/* 42:   */   
/* 43:   */   public boolean equals(Object obj)
/* 44:   */   {
/* 45:71 */     return this.targetDefinition.equals(obj);
/* 46:   */   }
/* 47:   */   
/* 48:   */   public int hashCode()
/* 49:   */   {
/* 50:76 */     return this.targetDefinition.hashCode();
/* 51:   */   }
/* 52:   */   
/* 53:   */   public String toString()
/* 54:   */   {
/* 55:81 */     return this.targetDefinition.toString();
/* 56:   */   }
/* 57:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.support.DelegatingTransactionDefinition
 * JD-Core Version:    0.7.0.1
 */