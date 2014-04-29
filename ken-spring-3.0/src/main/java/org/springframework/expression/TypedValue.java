/*  1:   */ package org.springframework.expression;
/*  2:   */ 
/*  3:   */ import org.springframework.core.convert.TypeDescriptor;
/*  4:   */ 
/*  5:   */ public class TypedValue
/*  6:   */ {
/*  7:32 */   public static final TypedValue NULL = new TypedValue(null);
/*  8:   */   private final Object value;
/*  9:   */   private TypeDescriptor typeDescriptor;
/* 10:   */   
/* 11:   */   public TypedValue(Object value)
/* 12:   */   {
/* 13:44 */     this.value = value;
/* 14:   */     
/* 15:46 */     this.typeDescriptor = null;
/* 16:   */   }
/* 17:   */   
/* 18:   */   public TypedValue(Object value, TypeDescriptor typeDescriptor)
/* 19:   */   {
/* 20:55 */     this.value = value;
/* 21:56 */     this.typeDescriptor = typeDescriptor;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public Object getValue()
/* 25:   */   {
/* 26:60 */     return this.value;
/* 27:   */   }
/* 28:   */   
/* 29:   */   public TypeDescriptor getTypeDescriptor()
/* 30:   */   {
/* 31:64 */     if (this.typeDescriptor == null) {
/* 32:65 */       this.typeDescriptor = TypeDescriptor.forObject(this.value);
/* 33:   */     }
/* 34:67 */     return this.typeDescriptor;
/* 35:   */   }
/* 36:   */   
/* 37:   */   public String toString()
/* 38:   */   {
/* 39:72 */     StringBuilder str = new StringBuilder();
/* 40:73 */     str.append("TypedValue: '").append(this.value).append("' of [").append(getTypeDescriptor() + "]");
/* 41:74 */     return str.toString();
/* 42:   */   }
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.TypedValue
 * JD-Core Version:    0.7.0.1
 */