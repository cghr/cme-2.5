/*  1:   */ package org.springframework.beans.factory.config;
/*  2:   */ 
/*  3:   */ import java.util.ArrayList;
/*  4:   */ import java.util.List;
/*  5:   */ import org.springframework.beans.BeanUtils;
/*  6:   */ import org.springframework.beans.TypeConverter;
/*  7:   */ import org.springframework.core.GenericCollectionTypeResolver;
/*  8:   */ 
/*  9:   */ public class ListFactoryBean
/* 10:   */   extends AbstractFactoryBean<List>
/* 11:   */ {
/* 12:   */   private List sourceList;
/* 13:   */   private Class targetListClass;
/* 14:   */   
/* 15:   */   public void setSourceList(List sourceList)
/* 16:   */   {
/* 17:46 */     this.sourceList = sourceList;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void setTargetListClass(Class targetListClass)
/* 21:   */   {
/* 22:56 */     if (targetListClass == null) {
/* 23:57 */       throw new IllegalArgumentException("'targetListClass' must not be null");
/* 24:   */     }
/* 25:59 */     if (!List.class.isAssignableFrom(targetListClass)) {
/* 26:60 */       throw new IllegalArgumentException("'targetListClass' must implement [java.util.List]");
/* 27:   */     }
/* 28:62 */     this.targetListClass = targetListClass;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public Class<List> getObjectType()
/* 32:   */   {
/* 33:68 */     return List.class;
/* 34:   */   }
/* 35:   */   
/* 36:   */   protected List createInstance()
/* 37:   */   {
/* 38:74 */     if (this.sourceList == null) {
/* 39:75 */       throw new IllegalArgumentException("'sourceList' is required");
/* 40:   */     }
/* 41:77 */     List result = null;
/* 42:78 */     if (this.targetListClass != null) {
/* 43:79 */       result = (List)BeanUtils.instantiateClass(this.targetListClass);
/* 44:   */     } else {
/* 45:82 */       result = new ArrayList(this.sourceList.size());
/* 46:   */     }
/* 47:84 */     Class valueType = null;
/* 48:85 */     if (this.targetListClass != null) {
/* 49:86 */       valueType = GenericCollectionTypeResolver.getCollectionType(this.targetListClass);
/* 50:   */     }
/* 51:88 */     if (valueType != null)
/* 52:   */     {
/* 53:89 */       TypeConverter converter = getBeanTypeConverter();
/* 54:90 */       for (Object elem : this.sourceList) {
/* 55:91 */         result.add(converter.convertIfNecessary(elem, valueType));
/* 56:   */       }
/* 57:   */     }
/* 58:   */     else
/* 59:   */     {
/* 60:95 */       result.addAll(this.sourceList);
/* 61:   */     }
/* 62:97 */     return result;
/* 63:   */   }
/* 64:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.config.ListFactoryBean
 * JD-Core Version:    0.7.0.1
 */