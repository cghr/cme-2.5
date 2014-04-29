/*   1:    */ package org.springframework.expression.common;
/*   2:    */ 
/*   3:    */ import org.springframework.core.convert.TypeDescriptor;
/*   4:    */ import org.springframework.expression.EvaluationContext;
/*   5:    */ import org.springframework.expression.EvaluationException;
/*   6:    */ import org.springframework.expression.TypeConverter;
/*   7:    */ import org.springframework.expression.TypedValue;
/*   8:    */ import org.springframework.util.ClassUtils;
/*   9:    */ 
/*  10:    */ public abstract class ExpressionUtils
/*  11:    */ {
/*  12:    */   public static <T> T convert(EvaluationContext context, Object value, Class<T> targetType)
/*  13:    */     throws EvaluationException
/*  14:    */   {
/*  15: 47 */     return convertTypedValue(context, new TypedValue(value), targetType);
/*  16:    */   }
/*  17:    */   
/*  18:    */   public static <T> T convertTypedValue(EvaluationContext context, TypedValue typedValue, Class<T> targetType)
/*  19:    */   {
/*  20: 62 */     Object value = typedValue.getValue();
/*  21: 63 */     if ((targetType == null) || (ClassUtils.isAssignableValue(targetType, value))) {
/*  22: 64 */       return value;
/*  23:    */     }
/*  24: 66 */     if (context != null) {
/*  25: 67 */       return context.getTypeConverter().convertValue(value, typedValue.getTypeDescriptor(), TypeDescriptor.valueOf(targetType));
/*  26:    */     }
/*  27: 69 */     throw new EvaluationException("Cannot convert value '" + value + "' to type '" + targetType.getName() + "'");
/*  28:    */   }
/*  29:    */   
/*  30:    */   public static int toInt(TypeConverter typeConverter, TypedValue typedValue)
/*  31:    */   {
/*  32: 76 */     return ((Integer)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/*  33: 77 */       TypeDescriptor.valueOf(Integer.class))).intValue();
/*  34:    */   }
/*  35:    */   
/*  36:    */   public static boolean toBoolean(TypeConverter typeConverter, TypedValue typedValue)
/*  37:    */   {
/*  38: 84 */     return ((Boolean)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/*  39: 85 */       TypeDescriptor.valueOf(Boolean.class))).booleanValue();
/*  40:    */   }
/*  41:    */   
/*  42:    */   public static double toDouble(TypeConverter typeConverter, TypedValue typedValue)
/*  43:    */   {
/*  44: 92 */     return ((Double)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/*  45: 93 */       TypeDescriptor.valueOf(Double.class))).doubleValue();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public static long toLong(TypeConverter typeConverter, TypedValue typedValue)
/*  49:    */   {
/*  50:100 */     return ((Long)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/*  51:101 */       TypeDescriptor.valueOf(Long.class))).longValue();
/*  52:    */   }
/*  53:    */   
/*  54:    */   public static char toChar(TypeConverter typeConverter, TypedValue typedValue)
/*  55:    */   {
/*  56:108 */     return ((Character)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/*  57:109 */       TypeDescriptor.valueOf(Character.class))).charValue();
/*  58:    */   }
/*  59:    */   
/*  60:    */   public static short toShort(TypeConverter typeConverter, TypedValue typedValue)
/*  61:    */   {
/*  62:116 */     return ((Short)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/*  63:117 */       TypeDescriptor.valueOf(Short.class))).shortValue();
/*  64:    */   }
/*  65:    */   
/*  66:    */   public static float toFloat(TypeConverter typeConverter, TypedValue typedValue)
/*  67:    */   {
/*  68:124 */     return ((Float)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/*  69:125 */       TypeDescriptor.valueOf(Float.class))).floatValue();
/*  70:    */   }
/*  71:    */   
/*  72:    */   public static byte toByte(TypeConverter typeConverter, TypedValue typedValue)
/*  73:    */   {
/*  74:132 */     return ((Byte)typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), 
/*  75:133 */       TypeDescriptor.valueOf(Byte.class))).byteValue();
/*  76:    */   }
/*  77:    */ }


/* Location:           Z:\home\sagpatke\cme-workspace\cme\ken-spring-3.0\ken-spring-3.0.jar
 * Qualified Name:     org.springframework.expression.common.ExpressionUtils
 * JD-Core Version:    0.7.0.1
 */