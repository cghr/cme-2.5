/*  1:   */ package org.springframework.transaction.annotation;
/*  2:   */ 
/*  3:   */ import java.io.Serializable;
/*  4:   */ import java.lang.reflect.AnnotatedElement;
/*  5:   */ import javax.ejb.ApplicationException;
/*  6:   */ import javax.ejb.TransactionAttributeType;
/*  7:   */ import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
/*  8:   */ 
/*  9:   */ public class Ejb3TransactionAnnotationParser
/* 10:   */   implements TransactionAnnotationParser, Serializable
/* 11:   */ {
/* 12:   */   public org.springframework.transaction.interceptor.TransactionAttribute parseTransactionAnnotation(AnnotatedElement ae)
/* 13:   */   {
/* 14:37 */     javax.ejb.TransactionAttribute ann = (javax.ejb.TransactionAttribute)ae.getAnnotation(javax.ejb.TransactionAttribute.class);
/* 15:38 */     if (ann != null) {
/* 16:39 */       return parseTransactionAnnotation(ann);
/* 17:   */     }
/* 18:42 */     return null;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public org.springframework.transaction.interceptor.TransactionAttribute parseTransactionAnnotation(javax.ejb.TransactionAttribute ann)
/* 22:   */   {
/* 23:47 */     return new Ejb3TransactionAttribute(ann.value());
/* 24:   */   }
/* 25:   */   
/* 26:   */   private static class Ejb3TransactionAttribute
/* 27:   */     extends DefaultTransactionAttribute
/* 28:   */   {
/* 29:   */     public Ejb3TransactionAttribute(TransactionAttributeType type)
/* 30:   */     {
/* 31:58 */       setPropagationBehaviorName("PROPAGATION_" + type.name());
/* 32:   */     }
/* 33:   */     
/* 34:   */     public boolean rollbackOn(Throwable ex)
/* 35:   */     {
/* 36:62 */       ApplicationException ann = (ApplicationException)ex.getClass().getAnnotation(ApplicationException.class);
/* 37:63 */       return ann != null ? ann.rollback() : super.rollbackOn(ex);
/* 38:   */     }
/* 39:   */   }
/* 40:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.transaction.annotation.Ejb3TransactionAnnotationParser
 * JD-Core Version:    0.7.0.1
 */