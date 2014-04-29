/*   1:    */ package org.springframework.core.convert.converter;
/*   2:    */ 
/*   3:    */ import java.util.Set;
/*   4:    */ import org.springframework.core.convert.TypeDescriptor;
/*   5:    */ import org.springframework.util.Assert;
/*   6:    */ 
/*   7:    */ public abstract interface GenericConverter
/*   8:    */ {
/*   9:    */   public abstract Set<ConvertiblePair> getConvertibleTypes();
/*  10:    */   
/*  11:    */   public abstract Object convert(Object paramObject, TypeDescriptor paramTypeDescriptor1, TypeDescriptor paramTypeDescriptor2);
/*  12:    */   
/*  13:    */   public static final class ConvertiblePair
/*  14:    */   {
/*  15:    */     private final Class<?> sourceType;
/*  16:    */     private final Class<?> targetType;
/*  17:    */     
/*  18:    */     public ConvertiblePair(Class<?> sourceType, Class<?> targetType)
/*  19:    */     {
/*  20: 77 */       Assert.notNull(sourceType, "Source type must not be null");
/*  21: 78 */       Assert.notNull(targetType, "Target type must not be null");
/*  22: 79 */       this.sourceType = sourceType;
/*  23: 80 */       this.targetType = targetType;
/*  24:    */     }
/*  25:    */     
/*  26:    */     public Class<?> getSourceType()
/*  27:    */     {
/*  28: 84 */       return this.sourceType;
/*  29:    */     }
/*  30:    */     
/*  31:    */     public Class<?> getTargetType()
/*  32:    */     {
/*  33: 88 */       return this.targetType;
/*  34:    */     }
/*  35:    */     
/*  36:    */     public boolean equals(Object obj)
/*  37:    */     {
/*  38: 93 */       if (this == obj) {
/*  39: 94 */         return true;
/*  40:    */       }
/*  41: 96 */       if ((obj == null) || (obj.getClass() != ConvertiblePair.class)) {
/*  42: 97 */         return false;
/*  43:    */       }
/*  44: 99 */       ConvertiblePair other = (ConvertiblePair)obj;
/*  45:100 */       return (this.sourceType.equals(other.sourceType)) && (this.targetType.equals(other.targetType));
/*  46:    */     }
/*  47:    */     
/*  48:    */     public int hashCode()
/*  49:    */     {
/*  50:106 */       return this.sourceType.hashCode() * 31 + this.targetType.hashCode();
/*  51:    */     }
/*  52:    */   }
/*  53:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.core.convert.converter.GenericConverter
 * JD-Core Version:    0.7.0.1
 */