/*  1:   */ package org.springframework.expression.spel.ast;
/*  2:   */ 
/*  3:   */ public enum TypeCode
/*  4:   */ {
/*  5:26 */   OBJECT(Object.class),  BOOLEAN(Boolean.TYPE),  BYTE(Byte.TYPE),  CHAR(Character.TYPE),  SHORT(Short.TYPE),  INT(Integer.TYPE),  LONG(Long.TYPE),  FLOAT(Float.TYPE),  DOUBLE(Double.TYPE);
/*  6:   */   
/*  7:   */   private Class<?> type;
/*  8:   */   
/*  9:   */   private TypeCode(Class<?> type)
/* 10:   */   {
/* 11:32 */     this.type = type;
/* 12:   */   }
/* 13:   */   
/* 14:   */   public Class<?> getType()
/* 15:   */   {
/* 16:36 */     return this.type;
/* 17:   */   }
/* 18:   */   
/* 19:   */   public static TypeCode forName(String name)
/* 20:   */   {
/* 21:40 */     String searchingFor = name.toUpperCase();
/* 22:41 */     TypeCode[] tcs = values();
/* 23:42 */     for (int i = 1; i < tcs.length; i++) {
/* 24:43 */       if (tcs[i].name().equals(searchingFor)) {
/* 25:44 */         return tcs[i];
/* 26:   */       }
/* 27:   */     }
/* 28:47 */     return OBJECT;
/* 29:   */   }
/* 30:   */   
/* 31:   */   public static TypeCode forClass(Class<?> c)
/* 32:   */   {
/* 33:51 */     TypeCode[] allValues = values();
/* 34:52 */     for (int i = 0; i < allValues.length; i++)
/* 35:   */     {
/* 36:53 */       TypeCode typeCode = allValues[i];
/* 37:54 */       if (c == typeCode.getType()) {
/* 38:55 */         return typeCode;
/* 39:   */       }
/* 40:   */     }
/* 41:58 */     return OBJECT;
/* 42:   */   }
/* 43:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.spel.ast.TypeCode
 * JD-Core Version:    0.7.0.1
 */