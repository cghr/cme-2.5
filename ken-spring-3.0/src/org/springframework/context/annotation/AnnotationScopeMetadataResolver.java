/*  1:   */ package org.springframework.context.annotation;
/*  2:   */ 
/*  3:   */ import java.lang.annotation.Annotation;
/*  4:   */ import java.util.Map;
/*  5:   */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*  6:   */ import org.springframework.beans.factory.config.BeanDefinition;
/*  7:   */ import org.springframework.core.type.AnnotationMetadata;
/*  8:   */ import org.springframework.util.Assert;
/*  9:   */ 
/* 10:   */ public class AnnotationScopeMetadataResolver
/* 11:   */   implements ScopeMetadataResolver
/* 12:   */ {
/* 13:40 */   protected Class<? extends Annotation> scopeAnnotationType = Scope.class;
/* 14:   */   private final ScopedProxyMode defaultProxyMode;
/* 15:   */   
/* 16:   */   public AnnotationScopeMetadataResolver()
/* 17:   */   {
/* 18:50 */     this.defaultProxyMode = ScopedProxyMode.NO;
/* 19:   */   }
/* 20:   */   
/* 21:   */   public AnnotationScopeMetadataResolver(ScopedProxyMode defaultProxyMode)
/* 22:   */   {
/* 23:58 */     Assert.notNull(defaultProxyMode, "'defaultProxyMode' must not be null");
/* 24:59 */     this.defaultProxyMode = defaultProxyMode;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void setScopeAnnotationType(Class<? extends Annotation> scopeAnnotationType)
/* 28:   */   {
/* 29:69 */     Assert.notNull(scopeAnnotationType, "'scopeAnnotationType' must not be null");
/* 30:70 */     this.scopeAnnotationType = scopeAnnotationType;
/* 31:   */   }
/* 32:   */   
/* 33:   */   public ScopeMetadata resolveScopeMetadata(BeanDefinition definition)
/* 34:   */   {
/* 35:75 */     ScopeMetadata metadata = new ScopeMetadata();
/* 36:76 */     if ((definition instanceof AnnotatedBeanDefinition))
/* 37:   */     {
/* 38:77 */       AnnotatedBeanDefinition annDef = (AnnotatedBeanDefinition)definition;
/* 39:78 */       Map<String, Object> attributes = 
/* 40:79 */         annDef.getMetadata().getAnnotationAttributes(this.scopeAnnotationType.getName());
/* 41:80 */       if (attributes != null)
/* 42:   */       {
/* 43:81 */         metadata.setScopeName((String)attributes.get("value"));
/* 44:82 */         ScopedProxyMode proxyMode = (ScopedProxyMode)attributes.get("proxyMode");
/* 45:83 */         if ((proxyMode == null) || (proxyMode == ScopedProxyMode.DEFAULT)) {
/* 46:84 */           proxyMode = this.defaultProxyMode;
/* 47:   */         }
/* 48:86 */         metadata.setScopedProxyMode(proxyMode);
/* 49:   */       }
/* 50:   */     }
/* 51:89 */     return metadata;
/* 52:   */   }
/* 53:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.AnnotationScopeMetadataResolver
 * JD-Core Version:    0.7.0.1
 */