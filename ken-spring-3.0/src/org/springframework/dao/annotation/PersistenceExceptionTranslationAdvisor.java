/*  1:   */ package org.springframework.dao.annotation;
/*  2:   */ 
/*  3:   */ import java.lang.annotation.Annotation;
/*  4:   */ import org.aopalliance.aop.Advice;
/*  5:   */ import org.springframework.aop.Pointcut;
/*  6:   */ import org.springframework.aop.support.AbstractPointcutAdvisor;
/*  7:   */ import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
/*  8:   */ import org.springframework.beans.factory.ListableBeanFactory;
/*  9:   */ import org.springframework.dao.support.PersistenceExceptionTranslationInterceptor;
/* 10:   */ import org.springframework.dao.support.PersistenceExceptionTranslator;
/* 11:   */ 
/* 12:   */ public class PersistenceExceptionTranslationAdvisor
/* 13:   */   extends AbstractPointcutAdvisor
/* 14:   */ {
/* 15:   */   private final PersistenceExceptionTranslationInterceptor advice;
/* 16:   */   private final AnnotationMatchingPointcut pointcut;
/* 17:   */   
/* 18:   */   public PersistenceExceptionTranslationAdvisor(PersistenceExceptionTranslator persistenceExceptionTranslator, Class<? extends Annotation> repositoryAnnotationType)
/* 19:   */   {
/* 20:57 */     this.advice = new PersistenceExceptionTranslationInterceptor(persistenceExceptionTranslator);
/* 21:58 */     this.pointcut = new AnnotationMatchingPointcut(repositoryAnnotationType, true);
/* 22:   */   }
/* 23:   */   
/* 24:   */   PersistenceExceptionTranslationAdvisor(ListableBeanFactory beanFactory, Class<? extends Annotation> repositoryAnnotationType)
/* 25:   */   {
/* 26:70 */     this.advice = new PersistenceExceptionTranslationInterceptor(beanFactory);
/* 27:71 */     this.pointcut = new AnnotationMatchingPointcut(repositoryAnnotationType, true);
/* 28:   */   }
/* 29:   */   
/* 30:   */   public Advice getAdvice()
/* 31:   */   {
/* 32:76 */     return this.advice;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public Pointcut getPointcut()
/* 36:   */   {
/* 37:80 */     return this.pointcut;
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.dao.annotation.PersistenceExceptionTranslationAdvisor
 * JD-Core Version:    0.7.0.1
 */