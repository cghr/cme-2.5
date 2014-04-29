/*  1:   */ package org.springframework.beans.factory.annotation;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*  4:   */ import org.springframework.core.type.AnnotationMetadata;
/*  5:   */ import org.springframework.core.type.StandardAnnotationMetadata;
/*  6:   */ 
/*  7:   */ public class AnnotatedGenericBeanDefinition
/*  8:   */   extends GenericBeanDefinition
/*  9:   */   implements AnnotatedBeanDefinition
/* 10:   */ {
/* 11:   */   private final AnnotationMetadata annotationMetadata;
/* 12:   */   
/* 13:   */   public AnnotatedGenericBeanDefinition(Class beanClass)
/* 14:   */   {
/* 15:49 */     setBeanClass(beanClass);
/* 16:50 */     this.annotationMetadata = new StandardAnnotationMetadata(beanClass);
/* 17:   */   }
/* 18:   */   
/* 19:   */   public final AnnotationMetadata getMetadata()
/* 20:   */   {
/* 21:55 */     return this.annotationMetadata;
/* 22:   */   }
/* 23:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition
 * JD-Core Version:    0.7.0.1
 */