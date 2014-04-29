/*  1:   */ package org.springframework.transaction.interceptor;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import org.springframework.transaction.support.DelegatingTransactionDefinition;
/*  5:   */ 
/*  6:   */ public abstract class DelegatingTransactionAttribute
/*  7:   */   extends DelegatingTransactionDefinition
/*  8:   */   implements TransactionAttribute, Serializable
/*  9:   */ {
/* 10:   */   private final TransactionAttribute targetAttribute;
/* 11:   */   
/* 12:   */   public DelegatingTransactionAttribute(TransactionAttribute targetAttribute)
/* 13:   */   {
/* 14:43 */     super(targetAttribute);
/* 15:44 */     this.targetAttribute = targetAttribute;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public String getQualifier()
/* 19:   */   {
/* 20:49 */     return this.targetAttribute.getQualifier();
/* 21:   */   }
/* 22:   */   
/* 23:   */   public boolean rollbackOn(Throwable ex)
/* 24:   */   {
/* 25:53 */     return this.targetAttribute.rollbackOn(ex);
/* 26:   */   }
/* 27:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.DelegatingTransactionAttribute
 * JD-Core Version:    0.7.0.1
 */