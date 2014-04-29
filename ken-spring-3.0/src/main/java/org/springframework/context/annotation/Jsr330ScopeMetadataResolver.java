/*   1:    */ package org.springframework.context.annotation;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ import java.util.Map;
/*   5:    */ import java.util.Set;
/*   6:    */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*   7:    */ import org.springframework.beans.factory.config.BeanDefinition;
/*   8:    */ import org.springframework.core.type.AnnotationMetadata;
/*   9:    */ 
/*  10:    */ public class Jsr330ScopeMetadataResolver
/*  11:    */   implements ScopeMetadataResolver
/*  12:    */ {
/*  13: 44 */   private final Map<String, String> scopeMap = new HashMap();
/*  14:    */   
/*  15:    */   public Jsr330ScopeMetadataResolver()
/*  16:    */   {
/*  17: 48 */     registerScope("javax.inject.Singleton", "singleton");
/*  18:    */   }
/*  19:    */   
/*  20:    */   public final void registerScope(Class annotationType, String scopeName)
/*  21:    */   {
/*  22: 59 */     this.scopeMap.put(annotationType.getName(), scopeName);
/*  23:    */   }
/*  24:    */   
/*  25:    */   public final void registerScope(String annotationType, String scopeName)
/*  26:    */   {
/*  27: 69 */     this.scopeMap.put(annotationType, scopeName);
/*  28:    */   }
/*  29:    */   
/*  30:    */   protected String resolveScopeName(String annotationType)
/*  31:    */   {
/*  32: 80 */     return (String)this.scopeMap.get(annotationType);
/*  33:    */   }
/*  34:    */   
/*  35:    */   public ScopeMetadata resolveScopeMetadata(BeanDefinition definition)
/*  36:    */   {
/*  37: 85 */     ScopeMetadata metadata = new ScopeMetadata();
/*  38: 86 */     metadata.setScopeName("prototype");
/*  39: 87 */     if ((definition instanceof AnnotatedBeanDefinition))
/*  40:    */     {
/*  41: 88 */       AnnotatedBeanDefinition annDef = (AnnotatedBeanDefinition)definition;
/*  42: 89 */       Set<String> annTypes = annDef.getMetadata().getAnnotationTypes();
/*  43: 90 */       String found = null;
/*  44: 91 */       for (String annType : annTypes)
/*  45:    */       {
/*  46: 92 */         Set<String> metaAnns = annDef.getMetadata().getMetaAnnotationTypes(annType);
/*  47: 93 */         if (metaAnns.contains("javax.inject.Scope"))
/*  48:    */         {
/*  49: 94 */           if (found != null) {
/*  50: 95 */             throw new IllegalStateException("Found ambiguous scope annotations on bean class [" + 
/*  51: 96 */               definition.getBeanClassName() + "]: " + found + ", " + annType);
/*  52:    */           }
/*  53: 98 */           found = annType;
/*  54: 99 */           String scopeName = resolveScopeName(annType);
/*  55:100 */           if (scopeName == null) {
/*  56:101 */             throw new IllegalStateException(
/*  57:102 */               "Unsupported scope annotation - not mapped onto Spring scope name: " + annType);
/*  58:    */           }
/*  59:104 */           metadata.setScopeName(scopeName);
/*  60:    */         }
/*  61:    */       }
/*  62:    */     }
/*  63:108 */     return metadata;
/*  64:    */   }
/*  65:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.context.annotation.Jsr330ScopeMetadataResolver
 * JD-Core Version:    0.7.0.1
 */