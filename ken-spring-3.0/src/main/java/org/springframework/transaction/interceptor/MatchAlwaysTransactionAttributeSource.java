/*  1:   */ package org.springframework.transaction.interceptor;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.lang.reflect.Method;
/*  5:   */ import org.springframework.util.ObjectUtils;
/*  6:   */ 
/*  7:   */ public class MatchAlwaysTransactionAttributeSource
/*  8:   */   implements TransactionAttributeSource, Serializable
/*  9:   */ {
/* 10:38 */   private TransactionAttribute transactionAttribute = new DefaultTransactionAttribute();
/* 11:   */   
/* 12:   */   public void setTransactionAttribute(TransactionAttribute transactionAttribute)
/* 13:   */   {
/* 14:48 */     this.transactionAttribute = transactionAttribute;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass)
/* 18:   */   {
/* 19:53 */     return this.transactionAttribute;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public boolean equals(Object other)
/* 23:   */   {
/* 24:59 */     if (this == other) {
/* 25:60 */       return true;
/* 26:   */     }
/* 27:62 */     if (!(other instanceof MatchAlwaysTransactionAttributeSource)) {
/* 28:63 */       return false;
/* 29:   */     }
/* 30:65 */     MatchAlwaysTransactionAttributeSource otherTas = (MatchAlwaysTransactionAttributeSource)other;
/* 31:66 */     return ObjectUtils.nullSafeEquals(this.transactionAttribute, otherTas.transactionAttribute);
/* 32:   */   }
/* 33:   */   
/* 34:   */   public int hashCode()
/* 35:   */   {
/* 36:71 */     return MatchAlwaysTransactionAttributeSource.class.hashCode();
/* 37:   */   }
/* 38:   */   
/* 39:   */   public String toString()
/* 40:   */   {
/* 41:76 */     return getClass().getName() + ": " + this.transactionAttribute;
/* 42:   */   }
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource
 * JD-Core Version:    0.7.0.1
 */