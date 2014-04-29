/*  1:   */ package org.springframework.core.convert;
/*  2:   */ 
/*  3:   */ import java.lang.annotation.Annotation;
/*  4:   */ import java.lang.reflect.Field;
/*  5:   */ import java.util.HashMap;
/*  6:   */ import java.util.Map;
/*  7:   */ import org.springframework.core.GenericCollectionTypeResolver;
/*  8:   */ 
/*  9:   */ class FieldDescriptor
/* 10:   */   extends AbstractDescriptor
/* 11:   */ {
/* 12:   */   private final Field field;
/* 13:   */   private final int nestingLevel;
/* 14:   */   private Map<Integer, Integer> typeIndexesPerLevel;
/* 15:   */   
/* 16:   */   public FieldDescriptor(Field field)
/* 17:   */   {
/* 18:40 */     super(field.getType());
/* 19:41 */     this.field = field;
/* 20:42 */     this.nestingLevel = 1;
/* 21:   */   }
/* 22:   */   
/* 23:   */   private FieldDescriptor(Class<?> type, Field field, int nestingLevel, int typeIndex, Map<Integer, Integer> typeIndexesPerLevel)
/* 24:   */   {
/* 25:46 */     super(type);
/* 26:47 */     this.field = field;
/* 27:48 */     this.nestingLevel = nestingLevel;
/* 28:49 */     this.typeIndexesPerLevel = typeIndexesPerLevel;
/* 29:50 */     this.typeIndexesPerLevel.put(Integer.valueOf(nestingLevel), Integer.valueOf(typeIndex));
/* 30:   */   }
/* 31:   */   
/* 32:   */   public Annotation[] getAnnotations()
/* 33:   */   {
/* 34:56 */     return TypeDescriptor.nullSafeAnnotations(this.field.getAnnotations());
/* 35:   */   }
/* 36:   */   
/* 37:   */   protected Class<?> resolveCollectionElementType()
/* 38:   */   {
/* 39:61 */     return GenericCollectionTypeResolver.getCollectionFieldType(this.field, this.nestingLevel, this.typeIndexesPerLevel);
/* 40:   */   }
/* 41:   */   
/* 42:   */   protected Class<?> resolveMapKeyType()
/* 43:   */   {
/* 44:66 */     return GenericCollectionTypeResolver.getMapKeyFieldType(this.field, this.nestingLevel, this.typeIndexesPerLevel);
/* 45:   */   }
/* 46:   */   
/* 47:   */   protected Class<?> resolveMapValueType()
/* 48:   */   {
/* 49:71 */     return GenericCollectionTypeResolver.getMapValueFieldType(this.field, this.nestingLevel, this.typeIndexesPerLevel);
/* 50:   */   }
/* 51:   */   
/* 52:   */   protected AbstractDescriptor nested(Class<?> type, int typeIndex)
/* 53:   */   {
/* 54:76 */     if (this.typeIndexesPerLevel == null) {
/* 55:77 */       this.typeIndexesPerLevel = new HashMap(4);
/* 56:   */     }
/* 57:79 */     return new FieldDescriptor(type, this.field, this.nestingLevel + 1, typeIndex, this.typeIndexesPerLevel);
/* 58:   */   }
/* 59:   */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.FieldDescriptor
 * JD-Core Version:    0.7.0.1
 */