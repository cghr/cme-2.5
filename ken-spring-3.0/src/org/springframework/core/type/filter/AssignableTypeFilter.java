/*  1:   */ package org.springframework.core.type.filter;
/*  2:   */ 
/*  3:   */ public class AssignableTypeFilter
/*  4:   */   extends AbstractTypeHierarchyTraversingFilter
/*  5:   */ {
/*  6:   */   private final Class targetType;
/*  7:   */   
/*  8:   */   public AssignableTypeFilter(Class targetType)
/*  9:   */   {
/* 10:37 */     super(true, true);
/* 11:38 */     this.targetType = targetType;
/* 12:   */   }
/* 13:   */   
/* 14:   */   protected boolean matchClassName(String className)
/* 15:   */   {
/* 16:44 */     return this.targetType.getName().equals(className);
/* 17:   */   }
/* 18:   */   
/* 19:   */   protected Boolean matchSuperClass(String superClassName)
/* 20:   */   {
/* 21:49 */     return matchTargetType(superClassName);
/* 22:   */   }
/* 23:   */   
/* 24:   */   protected Boolean matchInterface(String interfaceName)
/* 25:   */   {
/* 26:54 */     return matchTargetType(interfaceName);
/* 27:   */   }
/* 28:   */   
/* 29:   */   protected Boolean matchTargetType(String typeName)
/* 30:   */   {
/* 31:58 */     if (this.targetType.getName().equals(typeName)) {
/* 32:59 */       return Boolean.valueOf(true);
/* 33:   */     }
/* 34:61 */     if (Object.class.getName().equals(typeName)) {
/* 35:62 */       return Boolean.FALSE;
/* 36:   */     }
/* 37:64 */     if (typeName.startsWith("java.")) {
/* 38:   */       try
/* 39:   */       {
/* 40:66 */         Class clazz = getClass().getClassLoader().loadClass(typeName);
/* 41:67 */         return Boolean.valueOf(this.targetType.isAssignableFrom(clazz));
/* 42:   */       }
/* 43:   */       catch (ClassNotFoundException localClassNotFoundException) {}
/* 44:   */     }
/* 45:73 */     return null;
/* 46:   */   }
/* 47:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.type.filter.AssignableTypeFilter
 * JD-Core Version:    0.7.0.1
 */