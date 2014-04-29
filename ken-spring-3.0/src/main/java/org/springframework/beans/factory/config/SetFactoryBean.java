/*  1:   */ package org.springframework.beans.factory.config;
/*  2:   */ 
/*  3:   */ import java.util.LinkedHashSet;
/*  4:   */ import java.util.Set;
/*  5:   */ import org.springframework.beans.BeanUtils;
/*  6:   */ import org.springframework.beans.TypeConverter;
/*  7:   */ import org.springframework.core.GenericCollectionTypeResolver;
/*  8:   */ 
/*  9:   */ public class SetFactoryBean
/* 10:   */   extends AbstractFactoryBean<Set>
/* 11:   */ {
/* 12:   */   private Set sourceSet;
/* 13:   */   private Class targetSetClass;
/* 14:   */   
/* 15:   */   public void setSourceSet(Set sourceSet)
/* 16:   */   {
/* 17:46 */     this.sourceSet = sourceSet;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void setTargetSetClass(Class targetSetClass)
/* 21:   */   {
/* 22:56 */     if (targetSetClass == null) {
/* 23:57 */       throw new IllegalArgumentException("'targetSetClass' must not be null");
/* 24:   */     }
/* 25:59 */     if (!Set.class.isAssignableFrom(targetSetClass)) {
/* 26:60 */       throw new IllegalArgumentException("'targetSetClass' must implement [java.util.Set]");
/* 27:   */     }
/* 28:62 */     this.targetSetClass = targetSetClass;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public Class<Set> getObjectType()
/* 32:   */   {
/* 33:68 */     return Set.class;
/* 34:   */   }
/* 35:   */   
/* 36:   */   protected Set createInstance()
/* 37:   */   {
/* 38:74 */     if (this.sourceSet == null) {
/* 39:75 */       throw new IllegalArgumentException("'sourceSet' is required");
/* 40:   */     }
/* 41:77 */     Set result = null;
/* 42:78 */     if (this.targetSetClass != null) {
/* 43:79 */       result = (Set)BeanUtils.instantiateClass(this.targetSetClass);
/* 44:   */     } else {
/* 45:82 */       result = new LinkedHashSet(this.sourceSet.size());
/* 46:   */     }
/* 47:84 */     Class valueType = null;
/* 48:85 */     if (this.targetSetClass != null) {
/* 49:86 */       valueType = GenericCollectionTypeResolver.getCollectionType(this.targetSetClass);
/* 50:   */     }
/* 51:88 */     if (valueType != null)
/* 52:   */     {
/* 53:89 */       TypeConverter converter = getBeanTypeConverter();
/* 54:90 */       for (Object elem : this.sourceSet) {
/* 55:91 */         result.add(converter.convertIfNecessary(elem, valueType));
/* 56:   */       }
/* 57:   */     }
/* 58:   */     else
/* 59:   */     {
/* 60:95 */       result.addAll(this.sourceSet);
/* 61:   */     }
/* 62:97 */     return result;
/* 63:   */   }
/* 64:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.SetFactoryBean
 * JD-Core Version:    0.7.0.1
 */