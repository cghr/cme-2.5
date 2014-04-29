/*  1:   */ package org.springframework.context.annotation;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*  4:   */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*  5:   */ import org.springframework.core.type.AnnotationMetadata;
/*  6:   */ import org.springframework.core.type.classreading.MetadataReader;
/*  7:   */ import org.springframework.util.Assert;
/*  8:   */ 
/*  9:   */ public class ScannedGenericBeanDefinition
/* 10:   */   extends GenericBeanDefinition
/* 11:   */   implements AnnotatedBeanDefinition
/* 12:   */ {
/* 13:   */   private final AnnotationMetadata metadata;
/* 14:   */   
/* 15:   */   public ScannedGenericBeanDefinition(MetadataReader metadataReader)
/* 16:   */   {
/* 17:51 */     Assert.notNull(metadataReader, "MetadataReader must not be null");
/* 18:52 */     this.metadata = metadataReader.getAnnotationMetadata();
/* 19:53 */     setBeanClassName(this.metadata.getClassName());
/* 20:   */   }
/* 21:   */   
/* 22:   */   public final AnnotationMetadata getMetadata()
/* 23:   */   {
/* 24:58 */     return this.metadata;
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ScannedGenericBeanDefinition
 * JD-Core Version:    0.7.0.1
 */