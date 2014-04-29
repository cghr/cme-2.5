/*  1:   */ package org.springframework.transaction.interceptor;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.lang.reflect.Method;
/*  5:   */ import org.springframework.aop.support.StaticMethodMatcherPointcut;
/*  6:   */ import org.springframework.util.ObjectUtils;
/*  7:   */ 
/*  8:   */ abstract class TransactionAttributeSourcePointcut
/*  9:   */   extends StaticMethodMatcherPointcut
/* 10:   */   implements Serializable
/* 11:   */ {
/* 12:   */   public boolean matches(Method method, Class targetClass)
/* 13:   */   {
/* 14:35 */     TransactionAttributeSource tas = getTransactionAttributeSource();
/* 15:36 */     return (tas == null) || (tas.getTransactionAttribute(method, targetClass) != null);
/* 16:   */   }
/* 17:   */   
/* 18:   */   public boolean equals(Object other)
/* 19:   */   {
/* 20:41 */     if (this == other) {
/* 21:42 */       return true;
/* 22:   */     }
/* 23:44 */     if (!(other instanceof TransactionAttributeSourcePointcut)) {
/* 24:45 */       return false;
/* 25:   */     }
/* 26:47 */     TransactionAttributeSourcePointcut otherPc = (TransactionAttributeSourcePointcut)other;
/* 27:48 */     return ObjectUtils.nullSafeEquals(getTransactionAttributeSource(), otherPc.getTransactionAttributeSource());
/* 28:   */   }
/* 29:   */   
/* 30:   */   public int hashCode()
/* 31:   */   {
/* 32:53 */     return TransactionAttributeSourcePointcut.class.hashCode();
/* 33:   */   }
/* 34:   */   
/* 35:   */   public String toString()
/* 36:   */   {
/* 37:58 */     return getClass().getName() + ": " + getTransactionAttributeSource();
/* 38:   */   }
/* 39:   */   
/* 40:   */   protected abstract TransactionAttributeSource getTransactionAttributeSource();
/* 41:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.interceptor.TransactionAttributeSourcePointcut
 * JD-Core Version:    0.7.0.1
 */