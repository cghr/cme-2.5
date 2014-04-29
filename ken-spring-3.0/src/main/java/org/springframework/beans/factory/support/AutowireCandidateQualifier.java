/*  1:   */ package org.springframework.beans.factory.support;
/*  2:   */ 
/*  3:   */ import org.springframework.beans.BeanMetadataAttributeAccessor;
/*  4:   */ import org.springframework.util.Assert;
/*  5:   */ 
/*  6:   */ public class AutowireCandidateQualifier
/*  7:   */   extends BeanMetadataAttributeAccessor
/*  8:   */ {
/*  9:34 */   public static String VALUE_KEY = "value";
/* 10:   */   private final String typeName;
/* 11:   */   
/* 12:   */   public AutowireCandidateQualifier(Class type)
/* 13:   */   {
/* 14:45 */     this(type.getName());
/* 15:   */   }
/* 16:   */   
/* 17:   */   public AutowireCandidateQualifier(String typeName)
/* 18:   */   {
/* 19:56 */     Assert.notNull(typeName, "Type name must not be null");
/* 20:57 */     this.typeName = typeName;
/* 21:   */   }
/* 22:   */   
/* 23:   */   public AutowireCandidateQualifier(Class type, Object value)
/* 24:   */   {
/* 25:68 */     this(type.getName(), value);
/* 26:   */   }
/* 27:   */   
/* 28:   */   public AutowireCandidateQualifier(String typeName, Object value)
/* 29:   */   {
/* 30:81 */     Assert.notNull(typeName, "Type name must not be null");
/* 31:82 */     this.typeName = typeName;
/* 32:83 */     setAttribute(VALUE_KEY, value);
/* 33:   */   }
/* 34:   */   
/* 35:   */   public String getTypeName()
/* 36:   */   {
/* 37:93 */     return this.typeName;
/* 38:   */   }
/* 39:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.beans.factory.support.AutowireCandidateQualifier
 * JD-Core Version:    0.7.0.1
 */