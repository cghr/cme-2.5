/*  1:   */ package org.springframework.context.annotation;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*  4:   */ import org.springframework.core.type.AnnotationMetadata;
/*  5:   */ 
/*  6:   */ public class ImportSelectorContext
/*  7:   */ {
/*  8:   */   private final AnnotationMetadata importingClassMetadata;
/*  9:   */   private final BeanDefinitionRegistry registry;
/* 10:   */   
/* 11:   */   ImportSelectorContext(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry)
/* 12:   */   {
/* 13:22 */     this.importingClassMetadata = importingClassMetadata;
/* 14:23 */     this.registry = registry;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public AnnotationMetadata getImportingClassMetadata()
/* 18:   */   {
/* 19:27 */     return this.importingClassMetadata;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public BeanDefinitionRegistry getBeanDefinitionRegistry()
/* 23:   */   {
/* 24:31 */     return this.registry;
/* 25:   */   }
/* 26:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.ImportSelectorContext
 * JD-Core Version:    0.7.0.1
 */